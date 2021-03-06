package com.xzj.stu.java.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 *
 * @author zhijunxie
 * @date 2019/8/9 14:39
 */
@Target(ElementType.TYPE) //可以用于注解类、接口(包括注解类型) 或enum声明
@Retention(RetentionPolicy.RUNTIME) //注解的的RetentionPolicy的属性值是RUTIME,这样注解处理器可以通过反射，获取到该注解的属性值，从而去做一些运行时的逻辑处理
@Documented //用于描述其它类型的annotation应该被作为被标注的程序成员的公共API，因此可以被例如javadoc此类的工具文档化。Documented是一个标记注解
public @interface ClassAnnotation {

    public String species() default "human";
}
