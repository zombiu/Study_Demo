package com.hugo.study_dialog_demo

import android.app.Dialog
import android.content.DialogInterface
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
import com.bearever.async.chain.AsyncChain
import com.bearever.async.chain.core.AsyncChainRunnable
import com.bearever.async.chain.core.AsyncChainTask
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.hugo.study_dialog_demo.task.ActionChain
import com.hugo.study_dialog_demo.task.RealAction
import com.jaeger.library.StatusBarUtil
import im.yixin.ui.dialog.CommonDialog

class MainActivity : AppCompatActivity() {

    lateinit var actionChain: ActionChain
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
//            showTipDialog()
        }


        actionChain = ActionChain()
        var realAction1 = RealAction("1")
        realAction1.setPriority(1)
        realAction1.setConsumer {
            showTipDialog("任务1")
            Thread {
                Thread.sleep(1000)
                LogUtils.e("-->>任务1执行完了")
//                it.next()
            }.start()
        }
        var realAction2 = RealAction("5")
        realAction2.setPriority(5)
        realAction2.setConsumer {
            showTipDialog("任务5")
            Thread {
                Thread.sleep(1000)
                LogUtils.e("-->>任务5执行完了")
//                it.next()
            }.start()
        }

        var realAction3 = RealAction("2")
        realAction3.setPriority(2)
        realAction3.setConsumer {
            showTipDialog("任务2")
            Thread {
                var realAction4 = RealAction("4")
                realAction4.setPriority(4)
                realAction4.setConsumer {
                    showTipDialog("任务4")
                    Thread {
                        Thread.sleep(1000)
                        LogUtils.e("-->>任务1执行完了")
//                it.next()
                    }.start()
                }
                actionChain.register(realAction4)

                Thread.sleep(3000)
                LogUtils.e("-->>任务2执行完了")
//                it.next()
            }.start()
        }

        var realAction4 = RealAction("3")
        realAction4.setConsumer {
            showTipDialog("任务3")
            Thread {
                Thread.sleep(5000)
                LogUtils.e("-->>任务3执行完了")
//                it.next()
            }.start()
        }

        actionChain.register(realAction3)
        actionChain.register(realAction1)
        actionChain.register(realAction2)
        actionChain.register(realAction4)

        // iterator() 遍历并不保证队列内顺序
        actionChain.queue.forEach {
            var action = it as RealAction
            LogUtils.e("-->> head  ${action.tag()} ${action.priority()}")
        }
        actionChain.notifyAction()
        LogUtils.e("-->>任务链开始 ")


        AsyncChain.with(object :AsyncChainRunnable<String,String>() {
            override fun run(task: AsyncChainTask<String, String>?) {

            }

        }).go()
    }

    private fun showTipDialog(title: String) {
        var dialog = CommonDialog.show(
            this,
            title,
            object : CommonDialog.IDialogElementProvider {
                override fun bindLayoutId(): Int {
                    return R.layout.layout_dialog
                }

                override fun onViewCreated(dialogFragment: DialogFragment) {
                    var view = dialogFragment.view!!
                    var nickname_tv = view.findViewById<TextView>(R.id.nickname_tv)
                    var avatar = view.findViewById<ImageView>(R.id.avatar)
                    var close_iv = view.findViewById<ImageView>(R.id.close_iv)
                    var follow_btn: TextView = view.findViewById(R.id.follow_btn)
                    follow_btn.setOnClickListener {
                        ToastUtils.showShort("关注成功!")
                        dialogFragment.dismiss()

                    }
                    nickname_tv.setText(title)
                }

                override fun provideDialogPosition(dialog: Dialog) {
                    var window = dialog.window
//                        transparentStatusBar(window)
                    //使dialog不被状态栏遮挡
                    setTransparentForWindow(window!!)
                    var params = window.attributes
                    params.gravity = Gravity.TOP or Gravity.LEFT

                    // 设置setOnDismissListener 无效
                    dialog!!.setOnDismissListener {
                        LogUtils.e("-->> dismiss")
                        actionChain.notifyAction()
                    }
                }

                override fun onDismiss(dialog: DialogInterface) {
//                    LogUtils.e("-->>on dismiss")
                    actionChain.notifyAction()
                }

            })

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