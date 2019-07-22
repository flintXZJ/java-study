package com.xzj.stu.java.thread.create;

import java.util.TimerTask;

/**
 * @author zhijunxie
 * @date 2019/5/15
 */
public class MyTimerTask extends TimerTask {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+": Timer子线程已经成功启动...");
    }
}
