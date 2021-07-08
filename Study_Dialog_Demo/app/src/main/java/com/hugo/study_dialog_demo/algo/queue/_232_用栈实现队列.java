package com.hugo.study_dialog_demo.algo.queue;

import java.util.Stack;

/**
 * 232. 用栈实现队列
 * https://leetcode-cn.com/problems/implement-queue-using-stacks/
 * 请你仅使用两个栈实现先入先出队列。队列应当支持一般队列支持的所有操作（push、pop、peek、empty）
 */
public class _232_用栈实现队列 {

    class MyQueue {
        // 入队时的栈
        private Stack<Integer> stackA = new Stack<>();
        // 出队时的栈
        private Stack<Integer> stackB = new Stack<>();
        // 队列头部
        private int front;

        /**
         * Initialize your data structure here.
         */
        public MyQueue() {

        }

        /**
         * Push element x to the back of queue.
         */
        public void push(int x) {
            if (empty()) {
                front = x;
            }
            while (!stackB.isEmpty()) {
                stackA.push(stackB.pop());
            }
            stackA.push(x);
        }

        /**
         * Removes the element from in front of queue and returns that element.
         */
        public int pop() {
            while (!stackA.isEmpty()) {
                stackB.push(stackA.pop());
            }
            Integer top = stackB.pop();
            if (stackB.isEmpty()) {
                front = 0;
            } else {
                front = stackB.peek();
            }
            return top;
        }

        /**
         * Get the front element.
         */
        public int peek() {
            return front;
        }

        /**
         * Returns whether the queue is empty.
         */
        public boolean empty() {
            return stackA.isEmpty() && stackB.isEmpty();
        }
    }

}

