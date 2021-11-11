package com.hugo.study_dialog_demo.algo.dp

/**
 * 718. 最长重复子数组
 * https://leetcode-cn.com/problems/maximum-length-of-repeated-subarray/
 */
class _718_最长重复子数组 {

    class Solution {
        fun findLength(nums1: IntArray, nums2: IntArray): Int {
            var m = nums1.size
            var n = nums2.size
            var dp = Array(m + 1) {
                IntArray(n + 1)
            }
            var max = 0
            for (i in 1..m) {
                for (j in 1..n) {
                    if (nums1[i - 1] == nums2[j - 1]) {
                        dp[i][j] = dp[i - 1][j - 1] + 1
                        max = Math.max(max, dp[i][j])
                    }
                }
            }
            return max
        }
    }

}