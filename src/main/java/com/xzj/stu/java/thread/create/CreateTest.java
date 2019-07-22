package com.xzj.stu.java.thread.create;

/**
 * 创建线程
 *
 * @author zhijunxie
 * @date 2019/5/10
 */
public class CreateTest {
    public static void main(String[] args) throws Exception {
        //1、extends Thread
//        MyThread threadA = new MyThread("threadA");
//        MyThread threadB = new MyThread("threadB");
//        MyThread threadC = new MyThread("threadC");
//        threadA.start();
//        threadB.start();
//        threadC.start();

        //2、implements Runnable
//        MyRunnable myRunnable = new MyRunnable();
//        Thread thread1 = new Thread(myRunnable, "thread1");
//        Thread thread2 = new Thread(myRunnable, "thread2");
//        Thread thread3 = new Thread(myRunnable, "thread3");
//        thread1.start();
//        thread2.start();
//        thread3.start();

        //3、FutureTask
//        FutureTask<Integer> futureTask = new FutureTask<>(new MyCallable());
//        new Thread(futureTask, "futureTaskThread").start();
//        while (!futureTask.isDone()) {
//            try {
//                Thread.sleep(450L);
//                System.out.println("结果还在计算中，请稍等....");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("计算结果="+futureTask.get());

        //4、TimerTask
//        System.out.println(Thread.currentThread().getName()+": 线程开始...");
//        Timer timer = new Timer();
//        timer.schedule(new MyTimerTask(), 1000L);

//        ScheduledExecutorService;
//        ScheduledThreadPoolExecutor;

        //ExecutorService、Callable、Future
        //创建一个线程池
//        ExecutorService executorService = Executors.newFixedThreadPool(5);//不好
//        ExecutorService threadPoolExecutor = new ThreadPoolExecutor(5, 20, 2000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024));
//        List<Future> list = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            Callable myCallable = new MyCallable();
//            //执行任务并获取Future对象
//            Future result = threadPoolExecutor.submit(myCallable);
//            list.add(result);
//        }
//        //关闭线程池
//        threadPoolExecutor.shutdown();
//        //获取所有并发任务的运行结果
//        for (Future future : list) {
//            System.out.println("res: "+ future.get());
//        }

        //基于线程池的方式
//        ExecutorService threadPoolExecutor = new ThreadPoolExecutor(5, 20, 2000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024));
//        while (true) {
//            threadPoolExecutor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println(Thread.currentThread().getName()+ " thread is running...");
//                    try {
//                        Thread.sleep(2000L);
//                    } catch (Exception e) {}
//                    System.out.println(Thread.currentThread().getName()+ " thread is end.");
//                }
//            });
//        }
    }
}
