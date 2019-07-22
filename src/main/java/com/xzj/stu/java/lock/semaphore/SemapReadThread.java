package com.xzj.stu.java.lock.semaphore;

/**
 * @author zhijunxie
 * @date 2019/5/14
 */
public class SemapReadThread implements Runnable {
    private XzjBuffer xzjBuffer;

    public SemapReadThread(XzjBuffer xzjBuffer) {
        this.xzjBuffer = xzjBuffer;
    }

    @Override
    public void run() {
        try {
            xzjBuffer.read();
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + ": 读线程已经被中断.....");
        }
    }
}
