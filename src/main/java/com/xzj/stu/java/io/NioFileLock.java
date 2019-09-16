package com.xzj.stu.java.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 文件锁
 *
 * @author tiany
 */
public class NioFileLock {
    private static final Logger LOGGER = LoggerFactory.getLogger(NioFileLock.class);

    public static void main(String[] args) throws IOException {
        FileChannel fileChannel = new RandomAccessFile("D:\\1.txt", "rw").getChannel();
        // 写入4个字节
        fileChannel.write(ByteBuffer.wrap("abcd".getBytes()));
        // 将前2个字节区域锁定（共享锁）
        FileLock lock1 = fileChannel.lock(0, 2, true);
        // 当前锁持有锁的类型（共享锁/独占锁）
        System.out.println(lock1.isShared());
        // IOException 不能修改只读的共享区域
//         fileChannel.write(ByteBuffer.wrap("a".getBytes()));
        // 可以修改共享锁之外的区域，从第三个字节开始写入
        fileChannel.write(ByteBuffer.wrap("ef".getBytes()), 2);

        // OverlappingFileLockException 重叠的文件锁异常
//         FileLock lock2 = fileChannel.lock(0, 3, true);
//         FileLock lock3 = fileChannel.lock(0, 3, false);

        //得到创建锁的通道
        lock1.channel();

        //锁的起始位置
        long position = lock1.position();

        //锁的范围
        long size = lock1.size();

        //判断锁是否与指定文件区域有重叠
        System.out.println(lock1.overlaps(position, size));

        // 记得用try/catch/finally{release()}方法释放锁
        lock1.release();
    }
}
