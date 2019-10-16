package com.xzj.stu.java.leetcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * 罗马数字转整数
 *
 * @author zhijunxie
 * @date 2019/10/14 18:18
 */
public class LeetCodeRomanToInt {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeetCodeRomanToInt.class);
    private static HashMap<Character, Integer> hashMap = new HashMap();

    static {
        hashMap.put('I', 1);
        hashMap.put('V', 5);
        hashMap.put('X', 10);
        hashMap.put('L', 50);
        hashMap.put('C', 100);
        hashMap.put('D', 500);
        hashMap.put('M', 1000);
    }

    public static void main(String[] args) {
        LeetCodeRomanToInt leetCode = new LeetCodeRomanToInt();

        LOGGER.info("result : {}", leetCode.romanToInt("MCMXCIV"));
    }

    public int romanToInt(String s) {
        int length = s.length();
        int result = 0;
        for (int i = 0; i < length; i++) {
            if (i < length-1 && hashMap.get(s.charAt(i)) < hashMap.get(s.charAt(i+1))) {
                result -= hashMap.get(s.charAt(i));
            } else {
                result += hashMap.get(s.charAt(i));
            }
        }
        return result;
    }
}
