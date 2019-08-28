package com.xzj.stu.java.base;


import java.lang.ref.WeakReference;

/**
 *
 * @author zhijunxie
 * @date 2019/8/27 18:27
 */
public class ReferenceDemo {
    public static void main(String[] args) {
        String str = new String("test123456");
        WeakReference<String> weakReference = new WeakReference<>(str);

        //当没有设置str = null时，无论是否gc 取得的值都为test123456
        str = null;
        System.out.println("value: " + weakReference.get());
        System.gc();
        System.out.println("value2: " + weakReference.get());
    }
}
