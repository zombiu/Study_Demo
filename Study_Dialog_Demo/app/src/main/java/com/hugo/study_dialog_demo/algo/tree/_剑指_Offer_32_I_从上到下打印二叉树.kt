package com.hugo.study_dialog_demo.algo.tree

import java.util.*

/**
 * 剑指 Offer 32 - I. 从上到下打印二叉树
 * https://leetcode-cn.com/problems/cong-shang-dao-xia-da-yin-er-cha-shu-lcof/
 * 解题思路：1.将根节点入队。
 * 2.当队列不为空时，循环执行以下操作：
 * 从队列中取出元素并执行。
 * 将该元素的左右子节点入队。
 */
class _剑指_Offer_32_I_从上到下打印二叉树 {

    class Solution {
        fun levelOrder(root: TreeNode?): IntArray {
            if (root == null) {
                return IntArray(0)
            }
            var node: TreeNode = root
            var queue: Queue<TreeNode> = LinkedList<TreeNode>()
            queue.offer(node)

            var arrayList = arrayListOf<Int>()
            while (!queue.isEmpty()) {
                var poll = queue.poll()
                arrayList.add(poll.`val`)

                poll.left?.let {
                    queue.offer(it)
                }
                poll.right?.let {
                    queue.offer(it)
                }
            }
            return arrayList.toIntArray()
        }
    }
}