package com.xzj.stu.java.proxy.dynamic;

/**
 * 实际对象
 *
 * @author zhijunxie
 * @date 2019/8/8 18:10
 */
public class RealSubject implements Subject {
    @Override
    public String sayHello(String name) {
        System.out.println("RealSubject: sayHello params="+name);
        return "hello,  "+name;
    }
}
