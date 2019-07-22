package com.xzj.stu.java.thread.interrupt;

/**
 * 线程中断(interrupt)
 * 1、public void interrupt(); 中断线程。
 * 2、public static boolean interrupted(); 是一个静态方法，用于测试当前线程是否已经中断，
 * 并将线程的中断状态 清除。所以如果线程已经中断，调用两次interrupted，第二次时会返回false，
 * 因为第一次返回true后会清除中断状态。
 * 3、public boolean isInterrupted(); 测试线程是否已经中断。
 * @author zhijunxie
 * @date 2019/5/13
 */
public class InterruptTest {
    public static void main(String[] args) {
        Printer printer = new Printer();
        Thread thread = new Thread(printer, "printThread");
        thread.start();
        try {
            Thread.sleep(10L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("有紧急任务出现，需中断打印线程.");
        System.out.println("中断前的状态：" + thread.isInterrupted());
        // 中断打印线程
        thread.interrupt();
        System.out.println("中断前的状态：" + thread.isInterrupted());
    }
}
