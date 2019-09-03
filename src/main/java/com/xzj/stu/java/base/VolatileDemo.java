package com.xzj.stu.java.base;

/**
 * @author zhijunxie
 * @date 2019/8/30 16:58
 */
public class VolatileDemo {
    public static volatile int race = 0;
    private static final int THREADS_COUNT = 20;

    public static void increase() {
        race++;
    }

    public String setAbc(String abc) {
        return race + abc;
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+" starting...");
                    System.out.println(Thread.currentThread().getName()+" race=" + race);
                    for (int j = 0; j < 10000; j++) {
                        increase();
                    }
                    System.out.println(Thread.currentThread().getName()+" end.");
                }
            });
            threads[i].start();
        }

        while (Thread.activeCount() > 1) {
            Thread.yield();
        }
        System.out.println(race);
    }
}
