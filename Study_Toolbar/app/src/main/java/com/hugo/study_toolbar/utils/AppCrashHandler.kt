package com.hugo.study_toolbar.utils

import android.content.Context
import android.util.Log

object AppCrashHandler : Thread.UncaughtExceptionHandler {
    private const val TAG = "AppCrashHandler"

    private var context: Context? = null

    fun init(context: Context) {
        this.context = context
        Thread.setDefaultUncaughtExceptionHandler(this)
    }


    override fun uncaughtException(t: Thread, e: Throwable) {
        Log.e(TAG, "thread name ${t.name} throw error ${e.message}")

    }

    /*companion object {

        private const val TAG = "AppCrashHandler"

        val instance: AppCrashHandler by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AppCrashHandler()
        }
    }*/
}