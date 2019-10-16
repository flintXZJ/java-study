package com.xzj.stu.java.leetcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 整数反转
 *
 * @author zhijunxie
 * @date 2019/10/14 15:32
 */
public class LeetCodeReverse {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeetCodeReverse.class);

    public static void main(String[] args) {
        LeetCodeReverse leetCode = new LeetCodeReverse();

        LOGGER.info("result : {}", leetCode.reverse(-1234567892));
    }

    public int reverse(int x) {
        int min = Integer.MIN_VALUE, max = Integer.MAX_VALUE;
        long result = 0;
        while (x % 10 != 0) {
            result = result * 10 + x % 10;
            x = x / 10;
        }
        if (result < min || result > max) {
            throw new IllegalArgumentException("超出整数范围");
        }
        return (int) result;
    }
}
