package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 297. 二叉树的序列化与反序列化
 * https://leetcode-cn.com/problems/serialize-and-deserialize-binary-tree/
 * 超粗内存限制 最后两个用例过不去
 */
class _297_二叉树的序列化与反序列化 {

    /*class Codec() {
        // Encodes a URL to a shortened URL.
        fun serialize(root: TreeNode?): String {
            if (root == null) {
                return ""
            }
            var stringBuilder = StringBuilder()
            var queue: Queue<TreeNode?> = LinkedList()
            queue.offer(root)
            var levelSize = 1

            // todo 需要知道二叉树的深度
            var maxDepth = maxDepth(root)
            while (!queue.isEmpty() && maxDepth != 0) {
                var poll = queue.poll()
                stringBuilder.append(poll?.`val` ?: null)
                    .append(",")
                levelSize--

                queue.offer(poll?.left ?: null)
                queue.offer(poll?.right ?: null)
                if (levelSize == 0) {
                    levelSize = queue.size
                    maxDepth--
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
            // todo 移除尾部 所有的null 和 ,
            return stringBuilder.toString()
        }

        // Decodes your encoded data to tree.
        fun deserialize(data: String): TreeNode? {
            if (data.isNullOrBlank()) {
                return null
            }
            var list = data.split(",")
            if (list.isEmpty()) {
                return null
            }
            var listIndex = 0
            var node = TreeNode(list[listIndex++].toInt())
            var queue: Queue<TreeNode?> = LinkedList()
            queue.offer(node)
            var size = list.size
            while (!queue.isEmpty() && listIndex != size) {
                var poll = queue.poll()
                var s1 = list[listIndex++]
                if (listIndex == size) {
                    break
                }
                var s2 = list[listIndex++]

                if (poll == null) {
                    queue.offer(null)
                    queue.offer(null)
                } else {
                    if ("null" != s1) {
                        var left = TreeNode(s1.toInt())
                        poll.left = left
                        queue.offer(left)
                    } else {
                        queue.offer(null)
                    }
                    if ("null" != s2) {
                        var right = TreeNode(s2.toInt())
                        poll.right = right
                        queue.offer(right)
                    } else {
                        queue.offer(null)
                    }
                }
            }
            return node
        }

        fun maxDepth(root: TreeNode?): Int {
            if (root == null) {
                return 0
            }
            var depth = 0
            var queueA: Queue<TreeNode> = LinkedList()
            queueA.offer(root)
            // 记录每层的结点数量 第一层 只有一个根结点 数量为1
            var size = 1
            while (!queueA.isEmpty()) {
                var poll = queueA.poll()
                size--

                poll.left?.let {
                    queueA.offer(it)
                }
                poll.right?.let {
                    queueA.offer(it)
                }
                // 当前层数 的结点已遍历完 深度+1 记录下一层结点的数量
                if (size == 0) {
                    size = queueA.size
                    depth++
                }
            }
            return depth
        }
    }*/

    // 解题思路：从前序与中序遍历序列构造唯一二叉树
    class Codec2() {
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