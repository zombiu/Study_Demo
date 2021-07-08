package com.hugo.study_dialog_demo.algo.link

/**
 * 面试题 16.25. LRU 缓存
 * https://leetcode-cn.com/problems/lru-cache-lcci
 */
class _面试题_16_25_LRU缓存

// java内置LinkedHashMap就可以实现lrucache功能，重新LinkedHashMap的removeEldestEntry方法，如果需要还可以在构造函数中将访问顺序设置为true
class LRUCache(capacity: Int) {
    var linkedHashMap = object : LinkedHashMap<Int, Int>(capacity,0.75f,true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Int, Int>): Boolean {
            return size > capacity
        }
    }

    fun get(key: Int): Int {
        var i = linkedHashMap[key]
        linkedHashMap
        return i ?: -1
    }

    fun put(key: Int, value: Int) {
        linkedHashMap[key] = value
    }
}