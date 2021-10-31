package com.hugo.study_dialog_demo.algo.dp

/**
 * 213. 打家劫舍 II
 * https://leetcode-cn.com/problems/house-robber-ii/
 */
class _213_打家劫舍_II {
    class Solution {
        fun rob(nums: IntArray): Int {
            if (nums.size < 2) {
                return nums[0]
            } else if (nums.size < 3) {
                return Math.max(nums[0], nums[1])
            } else if (nums.size < 4) {
                var max = Math.max(nums[0], nums[1])
                return Math.max(max, nums[2])
            }
            var dp = IntArray(nums.size)
            dp[0] = nums[0]
            dp[1] = nums[1]
            dp[2] = nums[2]
            // 如果数组大于3个元素，将数组从逻辑上划分为两个数组 例如 [1,2,3,1] 划分为 [1,2,3] 和 [2,3,1] 然后计算max
            for (i in 3 until dp.size) {
                dp[i] = Math.max(dp[i - 3] + nums[i - 1], dp[i - 2] + nums[i])
                // 用来过 测试用例 [1,3,1,3,100] [1,3,1,3,100,50]
                if (i - 4 >= 0) {
                    dp[i] = Math.max(dp[i], dp[i - 3] + nums[i])
                }
            }
            return Math.max(dp[nums.size - 1], dp[nums.size - 2])
        }
    }
}