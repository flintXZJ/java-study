package com.xzj.stu.java.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhijunxie
 * @date 2019/8/9 14:43
 */
@Target(ElementType.FIELD) //仅可用于注解类的成员变量
@Retention(RetentionPolicy.RUNTIME) //在运行时有效（即运行时保留）
public @interface NameAnnotation {
    String name() default "无名氏";
}
