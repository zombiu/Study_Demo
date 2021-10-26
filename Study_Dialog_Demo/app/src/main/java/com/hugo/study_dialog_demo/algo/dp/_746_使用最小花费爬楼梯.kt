package com.hugo.study_dialog_demo.algo.dp

import kotlin.math.min

/**
 * 746. 使用最小花费爬楼梯
 * https://leetcode-cn.com/problems/min-cost-climbing-stairs/
 */
class _746_使用最小花费爬楼梯 {
    class Solution {
        fun minCostClimbingStairs(cost: IntArray): Int {
            var size = cost.size
            // dp存储对应的
            var dp = IntArray(size)
            // 初始化
            dp[0] = cost[0]
            dp[1] = cost[1]
            // 边界条件
            for (i in 2 until dp.size) {
                // 状态转移方程 f(x) = min(f(x - 1) ,f(x - 2) ) + 当前坐标的花费
                dp[i] = Math.min(dp[i - 1], dp[i - 2]) + cost[i]

            }
            return Math.min(dp[size - 1], dp[size - 2])
        }
    }
}