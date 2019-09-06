package com.xzj.stu.java.reflex;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author zhijunxie
 * @date 2019/7/30 18:15
 */
public class ReflexTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflexTest.class);

    public static void main(String[] args) throws Exception {
        UserPO userPO = new UserPO("xzj", 28);
        Class clazz = userPO.getClass();

        try {
            //通过实例object.getClass()获取到的Class与Class.forName()获取到的一致，
            //因为它们获取的都是运行时方法区的类信息，包含：构造器、方法、属性等信息。
//            Class clazz = Class.forName("com.xzj.stu.java.reflex.UserPO");
//            Object userPO = clazz.newInstance();

            //getConstructor 、 getField 和 getMethod只能获取public修饰的构造器、属性和方法
            //getDeclaredXXX可以获取所有权限的构造器、属性和方法，但是不能获取父类继承下来的。
            Field name = clazz.getDeclaredField("name");
            name.setAccessible(true);
            Object value = name.get(userPO);
            logger.info("name = {}", value);

            name.set(userPO, "xzj2");
            logger.info("userPO = {}", JSONObject.toJSONString(userPO));

            Method sayHello = clazz.getDeclaredMethod("sayHello", String.class, Integer.class);
            sayHello.setAccessible(true);
            sayHello.invoke(userPO, "hello world", 29);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
