package com.xzj.stu.java.thread.notify;

/**
 * Object的wait()、notify()、notifyAll()方法
 *
 * 1、Object的wait方法有三个重载方法，其中一个方法wait() 是无限期(一直)等待，直到其它线程调用notify或notifyAll方法唤醒当前的线程；
 * 另外两个方法wait(long timeout) 和wait(long timeout, int nanos)允许传入 当前线程在被唤醒之前需要等待的时间，timeout为毫秒数，nanos为纳秒数
 * 2、notify方法只唤醒一个等待（对象的）线程并使该线程开始执行
 * 3、notifyAll 会唤醒所有等待(对象的)线程，尽管哪一个线程将会第一个处理取决于操作系统的实现
 *
 * @author zhijunxie
 * @date 2019/5/13
 */
public class WaitNotifyTest {
    public static void main(String[] args) {
        System.out.println("beginning...");

        Message msg = new Message("msg11");

        Waiter waiter1 = new Waiter(msg);
        new Thread(waiter1, "xzjWaiter1").start();
        Waiter waiter2 = new Waiter(msg);
        new Thread(waiter2, "xzjWaiter2").start();
        Waiter waiter3 = new Waiter(msg);
        new Thread(waiter3, "xzjWaiter3").start();

        Notifier notifier = new Notifier(msg);
        new Thread(notifier, "xzjNotifier").start();

        System.out.println("All the threads are started");
    }
}
