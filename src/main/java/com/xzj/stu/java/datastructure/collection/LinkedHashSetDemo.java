package com.xzj.stu.java.datastructure.collection;

import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashSet;

/**
 * LinkedHashSet
 *
 * 底层使用LinkedHashMap
 * 采用hashmap存储，并用双向链表记录插入顺序
 *
 * 线程不安全
 *
 * @author zhijunxie
 * @date 2019/9/17 11:22
 */
public class LinkedHashSetDemo {
    public static void main(String[] args) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>(10);
        linkedHashSet.add("jun");
        System.out.println(JSONObject.toJSONString(linkedHashSet));
    }
}
