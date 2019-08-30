package com.xzj.stu.java.jvm;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * @author zhijunxie
 * @date 2019/8/30 18:27
 */
public class OopKlassDemo {

    public static void main(String[] args) {
        /**
         * # Running 64-bit HotSpot VM.
         * # Using compressed oop with 0-bit shift.
         * # Using compressed klass with 3-bit shift.
         * # Objects are 8 bytes aligned.
         * # Field sizes by type: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
         * # Array element sizes: 4, 1, 1, 2, 2, 4, 4, 8, 8 [bytes]
         *
         */
        System.out.println(VM.current().details());
        System.out.println("-----------");
        /**
         * com.xzj.stu.java.jvm.TestObject object internals:
         *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
         *       0    12        (object header)                           N/A  //对象头
         *      12     4    int TestObject.aa                             N/A  //实例数据
         *      16     4    int TestObject.bb                             N/A  //实例数据
         *      20     4        (loss due to the next object alignment)        //对齐填充 *HotSpot VM中要求对象的起始地址是8的倍数
         * Instance size: 24 bytes
         * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
         *
         */
        System.out.println(ClassLayout.parseClass(TestObject.class).toPrintable());
        System.out.println("-----------");
        /**
         * com.xzj.stu.java.jvm.TestObject2 object internals:
         *  OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
         *       0    12                    (object header)                           N/A  //对象头
         *      12     4                int Super.cc                                  N/A  //父类实例数据
         *      16     4   java.lang.String Super.dd                                  N/A  //父类实例数据
         *      20     4                int TestObject2.aa                            N/A  //本类实例数据
         *      24     8               long TestObject2.bb                            N/A  //本类实例数据
         * Instance size: 32 bytes
         * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
         *
         */
        System.out.println(ClassLayout.parseClass(TestObject2.class).toPrintable());
    }
}

class TestObject {
    private int aa;
    private int bb;

    public int add() {
        return 0;
    }
}

class TestObject2 extends Super {
    private int aa;
    private long bb;

    public int add() {
        return 1;
    }
}

class Super {
    private int cc;
    private String dd;

    public void printLog() {
        System.out.println(cc + dd);
    }
}