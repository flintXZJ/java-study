package com.xzj.stu.java.leetcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 移除元素
 *
 * @author zhijunxie
 * @date 2019/10/15 17:14
 */
public class LeetCodeRemoveElement {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeetCodeRemoveElement.class);

    public static void main(String[] args) {
        LeetCodeRemoveElement leetCode = new LeetCodeRemoveElement();

        LOGGER.info("result : {}", leetCode.removeElement(new int[]{0,1,2,2,3,0,4,2}, 2));
    }

    public int removeElement(int[] nums, int val) {
        int index = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[index] = nums[i];
                index++;
            }
        }
        return index;
    }
}
