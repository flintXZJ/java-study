package com.xzj.stu.java.lock.semaphore;

/**
 * @author zhijunxie
 * @date 2019/5/14
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        XzjBuffer xzjBuffer = new XzjBuffer();

        Thread semapWriteThread = new Thread(new SemapWriteThread(xzjBuffer), "SemapWriteThread");
        semapWriteThread.start();

        Thread semapReadThread1 = new Thread(new SemapReadThread(xzjBuffer), "SemapReadThread1");
        Thread semapReadThread2 = new Thread(new SemapReadThread(xzjBuffer), "SemapReadThread2");
        Thread semapReadThread3 = new Thread(new SemapReadThread(xzjBuffer), "SemapReadThread3");
        Thread semapReadThread4 = new Thread(new SemapReadThread(xzjBuffer), "SemapReadThread4");
        Thread semapReadThread5 = new Thread(new SemapReadThread(xzjBuffer), "SemapReadThread5");
        semapReadThread1.start();
        semapReadThread2.start();
        semapReadThread3.start();
        semapReadThread4.start();
        semapReadThread5.start();

        //开启第三者线程，用于监听并中断读线程: 等待1.6秒之后中断读线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 1600) {
                }
                System.out.println(Thread.currentThread().getName() + ": 读线程等待时间已超过1.6秒，请求中断....");
                semapReadThread1.interrupt();
                semapReadThread2.interrupt();
                semapReadThread3.interrupt();
                semapReadThread4.interrupt();
                semapReadThread5.interrupt();
            }
        }, "ThirdThread").start();
    }
}
