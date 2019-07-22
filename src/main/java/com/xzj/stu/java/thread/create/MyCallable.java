package com.xzj.stu.java.thread.create;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * @author zhijunxie
 * @date 2019/5/14
 */
public class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName()+": 计算开始...");
        Integer num = 0;
        Integer result = new Integer((new Random()).nextInt(10000));
        for (int i = 0; i < result; i++) {
            num = num + i;
        }
        Thread.sleep(2000L);
        System.out.println(Thread.currentThread().getName()+": 计算完成。");
        return num;
    }
}
