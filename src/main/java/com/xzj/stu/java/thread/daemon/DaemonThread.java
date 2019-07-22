package com.xzj.stu.java.thread.daemon;

import java.util.concurrent.TimeUnit;

/**
 * @author zhijunxie
 * @date 2019/5/15
 */
public class DaemonThread implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("start ADaemon...");
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            System.out.println("Exiting via InterruptedException");
        } finally {
            System.out.println("This shoud be always run ?");
        }
    }
}
