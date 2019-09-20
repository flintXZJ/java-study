package com.xzj.stu.java.datastructure.map;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * 如何实现？？
 * jdk1.7 Segment分段锁
 * 减小锁粒度
 * 是指缩小锁定对象的范围，从而减小锁冲突的可能性，从而提高系统的并发能力。减小锁粒度是一种削弱多线程锁竞争的有效手段，
 * 这种技术典型的应用是ConcurrentHashMap(高性能的HashMap)类的实现。
 * 对于HashMap而言，最重要的两个方法是get与set方法，如果我们对整个HashMap加锁，可以得到线程安全的对象，但是加锁粒度太大。
 * Segment的大小也被称为ConcurrentHashMap的并发度。
 *
 * 分段锁
 *
 * ```
 * Segment<K,V> s;
 * if (value == null)
 *     throw new NullPointerException();
 * int hash = hash(key);
 * int j = (hash >>> segmentShift) & segmentMask;
 * if ((s = (Segment<K,V>)UNSAFE.getObject          // nonvolatile; recheck
 *      (segments, (j << SSHIFT) + SBASE)) == null) //  in ensureSegment
 *     s = ensureSegment(j); //如何对应段没有Segment，则创建Segment
 * return s.put(key, hash, value, false);//在该方法中使用ReentrantLock加锁，存储数据
 * ```
 *
 *
 * jdk1.8 ？？？
 * 数据结构：取消了Segment分段锁的数据结构，取而代之的是数组+链表+红黑树的结构。
 * 保证线程安全机制：JDK1.7采用segment的分段锁机制实现线程安全，其中segment继承自ReentrantLock。JDK1.8采用CAS+Synchronized保证线程安全
 * 锁的粒度：原来是对需要进行数据操作的Segment加锁，现调整为对每个数组元素加锁（Node）。
 * 链表转化为红黑树:定位结点的hash算法简化会带来弊端,Hash冲突加剧,因此在链表节点数量大于8时，会将链表转化为红黑树进行存储。
 * 查询时间复杂度：从原来的遍历链表O(n)，变成遍历红黑树O(logN)。
 *
 * @author zhijunxie
 * @date 2019/9/17 15:26
 */
public class ConcurrentHashMapDemo {
    public static void main(String[] args) {
        //使用cas实现线程安全
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>(16);
        concurrentHashMap.put("123","xzj");
        concurrentHashMap.get("123");
    }
}
