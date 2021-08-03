package com.hugo.study_dialog_demo.algo.tree

/**
 * 543. 二叉树的直径
 * https://leetcode-cn.com/problems/diameter-of-binary-tree/
 * 解题思路：递归算法
 */
class _543_二叉树的直径 {
    // 未完成
    class Solution {
        var max = 0

        fun diameterOfBinaryTree(root: TreeNode?): Int {
            if (root == null) {
                return 0
            }
            if (root.left == null && root.right == null) {
                return 0
            }

            var i = diameterOfBinaryTree(root.left) + 1

            var i1 = diameterOfBinaryTree(root.right) + 1

            max = i + i1
            return max
        }
    }
}