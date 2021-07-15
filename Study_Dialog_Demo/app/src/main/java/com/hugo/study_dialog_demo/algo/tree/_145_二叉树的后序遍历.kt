package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 145. 二叉树的后序遍历
 * https://leetcode-cn.com/problems/binary-tree-postorder-traversal/
 *
 * 执行用时优于75%的用户
 */
class _145_二叉树的后序遍历 {
    class Solution {
        /**
         * 笨办法，插入null，来确定是否已经遍历过该结点
         */
        fun postorderTraversal(root: TreeNode?): List<Int> {
            if (root == null) {
                return emptyList()
            }
            var stack: Stack<TreeNode?> = Stack()
            stack.push(root)
            var linkedList: LinkedList<Int> = LinkedList()
            while (!stack.isEmpty()) {
                var pop = stack.pop() ?: continue
                if (stack.isEmpty() || stack.peek() != null) {
                    stack.push(null)
                    stack.push(pop)

                    pop.right?.let {
                        stack.push(it)
                    }

                    pop.left?.let {
                        stack.push(it)
                    }
                } else {
                    linkedList.add(pop.`val`)
                }
            }
            return linkedList
        }
    }
}