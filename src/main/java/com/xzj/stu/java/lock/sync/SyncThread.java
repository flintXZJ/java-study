package com.xzj.stu.java.lock.sync;

/**
 * 修饰一个代码块
 * 一个线程访问一个对象中的synchronized(this)同步代码块时，其他试图访问该对象的线程将被阻塞
 * @author zhijunxie
 * @date 2019/5/13
 */
public class SyncThread implements Runnable {
    private int count = 0;

    public SyncThread() {
    }

    @Override
    public void run() {
        synchronized (this) {
            for (int i = 0; i < 5; i++) {
                try {
                    count++;
                    System.out.println(Thread.currentThread().getName() + " count:" + count);
                    Thread.sleep(500L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
