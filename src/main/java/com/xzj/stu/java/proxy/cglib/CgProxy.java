package com.xzj.stu.java.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author zhijunxie
 * @date 2019/8/8 20:19
 */
public class CgProxy implements MethodInterceptor {

    /**
     * 被代理对象
     */
    private Class clazz;

    public CgProxy(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("doSomething before...");
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("doSomething after...");
        return result;
    }

    public Object getProxyObject() {
        Enhancer enhancer = new Enhancer();
        //设置父类
        enhancer.setSuperclass(clazz);
        //设置回调，在调用父类方法时，回调 this.intercept()
        enhancer.setCallback(this);
        //创建代理对象
        return enhancer.create();
    }
}
