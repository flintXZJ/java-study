package com.xzj.stu.java.reflex;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 父类非public方法属性如何获取
 *
 *
 * @author zhijunxie
 * @date 2019/7/30 18:15
 */
public class ReflexTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflexTest.class);

    public static void main(String[] args) throws Exception {
//        UserPO object = new UserPO("xzj", 28);
        // 获取class的三种方法
//        Class clazz = object.getClass();//触发类的初始化 todo 确认
//        Class clazz = UserPO.class;//不会触发类的初始化但XXX类已经被加载到方法区 todo 确认
        Class clazz = Class.forName("com.xzj.stu.java.reflex.ReflexTest$UserPO");//触发类的初始化 todo 确认

        // 获取对象的2中方法
        Object object = clazz.newInstance();
//        clazz.getDeclaredConstructor().newInstance();


        try {
            //通过实例object.getClass()获取到的Class与Class.forName()获取到的一致，
            //因为它们获取的都是运行时方法区的类信息，包含：构造器、方法、属性等信息。
//            Class clazz = Class.forName("com.xzj.stu.java.reflex.UserPO");
//            Object userPO = clazz.newInstance();

            //getConstructor 、 getField 和 getMethod只能获取当前class及其父类、接口中的public修饰的构造器、属性和方法，
            //getDeclaredXXX可以获取当前Class所有权限的构造器、属性和方法，但是不能获取父类继承下来的。
            Field name = clazz.getDeclaredField("name");
            name.setAccessible(true);
            Object value = name.get(object);
            logger.info("name = {}", value);

            name.set(object, "xzj2");
            logger.info("userPO = {}", JSONObject.toJSONString(object));

            Method sayHello = clazz.getDeclaredMethod("sayHello", String.class, Integer.class);
            sayHello.setAccessible(true);
            sayHello.invoke(object, "hello world", 29);

            //执行代码时加入：-XX:+TraceClassLoading
            /**
             * [Loaded sun.reflect.GeneratedMethodAccessor1 from __JVM_DefineClass__]
             *
             */
            for (int i = 0; i < 16; i++) {
                sayHello.invoke(object, "hello world" + i, 29);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Data
    public static class UserPO extends BasePO implements Serializable {
        private static final long serialVersionUID = 6703216809599385427L;

        public UserPO() {
            System.out.println("public UserPO()");
        }

        public UserPO(String name, Integer age) {
            System.out.println("public UserPO(String name, Integer age)");
            this.name = name;
            this.age = age;
        }

        public String publicName;

        protected String protectedName;

        private String name;

        private String address;

        private Integer age;

        private Integer sex;

        private String city;

        private void sayHello(String content, Integer age) {
            System.out.println(this.name + " is " + age + " old, say: " + content);
        }
    }

    @Setter
    @Getter
    public static class BasePO {

        private Long id;

        public Long testId;
    }
}
