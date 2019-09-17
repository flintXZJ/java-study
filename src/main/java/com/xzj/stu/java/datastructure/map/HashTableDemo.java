package com.xzj.stu.java.datastructure.map;

import java.util.Hashtable;

/**
 * Hashtable
 *
 * 线程安全
 *
 * 使用hash table存储数据，数组下标(hash & 0x7FFFFFFF) % tab.length计算。hash碰撞使用链表存储。
 *
 * key、value不可为null
 *
 * 元素个数大于阈值时，扩容1倍+1，新建数组并重新计算元素下标。
 *
 *
 * @author: xzj
 * @date: 2019/9/17 10:36
 */
public class HashTableDemo {
    public static void main(String[] args) {
        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("","");
    }
}
