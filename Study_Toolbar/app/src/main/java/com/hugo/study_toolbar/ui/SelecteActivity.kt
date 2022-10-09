package com.hugo.study_toolbar.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.hugo.study_toolbar.R
import com.hugo.study_toolbar.ui.adapter.UISelectType

class SelecteActivity : AppCompatActivity() {
    var item_select_type = 0
    var item_normal_type = 1
    var item_divider_type = 2

    companion object {
        fun go(context: Context) {
            context.startActivity(Intent(context, SelecteActivity::class.java))
        }
    }

    private lateinit var selectAdapter: SelectAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecte)


        initView()
    }

    private fun initView() {

        selectAdapter = SelectAdapter()

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = selectAdapter

        var list = getList()
        selectAdapter.setNewData(list)
        selectAdapter.onItemEventListener = object : OnItemEventListener {
            override fun onItemClick(view: View, position: Int) {
                var uiSelectType = list.get(position)
                LogUtils.e("-->> ${GsonUtils.toJson(uiSelectType)}")
                if (uiSelectType.type == item_select_type) {
                    uiSelectType.isSelected = !uiSelectType.isSelected
                    selectAdapter.notifyDataSetChanged()
                }
            }

        }
    }

    private fun getList(): List<UISelectType> {
        var list = ArrayList<UISelectType>()
        var uiSelectType1 = UISelectType()
        uiSelectType1.isSelected = false
        uiSelectType1.type = item_select_type
        uiSelectType1.name = "第一项"
        list.add(uiSelectType1)

        var uiSelectType2 = UISelectType()
        uiSelectType2.isSelected = false
        uiSelectType2.type = item_select_type
        uiSelectType2.name = "第2项"
        list.add(uiSelectType2)

        var uiSelectType3 = UISelectType()
        uiSelectType3.isSelected = true
        uiSelectType3.type = item_select_type
        uiSelectType3.name = "第3项"
        list.add(uiSelectType3)

        var uiSelectType4 = UISelectType()
        uiSelectType4.type = item_divider_type
        list.add(uiSelectType4)

        var uiSelectType5 = UISelectType()
        uiSelectType5.type = item_normal_type
        uiSelectType5.name = "第4项"
        list.add(uiSelectType5)

        return list
    }

    inner class SelectAdapter : RecyclerView.Adapter<ViewHolder>() {
        var list: List<UISelectType> = emptyList<UISelectType>()
        var onItemEventListener: OnItemEventListener? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            if (viewType == item_select_type) {
                var view = layoutInflater.inflate(R.layout.item_select_group_type, parent, false)
                return SelectGroupTypeViewHolder(view)
            } else if (viewType == item_normal_type) {
                var view = layoutInflater.inflate(R.layout.item_select_group_type, parent, false)
                return NoramlTypeViewHolder(view)
            } else {
                var view = layoutInflater.inflate(R.layout.item_divider_type, parent, false)
                return DividerTypeViewHolder(view)
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (holder is SelectGroupTypeViewHolder) {
                holder.refresh(list.get(position), position)
            } else if (holder is DividerTypeViewHolder) {
                holder.refresh(list.get(position), position)
            } else if (holder is DividerTypeViewHolder) {
                holder.refresh(list.get(position), position)
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun getItemViewType(position: Int): Int {
            return list.get(position).type
        }


        fun setNewData(list: List<UISelectType>) {
            this.list = list
            notifyDataSetChanged()
        }

        fun refreshSelected(uiSelectType: UISelectType) {
            // todo 遍历列表 将对应的item选中， 其它item取消选中
            notifyDataSetChanged()
        }

        inner class SelectGroupTypeViewHolder(itemView: View) : ViewHolder(itemView) {
            var tv_title: TextView
            var iv_select: ImageView
            var line: View

            init {
                tv_title = itemView.findViewById(R.id.tv_title)
                iv_select = itemView.findViewById(R.id.iv_select)
                line = itemView.findViewById(R.id.line)
            }

            fun refresh(item: UISelectType, position: Int) {
                tv_title.setText(item.name)
                if (item.isSelected) {
                    iv_select.setBackgroundResource(R.mipmap.ic_launcher)
                } else {
                    iv_select.setBackgroundResource(0)
                }
                itemView.setOnClickListener {
                    LogUtils.e("-->> ${absoluteAdapterPosition} ${layoutPosition}  ${adapterPosition}")
                    onItemEventListener?.onItemClick(itemView, absoluteAdapterPosition)
                }
            }

        }

        inner class DividerTypeViewHolder(var itemView: View) : ViewHolder(itemView) {

            fun refresh(item: UISelectType, position: Int) {

            }

        }

        inner class NoramlTypeViewHolder(var itemView: View) : ViewHolder(itemView) {

            fun refresh(item: UISelectType) {

            }

        }
    }

    interface OnItemEventListener {
        fun onItemClick(view: View, position: Int)
    }
}