package com.hugo.study_dialog_demo.algo.dp

/**
 * 877. 石子游戏
 * https://leetcode-cn.com/problems/stone-game/
 */
class _877_石子游戏 {
    // dp[i] 表示第几次取石子之后的 石子总数
    class Solution {
        fun stoneGame(piles: IntArray): Boolean {
            var n = piles.size
            var dp1 = IntArray((n / 2) + 1)
            var dp2 = IntArray((n / 2) + 1)
            var i = 0
            var j = n - 1
            var index = 1
            while (i < j) {
                if (piles[i] >= piles[j]) {
                    dp1[index] = piles[i] + dp1[index -1]
                    dp2[index] = piles[j]+ dp2[index -1]
                } else {
                    dp1[index] = piles[j]+ dp1[index -1]
                    dp2[index] = piles[i]+ dp2[index -1]
                }

                i++
                j--
                index++
            }
            return dp1[n / 2] > dp2[n / 2]
        }
    }
}