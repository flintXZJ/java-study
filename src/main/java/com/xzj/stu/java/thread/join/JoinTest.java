package com.xzj.stu.java.thread.join;

/**
 * 线程合并(join)
 *
 * 所谓合并，就是等待其它线程执行完，再执行当前线程，执行起来的效果就好像把其它线程合并到当前线程执行一样
 *
 * @author zhijunxie
 * @date 2019/5/13
 */
public class JoinTest {
    public static void main(String[] args) {
        System.out.println("主线程开启...");
        Thread thread = new Thread(new Plugin1());
        Thread thread2 = new Thread(new Plugin2());
        try {
            thread.start();
            thread.join();
            thread2.start();
            thread2.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("主线程结束，程序安装完成！");
    }
}
