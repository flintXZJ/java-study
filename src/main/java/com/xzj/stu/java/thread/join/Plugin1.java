package com.xzj.stu.java.thread.join;

/**
 * @author zhijunxie
 * @date 2019/5/13
 */
public class Plugin1 implements Runnable {
    @Override
    public void run() {
        System.out.println("插件1开始安装.");
        System.out.println("安装中...");
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("插件1完成安装.");
    }
}
