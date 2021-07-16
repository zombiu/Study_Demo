package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 114. 二叉树展开为链表
 * https://leetcode-cn.com/problems/flatten-binary-tree-to-linked-list/
 * 进阶：你可以使用原地算法（O(1) 额外空间）展开这棵树吗？
 * 没想出来怎么使用原地算法展开这棵树
 */
class _114_二叉树展开为链表 {
    // 执行用时： 160 ms , 在所有 Kotlin 提交中击败了 97.67% 的用户
    class Solution {
        fun flatten(root: TreeNode?): Unit {
            if (root == null) {
                return
            }
            var stack: Stack<TreeNode> = Stack()
            stack.push(root)
            var linkedList: LinkedList<TreeNode> = LinkedList()
            while (!stack.isEmpty()) {
                var pop = stack.pop()
                linkedList.add(pop)
                pop.right?.let {
                    stack.push(it)
                }
                pop.left?.let {
                    stack.push(it)
                }
            }
            var node = linkedList.removeFirst()
            while (!linkedList.isEmpty()) {
                var removeFirst = linkedList.removeFirst()
                node.left = null
                node.right = removeFirst
                node = removeFirst
            }
        }
    }
}