package com.example.admin.boxtest.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.example.admin.boxtest.adapter.provider.FirstProvider
import com.example.admin.boxtest.adapter.provider.NormalProvider
import com.example.admin.boxtest.adapter.provider.SecondProvider
import com.example.admin.boxtest.entity.NormalMultipleEntity

/**
 * item类型
 */
const val FIRST_ITEM = 100
const val SECOND_ITEM = 200
const val NORMAL_ITEM = 300

class TopMultipleItemRvAdapter(data: List<NormalMultipleEntity>) : MultipleItemRvAdapter<NormalMultipleEntity, BaseViewHolder>(data) {
    init {
        finishInitialize()
    }

    override fun registerItemProvider() {
        //注册相关的条目provider
        mProviderDelegate.registerProvider(FirstProvider())
        mProviderDelegate.registerProvider(SecondProvider())
        mProviderDelegate.registerProvider(NormalProvider())
    }

    override fun getViewType(t: NormalMultipleEntity): Int {
        /*if (t.type == NormalMultipleEntity.FIRST_ITEM) {
            return FIRST_ITEM
        } el*/
        return when (t.type) {
            NormalMultipleEntity.FIRST_ITEM -> FIRST_ITEM
            NormalMultipleEntity.SECOND_ITEM -> SECOND_ITEM
            NormalMultipleEntity.NORMAL_ITEM -> NORMAL_ITEM
            else -> 0
        }
    }

}