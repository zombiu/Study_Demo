package com.hugo.study_dialog_demo.algo.dp

/**
 * 70. 爬楼梯
 * https://leetcode-cn.com/problems/climbing-stairs/
 */
class _70_爬楼梯 {
    class Solution {
        fun climbStairs(n: Int): Int {
            // 1.确定状态
            var dp = IntArray(n + 1)
            // 确定流程
            for (i in 1..n) {
                // 3.初始化状态 确定边界
                if (i == 1) {
                    dp[i] = 1
                } else if (i == 2) {
                    dp[i] = 2
                } else {
                    // 2.状态转移方程
                    dp[i] = dp[i - 1] + dp[i - 1]
                }
            }
            return dp[n]
        }
    }
}