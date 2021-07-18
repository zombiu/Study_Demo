package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 449. 序列化和反序列化二叉搜索树
 * https://leetcode-cn.com/problems/serialize-and-deserialize-bst/
 */
class _449_序列化和反序列化二叉搜索树 {
    // 解题思路：从前序与中序遍历序列构造唯一二叉树
    class Codec() {
        // Encodes a URL to a shortened URL.
        fun serialize(root: TreeNode?): String {
            if (root == null) {
                return ""
            }
            var stringBuilder = StringBuilder()
            var stack = Stack<TreeNode>()
            stack.push(root)
            // 前序遍历
            while (!stack.isEmpty()) {
                var node = stack.pop()
                stringBuilder.append(node.`val`)
                    .append(",")
                node.right?.let {
                    stack.push(it)
                }
                node.left?.let {
                    stack.push(it)
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
                .append("#")
            // 中序遍历
            var curNode = root
            while (!stack.isEmpty() || curNode != null) {
                if (curNode != null) {
                    stack.push(curNode)
                    curNode = curNode.left
                } else {
                    var node = stack.pop()
                    stringBuilder.append(node.`val`)
                        .append(",")
                    curNode = node.right
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
            return stringBuilder.toString()
        }

        // Decodes your encoded data to tree.
        fun deserialize(data: String): TreeNode? {
            if (data.isNullOrBlank()) {
                return null
            }
            var list = data.split("#")
            if (list.isEmpty()) {
                return null
            }
            var preorderString = list.get(0)
            var inorderString = list.get(1)
            var preorderList = preorderString.split(",").map {
                it.toInt()
            }
            var inorderList = inorderString.split(",").map {
                it.toInt()
            }
            return buildTree(preorderList.toIntArray(),inorderList.toIntArray())
        }

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