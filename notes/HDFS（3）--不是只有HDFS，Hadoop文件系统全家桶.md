# Hadoop文件系统全家桶
Hadoop有一个抽象的文件系统概念，HDFS只是其中一个实现。Java抽象类`org.apache.hadoop.fs.FileSystem`定义了Hadoop中文件系统的客户端接口，该抽象类有几个具体的实现。

# Hadoop文件系统
### Local
URI方案：file
Java实现：`org.apache.hadoop.fs.LocalFileSystem`
描述：使用客户端校验的本地磁盘文件系统。

### HDFS
URI方案：hdfs
Java实现：`org.apache.hadoop.hdfs.DistributedFileSystem`
描述：Hadoop的分布式文件系统。将HDFS设计成与MapReduce结合使用，可以实现高性能。

### WebHDFS
URI方案：Webhdfs
Java实现：`org.apache.hadoop.hdfs.web.WebHdfsFileSystem`
描述：基于HTTP的文件系统，提供对HDFS的认证读/写访问。

### Secure WebHDFS
URI方案：swebhdfs
Java实现：`org.apache.hadoop.hdfs.web.SWebHdfsFileSystem`
描述：WebHDFS的HTTPS版本。

### HAR
URI方案：har
Java实现：`org.apache.hadoop.fs.HarFileSystem`
描述：一个构建在其他文件系统上用于文件存档的文件系统。Hadoop存档文件系统通常用于将HDFS中的多个文件打包成一个存档文件，以减少namenode的内存使用。使用Hadoop的achive命令来创建HAR文件。

### View
URI方案：viewfs
Java实现：`org.apache.hadoop.viewfs.ViewFileSystem`
描述：针对其他Hadoop文件系统的客户端挂载表。通常用于联邦namenode创建挂载点。

### FTP
URI方案：ftp
Java实现：`org.apache.hadoop.fs.ftp.FTPFileSystem`
描述：由FTP服务器支持的文件系统。

### S3
URI方案：S3a
Java实现：`org.apache.hadoop.fs.s3a.S3AFileSystem`
描述：由Amazon S3支持的文件系统。

### Azure
URI方案：wasb
Java实现：`org.apache.hadoop.fs.azure.NativeAzureFileSystem`
描述：由Microsoft Azure支持的文件系统。

### Swift
URI方案：swift
Java实现：`org.apache.hadoop.fs.swift.snative.SwiftNativeFileSystem`
描述：由OpenStack Swift支持的文件系统。

# 接口

###1. HTTP
Hadoop以Java API的方式提供文件系统访问接口，非Java开发人员的应用访问HDFS很不方便。Hadoop中由WebHDFS协议提供的HTTP REST API则脱离了编程语言的限制。但是，HTTP接口比Java客户端要慢很多。尽量不要用它来传输比较大的数据。

### 2. C语言
Hadoop提供了一个为libhdfs的C语言库。这个C语言API和Java的API非常相似，但它的开发滞后于Java API，一些新特性得不到及时的支持。

### 3. NFS
使用Hadoop的NFSv3网关可以将HDFS挂载为本地客户端的文件系统。挂载后便可以使用Unix系统的上命令（如ls和cat）与该文件系统交互。实际上他还是HDFS，修改文件只能通过追加的方式，不能随机修改。

### 4. FUSE
用户空间文件系统（FUSE），由Hadoop的Fuse-DFS模块支持。不做解释了，需要请查询维基百科。Fuse-DFS相比于3中的NFS，NFS方案更优。

