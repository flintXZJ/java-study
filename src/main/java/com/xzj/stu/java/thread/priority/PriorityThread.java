package com.xzj.stu.java.thread.priority;

/**
 * @author zhijunxie
 * @date 2019/5/13
 */
public class PriorityThread implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 1000; i ++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
        }
    }
}
