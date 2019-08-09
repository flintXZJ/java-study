package com.xzj.stu.java.annotation;

/**
 * 测试接口的子类、实现类是否会继承@Inherited修饰的注解
 * @author zhijunxie
 * @date 2019/8/9 15:44
 */
@IsInheritedAnnotation
@NoInherritedAnnotation
public interface InheritedInterface {
}
