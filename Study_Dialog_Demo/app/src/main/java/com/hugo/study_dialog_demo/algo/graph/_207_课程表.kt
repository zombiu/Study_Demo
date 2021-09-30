package com.hugo.study_dialog_demo.algo.graph

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class _207_课程表 {
    class Solution {
        fun canFinish(numCourses: Int, prerequisites: Array<IntArray>): Boolean {
            var allSet: HashSet<Int> = HashSet()
            // 存储 入度不为0的节点 和 对应的 依赖节点
            var map = HashMap<Int, HashSet<Int>>()
            prerequisites.forEach {
                var key = it[0]
                var value = it[1]
                if (map.containsKey(key)) {
                    map[key]!!.add(value)
                } else {
                    map[key] = HashSet()
                    map[key]!!.add(value)
                }

                // 存储所有的课程
                allSet.add(key)
                allSet.add(value)
            }

            // 入度为0的节点，也就是不依赖其他节点的节点
            var set: HashSet<Int> = HashSet()
            prerequisites.forEach {
                var v = it[1]
                if (!map.containsKey(v)) {
                    set.add(v)
                }
            }

            // 保存拓扑排序的结果
            var result = mutableListOf<Int>()

            // 用来遍历使用的队列
            var queue = LinkedList<Int>()
            set.forEach {
                queue.offer(it)
            }

            while (!queue.isEmpty()) {
                var poll = queue.poll()
                result.add(poll)


                var iterator = map.iterator()
                while (iterator.hasNext()) {
                    var next = iterator.next()
                    var value = next.value
                    if (value.contains(poll)) {
                        value.remove(poll)
                        if (value.isEmpty()) {
                            iterator.remove()
                            queue.offer(next.key)
                        }
                    }

                }
            }

            if (!map.isEmpty()) {
                result.clear()
                return false
            }

            for (index in 0 until numCourses) {
                if (!allSet.contains(index)) {
                    result.add(0, index)
                }
            }
            return true
        }
    }
}
