package com.xzj.stu.java.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhijunxie
 * @date 2019/8/9 16:24
 */
@Constraint(validatedBy = DateTimeValidate.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTime {
    String message() default "日期格式错误";

    String format() default "yyyyMMddHHmmss"; // 日期格式

    boolean required() default true;// 是否允许为空 fase 可以为空

    int length() default 5;

    // 下面这两个属性必须添加
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
