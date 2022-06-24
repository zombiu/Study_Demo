package com.example.study_customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.study_customview.adapter.SampleAdapter
import com.example.study_customview.databinding.ActivityNestedScrollBinding

class NestedScrollActivity : AppCompatActivity() {
    lateinit var binding: ActivityNestedScrollBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNestedScrollBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.scrollView.isNestedScrollingEnabled = true
//        binding.scrollViewInner.isNestedScrollingEnabled = true

        initView()
    }

    private fun initView() {
        var sampleAdapter = SampleAdapter(this)
        binding.recyclerView.adapter = sampleAdapter

        /*binding.recyclerView.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_UP -> {

                }
                else -> {}
            }
            return@setOnTouchListener false
        }*/

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // 当触发up事件 并且没有fling时，会触发此状态
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 这里 对CustomScrollView 进行回弹处理
                    binding.customScrollView.springBackOverScrollOffset(0, 0)
                }
                /*else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    // fling最多超过可滑动距离300之后，进行回弹处理
                    binding.customScrollView.springBackOverScrollOffset(0, 300)
                }*/
            }
        })
    }
}