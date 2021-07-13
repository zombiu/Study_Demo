package com.hugo.study_dialog_demo.algo.tree

import java.util.*
import kotlin.math.max

/**
 * 104. 二叉树的最大深度
 * https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/
 * 解题思路：用两个队列，对树进行层序遍历，遍历完一层时，换一个队列，深度+1
 */
class _104_二叉树的最大深度 {
    class Solution {
        fun maxDepth(root: TreeNode?): Int {
            if (root == null) {
                return 0
            }
            var depth = 0
            var queueA: Queue<TreeNode> = LinkedList()
            var queueB: Queue<TreeNode> = LinkedList()
            queueA.offer(root)
            while (!queueA.isEmpty()) {
                var poll = queueA.poll()

                poll.left?.let {
                    queueB.offer(it)
                }
                poll.right?.let {
                    queueB.offer(it)
                }
                if (queueA.isEmpty()) {
                    var tmp = queueA
                    queueA = queueB
                    queueB = tmp
                    depth++
                }
            }
            return depth
        }
    }
}