package com.xzj.stu.java.thread.notify;

/**
 * 一个Notifier类，处理Message对象并调用notify方法唤醒等待Message对象的线程。
 * 注意synchronized代码块被用于持有Message对象的监视器
 *
 * @author zhijunxie
 * @date 2019/5/13
 */
public class Notifier implements Runnable {
    private Message msg;

    public Notifier(Message msg) {
        this.msg = msg;
    }
    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + ": started.");
        try {
            Thread.sleep(1000L);
            synchronized (msg) {
                msg.setMsg(name +" Notifier work done");
//                msg.notify();
                msg.notifyAll();
                System.out.println(name + ": end.");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
