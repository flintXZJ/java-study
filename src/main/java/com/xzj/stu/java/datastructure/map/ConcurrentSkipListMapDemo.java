package com.xzj.stu.java.datastructure.map;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * [跳跃表（Skip list）原理与java实现](https://www.toutiao.com/a6755651650251145735/)
 *
 * @author zhijunxie
 * @date 2019/11/6 11:43
 */
public class ConcurrentSkipListMapDemo {
    public static void main(String[] args) {
        ConcurrentSkipListMap<String, String> stringStringConcurrentSkipListMap = new ConcurrentSkipListMap<>();

        stringStringConcurrentSkipListMap.put("", "");
    }
}
