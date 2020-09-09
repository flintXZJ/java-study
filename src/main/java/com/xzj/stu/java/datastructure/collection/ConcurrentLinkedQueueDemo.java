package com.xzj.stu.java.datastructure.collection;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 内存泄露问题
 * jdk1.8.0_20
 * 当集合中只有一个元素时，remove()只会将节点置为NULL，但是没有断开与ConcurrentLinkedQueue的连接。
 * 导致gc无法回收对象，表现为程序执行越来越慢，可用内存越来越少
 *
 *  [我的程序跑了60多小时，就为了让你看一眼JDK的BUG导致的内存泄漏](https://www.toutiao.com/i6848598759987741188/)
 * @author zhijunxie
 * @date 2020/7/23 11:19
 */
public class ConcurrentLinkedQueueDemo {
    public static void main(String[] args) {
        ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<Object>();
        //Required for the leak to appear.
        queue.add(new Object());
        Object object = new Object();
        int loops = 0;
        Runtime rt = Runtime.getRuntime();
        long last = System.currentTimeMillis();
        while (true) {
            if (loops % 10000 == 0) {
                long now = System.currentTimeMillis();
                long duration = now - last;
                last = now;
                System.err.printf("duration=%d q.size=%d memory max=%d free=%d total=%d%n", duration, queue.size(), rt.maxMemory(), rt.freeMemory(), rt.totalMemory());
            }
            queue.add(object);
            queue.remove(object);
            ++loops;
        }
    }
}
