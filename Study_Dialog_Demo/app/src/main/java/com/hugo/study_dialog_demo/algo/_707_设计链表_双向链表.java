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
                ListNode oldFirst = this.first;
                this.first = new ListNode(null, val, oldFirst);
                if (size == 0) {
                    last = first;
                } else {
                    oldFirst.prev = first;
                }
            } else if (index > size) {
                return;
            } else {
                ListNode node = node(index - 1);
                ListNode oldNextNode = node.next;
                node.next = new ListNode(node, val, oldNextNode);
                // 如果index == size -1, 那么oldNextNode == null
                if (oldNextNode != null) {
                    oldNextNode.prev = node.next;
                } else {
                    last = node.next;
                }
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
                if (size == 1) {
                    first = null;
                    last = null;
                } else {
                    ListNode oldFirst = this.first;
                    this.first = oldFirst.next;
                    first.prev = null;
                }
            } else {
                ListNode node = node(index);
                ListNode prevNode = node.prev;
                prevNode.next = node.next;
                node.prev = null;
                if (index == size - 1) {
                    last = prevNode;
                } else {
                    node.next.prev = prevNode;
                }
            }

            size--;
        }

        /**
         * 这里的循环查找有点搞人，浪费了太多时间
         * @param index
         * @return
         */
        private ListNode node(int index) {
            int mid = size >> 1;
            if (index >= mid) {
                ListNode node = last;
                if (index == size - 1) {
                    return node;
                }
                // 注意：开始循环时， 就已经执行了一次  node = node.prev;
                for (int i = size - 1; i > index; i--) {
                    node = node.prev;
                }
                return node;
            } else {
                ListNode node = first;
                if (index == 0) {
                    return node;
                }
                for (int i = 1; i <= index; i++) {
                    node = node.next;
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
