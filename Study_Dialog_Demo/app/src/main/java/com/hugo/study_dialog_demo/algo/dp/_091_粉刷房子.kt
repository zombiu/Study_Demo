package com.hugo.study_dialog_demo.algo.dp

class _091_粉刷房子 {

    class Solution {
        fun minCost(costs: Array<IntArray>): Int {
            var m = costs.size
            var n = costs[0].size
            var dp = Array<IntArray>(m) {
                IntArray(n)
            }
            dp[0][0] = costs[0][0]
            dp[0][1] = costs[0][1]
            dp[0][2] = costs[0][2]
            for (i in 1 until m) {
                dp[i][0] = Math.min(dp[i-1][1],dp[i-1][2]) + costs[i][0]
                dp[i][1] = Math.min(dp[i-1][0],dp[i-1][2]) + costs[i][1]
                dp[i][2] = Math.min(dp[i-1][1],dp[i-1][0]) + costs[i][2]
            }
            var ints = dp[m - 1]
            var min = Math.min(ints[0], ints[1])
            return Math.min(min,ints[2])
        }
    }

}