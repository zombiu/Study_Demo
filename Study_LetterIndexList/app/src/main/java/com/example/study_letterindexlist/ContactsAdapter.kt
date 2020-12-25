package com.example.study_letterindexlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.study_letterindexlist.bean.UIContact


class ContactsAdapter(var context: Context, var datas: List<UIContact>) : BaseAdapter() {
    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return datas.size
    }

    override fun getItem(position: Int): Any {
        return datas.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var uiContact = datas.get(position)
        var itemView: View = inflater.inflate(R.layout.contact_item_contact, null)
        var categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        var nameTextView:TextView = itemView.findViewById(R.id.nameTextView)
        if (uiContact.isShowCategory) {
            categoryTextView.visibility = View.VISIBLE;
        }else{
            categoryTextView.visibility = View.GONE;
        }
        categoryTextView.text = uiContact.category
        nameTextView.text = uiContact.mobileContact.name
        return itemView
    }


}