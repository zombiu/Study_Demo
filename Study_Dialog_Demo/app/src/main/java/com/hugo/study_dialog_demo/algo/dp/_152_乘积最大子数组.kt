package com.hugo.study_dialog_demo.algo.dp

import kotlin.math.min

/**
 * 152. 乘积最大子数组
 * https://leetcode-cn.com/problems/maximum-product-subarray/
 * 子数组 必须保证是连续的 不连续的叫子序列
 * 要考虑正数和负数两种情况
 */
class _152_乘积最大子数组 {
    class Solution {
        fun maxProduct(nums: IntArray): Int {
            var n = nums.size
            // 确定状态 dp[i] 表示以i结尾连续子数组的最大乘积 正数
            var dp1 = IntArray(n)
            // 确定状态 dp[i] 表示以i结尾连续子数组的最小乘积 负数
            var dp2 = IntArray(n)
            if (nums[0] > 0) {
                dp1[0] = nums[0]
            } else if (nums[0] < 0) {
                dp2[0] = nums[0]
            }
            var max = nums[0]
            for (i in 1 until n) {
                var num = nums[i]
                if (num > 0) {
                    // 如果 dp1[i - 1] = 0的话，就取num
                    dp1[i] = Math.max(num * dp1[i - 1], num)
                    // 取更小的
                    dp2[i] = Math.min(num * dp2[i - 1], 0)
                } else if (num < 0) {
                    // dp2[i - 1]<= 0 所以 num * dp2[i - 1] >= 0
                    dp1[i] = num * dp2[i - 1]
                    dp2[i] = Math.min(num, dp1[i - 1] * num)
                }
                max = Math.max(max, dp1[i])
            }
            return max
        }
    }
}