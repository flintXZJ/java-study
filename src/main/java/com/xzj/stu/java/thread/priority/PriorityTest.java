package com.xzj.stu.java.thread.priority;

/**
 * 优先级(Priority)
 *
 * 线程优先级是指获得CPU资源的优先程序。优先级高的容易获得CPU资源，
 * 优先级低的较难获得CPU资源，表现出来的情况就是优先级越高执行的时间越多。
 *
 * Java中通过getPriority和setPriority方法获取和设置线程的优先级。
 * Thread类提供了三个表示优先级的常量：
 *      MIN_PRIORITY优先级最低，为1；
 *      NORM_PRIORITY是正常的优先级，为5；
 *      MAX_PRIORITY优先级最高，为10。
 * 我们创建线程对象后，如果不显示的设置优先级的话，默认为5。
 *
 * @author zhijunxie
 * @date 2019/5/13
 */
public class PriorityTest {
    public static void main(String[] args) {
        //创建三个线程
        Thread thread1 = new Thread(new PriorityThread(), "Thread1");
        Thread thread2 = new Thread(new PriorityThread(), "Thread2");
        Thread thread3 = new Thread(new PriorityThread(), "Thread3");
        //设置优先级
        thread1.setPriority(Thread.MAX_PRIORITY);
//        thread2.setPriority(8);
        thread3.setPriority(Thread.MIN_PRIORITY);
        //开始执行线程
        thread3.start();
        thread2.start();
        thread1.start();
    }
}
