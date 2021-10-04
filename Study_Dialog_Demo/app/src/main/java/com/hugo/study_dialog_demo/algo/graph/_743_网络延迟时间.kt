package com.hugo.study_dialog_demo.algo.graph

import android.os.Build
import androidx.annotation.RequiresApi

/**
 * 743. 网络延迟时间
 * https://leetcode-cn.com/problems/network-delay-time/
 *
 * 计算最短路径，取最长的一个权值返回。
 */
@RequiresApi(Build.VERSION_CODES.N)
class _743_网络延迟时间 {

    class Solution {

        fun networkDelayTime(times: Array<IntArray>, n: Int, k: Int): Int {
            // k是源点 n是节点数量 times是边 起点-终点-权重

            // 1.从源点开始，对所有的边进行松弛操作
            // 组装所有 顶点是key  连接的有向边集合是value
            var edgesMap: HashMap<Int, HashSet<IntArray>> = HashMap()
            times.forEach {
                var startVertex = it[0]
                var startEdges = edgesMap[startVertex]
                if (startEdges == null) {
                    startEdges = HashSet()
                }
                startEdges.add(it)
                edgesMap[startVertex] = startEdges
            }

            if (!edgesMap.containsKey(k)) {
                return -1
            }

            // key是路径终点 value 是权值
            var selectedPaths: HashMap<Int, Int> = HashMap()
            var paths: HashMap<Int, Int> = HashMap()
            // 初始化
            var startPaths = edgesMap[k]
            startPaths!!.forEach {
                var end = it[1]
                var weight = it[2]
                paths[end] = weight
            }
            // 每次从paths中获取的权值最小的路径 必定是最短路径
            while (paths.isNotEmpty()) {
                var minPath = getMinPath(paths)
                var key = minPath.key
                var value = minPath.value

                selectedPaths[key] = value

                var hashSet = edgesMap[key]
                if (hashSet == null) {
                    continue
                }
                hashSet!!.forEach {
                    // 这是松弛边的终点
                    var i = it[1]
                    // 这是松弛边的权值
                    var newWeight = it[2]
                    var oldWeight = paths[i]
                    if (oldWeight == null || newWeight < oldWeight) {
                        paths[i] = newWeight
                    }
                }
            }

            if (selectedPaths.isEmpty()) {
                return -1
            }

            var iterator = selectedPaths.iterator()
            var maxPath = iterator.next()
            while (iterator.hasNext()) {
                var next = iterator.next()
                if (maxPath.value < next.value) {
                    maxPath = next
                }
            }
            return maxPath.value
        }

        fun getMinPath(paths: HashMap<Int, Int>): MutableMap.MutableEntry<Int, Int> {
            var iterator = paths.iterator()
            var min = iterator.next()
            while (iterator.hasNext()) {
                var next = iterator.next()
                if (min.value > next.value) {
                    min = next
                }
            }
            return min
        }
    }

}