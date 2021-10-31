package com.hugo.study_dialog_demo.algo.dp

/**
 * 55. 跳跃游戏
 * https://leetcode-cn.com/problems/jump-game/
 */
class _55_跳跃游戏 {

    class Solution {
        fun canJump(nums: IntArray): Boolean {
            var n = nums.size
            var dp = BooleanArray(n)
            dp[0] = true
            // 注意这里需要一个双层循环，每次都要查看从 i-1 到 j所有可能到达的路径
            for (i in 1 until n) {
                for (j in i until n) {
                    if (dp[i - 1] && nums[i - 1] + i - 1 >= j) {
                        dp[j] = true
                    }
                }
            }
            return dp[n - 1]
        }
    }

}