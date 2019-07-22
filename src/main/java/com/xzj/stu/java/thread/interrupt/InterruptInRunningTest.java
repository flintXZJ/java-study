package com.xzj.stu.java.thread.interrupt;

/**
 * 中断非阻塞状态线程
 *
 * @author zhijunxie
 * @date 2019/5/15
 */
public class InterruptInRunningTest implements Runnable{
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println(Thread.currentThread().getName()+": thread is running...");
            long starttime = System.currentTimeMillis();
            while (System.currentTimeMillis()-starttime < 1000){}
        }
        System.out.println(Thread.currentThread().getName()+": thread is interrupt.");
    }

    public static void main(String[] args) throws Exception {
        System.out.println("程序开始执行....");
        InterruptInRunningTest interruptInRunningTest = new InterruptInRunningTest();
        Thread thread = new Thread(interruptInRunningTest, "interruptInRunningTest");
        thread.start();
        Thread.sleep(3000L);
        System.out.println("主线程请求中断子线程....");
        //请求中断线程
        thread.interrupt();
        Thread.sleep(3000L);
        System.out.println("主线程结束....");
    }
}
