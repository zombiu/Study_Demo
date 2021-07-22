package com.hugo.study_dialog_demo.algo.tree

/**
 * 112. 路径总和
 * https://leetcode-cn.com/problems/path-sum/
 */
class _112_路径总和 {

    // 执行用时： 200 ms , 在所有 Kotlin 提交中击败了 82.35% 的用户
    class Solution {
        fun hasPathSum(root: TreeNode?, targetSum: Int): Boolean {
            if (root == null) {
                return false
            }

            if (root.`val` == targetSum && root.left == null && root.right == null) {
                return true
            }
           return hasPathSum(root.left,targetSum - root.`val`) || hasPathSum(root.right,targetSum-root.`val`)
        }
    }
}