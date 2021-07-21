package com.hugo.study_dialog_demo.algo.tree

import java.util.*
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.math.max

/**
 * 面试题 04.04. 检查平衡性
 * https://leetcode-cn.com/problems/check-balance-lcci/
 * 解题思路：后续遍历，map保存每个节点的高度
 */
class _面试题_04_04_检查平衡性 {

    // 执行用时： 192 ms , 在所有 Kotlin 提交中击败了 92.31% 的用户
    class Solution {
        var map: HashMap<TreeNode, Int> = HashMap()
        fun isBalanced(root: TreeNode?): Boolean {
            if (root == null) {
                return true
            }
            // 后续遍历
            var stack: Stack<TreeNode> = Stack()
            var node = root
            var prevNode = root
            while (!stack.isEmpty() || node != null) {
                if (node != null) {
                    stack.push(node)
                    node = node.left
                } else {
                    var pop = stack.pop()
                    if (pop.right != null && prevNode != pop.right) {
                        stack.push(pop)
                        node = pop.right
                    } else {
                        prevNode = pop
                        if (pop.left == null && pop.right == null) {
                            map[pop] = 1
                        } else {
                            var leftHeight = map.get(pop!!.left) ?: 0
                            var rightHeight = map.get(pop!!.right) ?: 0
                            var max = Math.max(leftHeight, rightHeight) + 1
                            map[pop] = max
                            if (Math.abs(leftHeight - rightHeight) > 1) {
                                return false
                            }
                        }
                    }
                }
            }
            return true
        }
    }
}