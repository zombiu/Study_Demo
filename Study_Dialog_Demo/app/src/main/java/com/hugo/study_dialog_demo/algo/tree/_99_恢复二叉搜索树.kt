package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 99. 恢复二叉搜索树
 * https://leetcode-cn.com/problems/recover-binary-search-tree/
 * 解题思路：二叉搜索树中序遍历是升序排列，
 * 第一个错误元素，肯定大于它右边的元素，第二个错误元素，肯定小于它左边的元素。
 * 这里需要注意，第一个错误元素之后的元素，一定是小于它左边的元素的
 */
class _99_恢复二叉搜索树 {
    // 执行用时： 220 ms , 在所有 Kotlin 提交中击败了 100.00% 的用户
    class Solution {
        fun recoverTree(root: TreeNode?): Unit {
            if (root == null) {
                return
            }
            // 进行中序遍历
            var stack: Stack<TreeNode> = Stack()
            var curNode = root
            var firstErrorNode: TreeNode? = null
            var lastErrorNode: TreeNode? = null
            var prevNode: TreeNode? = null
            while (!stack.isEmpty() || curNode != null) {
                if (curNode != null) {
                    stack.push(curNode)
                    curNode = curNode.left
                } else {
                    var node = stack.pop()
                    curNode = node.right
                    if (prevNode != null) {
                        if (firstErrorNode == null) {
                            if (prevNode.`val` > node.`val`) {
                                firstErrorNode = prevNode
                            }
                        }
                        if (firstErrorNode != null) {
                            if (prevNode.`val` > node.`val`) {
                                lastErrorNode = node
                            }
                        }
                    }
                    prevNode = node
                }
            }
            var tmp = firstErrorNode!!.`val`
            firstErrorNode.`val` = lastErrorNode!!.`val`
            lastErrorNode.`val` = tmp
        }
    }
}