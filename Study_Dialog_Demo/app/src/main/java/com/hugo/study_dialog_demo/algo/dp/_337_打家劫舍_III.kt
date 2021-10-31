package com.hugo.study_dialog_demo.algo.dp

import com.hugo.study_dialog_demo.algo.tree.TreeNode
import java.util.*
import kotlin.collections.ArrayList

/**
 * 337. 打家劫舍 III
 * https://leetcode-cn.com/problems/house-robber-iii/
 */
class _337_打家劫舍_III {

    class Solution {
        fun rob(root: TreeNode?): Int {
            if (root == null) {
                return 0
            }
            var dp = ArrayList<Int>()
            var queue: Queue<TreeNode> = LinkedList()
            queue.offer(root)
            var levelCount = 1
            var curCount = 0
            var index = 0
            while (queue.isNotEmpty()) {
                var poll = queue.poll()
                curCount += poll.`val`
                poll.left?.let {
                    queue.offer(it)
                }
                poll.right?.let {
                    queue.offer(it)
                }
                levelCount--
                if (levelCount == 0) {
                    levelCount = queue.size
                    if (index == 0) {
                        dp.add(curCount)
                    } else if (index == 1) {
                        dp.add(Math.max(dp[index - 1], curCount))
                    } else {
                        dp.add(Math.max(dp[index - 2] + curCount, dp[index - 1]))
                    }
                    index++
                    curCount = 0
                }
            }
            return dp[index -1]
        }
    }

}