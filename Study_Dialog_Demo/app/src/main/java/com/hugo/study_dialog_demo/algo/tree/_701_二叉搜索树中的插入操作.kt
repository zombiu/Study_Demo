package com.hugo.study_dialog_demo.algo.tree

/**
 * 701. 二叉搜索树中的插入操作
 * https://leetcode-cn.com/problems/insert-into-a-binary-search-tree/
 *
 */
class _701_二叉搜索树中的插入操作 {

    // 执行用时： 344 ms , 在所有 Kotlin 提交中击败了 77.78% 的用户
    class Solution {
        fun insertIntoBST(root: TreeNode?, `val`: Int): TreeNode? {
            if (root == null) {
                return TreeNode(`val`)
            }
            var curNode = root
            var nodeParent = root
            while (curNode != null) {
                nodeParent = curNode
                if (curNode.`val` > `val`) {
                    curNode = curNode.left
                } else {
                    curNode = curNode.right
                }
            }
            // 走到这里 表示curNode == null
            if (nodeParent!!.`val` > `val`) {
                nodeParent.left = TreeNode(`val`)
            } else {
                nodeParent.right = TreeNode(`val`)
            }
            return root
        }
    }
}