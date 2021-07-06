package com.hugo.study_dialog_demo.algo;

/**
 * 707. 设计链表
 * https://leetcode-cn.com/problems/design-linked-list/
 */
public class _707_设计链表_双向链表 {


    public static class MyLinkedList {

        public static class ListNode {
            public int val;
            public ListNode next;
            public ListNode prev;

            ListNode() {
            }

            ListNode(int val) {
                this.val = val;
            }

            ListNode(ListNode prev, int val, ListNode next) {
                this.prev = prev;
                this.val = val;
                this.next = next;
            }
        }

        private ListNode first;
        private ListNode last;
        private int size;

        /**
         * Initialize your data structure here.
         */
        public MyLinkedList() {

        }

        /**
         * Get the value of the index-th node in the linked list. If the index is invalid, return -1.
         */
        public int get(int index) {
            if (index < 0 || index >= size) {
                return -1;
            }

            return node(index).val;
        }

        /**
         * Add a node of value val before the first element of the linked list. After the insertion, the new node will be the first node of the linked list.
         */
        public void addAtHead(int val) {
            ListNode oldFirst = this.first;
            this.first = new ListNode(null, val, oldFirst);
            // 只有一个元素时，first和last指向同一个元素
            if (oldFirst == null) {
                last = first;
            } else {
                oldFirst.prev = first;
            }
            size++;
        }

        /**
         * Append a node of value val to the last element of the linked list.
         */
        public void addAtTail(int val) {
            // 为空时
            if (size == 0) {
                addAtHead(val);
            } else {
                // 获取最后一个元素
                /*ListNode node = node(size - 1);
                last = new ListNode(val);
                node.next = last;
                last.prev = node;*/
                addAtIndex(size, val);
            }
        }

        /**
         * Add a node of value val before the index-th node in the linked list. If index equals to the length of linked list, the node will be appended to the end of linked list. If index is greater than the length, the node will not be inserted.
         */
        public void addAtIndex(int index, int val) {
            if (index <= 0) {
                addAtHead(val);
                return;
            } else if (index > size) {
                return;
            }
            ListNode node = node(index - 1);
            if (node == last) {
                last = new ListNode(node, val, node.next);
                node.next = last;
            } else {
                node.next = new ListNode(node, val, node.next);
            }
            size++;
        }

        /**
         * Delete the index-th node in the linked list, if the index is valid.
         */
        public void deleteAtIndex(int index) {
            if (index < 0 || index >= size) {
                return;
            }
            if (index == 0) {
                first = first.next;
                if (size == 1) {
                    last = null;
                } else {
                    first.prev = null;
                }
            } else {
                // 要删除结点的前一个结点
                ListNode node = node(index - 1);

                ListNode delNode = node.next;
                node.next = delNode.next;
                if (index == size - 1) {
                    last = node;
                } else {
                    delNode.next.prev = node;
                }
            }
            size--;
        }

        private ListNode node(int index) {
            int mid = size >> 1;
            if (index > mid) {
                // 从尾部往头部遍历
                ListNode node = last;
                for (int i = size - 1; i >= index; i--) {
                    if (index == size - 1) {
                        return node;
                    } else {
                        if (index == i) {
                            return node;
                        }
                        node = node.prev;
                    }
                }
                return node;
            } else {
                // 从头外尾部 遍历
                ListNode node = first;
                for (int i = 0; i <= index; i++) {
                    if (index == 0) {
                        return node;
                    } else {
                        if (index == i) {
                            return node;
                        }
                        node = node.next;
                    }
                }
                return node;
            }
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            if (size > 0) {
                ListNode node = first;
                for (int i = 0; i < size; i++) {
                    if (node != null) {
                        stringBuilder.append(node.val).append(",");
                        node = node.next;
                    }
                }
            }
            return "MyLinkedList{" +
                    "[" + stringBuilder.toString() +
                    "], size=" + size +
                    '}';
        }
    }

}
