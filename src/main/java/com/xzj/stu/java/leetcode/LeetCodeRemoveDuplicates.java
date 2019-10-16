package com.xzj.stu.java.leetcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 删除排序数组中的重复项
 *
 * @author zhijunxie
 * @date 2019/10/15 17:02
 */
public class LeetCodeRemoveDuplicates {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeetCodeRemoveDuplicates.class);

    public static void main(String[] args) {
        LeetCodeRemoveDuplicates leetCode = new LeetCodeRemoveDuplicates();

        LOGGER.info("result : {}", leetCode.removeDuplicates(new int[]{0,0,1,1,1,1,1,2,2,3,3,4,5}));
    }

    public int removeDuplicates(int[] nums) {
        int index = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[index]) {
                index++;
                nums[index] = nums[i];
            }
        }

        return index+1;
    }
}
