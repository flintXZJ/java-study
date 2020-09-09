package com.xzj.stu.java.datastructure.map;

import org.apache.commons.collections4.map.LRUMap;

/**
 * @author zhijunxie
 * @date 2020/7/22 14:30
 */
public class LRUMapDemo {
    public static void main(String[] args) {
        LRUMap<String, Integer> lruMap = new LRUMap<>(100);
        lruMap.put("id",1);
        lruMap.putIfAbsent("id", 1);
    }
}
