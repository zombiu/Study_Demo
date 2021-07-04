package com.hugo.study_dialog_demo.algo;

/**
 * 82. 删除排序链表中的重复元素 II
 * https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list-ii/
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode deleteDuplicates(ListNode head) {
        head = findNewHead(head);

        return head;
    }

    public ListNode findNewHead(ListNode head) {
        if(head == null || head.next == null) {
            return head;
        }
        ListNode fast = head.next;
        ListNode slow = head;
        // 只有两个结点时
        if(fast.next == null) {
            if(slow.val == fast.val) {
                return null;
            }
            return head;
        }
        // 获取到头之后 才能定位 prev
        ListNode prev = null;

        int delVal = -101;
        while(fast != null) {
            // 已经定位到了 head和第一个指针的位置
            if(prev != null) {
                if(slow.val == fast.val) {
                    fast = fast.next;
                    slow.next = fast;
                    delVal = slow.val;
                }else if(slow.val == delVal) {
                    slow = fast;
                    fast = fast.next;
                    prev.next = slow;
                }else {
                    prev = slow;
                    slow = fast;
                    fast = fast.next;
                }
            }else {
                // 定位 prev
                if(slow.val == fast.val) {
                    // 先删除fast
                    fast = fast.next;
                    slow.next = fast;
                    delVal = slow.val;
                    // 这里有可能 fast为null 循环结束，所以结束后 要判断slow是否是重复元素
                }else if(slow.val == delVal) {
                    // 再删除slow
                    slow = fast;
                    fast = fast.next;
                }else {
                    // 定位到了 prev 和 需要返回的head
                    head = prev = slow;
                    slow = fast;
                    fast = fast.next;
                }
            }
        }

        // 定位到了head，可以直接返回head
        if(prev != null) {
            // 最后要判断一下 slow是不是重复元素，是的话，需要删除
            if(slow.val == delVal) {
                prev.next = null;
            }
        }else {
            // prev 未赋值时，head也未赋值，需要返回一个新的head
            if (slow.val == delVal) {
                return null;
            }
            return slow;
        }

        return head;
    }
}