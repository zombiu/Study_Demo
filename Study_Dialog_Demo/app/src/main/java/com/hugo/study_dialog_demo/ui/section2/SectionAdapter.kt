package com.hugo.study_dialog_demo.ui.section2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.entity.SectionEntity
import com.hugo.study_dialog_demo.databinding.DefSectionHeadBinding
import com.hugo.study_dialog_demo.databinding.ItemSectionContentBinding
import com.hugo.study_dialog_demo.ui.section1.MySection
import java.util.*

class SectionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data = Collections.emptyList<MySection>()
    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == SectionEntity.HEADER_TYPE) {
            var binding = DefSectionHeadBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
            )
            return Header(binding)
        } else {
            var binding = ItemSectionContentBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
            )
            return AppType(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Header) {
            holder.refresh(this, position, data.get(position))
        } else if (holder is AppType) {
            holder.refresh(this, position)
        }
    }

    override fun getItemCount(): Int {
        if (data == null) {
            return 0
        }
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        var section = data.get(position)
        return section.itemType
    }

    fun setNewData(data: List<MySection>) {
        this.data = data
        notifyDataSetChanged()
    }

    class Header(var binding: DefSectionHeadBinding) : RecyclerView.ViewHolder(binding.root) {

        fun refresh(sectionAdapter: SectionAdapter, position: Int, section: MySection) {
            if (sectionAdapter.onItemClickListener != null) {
                itemView.setOnClickListener {
                    sectionAdapter.onItemClickListener?.onClick(it, position)
                }
            }
        }
    }

    class AppType(var binding: ItemSectionContentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun refresh(sectionAdapter: SectionAdapter, position: Int) {
            if (sectionAdapter.onItemClickListener != null) {
                itemView.setOnClickListener {
                    sectionAdapter.onItemClickListener?.onClick(it, position)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(view: View, position: Int)
    }
}