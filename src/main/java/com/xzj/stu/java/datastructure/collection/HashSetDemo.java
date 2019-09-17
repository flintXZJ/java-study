package com.xzj.stu.java.datastructure.collection;

import com.alibaba.fastjson.JSONObject;

import java.util.HashSet;

/**
 * HashSet
 *
 * 底层使用HashMap，利用HashMap的key不可重复的特性实现
 * 排列无序，元素不可重复
 *
 * 线程不安全
 * 存储速度快
 *
 *
 * @author: xzj
 * @date: 2019/9/16 23:53
 */
public class HashSetDemo {
    public static void main(String[] args) {
        HashSet<Integer> hashSet = new HashSet<>();
        hashSet.add(10);
        hashSet.add(10);
        System.out.println(JSONObject.toJSONString(hashSet));
    }
}
