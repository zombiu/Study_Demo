package com.example.study_customview.adapter

object DataProvider {

    fun getSampleDatas(count: Int): List<String> {
        var list = ArrayList<String>(count)
        for (i in 0 until count) {
            list.add("标题${i}")
        }
        return list
    }
}