package com.example.admin.boxtest

import android.util.Log

class Child : Parent() {

    override fun layout() {
        super.layout()
    }

    override fun onLayout() {
        Log.i("-->>", "child 的onLayout被调用")
    }
}