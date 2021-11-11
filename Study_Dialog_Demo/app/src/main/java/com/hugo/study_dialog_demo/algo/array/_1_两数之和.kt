package com.hugo.study_dialog_demo.algo.array

import java.util.*
import kotlin.collections.HashMap

/**
 * 1. 两数之和
 * https://leetcode-cn.com/problems/two-sum/
 */
class _1_两数之和 {
    class Solution {
        fun twoSum(nums: IntArray, target: Int): IntArray {
            var result = IntArray(2)
            for (i in nums.indices) {
                var i1 = target - nums[i]
                for (j in nums.indices) {
                    if (i != j) {
                        if (nums[j] == i1) {
                            result[0] = i
                            result[1] = j
                            return result
                        }
                    }
                }
            }
            return result
        }
    }

    // 通过hashmap进行优化
    class Solution1 {
        fun twoSum(nums: IntArray, target: Int): IntArray {
            var hashMap1 = HashMap<Int, Int>()
            var hashMap2 = HashMap<Int, Int>()
            for (i in nums.indices) {
                var i1 = nums[i]
                // 有重复的值时
                if (hashMap1.containsKey(i1)) {
                    hashMap2.put(i1, i)
                } else {
                    hashMap1.put(i1, i)
                }
            }
            var result = IntArray(2)
            for (i in nums.indices) {
                var i1 = target - nums[i]
                var otherIndex = hashMap1.get(i1)
                otherIndex?.let {
                    if (i != otherIndex) {
                        result[0] = i
                        result[1] = it
                        return result
                    }
                }
                otherIndex = hashMap2.get(i1)
                otherIndex?.let {
                    if (i != otherIndex) {
                        result[0] = i
                        result[1] = it
                        return result
                    }
                }
            }
            return result
        }
    }
}