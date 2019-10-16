package com.xzj.stu.java.leetcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 回文数
 *
 * @author zhijunxie
 * @date 2019/10/14 16:40
 */
public class LeetCodePalindrome {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeetCodePalindrome.class);

    public static void main(String[] args) {
        LeetCodePalindrome leetCode = new LeetCodePalindrome();

        LOGGER.info("result : {}", leetCode.isPalindrome(123421));
    }

    public boolean isPalindrome(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }
        int tmp = 0;
        while (x > tmp) {
            tmp = tmp * 10 + x % 10;
            x = x / 10;
        }
        return x == tmp || x == tmp / 10;
    }
}
