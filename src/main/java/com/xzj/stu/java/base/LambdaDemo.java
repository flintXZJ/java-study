package com.xzj.stu.java.base;

import com.google.common.base.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhijunxie
 * @date 2019/10/18 14:34
 */
public class LambdaDemo {
    public static void main(String[] args) {
        //函数式接口
        Predicate<Integer> predicate = age -> age > 20;
        System.out.println(predicate.test(18));

        Consumer<String> consumer = System.out::println;
        consumer.accept("this is a lambda test");

        Function<Student, String> getName = Student::getName;
        String xzj = getName.apply(new Student("xzj",0));
        System.out.println(xzj);

        List<Student> collect = Stream.of(new Student("xzj1", 0), new Student("xzj2", 1), new Student("xzj3", 2))
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Student {
        private String name;
        private int age;
    }
}
