package com.hugo.study_dialog_demo.algo.dp

import android.util.Log

/**
 * 118. 杨辉三角
 * https://leetcode-cn.com/problems/pascals-triangle/
 */
class _118_杨辉三角 {
    class Solution {
        fun generate(numRows: Int): List<List<Int>> {
            var resultList: ArrayList<ArrayList<Int>> = ArrayList()
            for (i in 0 until numRows) {
                resultList.add(ArrayList<Int>())
                for (j in 0..i) {
                    resultList[i].add(1)
                }
            }
//            Log.e("-->> " , resultList.toString())
            for (i in 2 until numRows) {
                for (j in 1 until resultList[i].size - 1) {
//                    Log.e("-->> " , "i=${i} j=${j} size=${resultList[i].size}" )
                    resultList[i][j] = resultList[i - 1][j - 1] + resultList[i - 1][j]
//                    Log.e("-->> " , resultList.toString())
                }
            }
            return resultList
        }
    }
}