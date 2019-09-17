package com.xzj.stu.java.datastructure.collection;

import java.util.TreeSet;

/**
 * TreeSet
 *
 * 底层使用TreeMap，利用二叉树实现(红黑树？研究与hashmap中相同hashcode链表中节点超过8个之后转换成的红黑树实现是否一致)
 * 排列无序，元素不可重复
 *
 * 线程不安全
 *
 * @author zhijunxie
 * @date 2019/9/17 11:00
 */
public class TreeSetDemo {
    public static void main(String[] args) {
        TreeSet<String> treeSet = new TreeSet<String>();
        treeSet.add("zhi");
    }
}
