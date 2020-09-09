package com.xzj.stu.java.lambda;

import java.util.HashMap;
import java.util.Map;

/**
 * lambda 基本语法
 *
 * @author zhijunxie
 * @date 2019/12/30 19:54
 */
public class BaseGrammar {
    public static void main(String[] args) {
        // 1. 不需要参数,返回值为 5
//        () -> 5
        new Thread(()-> System.out.println("无参数，无返回")).start();

        // 2. 接收一个参数(数字类型),返回其2倍的值
//        x -> 2 * x

        // 3. 接受2个参数(数字),并返回他们的差值
//        (x, y) ->x –y
        Map<String, String> map = new HashMap<>();
        map.put("a","a1");
        map.put("b","b2");
        map.put("c","c3");
        map.forEach((key,  value) -> System.out.println(key + ":" + value));

        // 4. 接收2个int型整数,返回他们的和
//        ( int x, int y) ->x + y

        // 5. 接受一个 string 对象,并在控制台打印,不返回任何值(看起来像是返回void)
//        (String s) ->System.out.print(s)

    }
}
