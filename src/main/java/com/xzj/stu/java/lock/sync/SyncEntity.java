package com.xzj.stu.java.lock.sync;

/**
 * @author zhijunxie
 * @date 2019/5/15
 */
public class SyncEntity {
    public synchronized void fun1() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ": synchronized fun1()=" + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void fun2() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ": synchronized fun2()=" + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized static void fun3() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ": synchronized static fun3()=" + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void fun4() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ": fun4()=" + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void fun5() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + ": static fun5()=" + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
