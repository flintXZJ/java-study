package com.xzj.stu.java.base;

/**
 * 自动装箱与拆箱
 *
 * 自动装箱就是Java自动将原始类型值转换成对应的对象，比如将int的变量转换成Integer对象，这个过程叫做装箱，反之将Integer对象转换成int类型值，这个过程叫做拆箱。
 * 因为这里的装箱和拆箱是自动进行的非人为转换，所以就称作为自动装箱和拆箱。
 * 原始类型byte, short, char, int, long, float, double 和 boolean 对应的封装类为Byte, Short, Character, Integer, Long, Float, Double, Boolean。
 *
 * @author zhijunxie
 * @date 2019/9/2 11:40
 */
public class AutoBoxAndUnbox {
    /**
     * javap -c -v AutoBoxAndUnbox.class
     *
     * public static void main(java.lang.String[]);
     *     descriptor: ([Ljava/lang/String;)V
     *     flags: ACC_PUBLIC, ACC_STATIC
     *     Code:
     *       stack=1, locals=5, args_size=1
     *          0: iconst_5
     *          1: istore_1
     *          2: iload_1
     *          3: invokestatic  #2                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
     *          6: astore_2
     *          7: bipush        12
     *          9: invokestatic  #2                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
     *         12: astore_3
     *         13: aload_3
     *         14: invokevirtual #3                  // Method java/lang/Integer.intValue:()I
     *
     * @param args
     */
    public static void main(String[] args) {
        int abc = 5;
        Integer ann = abc;

        Integer def = 12;
        int fnn = def;
    }
}
