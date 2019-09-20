package com.xzj.stu.java.datastructure.collection;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * 支持优先级排序的无界阻塞队列
 * 由数组结构组成，初始容量11，容量已满自动扩容
 *
 * ReentrantLock实现线程安全
 *
 * @author zhijunxie
 * @date 2019/9/19 14:17
 */
public class PriorityBlockingQueueDemo {
    public static void main(String[] args) {
        PriorityBlockingQueue<String> priorityBlockingQueue = new PriorityBlockingQueue<>();
        priorityBlockingQueue.offer("xie");
    }
}
