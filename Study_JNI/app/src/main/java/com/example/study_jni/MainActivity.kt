package com.example.study_jni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.study_jni.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = stringFromJNI()

        binding.sampleText.setOnClickListener {
            var jniDemo = JNIDemo("碧海", 123L)
            var nativeGetName = nativeGetName(jniDemo)
            binding.sampleText.text = nativeGetName
        }
    }


    /**
     * A native method that is implemented by the 'study_jni' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun nativeGetName(jniDemo: JNIDemo): String

    companion object {
        // Used to load the 'study_jni' library on application startup.
        init {
            System.loadLibrary("study_jni")
        }
    }
}