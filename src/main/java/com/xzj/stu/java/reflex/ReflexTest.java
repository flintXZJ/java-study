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
//            Class clazz = Class.forName("com.xzj.stu.java.reflex.UserPO");
//            Object userPO = clazz.newInstance();

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
