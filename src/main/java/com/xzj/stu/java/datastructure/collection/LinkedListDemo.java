package com.xzj.stu.java.datastructure.collection;

import java.util.LinkedList;

/**
 * LinkedList 链表实现 线程不安全
 *
 * 底层使用双向链表实现
 * 线程不安全
 *
 * 排列有序（index），元素可重复
 * 查询设置速度慢，增加删除快
 *
 * 查询时如果index< size >> 1（小于节点数一半），从头节点开始遍历；否则从尾节点开始遍历
 *
 * @author tiany
 */
public class LinkedListDemo {
    public static void main(String[] args) {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.push("");
        linkedList.remove();
        linkedList.poll();
        linkedList.get(0);
    }
}
