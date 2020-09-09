package com.xzj.stu.java.thread.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author zhijunxie
 * @date 2020/4/9 11:02
 */
public class ThreadPoolDeadLockTest {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public class RenderPageTask implements Callable<String> {
        @Override
        public String call() throws Exception {
            Future<String> header, footer;
            header = executorService.submit(() -> {
                System.out.println("header start");
                return "header";
            });
            footer = executorService.submit(() -> {
                System.out.println("footer start");
                return "footer";
            });
            return header.get() + footer.get();
        }
    }

    public void submitTask() {
        executorService.submit(new RenderPageTask());
    }

    public static void main(String[] args) {
        ThreadPoolDeadLockTest threadPoolDeadLockTest = new ThreadPoolDeadLockTest();
        threadPoolDeadLockTest.submitTask();
    }
}
