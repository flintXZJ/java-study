package com.xzj.stu.java.leetcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 合并两个有序链表
 *
 * @author zhijunxie
 * @date 2019/10/15 16:47
 */
public class LeetCodeMergeTwoLists {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeetCodeMergeTwoLists.class);

    public static void main(String[] args) {
        LeetCodeMergeTwoLists leetCode = new LeetCodeMergeTwoLists();

        //1->2->4, 1->3->4
        ListNode l1 = new ListNode(1, new ListNode(2, new ListNode(4, null)));
        ListNode l2 = new ListNode(1, new ListNode(3, new ListNode(4, null)));

        LOGGER.info("result : {}", leetCode.mergeTwoLists(l1, l2));
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }

        ListNode(int x, ListNode listNode) {
            val = x;
            next = listNode;
        }
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode listNode = new ListNode(0);
        ListNode tmp = listNode;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                tmp.next = new ListNode(l1.val);
                tmp = tmp.next;
                l1 = l1.next;
            } else {
                tmp.next = new ListNode(l2.val);
                tmp = tmp.next;
                l2 = l2.next;
            }
        }
        if (l1 == null) {
            tmp.next = l2;
        } else {
            tmp.next = l1;
        }
        return listNode.next;
    }
}
