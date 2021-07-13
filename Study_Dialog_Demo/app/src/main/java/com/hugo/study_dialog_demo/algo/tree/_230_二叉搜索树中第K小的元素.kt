package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 230. 二叉搜索树中第K小的元素
 * https://leetcode-cn.com/problems/kth-smallest-element-in-a-bst/
 * 解题思路：中序遍历,需要使用栈
 */
class _230_二叉搜索树中第K小的元素 {
    class Solution {
        fun kthSmallest(root: TreeNode?, k: Int): Int {
            if (root == null) {
                return 0
            }
            var stack: Stack<TreeNode> = Stack()
            stack.push(root)
            var count = 0
            while (!stack.isEmpty()) {
                var pop = stack.pop()
                if (pop.right == null) {
                    count++
                }

                pop.right?.let {
                    stack.push(it)
                }
            }
            return 0
        }
    }
}