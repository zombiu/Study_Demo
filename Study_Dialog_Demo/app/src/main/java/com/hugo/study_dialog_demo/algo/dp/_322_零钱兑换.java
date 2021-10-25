package com.hugo.study_dialog_demo.algo.dp;

import java.util.Arrays;
import java.util.Collections;

/**
 * 322. 零钱兑换
 * https://leetcode-cn.com/problems/coin-change/
 */
class _322_零钱兑换 {

    class Solution {
        public int coinChange(int[] coins, int amount) {
            Arrays.sort(coins);
            // 确定状态
            int[] dp = new int[amount + 1];
            // 初始化
            dp[0] = 0;
            // 状态转移方程 f[x] = min(f[x - coins[0]],f[x - coins[1]],.....) + 1
            for (int i = 1; i < dp.length; i++) {
                dp[i] = Integer.MAX_VALUE;
                for (int j = 0; j < coins.length; j++) {
                    // 边界条件
                    if (i >= coins[j] && dp[i - coins[j]] != Integer.MAX_VALUE) {
                        dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
                    }
                }
            }
            if (dp[amount] == Integer.MAX_VALUE) {
                return -1;
            }
            return dp[amount];
        }
    }
}
