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

        fun maxDepth2(root: TreeNode?): Int {
            if (root == null) {
                return 0
            }
            var depth = 0
            var queueA: Queue<TreeNode> = LinkedList()
            queueA.offer(root)
            // 记录每层的结点数量 第一层 只有一个根结点 数量为1
            var size = 1
            while (!queueA.isEmpty()) {
                var poll = queueA.poll()
                size--

                poll.left?.let {
                    queueA.offer(it)
                }
                poll.right?.let {
                    queueA.offer(it)
                }
                // 当前层数 的结点已遍历完 深度+1 记录下一层结点的数量
                if (size == 0) {
                    size = queueA.size
                    depth++
                }
            }
            return depth
        }
    }
}