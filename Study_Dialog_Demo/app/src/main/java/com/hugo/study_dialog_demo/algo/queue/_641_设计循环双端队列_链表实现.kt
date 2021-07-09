package com.hugo.study_dialog_demo.algo.queue

/**
 * 641. 设计循环双端队列
 * https://leetcode-cn.com/problems/design-circular-deque/
 */
class _641_设计循环双端队列_双向链表实现

class MyCircularDeque(k: Int) {

    class Node(var prev: Node? = null, var data: Int, var next: Node? = null)

    // 队头索引
    var first: Node? = null

    // 尾部索引
    var last: Node? = null

    // 元素数量
    var count = 0

    var capacity = k

    /** Initialize your data structure here. Set the size of the deque to be k. */


    /** Adds an item at the front of Deque. Return true if the operation is successful. */
    fun insertFront(value: Int): Boolean {
        if (isFull()) {
            return false
        }
        var oldFirst = first
        first = Node(prev = last, data = value, next = oldFirst)
        if (last == null) {
            last = first
            first?.prev = last
        }
        oldFirst?.prev = first
        last?.next = first
        count++
        return true
    }

    /** Adds an item at the rear of Deque. Return true if the operation is successful. */
    fun insertLast(value: Int): Boolean {
        if (isFull()) {
            return false
        }
        var oldLast = last
        last = Node(prev = oldLast, data = value, next = first)
        if (first == null) {
            first = last
        }
        oldLast?.next = last
        first?.prev = last
        count++
        return true
    }

    /** Deletes an item from the front of Deque. Return true if the operation is successful. */
    fun deleteFront(): Boolean {
        if (isEmpty()) {
            return false
        }
        // count == 1
        if (first == last) {
            first = null
            last = null
        } else {
            var oldFirst = first

            first = oldFirst?.next
            oldFirst?.next?.prev = first
            first?.prev = last
            last?.next = first
        }
        count--
        return true
    }

    /** Deletes an item from the rear of Deque. Return true if the operation is successful. */
    fun deleteLast(): Boolean {
        if (isEmpty()) {
            return false
        }
        if (first == last) {
            first = null
            last = null
        } else {
            var oldLast = last

            last = oldLast?.prev
            first?.prev = last
            last?.next = first
        }
        count--
        return true
    }

    /** Get the front item from the deque. */
    fun getFront(): Int {
        return first?.data ?: -1
    }

    /** Get the last item from the deque. */
    fun getRear(): Int {
        return last?.data ?: -1
    }

    /** Checks whether the circular deque is empty or not. */
    fun isEmpty(): Boolean {
        return count == 0
    }

    /** Checks whether the circular deque is full or not. */
    fun isFull(): Boolean {
        return count == capacity
    }

}