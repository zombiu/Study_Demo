package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 105. 从前序与中序遍历序列构造二叉树
 * https://leetcode-cn.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
 */
class _105_从前序与中序遍历序列构造二叉树 {
    class Solution {
        fun buildTree(preorder: IntArray, inorder: IntArray): TreeNode? {
            if (preorder.isEmpty() || inorder.isEmpty()) {
                return null
            }
            // 根节点
            var rootValue = preorder[0]
            var rootNode = TreeNode(rootValue)

            var indexOf = inorder.indexOf(rootValue)
            var inorderLeft = inorder.copyOf(indexOf)
            if (indexOf + 1 > inorder.size) {
                return rootNode
            }
            // indexOf + 1 == inorder.size时，表示已经copy的是空数组
            var inorderRight = inorder.copyOfRange(indexOf + 1, inorder.size)

            var preorderLeft = preorder.copyOfRange(1, inorderLeft.size + 1)
            var preorderRight = preorder.copyOfRange(preorderLeft.size + 1, preorder.size)


            var leftNode = buildTree(preorderLeft, inorderLeft)
            rootNode.left = leftNode
            var rightNode = buildTree(preorderRight,inorderRight)
            rootNode.right = rightNode
            return rootNode
        }
    }
}