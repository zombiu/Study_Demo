package com.hugo.study_dialog_demo.algo.dp

import java.util.*

/**
 * 518. 零钱兑换 II
 * https://leetcode-cn.com/problems/coin-change-2/
 * 没做出来
 */
class _518_零钱兑换_II {
    class Solution {
        fun change(amount: Int, coins: IntArray): Int {
            if (amount == 0) {
                return 1
            }
            Arrays.sort(coins)
            // 确定状态 dp[i][j] 表示 使用前i个面值的硬币，凑够总额j 一共有多少种方法
            // 这里有三种情况
            // 1.第i个硬币面值大于需要凑够的总额j，那么就不能选第i个硬币，前i个硬币凑够总额j的方法数就等于前i-1个硬币的方法数
            // 2.第i个硬币面值等于需要凑够的总额j，那么前i个硬币凑够总额j的方法数就等于前i-1个硬币的方法数 再加1
            // 3.第i个硬币面值小于需要凑够的总额j,那么    dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i - 1]]
            var m = coins.size
            var dp = Array(m + 1) {
                IntArray(amount + 1)
            }
            for (i in 1 .. m) {
                for (j in 1..amount) {
                    if (coins[i - 1] == j) {
                        dp[i][j] = dp[i - 1][j] + 1
                    } else if (j > coins[i -1]) {
                        dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i - 1]]
                    } else {
//                        j < coins[i])
                        dp[i][j] = dp[i - 1][j]
                    }

                }
            }
            return dp[m][amount]
        }
    }
}