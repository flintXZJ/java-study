package com.xzj.stu.java.leetcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现strStr()
 *
 * @author zhijunxie
 * @date 2019/10/15 18:02
 */
public class LeetCodeStrStr {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeetCodeStrStr.class);

    public static void main(String[] args) {
        LeetCodeStrStr leetCode = new LeetCodeStrStr();

        LOGGER.info("result : {}", leetCode.strStr("hello", "llo"));
    }

    public int strStr(String haystack, String needle) {
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        if (haystack == null || haystack.length() < needle.length()) {
            return -1;
        }
        int index = 0;
        while (index < haystack.length()) {
            if (index + needle.length() - 1 < haystack.length()) {
                if (haystack.substring(index, index + needle.length()).equals(needle)) {
                    return index;
                }
                index++;
            } else {
                break;
            }
        }

        return -1;
    }
}
