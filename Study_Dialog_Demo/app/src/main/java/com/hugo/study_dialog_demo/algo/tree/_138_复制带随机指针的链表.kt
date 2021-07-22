package com.hugo.study_dialog_demo.algo.tree

/**
 * 138. 复制带随机指针的链表
 * https://leetcode-cn.com/problems/copy-list-with-random-pointer/
 */
class _138_复制带随机指针的链表 {

    class Node(var `val`: Int) {
        var next: Node? = null
        var random: Node? = null
    }

    // 非常笨的办法
    // 执行用时： 228 ms , 在所有 Kotlin 提交中击败了 44.44% 的用户
    class Solution {
        // 保存对应索引node的 random key节点对应的索引位置 value 节点对应的random结点
        var randomMap: HashMap<Int, Node> = HashMap()
        // 新链表 key 索引位置 value 节点
        var newAllNodeMap: HashMap<Int, Node> = HashMap()
        // 老链表 key 节点 value 索引位置
        var oldAllNodeMap: HashMap<Node, Int> = HashMap()
        fun copyRandomList(node: Node?): Node? {
            if (node == null) {
                return null
            }
            var curNode = node
            // 虚拟头结点
            var newHead = Node(-1)
            var slow: Node = newHead
            var index = 0
            while (curNode != null) {
                var next = Node(curNode.`val`)
                slow.next = next
                slow = next

                newAllNodeMap[index] = next
                if (curNode.random != null) {
                    randomMap[index] = curNode.random!!
                }
                oldAllNodeMap[curNode] = index
                curNode = curNode.next
                index++
            }

            var iterator = randomMap.iterator()
            while (iterator.hasNext()) {
                var next = iterator.next()
                var index = next.key
                var node = next.value
                var newIndex = oldAllNodeMap[node]
                newAllNodeMap[index]!!.random = newAllNodeMap[newIndex]
            }

            return newHead.next
        }
    }
}