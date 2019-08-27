package com.xzj.stu.java.lock.reentrantlock;

/**
 * ReentrantLock 可响应中断锁
 *
 * @author zhijunxie
 * @date 2019/5/14
 */
public class ReentrantLockInterruptTest {
    public static void main(String[] args) {
        MyBuffer myBuffer = new MyBuffer();

        //开启写线程
        Thread writeThread = new Thread(new WriteThread(myBuffer), "WriteThread");
        writeThread.start();

        //开启读线程
        Thread readThread = new Thread(new ReadThread(myBuffer), "ReadThread");
        readThread.start();

        //开启第三个线程，用于监听并中断读线程: 等待5秒之后中断读线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 5000) {

                }
                System.out.println(Thread.currentThread().getName() + ": 读线程等待时间已超过5秒，请求中断...");
                readThread.interrupt();
            }
        }, "ThirdThread").start();
    }
}
