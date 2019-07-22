package com.xzj.stu.java.thread.interrupt;

/**
 * 中断阻塞状态线程
 *
 * @author zhijunxie
 * @date 2019/5/15
 */
public class InterruptInBlockedTest implements Runnable {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println(Thread.currentThread().getName()+": 线程开始执行....");
            try {
                /*
                 * 如果线程阻塞，将不会去检查中断信号量stop变量，所 以thread.interrupt()
                 * 会使阻塞线程从阻塞的地方抛出异常，让阻塞线程从阻塞状态逃离出来，并
                 * 进行异常块进行 相应的处理
                 */
                Thread.sleep(1000);// 线程阻塞，如果线程收到中断操作信号将抛出异常
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+": 线程接受到被请求的中断操作，正在响应是否中断...");
                /*
                 * 如果线程在调用 Object.wait()方法，或者该类的 join() 、sleep()方法
                 * 过程中受阻，则其中断状态将被清除
                 */
                System.out.println(Thread.currentThread().isInterrupted());// false
                //中不中断由自己决定，如果需要真真中断线程，则需要重新设置中断位，
                //如果不需要，则不用调用
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(Thread.currentThread().getName()+": 线程已经被中断.......");
    }

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(new InterruptInBlockedTest(), "InterruptInBlockedTest");
        System.out.println("程序开始执行....");
        thread.start();
        Thread.sleep(3000);
        System.out.println("主线程请求中断子线程....");
        //请求中断
        thread.interrupt();
        Thread.sleep(3000);
        System.out.println("主线程结束....");
    }
}
