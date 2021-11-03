package com.hugo.study_dialog_demo.algo.dp

/**
 * 122. 买卖股票的最佳时机 II
 * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/
 */
class _122_买卖股票的最佳时机_II {
    // n-1天 多次交易加起来的最大值 和 一次交易的最大利润 取max
    class Solution {
        // 过不了 6,1,3,2,4,7
        fun maxProfit(prices: IntArray): Int {
            var n = prices.size
            var dp = IntArray(n)
            dp[0] = prices[0]
            // 保存最小值 计算一次交易的最大值 计算多次交易的累积值 两者进行比较
            var max = 0
            // 保存上一次交易结束后的临时最小值
            var tmpMin = prices[0]
            // 多次交易 累积利润
            var sum = 0
            for (i in 1 until n) {
                dp[i] = Math.min(dp[i - 1], prices[i])
                // 计算 只交易一次时的最大值

                max = Math.max(max, prices[i] - dp[i - 1])
                // 计算多次交易的累积利润
                if (prices[i] > tmpMin) {
                    sum += prices[i] - tmpMin
                    // 重置下次交易的最小值
                    tmpMin = Int.MAX_VALUE
                } else {
                    tmpMin = prices[i]
                }
            }
            return Math.max(max, sum)
        }
    }

    class Solution2 {
        fun maxProfit(prices: IntArray): Int {
            var n = prices.size
            var dp = IntArray(n)
            dp[0] = prices[0]
            // 保存最小值 计算一次交易的最大值 计算多次交易的累积值 两者进行比较
            var max = 0
            for (i in 1 until n) {
                dp[i] = Math.min(dp[i - 1], prices[i])
                // 计算 只交易一次时的最大值
                max = Math.max(max, prices[i] - dp[i - 1])
            }
            var sum = 0
            var currentMin = prices[0]
            for (i in 1 until n) {
                if (prices[i] > currentMin) {
                    sum += prices[i] - currentMin
                    currentMin = Int.MAX_VALUE
                } else {
                    currentMin = prices[i]
                }
            }
            return Math.max(max, sum)
        }
    }

}