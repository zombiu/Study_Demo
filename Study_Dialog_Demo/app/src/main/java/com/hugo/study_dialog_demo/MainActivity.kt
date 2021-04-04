package com.hugo.study_dialog_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.blankj.utilcode.util.ToastUtils
import im.yixin.ui.dialog.CommonDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                        var follow_btn:TextView = view.findViewById(R.id.follow_btn)
                        follow_btn.setOnClickListener {
                            ToastUtils.showShort("关注成功!")
                            dialogFragment.dismiss()

                        }
                    }

                    override fun provideDialogPosition(window: Window) {

                    }

                })
        }
    }
}