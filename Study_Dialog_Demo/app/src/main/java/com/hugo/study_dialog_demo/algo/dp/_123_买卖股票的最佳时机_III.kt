package com.hugo.study_dialog_demo.algo.dp

/**
 * 123. 买卖股票的最佳时机 III
 * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iii/
 * 未完成
 */
class _123_买卖股票的最佳时机_III {
    // 过不了 测试用例[1,2,4,2,5,7,2,4,9,0]  7 - 1 = 6  9 - 2 = 7
    // dp[i] 表示 以 i结尾的 严格递增序列 所能获取的利润 还需要计算 以[i]结尾的非递增序列的 最大利润
    class Solution {
        fun maxProfit(prices: IntArray): Int {
            var n = prices.size
            var dp = IntArray(n)
            // 表示最大的 一笔利润
            var first = 0
            // 表示次大的 一笔利润
            var second = 0
            for (i in 1 until n) {
                var diff = prices[i] - prices[i - 1]
                if (diff > 0) {
                    dp[i] = dp[i - 1] + diff
                } else {
                    dp[i] = 0

                    if (dp[i - 1] > second) {
                        second = dp[i - 1]
                    }
                    if (second > first) {
                        second = first
                        first = dp[i - 1]
                    }
                }
            }

            if (dp[n - 1] > second) {
                second = dp[n - 1]
            }
            if (second > first) {
                second = first
                first = dp[n - 1]
            }
            return Math.max(dp[n - 1], first + second)
        }
    }

    class Solution2 {
        fun maxProfit(prices: IntArray): Int {
            var n = prices.size
            var dp = IntArray(n)
            var onceDp = IntArray(n)
            // 表示最大的 一笔利润
            var first = 0
            // 表示次大的 一笔利润
            var second = 0
            for (i in 1 until n) {
                var diff = prices[i] - prices[i - 1]
                if (diff > 0) {
                    dp[i] = dp[i - 1] + diff
                } else {
                    dp[i] = 0

                    if (dp[i - 1] > second) {
                        second = dp[i - 1]
                    }
                    if (second > first) {
                        second = first
                        first = dp[i - 1]
                    }
                }
            }

            if (dp[n - 1] > second) {
                second = dp[n - 1]
            }
            if (second > first) {
                second = first
                first = dp[n - 1]
            }
            return Math.max(dp[n - 1], first + second)
        }
    }
}