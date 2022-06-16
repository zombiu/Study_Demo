package com.example.study_customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.OverScroller
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.study_customview.adapter.SampleAdapter
import com.example.study_customview.databinding.ActivityTestScrollBinding

class TestScrollActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestScrollBinding
    lateinit var scroller: OverScroller
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScrollBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()
    }

    fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        var adapter = SampleAdapter(this)
        binding.recyclerView.adapter = adapter


        binding.btnSpringBack.setOnClickListener {
            springBack()
        }

        scroller = OverScroller(this)
    }

    private fun springBack() {
        binding.recyclerView.smoothScrollBy(0, -500)
        /*if (scroller.springBack(binding.recyclerView.getScrollX(), binding.recyclerView.getScrollY(), 0, 0, 0, 100)) {
            binding.recyclerView.invalidate();
        }*/
    }


}