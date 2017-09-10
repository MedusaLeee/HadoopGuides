package com.jianxun.hdfs;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

public class MkDir {
    // 创建目录，运行需设置jvm参数 -DHADOOP_USER_NAME=hdfs
    // 不设置会报错：Permission denied: user=ubuntu, access=WRITE, inode="/":hdfs:supergroup:drwxr-xr-x
    public static void main(String[] args) throws IOException {
        final String hdfsDirPath = "hdfs://master:8020/test/";
        System.out.println("Creating " + hdfsDirPath + " on hdfs...");
        Configuration conf = new Configuration();
        FileSystem fs = null;
        try {
            Path dirPath = new Path(hdfsDirPath);
            fs = FileSystem.get(URI.create(hdfsDirPath), conf);
            fs.mkdirs(dirPath);
            System.out.println("Create " + hdfsDirPath + " on hdfs successfully.");
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }finally {
            if (fs != null) {
                fs.close();
            }
        }
    }
}
