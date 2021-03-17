package com.example.study_letterindexlist

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.LogUtils
import com.example.study_letterindexlist.bean.UIContact
import com.example.study_letterindexlist.databinding.ActivityLetterIndexBinding
import com.example.study_letterindexlist.widget.LetterIndexView


class LetterIndexActivity : AppCompatActivity() {
    lateinit var activityLetterIndexBinding: ActivityLetterIndexBinding
    private lateinit var contactsAdapter: ContactsAdapter
    lateinit var hashMap: LinkedHashMap<String,Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLetterIndexBinding = ActivityLetterIndexBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_letter_index)
        setContentView(activityLetterIndexBinding.root)

        initView();
        initData()
        initListener()

        var newInstance = TipFragment.newInstance()
        newInstance.isCancelable = false
        newInstance.dialog?.setCanceledOnTouchOutside(true)
        newInstance.show(supportFragmentManager,"123")
    }

    private fun initListener() {
        activityLetterIndexBinding.livIndex.setOnTouchingLetterChangedListener(object :
            LetterIndexView.OnTouchingLetterChangedListener {
            override fun onHit(letter: String) {
                activityLetterIndexBinding.indexLetterTextView.visibility = View.VISIBLE
                activityLetterIndexBinding.indexLetterTextView.setText(letter)
                var index = -1
                if ("↑" == letter) {
                    index = 0
                } else if (hashMap.containsKey(letter)) {
                    //这里的 index计算错误 ，所以跳转时 一直失败
//                    index = hashMap.get(letter)!!
                    //计算跳转的index
                    for (uiContact in contactsAdapter.datas) {
                        index++
                        if (letter.equals(uiContact.category,true)) {
                            break
                        }
                    }
                }
                if (index < 0) {
                    return
                }
//                activityLetterIndexBinding.listView.setSelectionFromTop(index,-1)
                activityLetterIndexBinding.listView.setSelection(index)
            }

            override fun onCancel() {
                activityLetterIndexBinding.indexLetterTextView.visibility = View.GONE
            }

        })
        activityLetterIndexBinding.listView.setOnScrollListener(object :
            AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
            }

            override fun onScroll(
                view: AbsListView,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                //判断是否到达底部，用来屏蔽点击索引view跳转底部时不能高亮字母的bug
                if (isReachBottom(
                        activityLetterIndexBinding.listView,
                        firstVisibleItem,
                        visibleItemCount,
                        totalItemCount
                    )
                ) {
                    return
                }
                var item: UIContact = contactsAdapter.getItem(firstVisibleItem) as UIContact
                item?.let {
                    updateLetterSelected(it)
                }
            }

        })
    }

    private fun initView() {
    }

    private fun initData() {
        var contacts = UIContact.getContacts()
        contactsAdapter = ContactsAdapter(this, contacts)

        activityLetterIndexBinding.listView.adapter = contactsAdapter

        var hashSet = LinkedHashSet<String>()
         hashMap = LinkedHashMap<String, Int>()
        var index = 0
        for (uiContact in contacts) {
            if (!hashSet.contains(uiContact.category)) {
                hashMap.put(uiContact.category,index)
                hashSet.add(uiContact.category)
                index++
            }
        }
        var arr = arrayOfNulls<String>(hashSet.size)
        var groups = hashSet.toList()
        activityLetterIndexBinding.livIndex.setLetters(groups.toTypedArray())


    }

    /**
     * 判断是否到达listview底部
     *
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     * @return
     */
    fun isReachBottom(
        mListView: ListView,
        firstVisibleItem: Int,
        visibleItemCount: Int,
        totalItemCount: Int
    ): Boolean {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            val lastVisibleItemView = mListView.getChildAt(mListView.childCount - 1)
            if (lastVisibleItemView != null && lastVisibleItemView.bottom == mListView.height) {
                Log.d("-->>ListView", "##### 滚动到底部 ######")
                return true
            } else if (visibleItemCount == totalItemCount) {
                //修复数据不满一屏时，无法触发索引高亮的bug
                return true
            }
        }
        return false
    }

    /**
     * 更新选中的字母
     *
     * @param item
     */
    private fun updateLetterSelected(item: UIContact) {
        val group: String = item.category
        if (!TextUtils.isEmpty(group)) {
            activityLetterIndexBinding.livIndex.updateLetterIndex(group)
        }
    }
}