package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 919. 完全二叉树插入器
 * https://leetcode-cn.com/problems/complete-binary-tree-inserter/
 */
class _919_完全二叉树插入器 {
    // 执行用时： 268 ms , 在所有 Kotlin 提交中击败了 66.67% 的用户
    class CBTInserter(root: TreeNode?) {
        var root = root
        var queueA: LinkedList<TreeNode> = LinkedList()
        var queueB: LinkedList<TreeNode> = LinkedList()

        init {
            traversal(root!!)
        }

        fun traversal(root: TreeNode) {
            queueA.add(root)
            while (!queueA.isEmpty()) {
                var poll = queueA.poll()
                // 到了度为0的节点
                if (poll.left == null) {
                    queueA.add(0, poll)
                    break
                }

                poll.left?.let {
                    queueB.offer(it)
                }
                // 到了度为1的节点
                if (poll.right == null) {
                    queueA.add(0, poll)
                    break
                }

                poll.right?.let {
                    queueB.offer(it)
                }
                if (queueA.isEmpty()) {
                    var tmp = queueA
                    queueA = queueB
                    queueB = tmp
                }
            }
        }

        fun insert(v: Int): Int {
            var poll = queueA.poll()
            var treeNode = TreeNode(v)
            if (poll.left == null) {
                poll.left = treeNode
                queueA.add(0, poll)
            } else {
                // poll.right == null
                poll.right = treeNode
            }
            queueB.offer(treeNode)
            if (queueA.isEmpty()) {
                var tmp = queueA
                queueA = queueB
                queueB = tmp
            }
            return poll.`val`
        }

        fun get_root(): TreeNode? {
            return root
        }

    }
}