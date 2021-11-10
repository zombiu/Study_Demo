package com.hugo.study_dialog_demo.algo.dp

/**
 * 1143. 最长公共子序列
 * https://leetcode-cn.com/problems/longest-common-subsequence/
 */
class _1143_最长公共子序列 {
    class Solution {
        fun longestCommonSubsequence(text1: String, text2: String): Int {
            var text1Arr = text1.toCharArray()
            var text2Arr = text2.toCharArray()
            var m = text1Arr.size
            var n = text2Arr.size
            var dp = Array<IntArray>(m + 1) {
                IntArray(n + 1)
            }
            var max = 0
            for (i in 1..m) {
                for (j in 1..n) {
                    if (text1Arr[i - 1] == text2Arr[j - 1]) {
                        // 注意 这里取的是 i-1 ，j-1 对应的公共子序列的长度
                        dp[i][j] = dp[i - 1][j - 1] + 1
                    } else {
                        // 这里也不怎么好理解  说明两个子字符串的最后一位不相等，那么此时的状态 dp[i][j] 应该是 dp[i - 1][j] 和 dp[i][j - 1] 的最大值
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1])
                    }
                }
            }
            return dp[m][n]
        }
    }
}