HDFS文件读取步骤是怎么样的？我们通过下图中的步骤顺序一步步梳理，然后再看这样设计的好处。
![hdfs-read.png](https://github.com/MedusaLeee/HadoopGuides/blob/master/notes/images/hdfs-read.png)

# 步骤（Java客户端）

### 1. open

客户端调用`FileSystem`对象的`open()`方法来打开希望读取的文件，对于HDFS来说，`FileSystem`对象是`DistributedFileSystem`的一个实例。

### 2. get block locations

`DistributedFileSystem`通过使用远程过程调用（RPC）来调用`namenode`，以确定文件起始块的位置。对于每个块，`namenode`返回存有该块副本的`datanode`地址。`datanode`根据距离客户端的距离来排序，会选择在最近的`datanode`中读取数据。

### 3. read，read from FSDataInputStream

`DistributedFileSystem`类返回一个`FSDataInputStream`对象给客户端用来读取数据，该对象支持文件定位，该对象对`datanode`和`namenode`的I/O进行了封装。客户端对这个输入流代用`read()`方法。

### 4. read，read from first datanode

存储着文件起始块的`datanode`地址的`DFSInputStream`(3 中的`FSDataInputStream`封装了`DFSInputStream` )连接距离最近的文件中的第一个块所在的`datanode`，通过`read()`方法，将数据从`datanode`传输到客户端。

### 5. read，read from next datanode

读取到块的末端时，`DFSInputStream`关闭与`datanode`的连接，然后寻找下一个块的最佳（距离最近）的`datanode`。对于客户端来说，客户端只是在读取一个连续的流。

### 6. close

客户端读取完成后，调用`FSDataInputStream`对象的`close()`方法关闭流。

# 设计优势

### 1.  容错

在 4 和 5 步骤中，如果`DFSInputStream`与`datanode`的通信失败，会尝试从这个块的另一个最邻近的`datanode`读取数据。也会记住这故障的`datanode`，并校验从`datanode`读取到的数据是否完整。如有损坏的块也会尝试从其他`datanode`读取这个块的副本，并将损坏的块的情况告诉`namenode`。

### 2. 提高客户端并发

客户端可以直接连接到`datanode`检索数据，并且`namenode`告知客户端每个块所在的距离客户端最近的`datanode`。这样数据流就分散在了集群中的所有的`datanode`上，使HDFS可以接受大量客户端的并发读取请求。

### 3. 减轻namenode的处理压力

这样设计`namenode`只需要响应获取块位置的请求，这些信息由`datanode`汇报并存在内存中，非常高效，数据响应交给`datanode`，自身不需要响应数据请求。如果不这样做，`namenode`将成为瓶颈。




