package com.xzj.stu.java.datastructure.collection;

import java.util.Vector;

/**
 * Vector 数组实现 线程安全
 *
 * 底层使用数组实现
 * 线程安全，每个public方法都是同步方法。所以效率低
 *
 * 排列有序（index），元素可重复
 * 当容量不够时创建新数组，自动增加当前容量1倍，将旧数据赋值到新数组并指向新数组
 *
 * 查询设置速度快，增加删除慢
 *
 * @author zhijunxie
 * @date 2019/09/17
 */
public class VectorDemo {
    public static void main(String[] args) {
        Vector<String> stringVector = new Vector<>(10);
        stringVector.add("xie");
        stringVector.remove(0);
    }
}
