package com.xzj.stu.java.base;

import com.google.common.base.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Base64;
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

        System.out.println("---------Stream-----------");
        List<Student> collect = Stream.of(new Student("xzj1", 0), new Student("xzj2", 1), new Student("xzj3", 2))
                .collect(Collectors.toList());
        System.out.println(collect);

        List<String> list = Arrays.asList("maa", "a", "ab", "c");
        list.stream()
                .filter(s -> s.contains("a"))
                .map(s -> s + "aa")
                .sorted()
                .forEach(System.out::println);

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        int res = numbers.stream().map(i -> i + 1).mapToInt(i -> i).summaryStatistics().getMax();
        System.out.println(res);

        System.out.println("---------日期时间 API-----------");
        LocalDate now = LocalDate.now();
        System.out.println(now);
        System.out.println(now.getYear());
        System.out.println(now.getMonth());
        System.out.println(now.getDayOfMonth());

        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
        LocalDateTime localDateTime = now.atTime(localTime);
        System.out.println(localDateTime);


        System.out.println("---------Base64-----------");
        String base64 = Base64.getEncoder().encodeToString("aaa".getBytes());
        System.out.println(base64);
        byte[] bytes = Base64.getDecoder().decode(base64);
        System.out.println(new String(bytes));

        System.out.println("---------并行数组 ParallelSort-----------");
        Arrays.parallelSort(new int[] {1, 2, 3, 4, 5});
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Student {
        private String name;
        private int age;
    }
}
