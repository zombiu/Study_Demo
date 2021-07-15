package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 94. 二叉树的中序遍历
 * https://leetcode-cn.com/problems/binary-tree-inorder-traversal/
 * 解题思路：首先现将根结点入栈，然后循环进行一下操作
 * 1.取出栈顶元素，然后将该元素和左右结点重新入栈，入栈时，在该元素和右子结点之间插入一个null，表示该结点已入栈过一次
 * 2.每次取出栈顶元素后，如果新栈顶元素是null，说明该元素已经重新入栈过，这里遍历后直接出栈即可。
 *
 * 执行用时优于93%
 */
class _94_二叉树的中序遍历 {
    class Solution {
        fun inorderTraversal(root: TreeNode?): List<Int> {
            if (root == null) {
                return emptyList()
            }
            var stack: Stack<TreeNode?> = Stack()
            stack.push(root)
            var list: LinkedList<Int> = LinkedList()
            while (!stack.isEmpty()) {
                var pop: TreeNode? = stack.pop() ?: continue
                pop!!
                if (stack.isEmpty() || stack.peek() != null) {
                    if (pop.right != null) {
                        stack.push(pop.right)
                    }
                    stack.push(null)
                    stack.push(pop)
                    if (pop.left != null) {
                        stack.push(pop.left)
                    }
                } else {
                    list.add(pop.`val`)
                }
            }
            return list
        }

        /**
         * 参考LeetCode 迭代解法
         */
        fun inorderTraversal2(root: TreeNode?): List<Int> {
            if (root == null) {
                return emptyList()
            }
            var stack: Stack<TreeNode> = Stack()
            var list: LinkedList<Int> = LinkedList()
            var node = root
            while (!stack.isEmpty() || node != null) {
                if (node != null) {
                    stack.add(node)
                    node = node.left
                } else {
                    var pop = stack.pop()
                    node = pop.right
                    node?.let{
                        stack.push(it)
                    }
                    list.add(pop.`val`)
                }
            }
            return list
        }
    }
}