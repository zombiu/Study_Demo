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
            Arrays.sort(coins)
            // 确定状态 dp[i] 表示 凑够总额i 一共有多少种方法
            var dp = IntArray(amount + 1)
            // 初始化 凑够总额0 有一种方法
            dp[0] = 0
            for (i in 1 until dp.size) {
                var num = 0
                for (j in coins.indices) {
                    if (i == coins[j]) {
                        num += 1
                    } else if (i > coins[j]) {
                        num += dp[i - coins[j]]
                    }
                }
                dp[i] = num
            }
            return dp[amount]
        }
    }
}