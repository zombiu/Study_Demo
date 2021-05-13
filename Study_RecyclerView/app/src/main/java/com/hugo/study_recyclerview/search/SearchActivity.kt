package com.hugo.study_recyclerview.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.hugo.study_recyclerview.R
import com.hugo.study_recyclerview.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    var lastIndex = 1
    var pageNumber = 15
    lateinit var searchView: SearchView
    lateinit var feedAdapter: FeedAdapter

     val searchViewModel by viewModels<SearchViewModel>()

    lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTitle()
        initFeed()
        setupSmartRefreshLayout()

        searchViewModel.keywordLiveData.observe(this, Observer {
//            LogUtils.e("-->>最终结果$it<<  ${it == null}")
        })
    }

    private fun initTitle() {
        searchView = findViewById(R.id.search_view)

        // 处于展开状态
        searchView.setIconified(false)
//        searchView.onActionViewExpanded()
        SearchViewUtils.setSearchViewStyle(searchView)
        SearchViewUtils.setSearchHintNoIcon(searchView, "输入点什么")


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                LogUtils.e("-->>$newText<<")
                searchViewModel.keywordLiveData.postValue(newText)
                return true
            }

        })
    }

    private fun initFeed() {
        feedAdapter = FeedAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = feedAdapter

        feedAdapter.submitList(getPageList())
    }

    private fun setupSmartRefreshLayout() {
        binding.refreshLayout.setEnableRefresh(false)
        binding.refreshLayout.setOnLoadMoreListener {
            lastIndex = lastIndex + pageNumber
            if (lastIndex >= 50) {
                binding.refreshLayout.finishRefreshWithNoMoreData()
                return@setOnLoadMoreListener
            }
            val pageList = getPageList()
            // 注意 更新需要一个新的list,否则无效
            val newList = mutableListOf<FeedItem>()
            newList.addAll(feedAdapter.currentList)
            newList.addAll(pageList)
            feedAdapter.submitList(newList)
            binding.refreshLayout.finishLoadMore(300)
        }
    }

    private fun getPageList(): List<FeedItem> {
//        val colorList = getColorList()
        var result = mutableListOf<FeedItem>()
        for (i in lastIndex until lastIndex + pageNumber) {
            var feedItem = FeedItem()
            feedItem.id = i.toLong()
            feedItem.title = "标题 ${i}"
            result.add(feedItem)
        }
        return result
    }

}