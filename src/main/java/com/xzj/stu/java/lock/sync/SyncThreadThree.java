package com.xzj.stu.java.lock.sync;

/**
 * @author zhijunxie
 * @date 2019/5/13
 */
public class SyncThreadThree implements Runnable {
    private static int count = 0;

    public void method() {
        synchronized (SyncThreadThree.class) {
            for (int i = 0; i < 5; i++) {
                try {
                    System.out.println(Thread.currentThread().getName() + ": c" + (count++));
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        method();
    }
}
