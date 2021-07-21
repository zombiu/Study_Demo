package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 958. 二叉树的完全性检验
 * https://leetcode-cn.com/problems/check-completeness-of-a-binary-tree/
 */
class _958_二叉树的完全性检验 {
    // 执行用时： 156 ms , 在所有 Kotlin 提交中击败了 100.00% 的用户
    class Solution {
        fun isCompleteTree(root: TreeNode?): Boolean {
            if (root == null) {
                return false
            }
            var queue: Queue<TreeNode?> = LinkedList()
            queue.offer(root)
            var vacancy = false
            while (!queue.isEmpty()) {
                var node = queue.poll()
                if (node == null) {
                    vacancy = true
                }else {
                    if (vacancy) {
                        return false
                    }
                    queue.offer(node.left)
                    queue.offer(node.right)
                }
            }
            return true
        }
    }
}