package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 226. 翻转二叉树
 * https://leetcode-cn.com/problems/invert-binary-tree/
 */
class _226_翻转二叉树 {

    class Solution {
        fun invertTree(root: TreeNode?): TreeNode? {
            if (root == null) {
                return root
            }
            var node: TreeNode = root
            var queue: Queue<TreeNode> = LinkedList<TreeNode>()
            queue.offer(node)

            while (!queue.isEmpty()) {
                var poll = queue.poll()
                var tmp = poll.left
                poll.left = poll.right
                poll.right = tmp
                poll.left?.let {
                    queue.offer(it)
                }
                poll.right?.let {
                    queue.offer(it)
                }
            }
            return root
        }
    }

}