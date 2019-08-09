package com.xzj.stu.java.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author zhijunxie
 * @date 2019/8/9 14:41
 */
public class Test {

    public static void main(String[] args) {

        //测试类是否会继承@Inherited修饰的注解
        //从结果看子类会继承父类的@Inherited修饰的注解
        Annotation[] annotations = InheritedBase.class.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println("InheritedBase.class.Annotations="+annotation);
        }

        Annotation[] annotations1 = MyInheritedClass.class.getAnnotations();
        for (Annotation annotation : annotations1) {
            System.out.println("MyInheritedClass.class.Annotations="+annotation);
        }

        //测试接口的子类、实现类是否会继承@Inherited修饰的注解
        //从测试结果看接口的子类、实现了均不会继承@Inherited修饰的注解
        Annotation[] annotations2 = InheritedInterface.class.getAnnotations();
        for (Annotation annotation : annotations2) {
            System.out.println("InheritedInterface.class.Annotations="+annotation);
        }

        Annotation[] annotations3 = InheritedInterfaceChild.class.getAnnotations();
        for (Annotation annotation : annotations3) {
            System.out.println("InheritedInterfaceChild.class.Annotations="+annotation);
        }

        Annotation[] annotations4 = MyInheritedInterfaceImpl.class.getAnnotations();
        for (Annotation annotation : annotations4) {
            System.out.println("MyInheritedInterfaceImpl.class.Annotations="+annotation);
        }


        //用反射机制来调用注解中的内容
        try {
            Field field = UserPO.class.getDeclaredField("name");
            //是否有类型为NameAnnotation的注解
            if (field.isAnnotationPresent(NameAnnotation.class)) {
                NameAnnotation annotation = field.getAnnotation(NameAnnotation.class);
                System.out.println("获取注解的内容="+annotation.name());
            }
        } catch (Exception e) {}

    }
}
