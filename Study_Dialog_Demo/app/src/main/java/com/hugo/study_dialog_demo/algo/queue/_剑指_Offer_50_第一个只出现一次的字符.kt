package com.hugo.study_dialog_demo.algo.queue

import java.util.*
import kotlin.collections.HashMap

/**
 * 剑指 Offer 50. 第一个只出现一次的字符
 * https://leetcode-cn.com/problems/di-yi-ge-zhi-chu-xian-yi-ci-de-zi-fu-lcof/
 * 解题思路:对每个字符进行入队操作时，对比是否与队列头部相等，如果是，就不入队，并将hashmap中的计数+1,并将头部出队，遍历完后，队头就是第一个只出现一次的字符串。
 */
class _剑指_Offer_50_第一个只出现一次的字符

class Solution {
    var queue: Queue<Char> = LinkedList<Char>()
    var hashMap = HashMap<Char, Int>()

    fun firstUniqChar(s: String): Char {
        if (s.isNullOrEmpty()) {
            return ' '
        }
        s.forEach {
            if (hashMap.containsKey(it)) {
                hashMap[it] = hashMap[it]!!.plus(1)
                if (it == queue.peek()) {
                    queue.poll()
                }
            } else {
                hashMap[it] = 1
                queue.offer(it)
            }
        }
        if (queue.isEmpty()) {
            return ' '
        }
        while (!queue.isEmpty()) {
            var poll = queue.poll()
            if (hashMap[poll] == 1) {
                return poll
            }
        }
        return ' '
    }
}