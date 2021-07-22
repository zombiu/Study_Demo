package com.hugo.study_dialog_demo.algo.array

import java.util.*

/**
 * 1. 两数之和
 * https://leetcode-cn.com/problems/two-sum/
 */
class _1_两数之和 {
    class Solution {
        fun twoSum(nums: IntArray, target: Int): IntArray {
            var firstIndex = 0
            var lastIndex = 1
            Arrays.sort(nums)
            while (nums[firstIndex] < 9 && firstIndex < nums.size - 1) {
                while (nums[lastIndex] <= 9 && lastIndex < nums.size) {
                    if (nums[firstIndex] + nums[lastIndex] == target) {
                        return intArrayOf(firstIndex, lastIndex)
                    } else {
                        lastIndex++
                    }
                }
                firstIndex++
            }
            return IntArray(0)
        }
    }
}