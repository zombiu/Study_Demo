package com.hugo.study_dialog_demo.algo.dp

/**
 * 62. 不同路径
 * https://leetcode-cn.com/problems/unique-paths/
 */
class _62_不同路径 {
    class Solution {
        fun uniquePaths(m: Int, n: Int): Int {
            // 二维数组
            var dp = Array<IntArray>(m) {
                IntArray(n)
            }
            for (i in dp.indices) {
                for (j in 0 until n) {
                    // 初始化 边界条件
                    if (i == 0 || j == 0) {
                        dp[i][j] = 1
                    } else {
                        // 状态转移方程
                        dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
                    }
                }
            }
            return dp[m - 1][n - 1]
        }
    }
}