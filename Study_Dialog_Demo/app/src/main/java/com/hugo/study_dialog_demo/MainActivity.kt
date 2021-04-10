package com.hugo.study_dialog_demo

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.jaeger.library.StatusBarUtil
import im.yixin.ui.dialog.CommonDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ImmersionBar.with(this)
            .transparentStatusBar()
            .init()

        StatusBarUtil.setTranslucent(this)

        findViewById<View>(R.id.tv1).setOnClickListener {
            ToastUtils.showShort("点击了")
        }
        findViewById<View>(R.id.show_tv).setOnClickListener {
            CommonDialog.show(
                this,
                "123",
                object : CommonDialog.IDialogElementProvider {
                    override fun bindLayoutId(): Int {
                        return R.layout.layout_dialog
                    }

                    override fun onViewCreated(dialogFragment: DialogFragment) {
                        var view = dialogFragment.view!!
                        var avatar = view.findViewById<ImageView>(R.id.avatar)
                        var close_iv = view.findViewById<ImageView>(R.id.close_iv)
                        var follow_btn: TextView = view.findViewById(R.id.follow_btn)
                        follow_btn.setOnClickListener {
                            ToastUtils.showShort("关注成功!")
                            dialogFragment.dismiss()

                        }
                    }

                    override fun provideDialogPosition(window: Window) {
//                        transparentStatusBar(window)
                        //使dialog不被状态栏遮挡
                        setTransparentForWindow(window)
                        var params = window.attributes
                        params.gravity = Gravity.TOP or Gravity.LEFT

                    }

                })
        }
    }

    private fun transparentStatusBar(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window
                .addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.WHITE
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    private fun setTransparentForWindow(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // 添加此flag 使dialog不被状态栏遮挡
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window
                .setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                )
        }
    }
}