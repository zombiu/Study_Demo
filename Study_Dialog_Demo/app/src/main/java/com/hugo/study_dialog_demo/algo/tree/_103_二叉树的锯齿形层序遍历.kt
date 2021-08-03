package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 103. 二叉树的锯齿形层序遍历
 * https://leetcode-cn.com/problems/binary-tree-zigzag-level-order-traversal/
 */
class _103_二叉树的锯齿形层序遍历 {

    //执行用时： 184 ms , 在所有 Kotlin 提交中击败了 70.37% 的用户
    class Solution {
        fun zigzagLevelOrder(root: TreeNode?): List<List<Int>> {
            if (root == null) {
                return emptyList()
            }
            var totalList: LinkedList<List<Int>> = LinkedList()
            var deque: Deque<TreeNode> = LinkedList()
            deque.offer(root)
            var forward = true
            // 每层节点的数量
            var levelCount = deque.size
            // 每层的节点
            var linkedList: LinkedList<Int> = LinkedList()
            while (!deque.isEmpty()) {
                levelCount--
                if (forward) {
                    var pollFirst = deque.pollFirst()
                    linkedList.add(pollFirst.`val`)

                    pollFirst.left?.let {
                        deque.offerLast(it)
                    }
                    pollFirst.right?.let {
                        deque.offerLast(it)
                    }
                } else {
                    var pollLast = deque.pollLast()
                    linkedList.add(pollLast.`val`)
                    pollLast.right?.let {
                        deque.offerFirst(it)
                    }
                    pollLast.left?.let {
                        deque.offerFirst(it)
                    }
                }
                if (levelCount == 0) {
                    levelCount = deque.size
                    forward = !forward
                    totalList.add(linkedList)
                    linkedList = LinkedList()
                }
            }
            return totalList
        }
    }
}