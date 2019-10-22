package com.xzj.stu.java.leetcode;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现strStr()
 * 描述：给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。
 * 如果不存在，则返回 -1。
 * <p>
 * 效率：
 * bfAlgorithm() > strStr()
 *
 * @author zhijunxie
 * @date 2019/10/15 18:02
 */
public class LeetCodeStrStr {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeetCodeStrStr.class);

    public static int strStr(String haystack, String needle) {
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        if (haystack == null || haystack.length() < needle.length()) {
            return -1;
        }

        int hlen = haystack.length();
        int nlen = needle.length();
        int index = 0;
        while (index < hlen) {
            if (index + nlen - 1 < hlen) {
                if (haystack.substring(index, index + nlen).equals(needle)) {
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
     * <p>
     * 常用算法
     * KISS（Keep it Simple and Stupid）设计原则
     *
     * @param haystack
     * @param needle
     * @return
     */
    public static int bfAlgorithm(String haystack, String needle) {
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        if (haystack == null || haystack.length() < needle.length()) {
            return -1;
        }

        //haystack字符串当前匹配到的字符下标
        int i = 0;
        //needle字符串当前匹配到的字符下标
        int j = 0;
        while (i < haystack.length() && j < needle.length()) {
            if (haystack.charAt(i) == needle.charAt(j)) {
                i++;
                j++;
            } else {
                //初始haystack比较字符的下一位，回溯
                i = i - j + 1;
                //初始needle比较字符位置
                j = 0;
            }
        }

        //如果haystack中有needle字符，while循环肯定是j == needle.length()退出的
        if (j == needle.length()) {
            return i - j;
        }

        return -1;
    }

    /**
     * rk 算法
     * https://blog.csdn.net/zhanglong_4444/article/details/90671650
     * <p>
     * 计算needle字符串的hash值，及haystack中等长子字符串的hash值，比较；
     * hash不相等，子字符串与needle肯定不相等；hash相等，考虑hash冲突的情况；
     * <p>
     * rk算法可以继续优化hash值获取算法
     *
     * @param haystack
     * @param needle
     * @return
     */
    public static int rkAlgorithm(String haystack, String needle) {
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        if (haystack == null || haystack.length() < needle.length()) {
            return -1;
        }

        int hlen = haystack.length();
        int nlen = needle.length();
        int nhash = needle.hashCode();

        int index = 0;
        while (index < hlen) {
            if (index + nlen - 1 < hlen) {
                String substring = haystack.substring(index, index + nlen);
                if (substring.hashCode() == nhash && substring.equals(needle)) {
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
     * kmp 算法
     * https://www.cnblogs.com/zhangtianq/p/5839909.html
     * <p>
     * <p>
     * 与BF算法相比
     * kmp算法就是为了在比较中让模式串尽量右移，从而达到提高效率效果
     * <p>
     * KMP的算法流程：
     * 假设现在文本串S匹配到 i 位置，模式串P匹配到 j 位置
     * 如果j = -1，或者当前字符匹配成功（即S[i] == P[j]），都令i++，j++，继续匹配下一个字符；
     * 如果j != -1，且当前字符匹配失败（即S[i] != P[j]），则令 i 不变，j = next[j]。此举意味着失配时，模式串P相对于文本串S向右移动了j - next [j] 位。
     * 换言之，当匹配失败时，模式串向右移动的位数为：失配字符所在位置 - 失配字符对应的next 值，即移动的实际位数为：j - next[j]，且此值大于等于1。(重点：i没变)
     * <p>
     * next 数组各值的含义：代表当前字符之前的子字符串中，有多大长度的相同前缀后缀。例如如果next [j] = k，代表j 之前的字符串中有最大长度为k 的相同前缀后缀。
     *
     * @param haystack
     * @param needle
     * @return
     */
    public static int kmpAlgorithm(String haystack, String needle) {
        if (needle == null || needle.length() == 0) return 0;
        if (haystack == null || haystack.length() < needle.length()) return -1;

        int i = 0;//haystack匹配下标
        int j = 0;//needle匹配下标
        int hlen = haystack.length();
        int nlen = needle.length();

        int[] next = getKMPNext(needle, nlen);
        while (i < hlen && j < nlen) {
            if (j == -1 || haystack.charAt(i) == needle.charAt(j)) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }

        if (j == nlen) return i - j;
        return -1;
    }

    private static int[] getKMPNext(String needle, int nlen) {
        int[] next = new int[nlen];
        next[0] = -1;
        int k = -1;
        int j = 0;
        while (j < nlen - 1) {
            if (k == -1 || needle.charAt(j) == needle.charAt(k)) {
                k++;
                j++;
                next[j] = k;
            } else {
                k = next[k];
            }
        }
        return next;
    }

    /**
     * next数组算法优化
     *
     * @param needle
     * @param nlen
     * @return
     */
    private static int[] getKMPNext_v2(String needle, int nlen) {
        int[] next = new int[nlen];
        next[0] = -1;
        int k = -1;
        int j = 0;
        while (j < nlen - 1) {
            if (k == -1 || needle.charAt(j) == needle.charAt(k)) {
                k++;
                j++;
                if (needle.charAt(j) != needle.charAt(k)) {
                    next[j] = k;
                } else {
                    next[j] = next[k];
                }
            } else {
                k = next[k];
            }
        }
        return next;
    }

    /**
     * bm 算法
     * https://mp.weixin.qq.com/s/S68mm8XicGerimD8zC6b-Q
     * https://blog.csdn.net/wwj_748/article/details/8686576
     * <p>
     * BM算法定义了两个规则：
     * <p>
     * 坏字符规则：当文本串中的某个字符跟模式串的某个字符不匹配时，我们称文本串中的这个失配字符为坏字符，此时模式串需要向右移动，
     *      移动的位数 = 坏字符在模式串中的位置 - 坏字符在模式串中最右出现的位置。此外，如果"坏字符"不包含在模式串之中，则最右出现位置为-1。
     * 好后缀规则：当字符失配时，后移位数 = 好后缀在模式串中的位置 - 好后缀在模式串上一次出现的位置，且如果好后缀在模式串中没有再次出现，则为-1。
     *
     * @param haystack
     * @param needle
     * @return
     */
    public static int bmAlgorithm(String haystack, String needle) {
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        if (haystack == null || haystack.length() < needle.length()) {
            return -1;
        }

        // TODO: 2019/10/18 -xzj bm算法错误

        int hlen = haystack.length();
        int nlen = needle.length();
        //从头对其
        int start = 0;
        //文本串开始比较的位置
        int h_index = start + nlen - 1;
        //模式串开始比较的位置
        int n_index = nlen - 1;

        String googSuffix = "";
        while (h_index >= start) {
            if (haystack.charAt(h_index) == needle.charAt(n_index)) {
                googSuffix = needle.charAt(n_index) + googSuffix;
                h_index--;
                n_index--;
            } else {
                //坏字符
                //int badCharDist = n_index - bmBadCharDist(needle, haystack.charAt(h_index));
                int[] badCharsDist = bmBadCharsDist(needle);
                int badCharDist = n_index - badCharsDist[haystack.charAt(h_index)];

                //好后缀
                int goodSuffixDist = bmGoodSuffixDist(needle, googSuffix);
                if (badCharDist > goodSuffixDist) {
                    start = start + badCharDist;
                } else {
                    start = start + goodSuffixDist;
                }
                //重置文本串、模式串的开始比较位置，重置好后缀
                h_index = start + nlen - 1;
                if (h_index > hlen - 1) {
                    return -1;
                }
                googSuffix = "";
                n_index = nlen - 1;
            }
        }
        return h_index + 1;
    }

    /**
     * 坏字符规则：当文本串中的某个字符跟模式串的某个字符不匹配时，我们称文本串中的这个失配字符为坏字符，
     * 此时模式串需要向右移动，移动的位数 = 坏字符在模式串中的位置 - 坏字符在模式串中最右出现的位置。
     * 此外，如果"坏字符"不包含在模式串之中，则最右出现位置为-1。
     *
     * @param needle       模式串
     * @param c            坏字符
     * @return
     */
    private static int bmBadCharDist(String needle, Character c) {
        int rightIndex = -1;
        int nlen = needle.length();
        for (int i = nlen - 1; i >= 0; i--) {
            if (c == needle.charAt(i)) {
                rightIndex = i;
                break;
            }
        }
        return rightIndex;
    }

    /**
     * 计算模式串中字符最右的位置
     *
     * @param needle
     * @return
     */
    private static int[] bmBadCharsDist(String needle) {
        int[] ints = new int[256];
        for (int i = 0; i < 256; i++) {
            ints[i] = -1;
        }
        for (int j = 0; j < needle.length(); j++) {
            int ascii =(int) needle.charAt(j);
            ints[ascii] = j;
        }
        return ints;
    }

    /**
     * 好后缀规则：当字符失配时，后移位数 = 好后缀在模式串中的位置 - 好后缀在模式串**上一次**（不是第一次）出现的位置，且如果好后缀在模式串中没有再次出现，则为-1。
     * <p>
     * 举例：
     * 文本字符串：here is a simple example
     * 模式串：example
     * 所有的“好后缀”（mple、ple、le、e）之中，只有“e”在“example”的头部出现，所以后移6-0=6位
     *
     * @return
     */
    private static int bmGoodSuffixDist(String needle, String suffix) {
        if (StringUtils.isEmpty(suffix)) return 0;
        int dist = 0;
        for (int i = suffix.length() - 1; i >= 0; i--) {
            String goodSuffix = suffix.substring(i, suffix.length());
            String substring = needle.substring(0, needle.length() - goodSuffix.length());
            //goodSuffix好后缀在模式串中的位置
            int goodsuffix_index = needle.lastIndexOf(goodSuffix);
            //goodSuffix在needle中上一次出现时的索引
            int last_index = substring.lastIndexOf(goodSuffix);
            if (goodsuffix_index - last_index > dist) {
                dist = goodsuffix_index - last_index;
            }
        }
        return dist;
    }

    /**
     * Sunday算法
     *
     * @param haystack
     * @param needle
     * @return
     */
    public static int sundayAlgorithm(String haystack, String needle) {
        if (needle == null || needle.length() == 0) {
            return 0;
        }
        if (haystack == null || haystack.length() < needle.length()) {
            return -1;
        }

        int hstart = 0;//每次遇到不匹配的字符之后，重置haystack开始匹配的字符索引
        int i = 0;
        int j = 0;
        int hlen = haystack.length();
        int nlen = needle.length();
        while (i < hlen && j < nlen) {
            if (haystack.charAt(i) != needle.charAt(j)) {
                if (hstart + nlen > hlen - 1) {
                    return -1;
                }
                int dist = sundayDist(needle, haystack.charAt(hstart + nlen));
                hstart = hstart + dist + 1;
                i = hstart;
                j = 0;
            } else {
                i++;
                j++;
            }
        }
        if (j == nlen) return i - j;
        return -1;
    }

    /**
     * 计算字符c在needle中最右端的该字符到末尾的距离
     * c不在needle中，距离=needle的长度
     *
     * @param needle
     * @param c
     * @return
     */
    private static int sundayDist(String needle, Character c) {
        int len = needle.length();
        int index = len - 1;
        while (index > -1) {
            if (needle.charAt(index) == c) {
                break;
            }
            index--;
        }

        return len - index - 1;
    }

    public static void main(String[] args) {
        String haystack = "baabbaaaaaaabbaaaaabbabbababaabbabbbbbabbabbbbbbabababaabbbbbaaabbbbabaababababbbaabbbbaaabbaababbbaabaabbabbaaaabababaaabbabbababbabbaaabbbbabbbbabbabbaabbbaa";
        String needle = "bbaaaababa";
//        String haystack = "substring searching algorithm";
//        String needle = "search";
//
        LOGGER.info("strStr.result : {}", LeetCodeStrStr.strStr(haystack, needle));
        LOGGER.info("bf_Algorithm.result : {}", LeetCodeStrStr.bfAlgorithm(haystack, needle));
        LOGGER.info("rkAlgorithm.result : {}", LeetCodeStrStr.rkAlgorithm(haystack, needle));
        LOGGER.info("kmpAlgorithm.result : {}", LeetCodeStrStr.kmpAlgorithm(haystack, needle));
//        //haystack : "here is a simple example"; needle : "example"
        LOGGER.info("bmAlgorithm.result : {}", LeetCodeStrStr.bmAlgorithm(haystack, needle));
//        //haystack : "substring searching algorithm"; needle : "search"
        LOGGER.info("sundayAlgorithm.result : {}", LeetCodeStrStr.sundayAlgorithm(haystack, needle));
    }
}
