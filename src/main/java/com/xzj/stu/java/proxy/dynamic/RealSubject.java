package com.xzj.stu.java.proxy.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实际对象
 *
 * @author zhijunxie
 * @date 2019/8/8 18:10
 */
public class RealSubject implements Subject {
    private static final Logger logger = LoggerFactory.getLogger(RealSubject.class);
    @Override
    public String sayHello(String name) {
        logger.info("RealSubject: sayHello params={}", name);
        return "hello,  "+name;
    }
}
