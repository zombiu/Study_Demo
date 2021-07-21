package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 513. 找树左下角的值
 * https://leetcode-cn.com/problems/find-bottom-left-tree-value/
 */
class _513_找树左下角的值 {
    class Solution {
        fun findBottomLeftValue(root: TreeNode?): Int {
            var node = root!!
            var queue: Queue<TreeNode> = LinkedList<TreeNode>()
            queue.offer(node)
            var leftBottomNode: TreeNode? = null
            var levelCount = 1
            while (!queue.isEmpty()) {
                var poll = queue.poll()
                if (leftBottomNode == null) {
                    leftBottomNode = poll
                }
                levelCount--
                poll.left?.let {
                    queue.offer(it)
                }
                poll.right?.let {
                    queue.offer(it)
                }
                if (levelCount == 0) {
                    levelCount = queue.size
                    if (levelCount == 0) {
                        break
                    } else {
                        leftBottomNode = null
                    }
                }
            }
            return leftBottomNode!!.`val`
        }
    }
}