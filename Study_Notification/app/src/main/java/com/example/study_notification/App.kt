package com.example.study_notification

import android.app.Application

class App :Application() {

    override fun onCreate() {
        super.onCreate()
        UIDelegate.getInstance().init(this)
    }
}