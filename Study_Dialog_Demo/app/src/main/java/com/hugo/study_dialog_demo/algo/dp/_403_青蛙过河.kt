package com.hugo.study_dialog_demo.algo.dp

/**
 * 403. 青蛙过河
 * https://leetcode-cn.com/problems/frog-jump/
 */
class _403_青蛙过河 {
    class Solution {
        fun canCross(stones: IntArray): Boolean {
            var m = stones.size
            var dp = Array(m + 1) {
                IntArray(3)
            }
            for (i in 0..2) {
                dp[1][i] = 1
            }


            var canCrossDp = BooleanArray(m)
            canCrossDp[0] = true

            for (i in 2..m) {
                var stoneCode = stones[i]
                for (j in i + 1..m) {
                    var codeJ = stones[j]
                    var i1 = codeJ - stoneCode
                    for (k in 0..2) {
                        if (i1 == dp[i-1][k]) {

                        }
                    }
                    if (i1 == dp[i - 1][0]) {
                        canCrossDp[i] = true
                    }
                    if (i1 == dp[i - 1][1]) {
                        canCrossDp[i] = true
                    }
                    if (i1 == dp[i - 1][2]) {
                        canCrossDp[i] = true
                    }
                    if (i1 > dp[i - 1][2]) {
                        break
                    }
                }
            }
            return false
        }
    }
}