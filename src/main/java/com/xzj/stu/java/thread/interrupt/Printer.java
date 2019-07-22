package com.xzj.stu.java.thread.interrupt;

/**
 * 线程中断(interrupt)
 * public void interrupt(); 中断线程。
 * public static boolean interrupted(); 是一个静态方法，用于测试当前线程是否已经中断，
 * 并将线程的中断状态 清除。所以如果线程已经中断，调用两次interrupted，第二次时会返回false，
 * 因为第一次返回true后会清除中断状态。
 * public boolean isInterrupted(); 测试线程是否已经中断。
 *
 * @author zhijunxie
 * @date 2019/5/13
 */
public class Printer implements Runnable {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println(Thread.currentThread().getName() + ": 打印中...");
        }
        if (Thread.currentThread().isInterrupted()) {
            //返回当前线程的状态，并清除状态
            System.out.println("interrupted:" + Thread.interrupted());
            System.out.println("isInterrupted:" + Thread.currentThread().isInterrupted());
        }
    }
}
