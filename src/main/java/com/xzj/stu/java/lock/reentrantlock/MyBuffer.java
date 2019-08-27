package com.xzj.stu.java.lock.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhijunxie
 * @date 2019/5/14
 */
public class MyBuffer {
    /**
     * 默认非公平锁
     */
    private ReentrantLock lock = new ReentrantLock();

    public void write() {
        try {
            lock.lock();

            long writeStartTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + ": 写操作开始，预计执行时间10秒...");
            while (System.currentTimeMillis() - writeStartTime < 10000) {
            }
            System.out.println(Thread.currentThread().getName() + ": 写操作完成.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 响应式中断锁
     * @throws InterruptedException
     */
    public void read() throws InterruptedException {
        try {
            //lock()方法设置锁机制为“忽略中断锁”，当调用此方法的线程自身或被其他线程请求中断(interrupt)时，操作线程不响应请求，继续处于等待状态
            //lockInterruptibly()方法可设置锁机制为“响应式中断锁”，当调用此方法的线程自身或被其他线程请求中断(interrupt)时，线程会相应请求，并在调用当前方法的操作时中断线程，中断后不操作线程后续任务
            //以上的响应指的是线程正在获取锁的过程中被请求中断，若线程在其他非阻塞与阻塞状态时被请求中断，lockInterruptibly()是无法响应中断的，
            //非阻塞状态可根据中断标记位Thread.currentThread().isInterrupted()，阻塞状态可通过抛出异常InterruptedException来中断线程
            //lock.lock();
            lock.lockInterruptibly();

//            //写线程4秒，第三方线程在读写线程启动5秒后中断读线程；实验能否中断已获取锁的线程？
            System.out.println(Thread.currentThread().getName() + ": 读操作获取锁...");
//            long readStartTime = System.currentTimeMillis();
//            while (System.currentTimeMillis() - readStartTime < 5000) {
//            }

            System.out.println(Thread.currentThread().getName() + ": 读操作完成.");
        } finally {
            lock.unlock();
        }
    }

    /**
     * 实现可轮询的锁请求
     * 定时锁请求
     *
     * @throws InterruptedException
     */
    public void readPolling() throws InterruptedException {
//        if (lock.tryLock(1500L, TimeUnit.MILLISECONDS)) {//可以指定轮询时间,定时锁请求
        if (lock.tryLock()) {
            try {
                //锁可用，则成功获取锁；获取锁后进行处理
                lock.lockInterruptibly();
                System.out.println(Thread.currentThread().getName() + ": 获取锁之后，读操作完成...");
            } finally {
                lock.unlock();
            }
        } else {
            //锁不可用，其他处理方法
            System.out.println(Thread.currentThread().getName() + ": 无锁，读操作完成.");
        }
    }
}
