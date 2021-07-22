package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 111. 二叉树的最小深度
 * https://leetcode-cn.com/problems/minimum-depth-of-binary-tree/
 * 解题思路：层序遍历，返回第一个没有叶子节点所在的深度
 */
class _111_二叉树的最小深度 {

    // 执行用时： 440 ms , 在所有 Kotlin 提交中击败了 94.23% 的用户
    class Solution {
        fun minDepth(root: TreeNode?): Int {
            if (root == null) {
                return 0
            }
            var queue: Queue<TreeNode> = LinkedList()
            queue.offer(root)
            var deep = 1
            var levelCount = 1
            while (queue.isNotEmpty()) {
                var poll = queue.poll()
                if (poll.left == null && poll.right == null) {
                    return deep
                }
                poll.left?.let {
                    queue.offer(it)
                }
                poll.right?.let {
                    queue.offer(it)
                }

                levelCount--
                if (levelCount == 0) {
                    levelCount = queue.size
                    deep++
                }
            }
            return deep
        }
    }
}