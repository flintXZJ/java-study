package com.xzj.stu.java.datastructure.map;

import java.util.LinkedHashMap;

/**
 * LinkedHashMap 是HashMap的子类
 * 保存了记录的插入顺序，在用Iterator遍历LinkedHashMap时，先得到的记录肯定是先插入的
 *
 * @author zhijunxie
 * @date 2019/9/17 15:41
 */
public class LinkedHashMapDemo {
    public static void main(String[] args) {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("","");
    }
}
