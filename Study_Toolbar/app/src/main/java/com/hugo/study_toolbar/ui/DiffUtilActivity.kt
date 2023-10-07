package com.hugo.study_toolbar.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.hugo.study_toolbar.databinding.ActivityDiffUtilBinding
import com.hugo.study_toolbar.entity.User
import com.hugo.study_toolbar.ui.adapter.DiffListAdapter
import com.hugo.study_toolbar.ui.adapter.UserListAdapter
import java.util.*
import kotlin.collections.ArrayList

class DiffUtilActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiffUtilBinding
    lateinit var diffListAdapter: DiffListAdapter
    lateinit var userListAdapter: UserListAdapter
    var random: Random = Random()


    companion object {
        fun go(context: Context) {
            context.startActivity(Intent(context, DiffUtilActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiffUtilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLogic()

//        initRecyclerView()
    }

    private fun initRecyclerView() {
        userListAdapter = UserListAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = userListAdapter

        userListAdapter.setNewData(getData())
        binding.tvRemove.setOnClickListener {
            var nextInt = random.nextInt(6)
            LogUtils.e("-->>random =$nextInt")
            userListAdapter.data.removeAt(nextInt)
            userListAdapter.notifyItemRemoved(nextInt)
        }

//        binding.recyclerView.itemAnimator = null
    }

    private fun initLogic() {
        diffListAdapter = DiffListAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = diffListAdapter

        diffListAdapter.submitList(getData())

        binding.tvSubmit.setOnClickListener {
            diffListAdapter.submitList(getData())
        }
        binding.tvRemove.setOnClickListener {
            var nextInt = random.nextInt(diffListAdapter.itemCount)
            LogUtils.e("-->>random =$nextInt")
//            var data = getData()
//            data.removeAt(nextInt)
//            diffListAdapter.submitList(data)

            var currentList = diffListAdapter.currentList
            var newData = ArrayList<User>()
            newData.addAll(currentList)
            newData.removeAt(nextInt)
            diffListAdapter.submitList(newData)
        }
    }

    fun getData(): java.util.ArrayList<User> {
        var result = ArrayList<User>()
        for (i in 0..10) {
            var user = User()
            user.id = i.toString()
            user.name = "张三"
            user.age = i
            user.type = if (i % 3 == 0) {
                0
            } else {
                1
            }

            result.add(user)
        }
        return result
    }
}