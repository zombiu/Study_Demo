package com.example.study_flow.data

import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

object LocalDataSource {

    fun getFlow(): Flow<Int> {
        return listOf(1, 2, 3, 4, 5).asFlow()
            .onEach {
                delay(100)
            }
    }

    fun getFlow2() = callbackFlow<Int> {
        for (element in listOf(1, 2, 3, 4, 5)) {
            delay(2000)
            offer(element)
        }
        awaitClose {
            Log.e("-->>", "关闭flow2")
        }
    }

}