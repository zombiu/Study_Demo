package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 剑指 Offer 28. 对称的二叉树
 * https://leetcode-cn.com/problems/dui-cheng-de-er-cha-shu-lcof/
 * 解题思路 从根节点开始，层序遍历两个子树，a树的左子树，跟b树的右子树进行对比，对比值是否相等
 */
class _剑指Offer_28_对称的二叉树 {
    class Solution {
        fun isSymmetric(root: TreeNode?): Boolean {
            if (root == null) {
                return true
            }
            if (root.left == null && root.right == null) {
                return true
            }
            var leftQueue: Queue<TreeNode?> = LinkedList()
            leftQueue.offer(root.left)
            var rightQueue: Queue<TreeNode?> = LinkedList()
            rightQueue.offer(root.right)
            while (!leftQueue.isEmpty() && !rightQueue.isEmpty()) {
                var poll = leftQueue.poll()
                var poll1 = rightQueue.poll()
                if (poll == null && poll1 == null) {
                    continue
                }
                if (poll == null || poll1 == null) {
                    return false
                }

                if (poll?.`val` != poll1?.`val`) {
                    return false
                }

                leftQueue.offer(poll?.left ?: null)
                leftQueue.offer(poll?.right ?: null)


                rightQueue.offer(poll1!!.right)
                rightQueue.offer(poll1!!.left)

            }
            if (leftQueue.isEmpty() && rightQueue.isEmpty()) {
                return true
            }
            return false
        }
    }
}