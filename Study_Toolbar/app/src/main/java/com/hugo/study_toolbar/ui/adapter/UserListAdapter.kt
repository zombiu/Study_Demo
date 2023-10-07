package com.hugo.study_toolbar.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.hugo.study_toolbar.R
import com.hugo.study_toolbar.entity.User

class UserListAdapter : Adapter<RecyclerView.ViewHolder>() {
    var data: java.util.ArrayList<User> = ArrayList()

    fun setNewData(data: java.util.ArrayList<User>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        if (viewType == 1) {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_layout, parent, false)
            return UserViewHolder(view)
        }
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_layout, parent, false)
        return UserViewHolder2(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserViewHolder2) {
            holder.bind(data.get(position))
        } else if (holder is UserViewHolder) {
            holder.bind(data.get(position))
        }
    }


    /**
     * 单item 动画正常
     * 多item下 动画有问题
     */
    override fun getItemViewType(position: Int): Int {
        if (position % 3 == 0) {
            return 0
        }
        return 1
    }


    open class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        protected var tvName: TextView? = null
        protected var tvAge: TextView? = null

        init {
            tvName = itemView.findViewById(R.id.title)
            tvAge = itemView.findViewById(R.id.desc)
        }

        open fun bind(user: User) {
            tvName?.text = user.name
            tvAge?.text = user.age.toString()
        }
    }

    class UserViewHolder2(itemView: View) : UserViewHolder(itemView) {

        override fun bind(user: User) {
            tvName?.text = "我是" + user.name
            tvAge?.text = user.age.toString()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}


