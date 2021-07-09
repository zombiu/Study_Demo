package com.hugo.study_dialog_demo.algo.queue

/**
 * 641. 设计循环双端队列
 * https://leetcode-cn.com/problems/design-circular-deque/
 */
class _641_设计循环双端队列_数组实现 {

    class MyCircularDeque(k: Int) {

        var elements = Array<Int>(k) {
            -1
        }

        // 队头索引
        var first = 0

        // 尾部索引
        var last = 0

        // 元素数量
        var count = 0

        /** Initialize your data structure here. Set the size of the deque to be k. */


        /** Adds an item at the front of Deque. Return true if the operation is successful. */
        fun insertFront(value: Int): Boolean {
            if (isFull()) {
                return false
            }
            var i = 0
            if (!isEmpty()) {
                i = (first - 1 + elements.size) % elements.size
            }
            first = i
            if (isEmpty()) {
                last = first
            }
            elements[i] = value
            count++
            return true
        }

        /** Adds an item at the rear of Deque. Return true if the operation is successful. */
        fun insertLast(value: Int): Boolean {
            if (isFull()) {
                return false
            }
            last = (count + first) % elements.size
            elements[last] = value
            count++
            return true
        }

        /** Deletes an item from the front of Deque. Return true if the operation is successful. */
        fun deleteFront(): Boolean {
            if (isEmpty()) {
                return false
            }
            elements[first] = -1
            first = (first + 1) % elements.size
            count--
            return true
        }

        /** Deletes an item from the rear of Deque. Return true if the operation is successful. */
        fun deleteLast(): Boolean {
            if (isEmpty()) {
                return false
            }
            elements[last] = -1
            last = (last - 1 + elements.size) % elements.size
            count--
            return true
        }

        /** Get the front item from the deque. */
        fun getFront(): Int {
            return elements[first]
        }

        /** Get the last item from the deque. */
        fun getRear(): Int {
            return elements[last]
        }

        /** Checks whether the circular deque is empty or not. */
        fun isEmpty(): Boolean {
            return count == 0
        }

        /** Checks whether the circular deque is full or not. */
        fun isFull(): Boolean {
            return count == elements.size
        }

    }
}