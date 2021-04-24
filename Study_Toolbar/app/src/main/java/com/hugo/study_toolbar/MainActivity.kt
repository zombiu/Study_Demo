package com.hugo.study_toolbar

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.card.MaterialCardView
import com.hugo.study_toolbar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var centeredTitleTextView: TextView
    var showBackIcon = true
    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setTitle("")
//        supportActionBar!!.setTitle("原始标题原始标题原始标题原始标题")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        getCenteredTitleTextView3()

        binding.toolbar.addView(centeredTitleTextView)

        var cardView:MaterialCardView
        centeredTitleTextView.setText("我是标题我是标题我是标题我是标题我是标题我是标题我是标题")

        // 在activity上设置 android:theme="@style/AppTheme.ToolbarHeight" 控制宽度
        binding.toolbar.setContentInsetStartWithNavigation(0)
        binding.toolbar.setContentInsetsRelative(0, 0);

        // app:contentInsetStartWithNavigation="0dp"
        // 解决标题和返回icon之间padding的问题


        binding.clickTv.setOnClickListener {
            showBackIcon = !showBackIcon
            supportActionBar!!.setDisplayHomeAsUpEnabled(showBackIcon)
        }

        binding.tv1.background = Theme.getRoundRectSelectorDrawable(R.color.design_default_color_error)
        binding.tv1.setOnClickListener {
            LogUtils.e("-->>点击了tv1")
        }
    }

    var expandListener: MenuItem.OnActionExpandListener = object : MenuItem.OnActionExpandListener {
        override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
            Log.e("-->>", "收缩")
            // Do something when action item collapses
            return true // Return true to collapse action view
        }

        override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
            Log.e("-->>", "展开")
            // Do something when expanded
            return true // Return true to expand action view
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        var searchItem = menu!!.findItem(R.id.action_search)
        Log.e("-->>", "onCreateOptionsMenu " + searchItem.itemId)

        searchItem.setOnActionExpandListener(expandListener)
        var search_rl: View = searchItem.actionView.findViewById(R.id.search_rl)

        var searchView: SearchView = searchItem.actionView.findViewById(R.id.search_view)

        var searchIcon: ImageView = searchItem.actionView.findViewById(R.id.search_icon)

        var cancel: TextView = searchItem.actionView.findViewById(R.id.cancel)
        cancel.setOnClickListener {
            searchView.setQuery("",false)
            // 处于收缩状态
            searchView.setIconified(true)
            cancel.visibility = View.GONE
            searchIcon.visibility = View.GONE
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            search_rl.setBackgroundResource(android.R.color.transparent)

            MenuItemCompat.collapseActionView(searchItem)
        }

        // 如何默认打开搜索框
        /*searchView.post {
//            searchLayout.expandActionView()
            //  搜索icon是否显示 设置 setQueryHint 会显示搜索icon
            searchView.setQueryHint("搜索")
            searchView.setIconified(false)//设置searchView处于展开状态
            searchView.onActionViewExpanded();// 当展开无输入内容的时候，没有关闭的图标

            cancel.visibility = View.VISIBLE

            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        }*/

        SearchViewUtils.setSearchViewStyle(searchView)
        SearchViewUtils.setSearchHintNoIcon(searchView, "输入点什么")

        searchView.setOnSearchClickListener {
            Log.e("-->>", "setOnSearchClickListener " + it.toString())
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            searchView.onActionViewExpanded();// 当展开无输入内容的时候，没有关闭的图标
            cancel.visibility = View.VISIBLE
            searchIcon.visibility = View.VISIBLE
            search_rl.setBackgroundResource(R.drawable.bg_search)

            MenuItemCompat.expandActionView(searchItem)
        }

        return true
    }


    // 代码对标题进行居中
    fun getCenteredTitleTextView3(): TextView {
        centeredTitleTextView = TextView(this)
//            centeredTitleTextView.setTypeface(...);
        centeredTitleTextView!!.setSingleLine();
        centeredTitleTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        centeredTitleTextView.setGravity(Gravity.CENTER)
        // 标题字体风格
        centeredTitleTextView.setTextAppearance(
            this,
            R.style.TextAppearance_AppCompat_Widget_ActionBar_Title
        )

        var lp = Toolbar.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.gravity = Gravity.CENTER;
        centeredTitleTextView.setLayoutParams(lp);
        return centeredTitleTextView;
    }

    /*private fun getActionBarTextView(): TextView? {
        var titleTextView: TextView? = null
        try {
            val f: Field = mToolBar.getClass().getDeclaredField("mTitleTextView")
            f.setAccessible(true)
            titleTextView = f.get(mToolBar)
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }
        return titleTextView
    }*/
}