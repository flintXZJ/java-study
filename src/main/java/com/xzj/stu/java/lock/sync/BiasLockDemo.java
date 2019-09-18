package com.xzj.stu.java.lock.sync;

/**
 * 偏向锁升级为轻量级锁
 *
 * 验证是否stop the world
 * 设置JVM参数：
 *  -XX:+PrintGCApplicationStoppedTime 会打出系统停止的时间
 *  -XX:+PrintSafepointStatistics -XX:PrintSafepointStatisticsCount=1 这两个参数会打印出详细信息，
 *  可以查看到使用偏向锁导致的停顿，时间非常短暂，但是争用严重的情况下，停顿次数也会非常多
 *
 * https://blog.csdn.net/zqz_zqz/article/details/70233767
 *
 * 高并发的应用会禁用掉偏向锁
 * 开启偏向锁：-XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
 * 关闭偏向锁：-XX:-UseBiasedLocking
 *
 * @author zhijunxie
 * @date 2019/9/18 17:10
 */
public class BiasLockDemo {
    private static Object lock = new Object();

    public static void main(String[] args) throws Exception {
        System.out.println(Thread.currentThread().getName() + ": starting...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "准备获取锁...");
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + "获取到锁.");
                    System.out.println(Thread.currentThread().getName() + " 休眠5秒.");
                    long start = System.currentTimeMillis();
                    try {
                        Thread.sleep(5000L);
                        System.out.println(Thread.currentThread().getName() + " 休眠结束.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 结束.");
                }
            }
        }, "demoThread1").start();

        //保证第一个线程已经启动
        Thread.sleep(1000L);

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "准备获取锁...");
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + "获取到锁.");
                    System.out.println(Thread.currentThread().getName() + " 休眠5秒.");
                    try {
                        Thread.sleep(5000L);
                        System.out.println(Thread.currentThread().getName() + " 休眠结束.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 结束.");
                }
            }
        }, "demoThread2").start();

        //保证1、2线程执行完
        Thread.sleep(20000L);
        System.out.println(Thread.currentThread().getName() + ": end.");


        /**
         * main: starting...
         * demoThread1准备获取锁...
         * demoThread1获取到锁.
         * demoThread1 休眠5秒.
         * demoThread2准备获取锁...
         *          vmop                    [threads: total initially_running wait_to_block]    [time: spin block sync cleanup vmop] page_trap_count
         * 4.367: EnableBiasedLocking              [      12          0              0    ]      [     0     0     0     0     0    ]  0
         * Total time for which application threads were stopped: 0.0022515 seconds
         * demoThread1 结束.
         * demoThread2获取到锁.
         * demoThread2 休眠5秒.
         * demoThread2 休眠结束.
         * demoThread2 结束.
         * main: end.
         *          vmop                    [threads: total initially_running wait_to_block]    [time: spin block sync cleanup vmop] page_trap_count
         * 11.337: no vm operation                  [       9          0              1    ]      [     0     0     0     0   310    ]  0
         *
         * Polling page always armed
         * EnableBiasedLocking                1
         *     0 VM operations coalesced during safepoint
         * Maximum sync time      0 ms
         * Maximum vm operation time (except for Exit VM operation)      0 ms
         */
    }
}
