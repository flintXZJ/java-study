package com.xzj.stu.java.thread.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author zhijunxie
 * @date 2019/12/30 14:17
 */
public class ThreadPoolTest {
    public static void main(String[] args) {
        ThreadPoolTest threadPoolTest = new ThreadPoolTest();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        Future<String> submit = threadPoolTest.submit();
                        try {
                            String result = submit.get();
                            System.out.println(Thread.currentThread().getName()+result);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        //模拟gc
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.gc();
                }
            }
        }).start();
    }

    public Future<String> submit() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        FutureTask<String> stringFutureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(50);
                return System.currentTimeMillis() + "";
            }
        });

        executorService.execute(stringFutureTask);
        return stringFutureTask;
    }
}
