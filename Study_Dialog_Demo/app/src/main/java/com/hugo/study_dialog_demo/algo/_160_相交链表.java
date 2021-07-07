package com.hugo.study_dialog_demo.algo;

/**
 * 160. 相交链表
 * https://leetcode-cn.com/problems/intersection-of-two-linked-lists/
 */
public class _160_相交链表 {
    // 解题思路1：先转换成有环链表，再使用快慢指针
    // 解题思路2：同时遍历两个链表，并同时记录两个链表的个数，对比最后两个链表结点是否相等，如果相等就表示有相交，然后长链表偏移到与短链表平齐的结点，对比两个链表同位元素是否==
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        /*// 如果其中一个链表 为null 或者只有一个结点 说明没有相交 返回null
        if (headA == null || headA.next == null || headB == null || headB.next == null) {
            return null;
        }
        // 保存第一个链表的尾结点
        ListNode tailA = headA;
        while (tailA.next != null) {
            tailA = tailA.next;
        }
        tailA.next = headA;

        // 快慢指针遍历第二个链表 如果有相交，快指针会追上慢指针*/

        // 解题思路2
        ListNode tailA = headA;
        ListNode tailB = headB;
        int countA = 1;
        int countB = 1;
        while (tailA.next != null || tailB.next != null) {
            if (tailA.next != null) {
                tailA = tailA.next;
                countA++;
            }
            if (tailB.next != null) {
                tailB = tailB.next;
                countB++;
            }
        }

        if (tailA != tailB) {
            return null;
        }
        ListNode curNodeA = headA;
        ListNode curNodeB = headB;
        if (countA >= countB) {
            int i = countA - countB;
            while (curNodeA != null) {
                if (i > 0) {
                    i--;
                    curNodeA = curNodeA.next;
                } else {
                    if (curNodeB == curNodeA) {
                        return curNodeA;
                    }
                    curNodeA = curNodeA.next;
                    curNodeB = curNodeB.next;
                }
            }
        } else {
            int i = countB - countA;
            while (curNodeB != null) {
                if (i > 0) {
                    i--;
                    curNodeB = curNodeB.next;
                } else {
                    if (curNodeB == curNodeA) {
                        return curNodeA;
                    }
                    curNodeA = curNodeA.next;
                    curNodeB = curNodeB.next;
                }
            }
        }
        return null;
    }
}
