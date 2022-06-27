package com.example.admin.boxtest

import android.util.Log

open class Parent  {
    open fun layout(){
        onLayout()
    }

    open fun onLayout() {
        Log.i("-->>","parent 的onLayout被调用")
    }

}