package com.hugo.study_dialog_demo.algo.dp

import android.util.Log
import java.util.*

/**
 * 5. 最长回文子串
 * https://leetcode-cn.com/problems/longest-palindromic-substring/
 */
class _5_最长回文子串 {
    class Solution {

        // 过不了 "bananas"这个用例
        // 过不了 "bacabab"
        // 过不了 "ababababababa"
        fun longestPalindrome(s: String): String {
            var m = s.length
            if (m == 1) {
                return s
            }
            var originlChars = s.toCharArray()
            /*var dp = Array<String>(m + 1) {
                ""
            }*/
            var dp = IntArray(m)
            var maxLength = 1
            var tailIndex = 0
            // 4种情况
            for (i in 1 until m) {
                var preIndex = dp[i - 1]
                if (preIndex - 1 >= 0) {
                    if (originlChars[i] == originlChars[preIndex - 1]) {
                        dp[i] = preIndex - 1
                    } else {
                        dp[i] = i
                        if (i - 2 >= 0 && originlChars[i] == originlChars[i - 2]) {
                            dp[i] = i - 2
                        }
                        // "cccccc" 用来过这种连续的回文字符
                        for (k in i - 1 downTo dp[i - 1]) {
                            if (originlChars[k] == originlChars[i]) {
                                dp[i] = Math.min(dp[i],k)
                            } else {
                                break
                            }
                        }
                    }
                } else {
                    dp[i] = i
                    if (i - 2 >= 0 && originlChars[i] == originlChars[i - 2]) {
                        dp[i] = i - 2
                    }
                    // "cccccc" 用来过这种连续的回文字符
                    for (k in i - 1 downTo dp[i - 1]) {
                        if (originlChars[k] == originlChars[i]) {
                            dp[i] = Math.min(dp[i],k)
                        } else {
                            break
                        }
                    }
                }
                if (i - dp[i] + 1 > maxLength) {
                    maxLength = Math.max(maxLength, i - dp[i] + 1)
                    tailIndex = i
                }
            }
            var i = dp[tailIndex]
            var substring = s.substring(i, maxLength + i)
            return substring
        }
    }

    /*class Solution {

        // 过不了 "bananas"这个用例
        // 过不了 "bacabab"
        fun longestPalindrome(s: String): String {
            var m = s.length
            if (m == 1) {
                return s
            }
            var originlChars = s.toCharArray()
            var dp = IntArray(m) {
                Int.MIN_VALUE
            }
            dp[0] = 0
            var maxLength = 1
            var tailIndex = 0
            for (i in 1 until m) {
                var preIndex = dp[i - 1]
                dp[i] = i
                if (preIndex - 1 >= 0) {
                    if (originlChars[i] == originlChars[preIndex - 1]) {
                        dp[i] = preIndex - 1
                    } else {
                        // "cccccc" 用来过这种连续的回文字符
                        for (k in i - 1 downTo dp[i - 1]) {
                            if (originlChars[k] == originlChars[i]) {
                                dp[i] = Math.min(dp[i],k)
                            } else {
                                break
                            }
                        }
                    }
                } else {
                    // "cccccc" 用来过这种连续的回文字符
                    for (k in i - 1 downTo dp[i - 1]) {
                        if (originlChars[k] == originlChars[i]) {
                            dp[i] = Math.min(dp[i],k)
                        } else {
                            break
                        }
                    }
                }
                if (i - dp[i] + 1 > maxLength) {
                    maxLength = Math.max(maxLength, i - dp[i] + 1)
                    tailIndex = i
                }
            }
            var i = dp[tailIndex]
            var substring = s.substring(i, maxLength + i)
            return substring
        }
    }*/

    /*class Solution {
        // 求回文子串时，可以反转一下字符串，然后再求最长公共子串
        // 这个用例 过不了 "aacabdkacaa"
        fun longestPalindrome(s: String): String {
            var m = s.length
            if (m == 1) {
                return s
            }
            var originalChars = s.toCharArray()
            var toList = originalChars.toList()
            Collections.reverse(toList)
            var reversalChars = toList.toCharArray()
            var dp = Array(m + 1) {
                IntArray(m + 1)
            }
            var index = 0
            var max = 0
            for (i in 1..m) {
                for (j in 1..m) {
                    if (originalChars[i - 1] == reversalChars[j - 1]) {
                        dp[i][j] = dp[i - 1][j - 1] + 1
                    }
                    if (dp[i][j] > max) {
                        max = dp[i][j]
                        index = j
                    }
                }
            }
            var result = String(reversalChars).substring(index - max, index)
            return result
        }
    }*/

    /*class Solution {

        // 过不了 "aacabdkacaa"这个用例
        fun longestPalindrome(s: String): String {
            var m = s.length
            if (m == 1) {
                return s
            }
            var originlChars = s.toCharArray()
            var dp = IntArray(m) {
                Int.MIN_VALUE
            }
            dp[0] = 0
            var maxLength = 1
            var tailIndex = 0
            for (i in 1 until m) {
                var preIndex = dp[i - 1]
                if (originlChars[i] == originlChars[i - 1]) {
                    dp[i] = i - 1
                } else {
                    dp[i] = i
                }
                if (preIndex - 1 >= 0) {
                    if (originlChars[i] == originlChars[preIndex - 1]) {
                        dp[i] = preIndex - 1
                    }
                }
                if (i - dp[i] + 1 > maxLength) {
                    maxLength = Math.max(maxLength, i - dp[i] + 1)
                    tailIndex = i
                }
            }
            var i = dp[tailIndex]
            var substring = s.substring(i, maxLength + i)
            return substring
        }
    }*/
}