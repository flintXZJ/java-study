package com.xzj.stu.java.lock.sync;

/**
 * @author zhijunxie
 * @date 2019/5/15
 */
public class SyncRunnable implements Runnable {
    @Override
//    public void run() {
    public synchronized void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000L);
                System.out.println(Thread.currentThread().getName() + " : " + i);
            }
        } catch (Exception e){}
    }
}
