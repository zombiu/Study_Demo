package com.example.study_customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.example.study_customview.databinding.ActivityNestedScrollBinding

class NestedScrollActivity : AppCompatActivity() {
    lateinit var binding: ActivityNestedScrollBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNestedScrollBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.scrollView.isNestedScrollingEnabled = true
//        binding.scrollViewInner.isNestedScrollingEnabled = true
    }
}