package com.hugo.study_dialog_demo.algo.dp;

class Test {
    class Solution {
        public int maxSubArray(int[] nums) {
            int max = nums[0];
            for (int begin = 0; begin < nums.length; begin++) {
                for (int end = begin; end < nums.length; end++) {
                    int result = 0;
                    for (int i = begin; i <= end; i++) {
                        result += nums[i];
                    }
                    if (result > max) {
                        max = result;
                    }
                }
            }
            return max;
        }
    }

    class Solution2 {
        public int maxSubArray(int[] nums) {
            int max = nums[0];
            for (int begin = 0; begin < nums.length; begin++) {
                int result = 0;
                for (int end = begin; end < nums.length; end++) {
                    result += nums[end];
                    if (result > max) {
                        max = result;
                    }
                }
            }
            return max;
        }
    }

    class Solution3 {
        public int maxSubArray(int[] nums) {
            int max = nums[0];
            for (int begin = 0; begin < nums.length; begin++) {
                int result = 0;
                for (int end = begin; end < nums.length; end++) {
                    result += nums[end];
                    if (result > max) {
                        max = result;
                    }
                }
            }
            return max;
        }
    }
}
