package com.hugo.study_dialog_demo.algo.dp

import android.util.Log

/**
 * 63. 不同路径 II
 * https://leetcode-cn.com/problems/unique-paths-ii/
 */
class _63_不同路径_II {
    class Solution {
        fun uniquePathsWithObstacles(obstacleGrid: Array<IntArray>): Int {
            var m = obstacleGrid.size
            var n = obstacleGrid[0].size
            // 1.确定状态， dp[i][j] 表示到达这一步，一共有多少条不同的路径
            var dp = Array(m) {
                IntArray(n)
            }
            // 3.初始化状态 确定边界条件
            var hasBarrier = false
            for (i in 0 until m) {
                if (hasBarrier || obstacleGrid[i][0] == 1) {
                    dp[i][0] = 0
                    hasBarrier = true
                } else {
                    dp[i][0] = 1
                }
            }
            hasBarrier = false
            for (j in 0 until n) {
                if (hasBarrier || obstacleGrid[0][j] == 1) {
                    dp[0][j] = 0
                    hasBarrier = true
                } else {
                    dp[0][j] = 1
                }
            }
            for (i in 1 until m) {
                for (j in 1 until n) {
                    // 2.状态转移方程
                    if (obstacleGrid[i][j] == 1) {
                        dp[i][j] = 0
                    } else {
                        dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
                    }
                }
            }
            return dp[m - 1][n - 1]
        }
    }
}