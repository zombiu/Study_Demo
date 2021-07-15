package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 450. 删除二叉搜索树中的节点
 * https://leetcode-cn.com/problems/delete-node-in-a-bst/
 * 解题思路：删除节点一共分三种情况，度为0的节点，度为1的节点，度为2的节点
 * 1.度为0的节点是叶子节点，可以直接删除，特殊情况是整棵树只有一个根结点，将根节点置null
 * 2.度为1的节点，只要一个自结点，那么此时将被删除结点的父节点指向被删除节点的字节点即可。
 * 3.度为2的节点，此时需要查找该节点的前驱或者后继节点覆盖被删除的节点，此时将问题转换为删除前驱后继节点，前驱、后继节点的度必为0或者1，那么就变成上面两种情况了。
 */
class _450_删除二叉搜索树中的节点 {
    // 执行用时： 236 ms , 在所有 Kotlin 提交中击败了 100.00% 的用户
    class Solution {
        fun deleteNode(root: TreeNode?, key: Int): TreeNode? {
            if (root == null) {
                return null
            }
            var rootNode = root
            // 二分查找
            var node = root
            var nodeParent: TreeNode? = null
            while (node != null) {
                if (node.`val` == key) {
                    break
                }
                nodeParent = node
                if (node.`val` > key) {
                    node = node.left
                } else {
                    node = node.right
                }
            }

            // 没有对应的节点
            if (node == null) {
                return rootNode
            }

            // 被删除节点的度为2
            if (node.left != null && node.right != null) {
//                var successor = successor(node)!!
                nodeParent = node
                // 后继节点覆盖了删除节点之后 ，需要将后继节点删掉
                var delSuccessor = node.right!!
                while (delSuccessor.left != null) {
                    nodeParent = delSuccessor
                    delSuccessor = delSuccessor.left!!
                }
                node.`val` = delSuccessor.`val`
                node = delSuccessor
            }

            node!!
            // 走到这里 说明节点 度为1 或者 0
            var child = node.left
            if (child == null) {
                child = node.right
            }
            // 说明节点 度为1
            if (child != null) {
                // 删除的是节点度为1 并且是根节点
                if (nodeParent == null) {
                    rootNode = child
                } else if (nodeParent.left == node) {
                    nodeParent.left = child
                } else {
                    nodeParent.right = child
                }
            } else {
                // 删除的节点是根节点
                if (nodeParent == null) {
                    rootNode = null
                } else if (nodeParent.left == node) {
                    // 删除叶子节点
                    nodeParent.left = null
                } else {
                    nodeParent.right = null
                }
            }
            return rootNode
        }

        fun successor(root: TreeNode): TreeNode? {
            var node = root.right
            if (node == null) {
                return null
            }

            while (node!!.left != null) {
                node = node.left
            }
            return node
        }
    }
}