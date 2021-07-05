package com.hugo.study_dialog_demo.algo;

/**
 * 142. 环形链表 II
 * https://leetcode-cn.com/problems/linked-list-cycle-ii/
 * 解题思路：快慢指针
 * 慢指针一次走一步，快指针一次走两步，如果有环，快指针必定会在环中追上慢指针
 * 此时快指针走了慢指针一倍的距离，并且，从头结点到慢指针的距离 等于 快指针到尾结点的距离，
 * 此时慢指针要先多挪动一步，头结点和慢指针一步一步往尾部移动，交点就是环入口
 */
public class _142_环形链表_II {

    public ListNode detectCycle(ListNode head) {
        // 没有结点 或者 只有一个结点
        if (head == null || head.next == null) {
            return null;
        }
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null) {
            fast = fast.next;
            if (fast == null) {
                return null;
            }
            fast = fast.next;
            // 如果fast为null 说明没环
            if (fast == null) {
                return null;
            }
            if (fast.val == slow.val) {
                slow = slow.next;
                break;
            }
            slow = slow.next;
        }
        // 没有环
        if (fast == null) {
            return null;
        }
        // 有环
        slow = slow.next;
        while (slow.val != head.val) {
            slow = slow.next;
            head = head.next;
        }
        // 查找到了入口
        return slow;
    }
}
