package com.hugo.study_dialog_demo.algo.dp

/**
 * 931. 下降路径最小和
 * https://leetcode-cn.com/problems/minimum-falling-path-sum/
 */
class _931_下降路径最小和 {
    class Solution {
        // dp[i][j]表示 当前路径下的最小下降路径
        fun minFallingPathSum(matrix: Array<IntArray>): Int {
            var m = matrix.size
            var n = matrix[0].size
            var dp = Array<IntArray>(m) {
                IntArray(n)
            }
            // 初始化
            for (i in 0 until n) {
                dp[0][i] = matrix[0][i]
            }
            for (i in 1 until m) {
                for (j in 0 until n) {
                    dp[i][j] = Int.MAX_VALUE
                    var cur = matrix[i][j]
                    if (j > 0) {
                        dp[i][j] = Math.min(dp[i - 1][j - 1] + cur, dp[i][j])
                    }
                    if (j < n - 1) {
                        dp[i][j] = Math.min(dp[i - 1][j + 1] + cur, dp[i][j])
                    }
                    dp[i][j] = Math.min(dp[i - 1][j] + cur, dp[i][j])
                }
            }

            var min = Int.MAX_VALUE
            // 初始化
            for (i in 0 until n) {
                min = Math.min(min, dp[m - 1][i])
            }
            return min
        }
    }
}