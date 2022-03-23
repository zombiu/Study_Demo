package com.example.study_mediacodec.video

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.study_mediacodec.databinding.ActivityAudioBinding
import kotlin.concurrent.thread

class AudioActivity : AppCompatActivity() {
    lateinit var binding: ActivityAudioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val path = getExternalFilesDir("").toString() + "/yazi.mp4"
        var audioDecoder = AudioDecoder()
        audioDecoder.init(path)
        thread {
            audioDecoder.run()
        }
    }
}