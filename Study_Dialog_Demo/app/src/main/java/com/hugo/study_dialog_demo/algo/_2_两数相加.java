package com.hugo.study_dialog_demo.algo;

/**
 * 2. 两数相加
 * https://leetcode-cn.com/problems/add-two-numbers/
 * 解题思路：快慢指针解题，首先在一个循环中，同时通过快慢指针进行结点相加保存到第一个结点中，此时有一下几种情况：
 * 1.两个链表一样长，那么此时只需要判断是否需要进位即可。
 * 2.第二个列表更长，那么需要将第一个链表的尾结点(也就是慢指针)指向第二个链表的快指针。
 * 3.如果有进位，从链接处，继续进行循环进位。
 * 4.循环结束，判断是否进位操作
 *
 * 对应结点进行相加时，注意进位问题
 */
public class _2_两数相加 {

    /**
     * 测试通过
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        // 虚拟头结点
        ListNode newL1 = new ListNode(-1, l1);
        ListNode slowL1 = newL1;
        ListNode fastL1 = newL1.next;

        ListNode newL2 = new ListNode(-1, l2);
        ListNode slowL2 = newL2;
        ListNode fastL2 = newL2.next;

        boolean flag = false;
        while (fastL1 != null && fastL2 != null) {
            // 合并后的结点数
            int i = fastL1.val + fastL2.val;
            // 如果前面的结点和 有进位
            if (flag) {
                i = i + 1;
            }
            // 合并后的值 需要进位时
            flag = i > 9;

            int mod = i % 10;
            fastL1.val = mod;
            // 移动指针
            slowL1 = slowL1.next;
            fastL1 = fastL1.next;

            slowL2 = slowL2.next;
            fastL2 = fastL2.next;

        }

        // 如果两个链表一样长
        if (fastL1 == null && fastL2 == null) {
            if (flag) {
                slowL1.next = new ListNode(1);
            }
            return newL1.next;
        }

        // 如果第二个链表更长
        if (fastL2 != null) {
            // 短链表 连接 长链表
            slowL1.next = fastL2;
            fastL1 = slowL1.next;
        }

        while (fastL1 != null && flag) {
            // 合并后的结点数
            int i = fastL1.val + 1;
            // 如果前面的结点和 有进位
            flag = i > 9;

            int mod = i % 10;
            fastL1.val = mod;
            // 移动指针
            slowL1 = slowL1.next;
            fastL1 = fastL1.next;
        }

        if (flag) {
            slowL1.next = new ListNode(1);
        }

        return newL1.next;
    }
}
