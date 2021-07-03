package com.hugo.study_dialog_demo;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 * int val;
 * ListNode next;
 * ListNode() {}
 * ListNode(int val) { this.val = val; }
 * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution2 {
    /**
     * 使用三指针结题
     *
     * @param head
     * @return
     */
    public ListNode deleteDuplicates(ListNode head) {
        // 这里主要是找出 head结点 和prev结点
        head = findNewHead(head);

        if (head == null) {
            return head;
        }
        ListNode prev = head;
        ListNode slow = head.next;
        ListNode fast = slow.next;
        int delVal = -101;
        return head;
    }

    public ListNode findNewHead(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode fast = head.next;
        ListNode slow = head;
        int delVal = -101;

        while (fast != null) {
            if (slow.val == fast.val) {
                fast = fast.next;
                slow.next = fast;
                delVal = slow.val;
                // 比如说 【1，1，1】 这里有可能 fast为null 循环结束，所以结束后 要判断slow是否是重复元素
            } else if (slow.val == delVal) {
                slow = fast;
                fast = fast.next;
            } else {
                // 找到头结点 直接返回
                head = slow;
                return head;
            }
        }
        // 如果走到这里，说明head没有被赋值 并且 fast为null
        if (slow.val == delVal) {
            slow = fast;
        }
        return slow;
    }
}