package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 101. 对称二叉树
 * https://leetcode-cn.com/problems/symmetric-tree/
 * 解题思路:层序遍历，deque来存放每一层的元素
 */
class _101_对称二叉树 {

    // 执行用时： 176 ms , 在所有 Kotlin 提交中击败了 76.67% 的用户
    class Solution {
        fun isSymmetric(root: TreeNode?): Boolean {
            if (root == null) {
                return true
            }
            var queue: Queue<TreeNode> = LinkedList()
            var deque: Deque<TreeNode?> = LinkedList()
            queue.offer(root)
            var levelCount = queue.size
            while (!queue.isEmpty()) {
                var poll = queue.poll()
                levelCount--
                poll.left?.let {
                    queue.offer(it)
                }
                poll.right?.let {
                    queue.offer(it)
                }
                deque.offer(poll.left)
                deque.offer(poll.right)
                if (levelCount == 0) {
                    levelCount = queue.size
                    while (!deque.isEmpty()) {
                        var pollFirst = deque.pollFirst()
                        var pollLast = deque.pollLast()
                        if (!valEquals(pollFirst, pollLast)) {
                            return false
                        }
                    }
                }
            }
            return true
        }

        fun valEquals(node1: TreeNode?, node2: TreeNode?): Boolean {
            if (node1 == null && node2 == null) {
                return true
            }
            if (node1 == null || node2 == null) {
                return false
            }
            return node1.`val` == node2.`val`
        }
    }
}