package com.hugo.study_dialog_demo.algo.dp

/**
 * 53. 最大子序和
 * https://leetcode-cn.com/problems/maximum-subarray/
 */
class _53_最大子序列和 {

    class Solution {
        fun maxSubArray(nums: IntArray): Int {
            var max = nums[0]
            for (begin in nums.indices) {
                for (end in nums.indices) {
                    var result = 0
                    for (i in begin..end) {
                        result += nums[i]
                    }
                    if (result > max) {
                        max = result
                    }
                }

            }
            return max
        }
    }

    // 进行优化
    class Solution2 {
        fun maxSubArray(nums: IntArray): Int {
            var max = nums[0]
            for (begin in nums.indices) {
                for (end in nums.indices) {
                    var result = 0
                    for (i in begin..end) {
                        result += nums[i]
                    }
                    if (result > max) {
                        max = result
                    }
                }

            }
            return max
        }
    }
}