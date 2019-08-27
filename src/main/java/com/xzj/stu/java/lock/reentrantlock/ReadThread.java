package com.xzj.stu.java.lock.reentrantlock;

/**
 * @author zhijunxie
 * @date 2019/5/14
 */
public class ReadThread implements Runnable {
    private MyBuffer myBuffer;

    public ReadThread(MyBuffer myBuffer) {
        this.myBuffer = myBuffer;
    }

    @Override
    public void run() {
        try {
            myBuffer.read();
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + ": 读线程已经被中断.");
        }
    }
}
