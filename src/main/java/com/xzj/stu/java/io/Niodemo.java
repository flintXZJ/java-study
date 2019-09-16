package com.xzj.stu.java.io;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Niodemo {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\test.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

        ByteBuffer allocate = ByteBuffer.allocate(1024);
        byte[] bytes = new byte[1024];

        long len = randomAccessFile.length();
        long l = System.currentTimeMillis();
        //读取内存映射文件
        for (long i = 0; i < len; i = i + 10240) {
            if (len - i > 1024) {
                map.get(bytes);
            } else {
                map.get(new byte[(int) (len - i)]);
            }
        }
        System.out.println("mappedbytebuffer time=" + (System.currentTimeMillis() - l));

        long start = System.currentTimeMillis();
        //普通IO流读取文件
        while (channel.read(allocate) > 0) {
            allocate.flip();
            allocate.clear();
        }
        System.out.println("channel.io time=" + (System.currentTimeMillis() - start));

    }
}
