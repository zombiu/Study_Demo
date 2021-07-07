package com.hugo.study_dialog_demo.algo;

/**
 * 707. 设计链表
 * https://leetcode-cn.com/problems/design-linked-list/
 */
public class _707_设计链表_单链表 {


    public static class MyLinkedList {

        public static class ListNode {
            public int val;
            public ListNode next;

            ListNode() {
            }

            ListNode(int val) {
                this.val = val;
            }

            ListNode(int val, ListNode next) {
                this.val = val;
                this.next = next;
            }
        }

        private ListNode first;
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
            addAtIndex(0, val);
        }

        /**
         * Append a node of value val to the last element of the linked list.
         */
        public void addAtTail(int val) {
            addAtIndex(size, val);
        }

        /**
         * Add a node of value val before the index-th node in the linked list. If index equals to the length of linked list, the node will be appended to the end of linked list. If index is greater than the length, the node will not be inserted.
         */
        public void addAtIndex(int index, int val) {
            if (index <= 0) {
                first = new ListNode(val, first);
            } else if (index > size) {
                return;
            } else {
                ListNode node = node(index - 1);
                ListNode nextNode = node.next;
                node.next = new ListNode(val, nextNode);
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
            } else {
                ListNode node = node(index - 1);
                ListNode next = node.next;
                node.next = next.next;
            }
            size--;
        }

        private ListNode node(int index) {
            ListNode node = first;
            if (index == 0) {
                return node;
            }
            for (int i = 1; i <= index; i++) {
                node = node.next;
            }
            return node;
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
