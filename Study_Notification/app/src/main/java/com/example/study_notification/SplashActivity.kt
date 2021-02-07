package com.example.study_notification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.shoucai.reslib.utils.UIKit

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        UIKit.getUIHandler().postDelayed({
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            onBackPressed()
        }, 2000)
        Log.e("-->>","SplashActivity.onCreate")
    }
}