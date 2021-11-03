package com.hugo.study_dialog_demo.algo.dp

/**
 * 122. 买卖股票的最佳时机 II
 * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/
 */
class _122_买卖股票的最佳时机_II {
    // dp[i] 表示 以i结尾的 连续上升子串 的利润
    class Solution {
        // 测试用例 6,1,3,2,4,7
        fun maxProfit(prices: IntArray): Int {
            var n = prices.size
            var dp = IntArray(n)
            dp[0] = 0
            var max = 0
            for (i in 1 until n) {
                var diff = prices[i] - prices[i - 1]
                // 表示从i-1 到 i是连续上升的
                if (diff > 0) {
                    dp[i] = dp[i - 1] + diff
                    max += diff
                } else {
                    dp[i] = 0
                }
            }
            return max
        }
    }

}