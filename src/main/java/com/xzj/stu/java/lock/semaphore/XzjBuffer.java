package com.xzj.stu.java.lock.semaphore;

import java.util.concurrent.Semaphore;

/**
 * @author zhijunxie
 * @date 2019/5/14
 */
public class XzjBuffer {
    private Semaphore semaphore = new Semaphore(3);

    public void write() {
        try {
            //忽略中断锁
            semaphore.acquireUninterruptibly();

            long writeStartTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + ": 写操作开始，预计执行时间2秒....");
            while (System.currentTimeMillis() - writeStartTime < 2000) {
            }
            System.out.println(Thread.currentThread().getName() + ": 写操作完成....");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    /**
     *
     * @throws InterruptedException
     */
    public void read() throws InterruptedException {
        try {
            //忽略中断锁
//            semaphore.acquireUninterruptibly(2);
            //默认为可响应中断锁
            semaphore.acquire();

            long writeStartTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + ": 读操作开始，预计执行时间1.5秒....");
            while (System.currentTimeMillis() - writeStartTime < 1500) {
            }

            System.out.println(Thread.currentThread().getName() + ": 读操作完成....");
        } finally {
            semaphore.release();
        }
    }

    /**
     * 实现可轮询的锁请求
     * 定时锁请求
     *
     * @throws InterruptedException
     */
    public void readPolling() throws InterruptedException {
//        if (semaphore.tryAcquire(1500L, TimeUnit.MILLISECONDS)) {//可以指定轮询时间,定时锁请求
        if (semaphore.tryAcquire()) {
            try {
                //锁可用，则成功获取锁；获取锁后进行处理
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + ": 获取锁之后，读操作完成....");
            } finally {
                semaphore.release();
            }
        } else {
            //锁不可用，其他处理方法
            System.out.println(Thread.currentThread().getName() + ": 无锁，读操作完成....");
        }
    }
}
