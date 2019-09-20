package com.xzj.stu.java.lock;

import java.util.concurrent.CountDownLatch;

/**
 * 线程计数器
 *
 * CountDownLatch内部类Sync继承自AbstractQueuedSynchronizer(AQS)。
 * new CountDownLatch(Count), Count赋值给AQS中的同步state，每调用一次countDown()，就会使用cas设置state-1
 *
 *
 * @author zhijunxie
 * @date 2019/9/19 14:58
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+"正在执行");
                try {
                    Thread.sleep(2000L);
                } catch (Exception e) {}
                System.out.println(Thread.currentThread().getName()+"执行完毕");
                countDownLatch.countDown();
            }
        },"countDownThread1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+"正在执行");
                try {
                    Thread.sleep(1000L);
                } catch (Exception e) {}
                System.out.println(Thread.currentThread().getName()+"执行完毕");
                countDownLatch.countDown();
            }
        },"countDownThread2").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+"正在执行");
                try {
                    Thread.sleep(3000L);
                } catch (Exception e) {}
                System.out.println(Thread.currentThread().getName()+"执行完毕");
                countDownLatch.countDown();
            }
        },"countDownThread3").start();

        System.out.println("等待3个子线程执行完毕...");
        countDownLatch.await();
        System.out.println("3个子线程已经执行完毕");
    }
}
