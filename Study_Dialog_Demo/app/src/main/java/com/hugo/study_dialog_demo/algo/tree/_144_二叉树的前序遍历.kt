package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 144. 二叉树的前序遍历
 * https://leetcode-cn.com/problems/binary-tree-preorder-traversal/
 */
class _144_二叉树的前序遍历 {
    class Solution {
        fun preorderTraversal(root: TreeNode?): List<Int> {
            if (root == null) {
                return emptyList()
            }
            var stack: Stack<TreeNode> = Stack()
            stack.push(root)
            var list: LinkedList<Int> = LinkedList()
            while (!stack.isEmpty()) {
                var pop = stack.pop()
                list.add(pop.`val`)
                pop.right?.let {
                    stack.push(it)
                }
                pop.left?.let {
                    stack.push(it)
                }
            }
            return list
        }
    }
}