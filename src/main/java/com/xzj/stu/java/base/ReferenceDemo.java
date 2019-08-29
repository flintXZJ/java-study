package com.xzj.stu.java.base;


import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * 强引用、软引用(SoftReference)、弱引用(WeakReference)、虚引用(PhantomReference)
 * <p>
 * SoftReference: 对于只有软引用的对象来说，当系统内存足够时它不会被回收，当系统内存空间不足时它会被回收。软引用通常用在对内存敏感的程序中
 * WeakReference: 它比软引用的生存期更短，对于只有弱引用的对象来说，只要垃圾回收机制一运行，不管JVM的内存空间是否足够，总会回收该对象占用的内存
 * PhantomReference: 它不能单独使用，必须和引用队列联合使用。虚引用的主要作用是跟踪对象被垃圾回收的状态
 *
 * @author zhijunxie
 * @date 2019/8/27 18:27
 */
public class ReferenceDemo {

    private static volatile boolean isRun = true;

    public static void main(String[] args) throws Exception {
        //WeakReference
        String weakStr = new String("WeakReference");// 如果使用字面量定义"WeakReference"， JVM内建字符串池机制的存在会导致字符串池强引用的存在，因此不会被垃圾回收
        WeakReference<String> weakReference = new WeakReference<>(weakStr);
        //当没有设置str = null时，无论是否gc 都能取得值
        weakStr = null;
        System.out.println("gc before weakReference.value: " + weakReference.get());
        System.gc();
        System.out.println("gc after weakReference.value: " + weakReference.get());


        //PhantomReference
        User user = new User();
        System.out.println("user: " + user.getClass() + "@" + user.hashCode());
        ReferenceQueue<User> referenceQueue = new ReferenceQueue<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    Object poll = referenceQueue.poll();
                    if (poll != null) {
                        try {
                            Field rereferent = Reference.class.getDeclaredField("referent");
                            rereferent.setAccessible(true);
                            Object result = rereferent.get(poll);
                            System.out.println("gc will collect:" + result.getClass() + "@" + result.hashCode());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        PhantomReference<User> phantomReference = new PhantomReference<>(user, referenceQueue);
        //触发gc
        user = null;
        Thread.sleep(1000L);
        System.gc();
        Thread.sleep(1000L);
        isRun = false;



        //SoftReference
        String softStr = new String("SoftReference");
        SoftReference<String> softReference = new SoftReference<>(softStr);
        softStr = null;
        System.out.println("gc before softReference.value: " + softReference.get());
        //jvm: -Xmx10m -Xms10m -XX:+PrintGCDetails
        //模拟内存空间不足，触发gc
        for (int i=0; i<1000; i++) {
            byte[] bytes = new byte[256*1024];
        }
        Thread.sleep(1000L);
        //没有复现系统内存空间不足被回收的情况？？？
        System.out.println("gc after softReference.value: " + softReference.get());
    }

    static class User {
    }
}
