package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 98. 验证二叉搜索树
 * https://leetcode-cn.com/problems/validate-binary-search-tree/
 * 解题思路：使用中序遍历，保持到list中，看是否是顺序排列
 */
class _98_验证二叉搜索树 {
    class Solution {
        fun isValidBST(root: TreeNode?): Boolean {
            if (root == null) {
                return true
            }
            var list:LinkedList<Int> = LinkedList()
            var stack: Stack<TreeNode> = Stack()
            stack.push(root)
            var node = root
            while (!stack.isEmpty() || node != null) {
                if (node == null) {
                    var pop = stack.pop()
                    list.add(pop.`val`)
                    node = pop.right
                    node?.let{
                        stack.push(it)
                    }
                    if (list.size > 1) {
                        if (list[list.size - 1] <= list[list.size - 2]) {
                            return false
                        }
                    }
                } else {
                    node.left?.let {
                        stack.push(it)
                    }
                    node = node.left
                }
            }
            return true
        }
    }
}