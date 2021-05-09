package com.hugo.study_recyclerview.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hugo.study_recyclerview.R
import com.hugo.study_recyclerview.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    var lastIndex = 1
    var pageNumber = 15
    lateinit var feedAdapter: FeedAdapter

    lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFeed()
        setupSmartRefreshLayout()
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
            // 注意 更新需要一个新的list
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