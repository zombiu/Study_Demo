package com.example.study_mediacodec.video

import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import com.example.study_mediacodec.R
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadPoolExecutor

class AVActivity : AppCompatActivity(), SurfaceHolder.Callback {
    lateinit var threadPoolExecutor: ExecutorService
    lateinit var surfaceHolder: SurfaceHolder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avactivity)

        threadPoolExecutor = Executors.newFixedThreadPool(4)

        var surface_view = findViewById<SurfaceView>(R.id.surface_view)
        surface_view.holder.addCallback(this)
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        this.surfaceHolder = surfaceHolder
        startDecode()
    }

    private fun startDecode() {
        val path = getExternalFilesDir("").toString() + "/yazi.mp4"
        val videoDecoder = VideoDecoder()
        videoDecoder.init(surfaceHolder.getSurface(), path)
        threadPoolExecutor.submit(videoDecoder)

        var audioDecoder = AudioDecoder()
        audioDecoder.init(path)
        threadPoolExecutor.submit(audioDecoder)

    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {}
}