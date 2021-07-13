package com.hugo.study_dialog_demo.algo.tree

import java.util.*
import kotlin.collections.ArrayList

/**
 * 107. 二叉树的层序遍历 II
 * https://leetcode-cn.com/problems/binary-tree-level-order-traversal-ii/
 */
class _107_二叉树的层序遍历_II {
    class Solution {
        fun levelOrderBottom(root: TreeNode?): List<List<Int>> {
            if (root == null) {
//                return arrayListOf(ArrayList<Int>())
                return emptyList()
            }
            var tierList: LinkedList<LinkedList<Int>> = LinkedList()
            var queueA: Queue<TreeNode> = LinkedList()
            var queueB: Queue<TreeNode> = LinkedList()
            queueA.offer(root)
            var arrayList: LinkedList<Int> = LinkedList()
            while (!queueA.isEmpty()) {
                var poll = queueA.poll()
                arrayList.add(poll.`val`)
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
                    tierList.add(0, arrayList)
                    arrayList = LinkedList()
                }
            }
            return tierList
        }
    }
}