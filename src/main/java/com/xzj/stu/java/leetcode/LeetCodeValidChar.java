package com.xzj.stu.java.leetcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * 有效括号
 * 栈操作
 *
 * @author zhijunxie
 * @date 2019/10/15 16:10
 */
public class LeetCodeValidChar {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeetCodeValidChar.class);

    public static void main(String[] args) {
        LeetCodeValidChar leetCode = new LeetCodeValidChar();

        LOGGER.info("result : {}", leetCode.isValid("([][]){}"));
    }

    public boolean isValid(String s) {
        if (s.length() % 2 != 0) {
            return false;
        }
        Stack<Character> stack = new Stack();
        int i = 0;
        while (i < s.length()) {
            if (!stack.empty()) {
                char peek = stack.peek();
                if ((peek == '(' && s.charAt(i) == ')') ||
                        (peek == '[' && s.charAt(i) == ']') ||
                        (peek == '{' && s.charAt(i) == '}')) {
                    stack.pop();
                } else {
                    stack.add(s.charAt(i));
                }
            } else {
                stack.add(s.charAt(i));
            }
            i++;
        }
        return stack.empty();
    }
}
