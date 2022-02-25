package com.hugo.study_dialog_demo.ui.section2

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.hugo.study_dialog_demo.databinding.ActivitySection2Binding
import com.hugo.study_dialog_demo.ui.UIState
import com.hugo.study_dialog_demo.ui.section1.DataServer


class Section2Activity : AppCompatActivity() {
    lateinit var binding: ActivitySection2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySection2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        var sectionData = DataServer.getSectionData()
        var adapter = SectionAdapter()

        val layoutManage = GridLayoutManager(this, 4)
        layoutManage.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                var element = sectionData.get(position)
                if (element.isHeader) {
                    return 4
                } else {
                    return 1
                }
            }
        }
        binding.rvList.layoutManager = layoutManage
        binding.rvList.adapter = adapter
        adapter.setNewData(sectionData)

        adapter.onItemClickListener = object : SectionAdapter.OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                LogUtils.e("-->>", "点击了 $position")
            }

        }

        UIState.success(123)
    }


}