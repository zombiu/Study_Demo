package com.hugo.study_dialog_demo.algo.dp

/**
 * 64. 最小路径和
 * https://leetcode-cn.com/problems/minimum-path-sum/
 */
class _64_最小路径和 {
    class Solution {
        fun minPathSum(grid: Array<IntArray>): Int {
            var m = grid.size
            var n = grid[0].size
            var dp = Array<IntArray>(m) {
                IntArray(n)
            }
            dp[0][0] = grid[0][0]
            for (i in 1 until m) {
                dp[i][0] = dp[i - 1][0] + grid[i][0]
            }
            for (j in 1 until n) {
                dp[0][j] = dp[0][j - 1] + grid[0][j]
            }
            for (i in 1 until m) {
                for (j in 1 until n) {
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j]
                }
            }
            return dp[m - 1][n - 1]
        }
    }
}