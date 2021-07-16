package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 108. 将有序数组转换为二叉搜索树
 * https://leetcode-cn.com/problems/convert-sorted-array-to-binary-search-tree/
 * 递归很好实现，迭代如何做?
 */
class _108_将有序数组转换为二叉搜索树 {
    // 执行用时： 180 ms , 在所有 Kotlin 提交中击败了 96.55% 的用户
    class Solution {
        fun sortedArrayToBST(nums: IntArray): TreeNode? {
            if (nums.isEmpty()) {
                return null
            }
            // 右移运算符(shr) 右移1位
            var midIndex = nums.size shr 1
            var node = TreeNode(nums[midIndex])
            var leftArray = Arrays.copyOf(nums, midIndex)
            var leftNode = sortedArrayToBST(leftArray)
            node.left = leftNode
            if (midIndex + 1 < nums.size) {
                var rightArray = Arrays.copyOfRange(nums, midIndex + 1, nums.size)
                var rightNode = sortedArrayToBST(rightArray)
                node.right = rightNode
            }
            return node
        }
    }
}