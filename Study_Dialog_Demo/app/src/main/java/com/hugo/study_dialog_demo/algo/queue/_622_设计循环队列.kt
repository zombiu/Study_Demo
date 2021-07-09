package com.hugo.study_dialog_demo.algo.queue

/**
 * 622. 设计循环队列
 * https://leetcode-cn.com/problems/design-circular-queue/
 */
class _622_设计循环队列 {
}

// 动态数组实现循环队列
class MyCircularQueue(k: Int) {

    var elements = Array<Int>(k) {
        -1
    }

    // 队头索引
    var front = 0
    // 元素数量
    var count = 0

    fun enQueue(value: Int): Boolean {
        // 满了之后 不能再入队了
        if (count == elements.size) {
            return false
        }
        var i = (count + front) % elements.size
        elements[i] = value
        count++
        return true
    }

    fun deQueue(): Boolean {
        if (isEmpty()) {
            return false
        } else {
            elements[front++] = -1
            front = front % elements.size
            count--
        }
        return true
    }

    fun Front(): Int {
        return elements[front]
    }

    fun Rear(): Int {
        if (isEmpty()) {
            return -1
        }
        var i = (count - 1 + front) % elements.size
        return elements[i]
    }

    fun isEmpty(): Boolean {
        return count == 0
    }

    fun isFull(): Boolean {
        return count == elements.size
    }

    override fun toString(): String {
        return "MyCircularQueue(elements=${elements.contentToString()}, front=$front, count=$count)"
    }

}