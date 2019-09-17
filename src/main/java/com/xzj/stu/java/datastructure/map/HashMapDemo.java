package com.xzj.stu.java.datastructure.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HashMap
 *
 * 底层使用Node数组使用，数组下标使用(table.length - 1) & hash计算。hash碰撞时，链表存储。当链表节点数大于8时，转化为红黑树
 * 当map中元素个数 大于阈值(0.75*tablesize)时, 扩容1倍。重新计算每个元素的数组下标。红黑树可能转化为链表。
 *
 * 线程不安全
 * key不可重复，可以为null
 * 值可重复，可以为null
 *
 *
 * @author: xzj
 * @date: 2019/9/17 10:35
 */
public class HashMapDemo {
    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>(16);
        hashMap.put("1", "xie");
        hashMap.get("1");

        //使用synchronized关键字实现线程安全
        Map<String, String> map = Collections.synchronizedMap(hashMap);

        //使用cas实现线程安全
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>(16);
        concurrentHashMap.put("123","xzj");
    }
}
