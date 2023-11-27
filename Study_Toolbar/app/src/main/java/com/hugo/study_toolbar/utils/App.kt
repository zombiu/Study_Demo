package com.hugo.study_toolbar.utils

import android.app.Application

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        AppCrashHandler.init(this)
    }
}