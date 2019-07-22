package com.xzj.stu.java.thread.create;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhijunxie
 * @date 2019/5/14
 */
public class MyRunnable implements Runnable {
    private AtomicInteger ticket = new AtomicInteger(0);

    @Override
    public void run() {
        //几个线程共同卖100张票
        while (ticket.get() < 100) {
            String threadName = Thread.currentThread().getName();
            ticket.getAndIncrement();
            System.out.println(threadName + ": 售出第" + ticket + "张票");
        }
    }

    public int getTicket() {
        return ticket.get();
    }
}
