package com.xzj.stu.java.leetcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 最长公共前缀
 *
 * @author zhijunxie
 * @date 2019/10/15 16:07
 */
public class LeetCodeLongestCommonPrefix {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeetCodeLongestCommonPrefix.class);

    public static void main(String[] args) {
        LeetCodeLongestCommonPrefix leetCode = new LeetCodeLongestCommonPrefix();

        LOGGER.info("result : {}", leetCode.longestCommonPrefix(new String[]{"flower","flow","flight"}));
    }

    public String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }
        String tmp = strs[0];
        for (int i = 0; i < tmp.length(); i++) {
            char c = tmp.charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (i == strs[j].length() || c != strs[j].charAt(i)) {
                    return tmp.substring(0, i);
                }
            }
        }
        return tmp;
    }
}
