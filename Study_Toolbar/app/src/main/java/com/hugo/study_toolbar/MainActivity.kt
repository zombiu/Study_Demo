package com.hugo.study_toolbar

import android.Manifest
import android.animation.LayoutTransition
import android.app.StatusBarManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.hugo.study_toolbar.databinding.ActivityMainBinding
import com.hugo.study_toolbar.ui.DiffUtilActivity
import com.hugo.study_toolbar.ui.SelecteActivity
import com.hugo.study_toolbar.utils.MemoryUtils
import com.hugo.study_toolbar.widget.PermissionDialog
import im.yixin.b.qiye.common.util.PermissionKit

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var centeredTitleTextView: TextView
    var showBackIcon = true
    var testList = ArrayList<ByteArray>()


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

        var cardView: MaterialCardView
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

//        binding.tv1.background = Theme.getRoundRectSelectorDrawable(R.color.design_default_color_error)
        binding.tv1.background = Theme.getRoundRectSelectorDrawable(
            10, ContextCompat.getColor(this, android.R.color.darker_gray), ContextCompat.getColor(this, android.R.color.holo_red_light), false
        )
        binding.tv1.setOnClickListener {
            LogUtils.e("-->>点击了tv1")
            // 很奇怪 跳转页面时崩溃 有的手机 全局异常处理捕获不到 比如 OPPO Reno4 5g
            ClipDrawableActivity.go(this)
            throw IllegalThreadStateException("异常测试")
        }

//        var img = "https://t7.baidu.com/it/u=2621658848,3952322712&fm=193&f=GIF"
//        Glide.with(this).load(img).into(binding.iv1)


        binding.tvDialogConfirm.setOnClickListener {
            var build = AlertDialog.Builder(this)
            var dialogView = View.inflate(this, R.layout.dialog_confirm_layout, null)
            build.setView(dialogView)
            var dialog = build.create()
            dialog.show()
            var window = dialog.window
            window!!.setBackgroundDrawableResource(android.R.color.transparent)
            var params = window.attributes
            params.width = ScreenUtils.getScreenWidth() - ConvertUtils.dp2px(40f)
            window.attributes = params

            var tv_cancel: View = dialogView.findViewById(R.id.tv_cancel)
            tv_cancel.setOnClickListener {

            }

            var tv_confirm: View = dialogView.findViewById(R.id.tv_confirm)
            tv_confirm.setOnClickListener {

            }
        }

        binding.tvBottomDialog.setOnClickListener {
            /*val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(R.layout.dialog_choose_call)
            bottomSheetDialog.show()
            var bottom: View? = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
            if (bottom != null) {
                bottom.setBackgroundResource(android.R.color.transparent)
            }*/
            var commonSheetDialog = CommonSheetDialog(this)
            commonSheetDialog.setTitle("set标题")
            commonSheetDialog.addItem("第一项") {
                LogUtils.e("-->>第一项")
                commonSheetDialog.dismiss()
            }

            commonSheetDialog.addItem("第二项", object : OnClickListener {
                override fun onClick(v: View?) {
                    LogUtils.e("-->>第二项")
                    commonSheetDialog.dismiss()
                }

            })
            commonSheetDialog.addItem("第三项", object : OnClickListener {
                override fun onClick(v: View?) {
                    LogUtils.e("-->>第三项")
                    commonSheetDialog.dismiss()
                }

            })
            commonSheetDialog.show()
        }

        binding.btnSelect.setOnClickListener {
            SelecteActivity.go(this)
        }

        binding.btnClipDrawable.setOnClickListener {
            ClipDrawableActivity.go(this)
        }

        binding.btnTab.setOnClickListener {
            SimpleTabActivity.go(this)
        }

        binding.btnListview.setOnClickListener {
            ListViewAnimationActivity.go(this)
        }

        binding.btnDiffUtil.setOnClickListener {
            DiffUtilActivity.go(this)

            MemoryUtils.getProcessRealMemory()

            MemoryUtils.getAppPss()
        }

        binding.btnPermission.setOnClickListener {
            DialogActivity.go(this)
        }

        binding.btnAnimateLayoutChanges.setOnClickListener {
            binding.llAnimateLayoutChanges.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            binding.btnAnimateLayoutChanges.text = "binding.btnAnimateLayoutChanges"
        }

        binding.btnMemoryGrow.setOnClickListener {
//            我说怎么之前 点击了 内存显示没有增加 原来是需要对 数组初始化
            var byteArr = ByteArray(1024 * 1024 * 1) {
                0
            }
            testList.add(byteArr)
            LogUtils.e("-->>${byteArr.size}")

           MemoryUtils.getDebugMemState()

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu)
        var searchItem = menu!!.findItem(R.id.action_search)
        Log.e("-->>", "onCreateOptionsMenu " + searchItem.itemId)

        searchItem.setOnActionExpandListener(expandListener)
        var search_rl: View = searchItem.actionView.findViewById(R.id.search_rl)

        var searchView: SearchView = searchItem.actionView.findViewById(R.id.search_view)

        var searchIcon: ImageView = searchItem.actionView.findViewById(R.id.search_icon)

        var cancel: TextView = searchItem.actionView.findViewById(R.id.cancel)
        cancel.setOnClickListener {
            searchView.setQuery("", false)
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
            this, R.style.TextAppearance_AppCompat_Widget_ActionBar_Title
        )

        var lp = Toolbar.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
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