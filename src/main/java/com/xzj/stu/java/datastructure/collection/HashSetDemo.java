package com.xzj.stu.java.datastructure.collection;

import com.alibaba.fastjson.JSONObject;

import java.util.HashSet;

/**
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
