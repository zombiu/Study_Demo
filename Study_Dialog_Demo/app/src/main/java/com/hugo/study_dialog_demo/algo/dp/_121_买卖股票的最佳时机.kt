package com.hugo.study_dialog_demo.algo.dp

/**
 * 121. 买卖股票的最佳时机
 * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock/
 */
class _121_买卖股票的最佳时机 {
    // 拆分子问题 要求出最大利润，那么就要找到前i天的最佳买入时机
    // 通过前n-1天的最佳买入时机，计算在前n天卖出时的利润，计算max
    // 定义状态 dp[i] 表示前i天，买入的最佳时机
    class Solution {
        fun maxProfit(prices: IntArray): Int {
            var n = prices.size
            var dp = IntArray(n)
            // [7,1,5,3,6,4]
            // [7,1,5,3,6]
            // [7,1,5,3]
            // [7,1,5]
            // 保存最小值 和 最大利润
            dp[0] = prices[0]
            var maxProfit = 0
            for (i in 1 until n) {
                dp[i] = Math.min(dp[i - 1], prices[i])
                maxProfit = Math.max(maxProfit, prices[i] - dp[i])
            }
            return maxProfit
        }
    }
}