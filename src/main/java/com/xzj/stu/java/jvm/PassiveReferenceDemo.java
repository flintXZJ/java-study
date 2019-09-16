package com.xzj.stu.java.jvm;

/**
 * 被动引用demo
 *
 * 被动引用不会触发初始化
 *
 * @author zhijunxie
 * @date 2019/9/16 19:27
 */
public class PassiveReferenceDemo {
    public static void main(String[] args) {
        // SuperClass init!
        // 对于静态字段，只有直接定义这个字段的类才会被初始化，因此通过其子类来引用父类中定义的静态字段，只会触发父类的初始化而不会触发子类的初始化
//        System.out.println(SubClass.value);

        //通过数组定义来引用类，不会触发此类的初始化
        //但会触发“[L 全类名”这个类的初始化，它由虚拟机自动生成，直接继承自 java.lang.Object，创建动作由字节码指令 newarray 触发
//        System.out.println("数组定义");
//        SuperClass[] superClass = new SuperClass[10];

        //常量在编译阶段会存入调用类的常量池中，本质上并没有直接引用到定义常量的类，因此不会触发定义常量的类的初始化。
        //编译通过之后，常量存储到PassiveReferenceDemo类的常量池中
        System.out.println(ConstClass.HELLO_WORLD);
    }
}

class SuperClass {
    static {
        System.out.println("SuperClass init!");
    }

    public static int value = 100;
}

class SubClass extends SuperClass {
    static {
        System.out.println("SubClass init!");
    }
}

class ConstClass {
    static {
        System.out.println("ConstClass init!");
    }
    public static final String HELLO_WORLD = "Hello world";
}