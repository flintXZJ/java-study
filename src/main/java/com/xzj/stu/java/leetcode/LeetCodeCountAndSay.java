package com.xzj.stu.java.leetcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 报数
 * 遍历、递归
 *
 * @author zhijunxie
 * @date 2019/10/16 15:21
 */
public class LeetCodeCountAndSay {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeetCodeCountAndSay.class);

    public static void main(String[] args) {
        LeetCodeCountAndSay leetCode = new LeetCodeCountAndSay();

        LOGGER.info("result : {}", leetCode.countAndSay(6));
    }

    public String countAndSay(int n) {
        int i = 1;
        String tmp = "1";
        while (i < n) {
            StringBuilder stringBuffer = new StringBuilder();
            char c = tmp.charAt(0);
            int count = 1;
            for (int j = 1; j < tmp.length(); j++) {
                if (c == tmp.charAt(j)) {
                    count++;
                } else {
                    stringBuffer.append(count).append(c);
                    count = 1;
                    c = tmp.charAt(j);
                }
            }
            stringBuffer.append(count).append(c);
            tmp = stringBuffer.toString();
            i++;
        }

        return tmp;
    }
}
