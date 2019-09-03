package com.xzj.stu.java.base;

/**
 * Switch
 * 语法糖
 *
 * 对于编译器来说，switch中其实只能使用整型，任何类型的比较都要转换成整型
 *
 * @author zhijunxie
 * @date 2019/9/2 11:02
 */
public class SwitchDemo {
    public static void main(String[] args) {
        SwitchDemo switchDemo = new SwitchDemo();
        switchDemo.intCaseTest(0);
        switchDemo.charCaseTest('b');
        switchDemo.stringCaseTest("def");
    }

    private void intCaseTest(int a) {
        switch (a) {
            case 0:
                System.out.println("intCaseTest case = 0");
                break;
            case 1:
                System.out.println("intCaseTest case = 1");
                break;
            default:
                System.out.println("intCaseTest case not know");
                break;
        }
        System.out.println("out1");
    }

    private void charCaseTest(char a) {
        switch (a) {
            case 'a':
                System.out.println("intCaseTest case = a");
                break;
            case 'b':
                System.out.println("intCaseTest case = b");
                break;
            default:
                System.out.println("intCaseTest case not know");
                break;
        }
        System.out.println("out2");
    }

    /**
     * javap -c -v SwitchDemo.class
     *
     * private void stringCaseTest(java.lang.String);
     *     Code:
     *        0: aload_1
     *        1: astore_2
     *        2: iconst_m1
     *        3: istore_3
     *        4: aload_2
     *        5: invokevirtual #17                 // Method java/lang/String.hashCode:()I
     *        8: lookupswitch  { // 2
     *                  96354: 36
     *                  99333: 50
     *                default: 61
     *           }
     *       36: aload_2
     *       37: ldc           #18                 // String abc
     *       39: invokevirtual #19                 // Method java/lang/String.equals:(Ljava/lang/Object;)Z
     *
     * 反编译之后可以看出switch在比较字符串时，会使用字符串的hashcode来实现。又因为hashcode可能碰撞，然后通过使用equals方法比较进行安全检查。
     *
     * @param str
     */
    private void stringCaseTest(String str) {
        switch (str) {
            case "abc":
                System.out.println("intCaseTest case = abc");
                break;
            case "def":
                System.out.println("intCaseTest case = def");
                break;
            default:
                System.out.println("intCaseTest case not know");
                break;
        }
        System.out.println("out3");
    }
}
