package com.xzj.stu.java.lock.atomic;

/**
 * @author zhijunxie
 * @date 2019/5/14
 */
public class AtomicTest {
    public static void main(String[] args) throws Exception {
        AtomicThread atomicThread = new AtomicThread();

        Thread thread1 = new Thread(atomicThread);
        Thread thread2 = new Thread(atomicThread);
        thread1.start();
        thread2.start();
        Thread.sleep(500L);

        System.out.println("AtomicThread.num = "+AtomicThread.num.get());
    }
}
