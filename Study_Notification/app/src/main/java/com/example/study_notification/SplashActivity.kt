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
        Log.e("-->>","SplashActivity.onCreate")
        if (!this.isTaskRoot()) {//解决按home，再次点击图标重新启动的问题
            finish();
            return;
        }

        UIKit.getUIHandler().postDelayed({
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            onBackPressed()
        }, 2000)
    }
}