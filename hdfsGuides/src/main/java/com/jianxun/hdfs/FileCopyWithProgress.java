package com.jianxun.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.*;
import java.net.URI;

public class FileCopyWithProgress {
    public static void main(String[] args) throws IOException {
        final String localSrc = "/tmp/log/bigdata.pdf";
        final String hdfsUri = "hdfs://master:8020/test/bigdata.pdf";
        InputStream in = new BufferedInputStream(new FileInputStream(localSrc));
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(hdfsUri), conf);
        OutputStream out = fs.create(new Path(hdfsUri), new Progressable() {
            // progress只有在Hadoop文件系统是HDFS的时候才调用，local,S3,FTP都不会调用
            @Override
            public void progress() {
                System.out.print(">");
            }
        });
        IOUtils.copyBytes(in, out, 4096, true);
    }
}
