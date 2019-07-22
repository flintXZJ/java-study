package com.xzj.stu.java.thread.create;

/**
 * @author zhijunxie
 * @date 2019/5/14
 */
public class MyThread extends Thread {
    //静态变量属于类所有，该类的所有对象均为操作同一个内存地址。多线程执行时与非静态属性不同
//    private static int ticket = 0;
    private int ticket = 0;

    public MyThread(String threadName) {
        super(threadName);
    }

    @Override
    public void run() {
        // 每个线程都拥有100张票，各自卖各自得票
        while (ticket < 100) {
            String threadName = Thread.currentThread().getName();
            ticket++;
            System.out.println(threadName + ": 售出第" + ticket + "张票");
        }
    }
}
