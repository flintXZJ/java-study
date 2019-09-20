package com.xzj.stu.java.datastructure.collection;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 由数组结构组成的有界阻塞队列
 * Object[] items数组存储元素
 *
 * ReentrantLock实现线程安全
 *
 * 队列各种方法参照：LinkedBlockingQueueDemo.java
 *
 * @author zhijunxie
 * @date 2019/9/19 14:06
 */
public class ArrayBlockingQueueDemo {
    public static void main(String[] args) {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(10);
        //如果容量已满，会抛出异常
        arrayBlockingQueue.add("xie");
    }
}
