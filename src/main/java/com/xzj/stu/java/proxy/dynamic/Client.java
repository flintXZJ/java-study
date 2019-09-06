package com.xzj.stu.java.proxy.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 动态代理
 * @author zhijunxie
 * @date 2019/8/8 18:21
 */
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {

        //代理的真实对象
        Subject realSubject = new RealSubject();

        /**
         * InvocationHandlerImpl 实现了 InvocationHandler 接口，并能实现方法调用从代理类到委托类的分派转发
         * 其内部通常包含指向委托类实例的引用，用于真正执行分派转发过来的方法调用.
         * 即：要代理哪个真实对象，就将该对象传进去，最后是通过该真实对象来调用其方法
         */
        InvocationHandler invocationHandler = new InvocationHandlerImpl(realSubject);
        ClassLoader classLoader = invocationHandler.getClass().getClassLoader();
        Class<?>[] interfaces = realSubject.getClass().getInterfaces();

        //该方法用于为指定类装载器、一组接口及调用处理器生成动态代理类实例
        //jdk中的动态代理只支持对接口的代理不能对一个普通的 Java 类提供代理。
        //当RealSubject没有实现Subject的时候，报错【ClassCastException: com.sun.proxy.$Proxy0 cannot be cast to com.xzj.stu.java.proxy.dynamic.RealSubject】
        Subject subject = (Subject) Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);

        logger.info("client: 动态代理对象的类型：{}", subject.getClass().getName());

        String retStr = subject.sayHello("xzj");
        logger.info("client: result = {}", retStr);
    }
}
