package com.xzj.stu.java.thread.join;

/**
 * @author zhijunxie
 * @date 2019/5/13
 */
public class Plugin2 implements Runnable {
    @Override
    public void run() {
        System.out.println("插件2开始安装.");
        System.out.println("安装中...");
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("插件2完成安装.");
    }
}
