package com.example.study_customview

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.example.study_customview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        XLog.init(LogLevel.ALL)

        XLog.e("-->>日志初始化")
        /*var objectAnimator = ObjectAnimator.ofInt(binding.cameraRotateView, "flipRotation", 360)
        objectAnimator.setDuration(2000)
        objectAnimator.startDelay = 2000
        objectAnimator.start()*/

        binding.tv1.setOnClickListener {
//            startActivity(Intent(this, CustomViewActivity::class.java))

//            startActivity(Intent(this, CustomScrollViewActivity::class.java))

            startActivity(Intent(this, TestScrollActivity::class.java))
        }

        binding.btnNestedScroll.setOnClickListener {
            startActivity(Intent(this, NestedScrollActivity::class.java))
        }

        binding.btnDrawTable.setOnClickListener {
            startActivity(Intent(this, CircleTableActivity::class.java))
        }
    }
}