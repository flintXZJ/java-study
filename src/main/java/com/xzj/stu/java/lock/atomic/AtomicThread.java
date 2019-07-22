package com.xzj.stu.java.lock.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhijunxie
 * @date 2019/5/14
 */
public class AtomicThread implements Runnable {

//    static int num = 0;
//    static volatile int num = 0;

    static AtomicInteger num = new AtomicInteger(0);

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
//            num = num + 1;
            num.getAndIncrement();
        }
    }
}
