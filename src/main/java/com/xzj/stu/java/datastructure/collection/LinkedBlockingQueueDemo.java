package com.xzj.stu.java.datastructure.collection;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author zhijunxie
 * @date 2019/9/18 20:53
 */
public class LinkedBlockingQueueDemo {
    public static void main(String[] args) throws Exception {
        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>(5);
        // 将指定元素插入此队列中（如果立即可行且不会违反容量限制），成功时返回 true，
        // 如果当前没有可用的空间，则抛出 IllegalStateException。如果该元素是NULL，则会抛出NullPointerException异常。
        linkedBlockingQueue.add("xie");
        // 将指定元素插入此队列中，将等待可用的空间（如果有必要）
        linkedBlockingQueue.put("zhi");
        // 将指定元素插入此队列中（如果立即可行且不会违反容量限制），成功时返回 true，如果当前没有可用的空间，则返回 false。
        linkedBlockingQueue.offer("jun");
        // 可以设定等待的时间，如果在指定的时间内，还不能往队列中加入BlockingQueue，则返回失败。
        linkedBlockingQueue.offer("test", 3, TimeUnit.SECONDS);
        System.out.println(JSONObject.toJSONString(linkedBlockingQueue));

        // 取走BlockingQueue里排在首位的对象,若不能立即取出,则可以等time参数规定的时间,取不到时返回null;
        linkedBlockingQueue.poll();

        // 取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到BlockingQueue有新的数据被加入。
        linkedBlockingQueue.take();

        // 一次性从BlockingQueue获取所有可用的数据对象（还可以指定获取数据的个数），通过该方法，可以提升获取数据效率；不需要多次分批加锁或释放锁。
//        linkedBlockingQueue.drainTo();

        System.out.println(JSONObject.toJSONString(linkedBlockingQueue));
    }
}
