package com.hugo.study_toolbar

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.ArrayList

@SuppressLint("MissingInflatedId")
class CommonSheetDialog(context: Context) : BottomSheetDialog(context) {
    private var hashMap = LinkedHashMap<String, OnClickListener>(4)

    init {
        setContentView(R.layout.dialog_choose_call)
        // 用来禁止拖拽  低版本比如1.0 不能访问
//        behavior.isDraggable = false
        var bottom: View? = findViewById(com.google.android.material.R.id.design_bottom_sheet)
        bottom?.setBackgroundResource(android.R.color.transparent)

        var iv_close: ImageView? = findViewById<ImageView>(R.id.iv_close)
        iv_close?.visibility = View.VISIBLE
        iv_close?.setBackgroundResource(R.mipmap.ic_launcher)
        iv_close?.setOnClickListener {
            dismiss()
        }
        //R.id.design_bottom_sheet基本是固定的,不用担心后面API的更改
        var behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottom!!);
        behavior.setHideable(false);//此处设置表示禁止BottomSheetBehavior的执行
    }

    override fun show() {
        initView()
        super.show()
    }

    private fun initView() {
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        var list = ArrayList(hashMap.keys)
        recyclerView!!.adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.dialog_item_call_type, list) {
            override fun convert(holder: BaseViewHolder, item: String) {
                holder.setText(R.id.tv_title, item)
                var listener = hashMap.get(item)
                holder.itemView.setOnClickListener {
                    listener!!.onClick(it)
                }
            }
        }
    }

    fun addItem(title: String, onClickListener: OnClickListener) {
        hashMap.put(title, onClickListener)
    }

    fun setTitle(title: String) {
        var tv_title: TextView? = findViewById(R.id.tv_title)
        tv_title!!.setText(title)
    }
}