package com.xzj.stu.java.base;

/**
 * 逃逸分析
 * jdk 1.7开始已经默认开始逃逸分析
 * -XX:+DoEscapeAnalysis ： 表示开启逃逸分析
 * -XX:-DoEscapeAnalysis ： 表示关闭逃逸分析
 *
 * 1、关闭逃逸分析
 * JVM参数为：-Xmx4G -Xms4G -XX:-DoEscapeAnalysis -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
 * 运行程序之后使用jmap -histo pid 命令，能清楚的看出堆内存中有100万个user对象；
 * num     #instances         #bytes  class name
 * ----------------------------------------------
 *    1:           617       67459600  [I
 *    2:       1000000       16000000  com.xzj.stu.java.base.EscapeAnalysisDemo$User
 * 在关闭逃避分析的情况下（-XX:-DoEscapeAnalysis），虽然在alloc方法中创建的User对象并没有逃逸到方法外部，但是还是被分配在堆内存中。
 * 也就说，如果没有JIT编译器优化，没有逃逸分析技术，正常情况下就应该是这样的。即所有对象都分配到堆内存中。
 *
 * 2、开启逃逸分析
 * JVM参数为：-Xmx4G -Xms4G -XX:+DoEscapeAnalysis -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
 * 从结果中可以看出，开启逃逸分析之后，在堆内存中只有9万个user对象。也就是说在经过JIT优化之后，堆内存中分配的对象数量，从100万降到了9万
 *  num     #instances         #bytes  class name
 * ----------------------------------------------
 *    1:           625       60722832  [I
 *    2:         92818        1485088  com.xzj.stu.java.base.EscapeAnalysisDemo$User
 *
 *
 * 是不是所有的对象和数组都会在堆内存分配空间？
 * 不一定，随着JIT编译器的发展，在编译期间，如果JIT经过逃逸分析，发现有些对象没有逃逸出方法，那么有可能堆内存分配会被优化成栈内存分配。
 * 但是这也并不是绝对的。就像我们前面看到的一样，在开启逃逸分析之后，也并不是所有User对象都没有在堆上分配。
 *
 * @author zhijunxie
 * @date 2019/8/28 15:17
 */
public class EscapeAnalysisDemo {
    public static void main(String[] args) {
        long a1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            alloc();
        }
        // 查看执行时间
        long a2 = System.currentTimeMillis();
        System.out.println("cost " + (a2 - a1) + " ms");
        // 为了方便查看堆内存中对象个数，线程sleep
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    private static void alloc() {
        User user = new User();
    }

    static class User {
    }
}
