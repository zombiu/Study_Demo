package com.hugo.study_dialog_demo.algo;

import com.blankj.utilcode.util.LogUtils;

/**
 * 请判断一个链表是否为回文链表。
 * https://leetcode-cn.com/problems/palindrome-linked-list/
 * 力扣提交时，如果是奇数个数的链表，中间结点不参与回文
 *
 * 解题思路：
 * 在一次遍历过程中，主要是通过快慢指针，先反转链表前半部分，然后通过后与后半部分对比，判断是否回文。
 * 需要注意的是，题目具有一定的误导性，如果是链表结点为奇数个时，不能直接返回false，最中间的元素在计算时不参与回文计算
 */
public class _234_回文链表 {

    /**
     * 快慢指针 从中间分割 反转其中一个，再对比
     *
     * @param head
     * @return
     */
    public boolean isPalindrome(ListNode head) {
        // 没有结点 或者只有一个结点 返回true
        if (head == null || head.next == null) {
            // 力扣这里返回false 会出错
            return true;
        }
        // 虚拟头结点
        // ListNode newHead = new ListNode(-1,head);
        ListNode fast = head;
        ListNode slow = head;

        // 使用一个额外的 head结点 保存反转的链表头结点
        ListNode newHead = null;
        // 可能回文的 链表头部
        ListNode palindromeNode = null;
        while (fast != null) {

            // 如果下一节点为null 说明到了链表尾部 数目链表数目为奇数 计算回文时 中间的结点不参与
            ListNode fastNext = fast.next;
            if (fastNext == null) {
                slow = slow.next;
                palindromeNode = slow;
                break;
            }
            // 移动fast到下一个节点
            fast = fastNext;
            fast = fast.next;
            // 这里等于null 说明已经到了链表尾部 并且链表数目为偶数 可能是回文链表
            if (fast == null) {
                LogUtils.e(fast);
            }

            ListNode slowNext = slow.next;
            // 先保存fast结点
            ListNode tmp = slow;
            tmp.next = newHead;
            newHead = tmp;

            // 此时的slow 是 后半段的链表 的开始
            slow = slowNext;
            palindromeNode = slow;
        }

        // 链表为奇数个数时，中间结点不参与回文，这里循环 必须使用后面的回文链表头
        while (palindromeNode != null) {
            if (palindromeNode.val != newHead.val) {
                return false;
            }
            palindromeNode = palindromeNode.next;
            newHead = newHead.next;
        }
        return true;
    }
}
