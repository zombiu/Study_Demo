package com.hugo.study_toolbar

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hugo.study_toolbar.databinding.ActivityTabBinding

class SimpleTabActivity : AppCompatActivity() {
    lateinit var binding: ActivityTabBinding
    companion object {
        fun go(context: Context) {
            context.startActivity(Intent(context, SimpleTabActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 在xml中设置tabItem宽度无效
        // 文字高20 上下padding都是10 下划线高度为3 文字距离下划线为7
        // 路走偏了  不要把50当成整个tab的高度，其实只要把tab设置为40的高度就能实现 根本不需要设置其它设置就能实现 文字和下划线的距离为7
    }
}