package com.hugo.study_dialog_demo.algo;

class _24_反转链表 {
    /**
     * 已测试通过
     * @param head
     * @return
     */
    public ListNode reverseList(ListNode head) {
        ListNode fast = head;
        // 使用一个额外的 head结点 保存反转的链表头结点
        ListNode newHead = null;
        while (fast != null) {
            // 先保存fast结点
            ListNode tmp = fast;
            // 移动fast到下一个节点
            fast = fast.next;

            tmp.next = newHead;
            newHead = tmp;
        }
        return newHead;
    }
}
