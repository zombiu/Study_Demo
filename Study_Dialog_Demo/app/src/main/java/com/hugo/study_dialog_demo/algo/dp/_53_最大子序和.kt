package com.hugo.study_dialog_demo.algo.dp

/**
 * 53. 最大子序和
 * https://leetcode-cn.com/problems/maximum-subarray/
 */
class _53_最大子序和 {

    /**
     * 无论最大子序列是什么，一定是以1 - N当中的某一个元素结尾，所以前面拆分的子问题是最优子问题，我们只需要找到最优子问题当中解的最大值，就是最终问题的解
     * dp[i]保存的是 以i结尾的子序列和的值，此时我们需要求出 子序列和中最大的一个值即可，这个就是最大子序列和
     */
    class Solution {
        // 状态转移方程 f(x) = min(f(x - 1),f(x - 1) + 当前数字)
        fun maxSubArray(nums: IntArray): Int {
            var dp = IntArray(nums.size)
            dp[0] = nums[0]
            var max = dp[0]
            for (i in 1 until nums.size) {
                dp[i] = Math.max(dp[i - 1] + nums[i], nums[i])
                max = Math.max(dp[i],max)
            }
            return max
        }
    }

}