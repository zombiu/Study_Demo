package com.hugo.study_recyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hugo.study_recyclerview.databinding.ActivityMainBinding
import com.hugo.study_recyclerview.search.SearchActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchGoBtn.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        binding.tv1.setOnClickListener {
            startActivity(Intent(this, ViewPage2Activity::class.java))
        }
    }
}