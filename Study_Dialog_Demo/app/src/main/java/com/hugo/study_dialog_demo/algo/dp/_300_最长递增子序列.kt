package com.hugo.study_dialog_demo.algo.dp

import java.util.*

/**
 * 300. 最长递增子序列
 * https://leetcode-cn.com/problems/longest-increasing-subsequence/
 */
class _300_最长递增子序列 {
    // 动态规划的经典例子
    // dp[i]表示 序列中，以i结尾的最长递增子序列
    class Solution {
        fun lengthOfLIS(nums: IntArray): Int {
            var n = nums.size
            // dp[i]表示 以i结尾的最长递增子序列
            var dp = IntArray(n)
            // 初始化
            Arrays.fill(dp, 1)
            for (i in 0 until dp.size) {
                // 上面 或者 这里需要初始化
//                dp[i] = 1
    // 注意，这里的循环是为了计算出 以i结尾的最长递增子序列
                for (j in 0 until i) {
                    if (nums[i] > nums[j]) {
                        dp[i] = Math.max(dp[i], dp[j] + 1)
                    }
                }
            }

            var max = 1
            for (i in dp.indices) {
                max = Math.max(dp[i], max)
            }
            return max
        }
    }

}