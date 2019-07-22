package com.xzj.stu.java.thread.daemon;

/**
 * @author zhijunxie
 * @date 2019/5/15
 */
public class DaemonTest {
    public static void main(String[] args) throws Exception {
        System.out.println("main thread starting...");
        Thread thread = new Thread(new DaemonThread());
        //设置为守护线程，则主线程结束守护进程就结束了，不会执行DaemonThread中的finally输出语句
        //不设置为守护线程时，会执行finally输出语句
        thread.setDaemon(true);
        thread.start();
        Thread.sleep(1000L);
        System.out.println("main thread end.");
    }
}
