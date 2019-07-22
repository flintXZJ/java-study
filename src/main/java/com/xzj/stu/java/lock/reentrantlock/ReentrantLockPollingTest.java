package com.xzj.stu.java.lock.reentrantlock;

/**
 * ReentrantLock 实现可轮询的锁请求
 *
 * @author zhijunxie
 * @date 2019/5/14
 */
public class ReentrantLockPollingTest {
    public static void main(String[] args) {
        MyBuffer myBuffer = new MyBuffer();

        //开启写线程
        Thread writeThread = new Thread(new WriteThread(myBuffer), "WriteThread");
        writeThread.start();

        //开启读线程
        Thread readThread = new Thread(new ReadThread2(myBuffer), "ReadThread");
        readThread.start();
    }
}
