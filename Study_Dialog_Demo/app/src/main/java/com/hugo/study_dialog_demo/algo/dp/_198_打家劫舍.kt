package com.hugo.study_dialog_demo.algo.dp

/**
 * 198. 打家劫舍
 * https://leetcode-cn.com/problems/house-robber/
 *
 */
class _198_打家劫舍 {
    // 状态转移方程  f(x) = max(f(x-1),f(x-1) + 当前金额)
    class Solution {
        fun rob(nums: IntArray): Int {
            if (nums.size < 2) {
                return nums[0]
            }
            var dp = IntArray(nums.size)
            dp[0] = nums[0]
            dp[1] = nums[1]
            for (i in 2 until dp.size) {
                dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i])
                // 用来过 测试用例 [2,1,1,2]
                if (i - 3 >= 0) {
                    dp[i] = Math.max(dp[i], dp[i - 3] + nums[i])
                }
            }
            return Math.max(dp[nums.size - 1], dp[nums.size - 2])
        }
    }
}