package com.xzj.stu.java.proxy.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 调用处理器实现类
 * 每次生成动态代理类对象时都需要指定一个实现了该接口的调用处理器对象
 *
 * @author zhijunxie
 * @date 2019/8/8 18:12
 */
public class InvocationHandlerImpl implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(InvocationHandlerImpl.class);

    private Object subject;

    public InvocationHandlerImpl(Object subject) {
        this.subject = subject;
    }

    /**
     * 该方法负责集中处理动态代理类上的所有方法调用
     * 调用处理器根据这三个参数进行预处理或分派到委托类实例上反射执行
     *
     * @param proxy 代理类实例
     * @param method 被调用的方法对象
     * @param args 调用参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // TODO: 2019/8/8 doSomething
        logger.info("InvocationHandlerImpl: 调用真实对象方法前...");

        //当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的handler对象的invoke方法来进行调用
        Object returnValue = method.invoke(subject, args);
        logger.info("InvocationHandlerImpl: 调用真实对象方法后.");
        return returnValue;
    }
}
