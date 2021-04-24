package com.hugo.study_dialog_demo

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.blankj.utilcode.util.LogUtils

object UIKit {
    private val gUiHandler = Handler(Looper.getMainLooper())

    fun runUI(runnable: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
        }else {
            gUiHandler.post(runnable)
        }
    }

    fun post(runnable: Runnable) {
            gUiHandler.post(runnable)
    }

    fun getUIHandler() = gUiHandler
}
//展示软键盘
fun showKeyboard(view: View): Boolean {
    if (view == null) {
        return false
    }
    try {
        val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    } catch (e: Exception) {
        LogUtils.e(e)
    }

    return false
}
//隐藏软件盘
fun hideKeyboard(view: View) {
    if (view == null) {
        return
    }
    try {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!imm.isActive) {
            return
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    } catch (e: Exception) {
        LogUtils.e(e)
    }

}

//软键盘是否弹出
fun isKeyboardShowed(view: View): Boolean {
    if (view == null) {
        return false
    }
    try {
        val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputManager.isActive(view)
    } catch (e: Exception) {
        LogUtils.e(e)
    }
    return false
}