package com.xzj.stu.java.thread.interrupt;

/**
 * 线程中断(interrupt)
 * 1、public void interrupt(); 中断线程。
 * 2、public static boolean interrupted(); 是一个静态方法，用于测试当前线程是否已经中断，
 * 并将线程的中断状态 清除。所以如果线程已经中断，调用两次interrupted，第二次时会返回false，
 * 因为第一次返回true后会清除中断状态。
 * 3、public boolean isInterrupted(); 测试线程是否已经中断。
 *
 * @author zhijunxie
 * @date 2019/5/13
 */
public class InterruptTest {
    public static void main(String[] args) throws Exception {
//        Printer printer = new Printer();
//        Thread thread = new Thread(printer, "printThread");
//        thread.start();
//        try {
//            Thread.sleep(10L);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("有紧急任务出现，需中断打印线程.");
//        System.out.println("中断前的状态：" + thread.isInterrupted());
//        // 中断打印线程
//        thread.interrupt();
//        System.out.println("中断前的状态：" + thread.isInterrupted());

        //1、线程中断失败：虽然给线程发出了中断信号，但程序中并没有响应中断信号的逻辑，所以程序不会有任何反应
//        InterruptTest.test1();

        //2、线程中断成功
//        InterruptTest.test2();

        //3、线程中断失败: 因为sleep()方法被中断后会清除中断标记，所以循环会继续运行
//        InterruptTest.test3();

        //4、线程中断成功：在 sleep()方法被中断并清除标记后手动重新中断当前线程，然后程序接收中断信号返回退出
        InterruptTest.test4();
    }

    private static void test1() {
        Thread thread1 = new Thread(() -> {
            System.out.println("线程开始");
            while (true) {
                Thread.yield();
            }
        });
        thread1.start();
        thread1.interrupt();
    }

    private static void test2() {
        Thread thread2 = new Thread(() -> {
            while (true) {
                Thread.yield();
                // 响应中断
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("线程被中断，程序退出。");
                    return;
                }
            }
        });
        thread2.start();
        thread2.interrupt();
    }

    private static void test3() throws Exception {
        Thread thread3 = new Thread(() -> {
            while (true) {
                // 响应中断
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("线程被中断，程序退出。");
                    return;
                }
                try {
                    /**
                     * @throws InterruptedException
                     *          if any thread has interrupted the current thread. The
                     *          <i>interrupted status</i> of the current thread is
                     *          cleared when this exception is thrown.
                     */
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println("线程休眠被中断，程序退出。");
                }
            }
        });
        thread3.start();
        Thread.sleep(2000);
        thread3.interrupt();
    }

    private static void test4() throws Exception {
        Thread thread4 = new Thread(() -> {
            while (true) {
                // 响应中断
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("线程被中断，程序退出。");
                    return;
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println("线程休眠被中断，程序退出。");
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread4.start();
        Thread.sleep(2000);
        thread4.interrupt();
    }
}
