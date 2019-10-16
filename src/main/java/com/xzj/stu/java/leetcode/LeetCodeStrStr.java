package com.xzj.stu.java.leetcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现strStr()
 * 描述：给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。
 * 如果不存在，则返回 -1。
 *
 * 效率：
 * bfAlgorithm() > strStr()
 *
 * @author zhijunxie
 * @date 2019/10/15 18:02
 */
public class LeetCodeStrStr {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeetCodeStrStr.class);

    public static void main(String[] args) {
        LeetCodeStrStr leetCode = new LeetCodeStrStr();
        String haystack = "vpmfakdqlqbfilgpggotuhtnofpgcagohigvsaeqhrbnqwoedldspggastwnaicgekgllgkhxeevuyvchuoqvkkceiykfrnuiltiyccdlqdrwkawkhtragtgnmmripcoydugthcivmuhqbnluehyleuymikgxmyclyycxtdrkvglhfdkcfscgrxgtenugcsgbbeqwucp";
        String needle = "ripcoydugt";

        int count = 1000_000;

        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            leetCode.strStr(haystack, needle);
        }
        LOGGER.info("strStr.result : {}", (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            leetCode.bfAlgorithm(haystack, needle);
        }
        LOGGER.info("bf_Algorithm.result : {}", (System.currentTimeMillis() - start));

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

    /**
     * BF Brute-Force算法
     * 时间复杂度：最好O(n+m)，最坏O(n*m)
     * https://mp.weixin.qq.com/s/Z1ehFKIVd07UCqjtTXDY2w
     * https://blog.csdn.net/zhanglong_4444/article/details/90671650
     *
     * @param haystack
     * @param needle
     * @return
     */
    public int bfAlgorithm(String haystack, String needle) {
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        if (haystack == null || haystack.length() < needle.length()) {
            return -1;
        }

        int i = 0;
        int j = 0;
        while (i < haystack.length() && j < needle.length()) {
            if (haystack.charAt(i) == needle.charAt(j)) {
                i++;
                j++;
            } else {
                i = i - j + 1;
                j = 0;
            }
        }

        if (j == needle.length()) {
            return i - j;
        }

        return -1;
    }

    /**
     * rk 算法
     * https://blog.csdn.net/zhanglong_4444/article/details/90671650
     *
     * @param haystack
     * @param needle
     * @return
     */
    public int rkAlgorithm(String haystack, String needle) {
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        if (haystack == null || haystack.length() < needle.length()) {
            return -1;
        }

        // TODO: 2019/10/16 待实现
        return -1;
    }

    /**
     * kmp 算法
     * https://mp.weixin.qq.com/s/2JyZoy9pESrcd5hsGWLWgw
     * https://blog.csdn.net/zhanglong_4444/article/details/90671650
     *
     * @param haystack
     * @param needle
     * @return
     */
    public int kmpAlgorithm(String haystack, String needle) {
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        if (haystack == null || haystack.length() < needle.length()) {
            return -1;
        }

        // TODO: 2019/10/16 待实现
        return -1;
    }

    /**
     * bm 算法
     * https://mp.weixin.qq.com/s/S68mm8XicGerimD8zC6b-Q
     * https://blog.csdn.net/zhanglong_4444/article/details/90671650
     *
     * @param haystack
     * @param needle
     * @return
     */
    public int bmAlgorithm(String haystack, String needle) {
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        if (haystack == null || haystack.length() < needle.length()) {
            return -1;
        }

        // TODO: 2019/10/16 待实现
        return -1;
    }
}
