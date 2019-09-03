package com.xzj.stu.java.base;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 泛型
 * 语法糖
 *
 * 通常情况下，一个编译器处理泛型有两种方式：Code specialization和Code sharing。
 * C++和C#是使用Code specialization的处理机制，而Java使用的是Code sharing的机制。
 * Code sharing方式为每个泛型类型创建唯一的字节码表示，并且将该泛型类型的实例都映射到这个唯一的字节码表示上。
 * 将多种泛型类形实例映射到唯一的字节码表示是通过类型擦除（type erasue）实现的。
 *
 * 也就是说，对于Java虚拟机来说，他根本不认识Map<String, BigDecimal> map这样的语法。需要在编译阶段通过类型擦除的方式进行解语法糖。
 * 类型擦除的主要过程如下：
 * 1.将所有的泛型参数用其最左边界（最顶级的父类型）类型替换。
 * 2.移除所有的类型参数。
 *
 * 虚拟机中没有泛型，只有普通类和普通方法，所有泛型类的类型参数在编译时都会被擦除，泛型类并没有自己独有的Class类对象。
 * 比如并不存在List<String>.class或是List<Integer>.class，而只有List.class
 *
 * @author zhijunxie
 * @date 2019/9/2 11:29
 */
public class GenericDemo {
    /**
     * javap -c -v GenericDemo.class
     * @param args
     */
    public static void main(String[] args) {
        HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        map.put("value", new BigDecimal(100));

    }
}
