package com.xzj.stu.java.lock.sync;

/**
 * synchronized 实现原理
 *
 * @author zhijunxie
 * @date 2019/8/29 10:56
 */
public class SynchronizedDemo {

    private int count = 0;

    private static long sum = 0L;

    public SynchronizedDemo() {

    }

    public synchronized void fun1() {
        count++;
    }

    public static synchronized void fun2() {
        sum = sum + 100L;
    }

    public void fun3() {
        synchronized (this) {
            count++;
        }
    }

    public void fun4() {
        synchronized (SynchronizedDemo.class) {
            sum = sum +200L;
        }
    }

}
