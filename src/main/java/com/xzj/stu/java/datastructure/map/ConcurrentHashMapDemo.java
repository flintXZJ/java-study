package com.xzj.stu.java.datastructure.map;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 如何实现？？
 *
 * @author zhijunxie
 * @date 2019/9/17 15:26
 */
public class ConcurrentHashMapDemo {
    public static void main(String[] args) {
        //使用cas实现线程安全
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>(16);
        concurrentHashMap.put("123","xzj");
    }
}
