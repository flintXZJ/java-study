package com.xzj.stu.java.lock.executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhijunxie
 * @date 2019/9/18 10:51
 */
public class ExecutorDemo {
    public static void main(String[] args) {
        System.out.println("starting...");
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("延迟3秒后执行");
            }
        }, 3, TimeUnit.SECONDS);
        System.out.println("end.");
    }
}
