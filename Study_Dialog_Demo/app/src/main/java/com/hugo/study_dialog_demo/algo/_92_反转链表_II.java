package com.hugo.study_dialog_demo.algo;

/**
 * 92. 反转链表 II
 * https://leetcode-cn.com/problems/reverse-linked-list-ii/
 */
public class _92_反转链表_II {

    // 解题思路：快慢指针，循环时定位到left开始位置，通过快慢指针进行反转，到达right位置后，结束反转。

    /**
     * @param head
     * @param left  开始时的结点的值 不是索引
     * @param right 结束时的结点的值
     * @return
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {
        // 虚拟头结点一般用来应对边界检查
        ListNode newHead = new ListNode(-1, head);
        ListNode slow = newHead;
        ListNode fast = newHead.next;

        ListNode tmpHead = null;
        ListNode reverseStart = null;
        ListNode reverseEnd = null;
        while (fast != null) {

            // 找到了 left 保存left之前的一个结点
            if (fast.val == left) {
                reverseStart = slow;
                ListNode slowNext = slow.next;
                slow.next = null;

                slow = slowNext;
                fast = fast.next;
                continue;
            }

            if (reverseStart != null) {
                ListNode tmp = fast;
                fast = fast.next;
                tmp.next = tmpHead;
                tmpHead = tmp;
            }

            fast = fast.next;
            slow = slow.next;

            // 找到了right 保存right之后的结点
            if (fast.val == right) {
                reverseEnd = fast.next;


                continue;
            }
        }
        return slow;
    }
}
