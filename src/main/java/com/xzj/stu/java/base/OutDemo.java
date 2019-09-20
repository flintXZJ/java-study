package com.xzj.stu.java.base;

/**
 * @author zhijunxie
 * @date 2019/9/20 17:03
 */
public class OutDemo {
    private static int a;

    /**
     * 成员内部类
     * 成员内部类不允许定义静态变量、静态方法
     */
    public class InnerDemo {
        private int b;
        private final int c = 1;
    }

    /**
     * 静态内部类
     */
    public static class InnerDemo2 {
        private int d;
        private static int e;
    }

    public void fun() {

        /**
         * 局部内部类
         */
        class InnerDemo3{
            public void fun() {
                System.out.println("local inner class");
            }
        }

    }

}
