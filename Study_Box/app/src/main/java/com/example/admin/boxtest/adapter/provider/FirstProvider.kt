package com.example.admin.boxtest.adapter.provider

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.example.admin.boxtest.R
import com.example.admin.boxtest.adapter.FIRST_ITEM
import com.example.admin.boxtest.entity.NormalMultipleEntity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.web_new_item.*

/**
 * 第一名布局
 */
class FirstProvider :BaseItemProvider<NormalMultipleEntity,BaseViewHolder>() {
    var tagList = mutableListOf("无限道具", "无敌", "秒杀")
    init {

    }
    override fun layout(): Int {
        return R.layout.top_item_first
    }

    override fun viewType(): Int {
        return FIRST_ITEM
    }

    override fun convert(helper: BaseViewHolder, data: NormalMultipleEntity, position: Int) {

        var view = helper.getView<TagFlowLayout>(R.id.flowlayout)
         var inflater = LayoutInflater.from(view.context)
        view.adapter = object : TagAdapter<String>(tagList) {
            override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                var view = inflater.inflate(R.layout.item_textview, view, false) as TextView
                view.text = t
//                view.x
                return view
            }

        }
    }

}