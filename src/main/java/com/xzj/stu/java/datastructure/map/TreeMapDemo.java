package com.xzj.stu.java.datastructure.map;

import java.util.TreeMap;

/**
 * TreeMap
 *
 * 线程不安全
 * 底层二叉树实现(红黑树)
 *
 *
 * @author: xzj
 * @date: 2019/9/17 10:36
 */
public class TreeMapDemo {
    public static void main(String[] args) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("x1", "xx1");
        treeMap.remove("x1");
    }
}
