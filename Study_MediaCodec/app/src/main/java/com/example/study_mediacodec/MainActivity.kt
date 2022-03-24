package com.example.study_mediacodec

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.study_mediacodec.databinding.ActivityMainBinding
import com.example.study_mediacodec.video.AVActivity
import com.example.study_mediacodec.video.AudioActivity
import com.example.study_mediacodec.video.VideoActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tv1.setOnClickListener {
            startActivity(Intent(this, VideoActivity::class.java))
        }

        binding.tv2.setOnClickListener {
            startActivity(Intent(this, AudioActivity::class.java))
        }

        binding.tv3.setOnClickListener {
            startActivity(Intent(this, AVActivity::class.java))
        }
    }
}