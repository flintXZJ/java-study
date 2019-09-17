package com.xzj.stu.java.datastructure.collection;

import java.util.ArrayList;

/**
 * ArrayList 数组实现 线程不安全
 *
 * 底层使用数组实现
 * 线程不安全
 *
 * 排列有序（index），元素可重复
 * 当容量不够时创建新数组，自动增加当前容量0.5倍，将旧数据赋值到新数组并指向新数组
 *
 * 查询设置速度快，增加删除慢
 *
 * @author xzj
 */
public class ArrayListDemo {
    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<>(20);
        arrayList.add(13);
        arrayList.add(120);
        arrayList.get(0);
    }
}
