package com.hugo.study_dialog_demo.algo.dp

/**
 * 91. 解码方法
 * https://leetcode-cn.com/problems/decode-ways/
 *
 */
class _91_解码方法 {
    // 设数字串长度为N
    //要求数字串前N个字符的解密方式数
    //需要会知道数字串前N-1和N-2个字符的解密方式数
    // 状态:设数字串S前i个数字解密成字母串有f[i]种方式
    //
    // 状态转移方程 f(x) = f(x -1)|(x-1>= 1 & x -1 <= 9) + f(x -2)
    class Solution {
        fun numDecodings(s: String): Int {
            var n = s.length
            // dp[i] 表示 解密i个数字串有dp[i]种方式
            var dp = IntArray(n + 1)
            var charArr = s.toCharArray()
            if (charArr[0] == '0') {
                return 0
            }
            // 初始条件  空字符串 有1种解密方式
            dp[0] = 1
            for (i in 1..n) {
                if (charArr[i-1] in '1'..'9') {
                    dp[i] += dp[i - 1]
                }
                // 边界条件
                if (i > 1) {
                    var j = 10 * (charArr[i - 2] - '0') + (charArr[i - 1] - '0')
                    if (j in 10..26) {
                        dp[i] += dp[i - 2]
                    }
                }
            }
            return dp[n]
        }
    }
}