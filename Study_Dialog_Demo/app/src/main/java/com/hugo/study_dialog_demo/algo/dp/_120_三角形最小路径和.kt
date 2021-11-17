package com.hugo.study_dialog_demo.algo.dp

/**
 * 120. 三角形最小路径和
 * https://leetcode-cn.com/problems/triangle/
 */
class _120_三角形最小路径和 {
    class Solution {
        fun minimumTotal(triangle: List<List<Int>>): Int {
            var m = triangle.size
            var n = triangle[m - 1].size
            var dp = Array(m) {
                IntArray(n)
            }
            // 初始化
            dp[0][0] = triangle[0][0]
            for (i in 1 until m) {
                var size = triangle[i].size
                for (j in triangle[i].indices) {
                    if (j == (size - 1)) {
                        dp[i][j] = dp[i - 1][j - 1] + triangle[i][j]
                    } else if (j > 0) {
                        dp[i][j] = Math.min(dp[i - 1][j - 1], dp[i - 1][j]) + triangle[i][j]
                    } else {
                        dp[i][j] = dp[i - 1][j] + triangle[i][j]
                    }
                }
            }
            var min = dp[m - 1][0]
            for (j in 1 until n) {
                min = Math.min(min, dp[m - 1][j])
            }
            return min
        }
    }
}