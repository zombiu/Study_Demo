package com.hugo.study_dialog_demo.algo.dp

import android.util.Log
import java.util.*

/**
 * 213. 打家劫舍 II
 * https://leetcode-cn.com/problems/house-robber-ii/
 *
 *
 */
class _213_打家劫舍_II {
    class Solution {
        fun rob(nums: IntArray): Int {
            var n = nums.size
            if (n == 1) {
                return nums[0]
            } else if (n == 2) {
                return Math.max(nums[0], nums[1])
            }
            var range1 = Arrays.copyOfRange(nums, 0, n - 1)
            var range2 = Arrays.copyOfRange(nums, 1, n )
            // dp[i] 表示前i个房间，所有偷窃的最大金额
            var dp1 = IntArray(n)
            var dp2 = IntArray(n)
            dp1[1] = nums[0]
            dp2[1] = nums[1]
            // 如果数组大于3个元素，将数组从逻辑上划分为两个数组 例如 [1,2,3,1] 划分为 [1,2,3] 和 [2,3,1] 然后计算max
            // [4,1,2,7,5,3,1]

            // [4,1,2,7,5,3]
            for (i in 2 until dp1.size) {
                dp1[i] = Math.max(dp1[i - 1], dp1[i - 2] + range1[i - 1])
            }
            // [1,2,7,5,3,1]
            for (i in 2 until dp1.size) {
                dp2[i] = Math.max(dp2[i - 1], dp2[i - 2] + range2[i - 1])
            }

            return Math.max(dp1[n - 1], dp2[n - 1])
        }
    }
}