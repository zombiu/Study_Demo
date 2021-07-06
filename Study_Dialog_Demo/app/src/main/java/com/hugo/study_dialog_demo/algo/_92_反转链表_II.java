package com.hugo.study_dialog_demo.algo;

/**
 * 92. 反转链表 II
 * https://leetcode-cn.com/problems/reverse-linked-list-ii/
 */
public class _92_反转链表_II {

    // 解题思路：快慢指针，循环时定位到left开始位置，通过快慢指针进行反转，到达right位置后，结束反转。

    /**
     * @param head
     * @param left  开始时的结点的值 不是索引，表示链表第几个元素 真坛蜜恶心
     * @param right 结束时的结点的值
     * @return
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {
        // 如果只有一个 结点需要反转 直接返回
        if (left == right) {
            return head;
        }
        // 虚拟头结点一般用来应对边界检查
        ListNode newHead = new ListNode(-1, head);
        ListNode slow = newHead;
        ListNode fast = newHead.next;
        // 需要反转的链表头
        ListNode tmpHead = null;
        ListNode reverseBefore = null;
        ListNode reverseTail = null;
        int count = 0;
        while (fast != null) {
            count++;
            // 找到了 left 保存left之前的一个结点
            if (count == left) {
                // 反转之后的尾部
                reverseTail = slow.next;
                // 将开始反转的结点和之前的结点 断开
                reverseBefore = slow;
                slow.next = null;
            }

            if (reverseBefore != null) {
                ListNode tmp = fast;
                fast = fast.next;
                tmp.next = tmpHead;
                // 反转之后的头结点
                tmpHead = tmp;
            } else {
                fast = fast.next;
                slow = slow.next;
            }

            // 反转之后，检查反转头 是否是right结点，如果是 ，将反转之后的链表进行 与之前的链表进行连接
            if (tmpHead != null && count == right) {
                reverseBefore.next = tmpHead;
                reverseTail.next = fast;
                break;
            }

        }
        return newHead.next;
    }
}
