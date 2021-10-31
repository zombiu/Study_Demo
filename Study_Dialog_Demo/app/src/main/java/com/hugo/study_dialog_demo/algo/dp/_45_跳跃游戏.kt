package com.hugo.study_dialog_demo.algo.dp

/**
 * 45. 跳跃游戏 II
 * https://leetcode-cn.com/problems/jump-game-ii/
 */
class _45_跳跃游戏 {
    // 状态转移方程 定义 dp[i] 表示 i个长度的数组，到达最后一个位置的次数
    class Solution {
        fun jump(nums: IntArray): Int {
            var n = nums.size
            var dp = IntArray(n)
            if (n < 2) {
                return 1
            }
            for (i in 1 until n) {
                // 注意必须双层循环  这里要计算每个每个 i能到达的下一个元素
                for (j in i .. (nums[i-1] + i)) {
                    if (dp[j] == 0 && nums[i - 1] + i - 1 >= j) {
                        dp[j] = dp[i - 1] + 1
                    }
                    // 优化 提前break
                    if (nums[i - 1] + i - 1 < j) {
                        break
                    }
                }
            }

            return dp[n - 1]
        }
    }
}