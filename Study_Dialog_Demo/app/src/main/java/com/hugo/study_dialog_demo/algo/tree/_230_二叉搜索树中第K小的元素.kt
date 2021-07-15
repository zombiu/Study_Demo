package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 230. 二叉搜索树中第K小的元素
 * https://leetcode-cn.com/problems/kth-smallest-element-in-a-bst/
 * 解题思路：中序遍历,需要使用栈
 *
 * 执行用时优于100%
 */
class _230_二叉搜索树中第K小的元素 {
    class Solution {
        fun kthSmallest(root: TreeNode?, k: Int): Int {
            if (root == null) {
                return -1
            }
            // 迭代算法 进行 中序遍历
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
                }else {
                    list.add(pop.`val`)
                    if (list.size == k) {
                        return pop.`val`
                    }
                }
            }
            return -1
        }
    }
}