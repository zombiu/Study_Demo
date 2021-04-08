package com.hugo.study_toolbar

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.Menu
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.hugo.study_toolbar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
     lateinit var centeredTitleTextView: TextView
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

        centeredTitleTextView.setText("我是标题我是标题我是标题我是标题我是标题我是标题我是标题")

        // app:contentInsetStartWithNavigation="0dp"
        // 解决标题和返回icon之间padding的问题
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu)
    }

    // 代码对标题进行居中
     fun getCenteredTitleTextView3(): TextView {
            centeredTitleTextView =  TextView (this)
//            centeredTitleTextView.setTypeface(...);
            centeredTitleTextView!!.setSingleLine();
            centeredTitleTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            centeredTitleTextView.setGravity(Gravity.CENTER);
//            centeredTitleTextView.setTextAppearance(this, R.style.TextAppearance_AppCompat_Widget_ActionBar_Title)

            var lp =  Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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