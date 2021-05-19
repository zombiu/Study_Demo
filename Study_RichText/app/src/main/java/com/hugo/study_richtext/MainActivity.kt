package com.hugo.study_richtext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import com.blankj.utilcode.util.LogUtils
import com.bobomee.android.mentions.edit.util.ClipboardHelper
import com.hugo.study_richtext.databinding.ActivityMainBinding
import com.hugo.study_richtext.test.MainActivity2

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var mClipboardManager: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager


        // 由于每次粘贴需要长按 这里设置长按监听 每次长按时，获取剪切板的内容，然后去掉格式后再添加回剪切板
        binding.inputEt1.setOnLongClickListener {
            var clipboardHelper = ClipboardHelper.getInstance(this)
            var clipText = clipboardHelper.getClipText(this)
            LogUtils.e("-->>$clipText")
            if (!clipText.isNullOrEmpty()) {
                LogUtils.e("-->>${clipText::class.java.canonicalName}")
                val clip: ClipData = ClipData.newPlainText("simple text", clipText)
                mClipboardManager.setPrimaryClip(clip)
            }
            return@setOnLongClickListener false
        }

        binding.topicTv.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }

        binding.inputEt1.filters = arrayOf(TextLengthFilter())
        binding.inputEt4.filters = arrayOf(InputFilter.LengthFilter(10))

        LimitEditUtils.getInstance().registerWatcher(binding.inputEt1)
        LimitEditUtils.getInstance().registerWatcher(binding.inputEt2)
        LimitEditUtils.getInstance().registerWatcher(binding.inputEt3)

        copy()
    }

    fun copy() {

    }
}