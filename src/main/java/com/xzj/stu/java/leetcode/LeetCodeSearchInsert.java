package com.xzj.stu.java.leetcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 搜索插入位置
 * 二分法
 *
 * @author zhijunxie
 * @date 2019/10/16 14:50
 */
public class LeetCodeSearchInsert {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeetCodeSearchInsert.class);

    public static void main(String[] args) {
        LeetCodeSearchInsert leetCode = new LeetCodeSearchInsert();

        LOGGER.info("result : {}", leetCode.searchInsert(new int[]{1, 3, 5, 6, 10, 20, 30, 40, 50}, 51));
    }

    public int searchInsert(int[] nums, int target) {
        if (nums == null || nums.length == 0) return 0;
        int left = 0;
        int right = nums.length;

        while (left < right) {
            int mid = (left + right) >>> 1;
            System.out.println("left=" + left + ", right=" + right + ", mid=" + mid);
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }
}
