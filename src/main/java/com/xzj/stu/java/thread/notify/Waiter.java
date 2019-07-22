package com.xzj.stu.java.thread.notify;

/**
 * 一个Waiter类，等待其它的线程调用notify方法以唤醒线程完成处理。
 * 注意等待线程必须通过加synchronized同步锁拥有Message对象的监视器
 *
 * @author zhijunxie
 * @date 2019/5/13
 */
public class Waiter implements Runnable {
    private Message msg;

    public Waiter(Message msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        synchronized (msg) {
            try {
                System.out.println(name + ": waiting to get notified at time:" + System.currentTimeMillis());
                msg.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(name + ": waiter thread got notified at time:" + System.currentTimeMillis());
            //process the message now
            System.out.println(name + ": processed: " + msg.getMsg());
        }
    }
}
