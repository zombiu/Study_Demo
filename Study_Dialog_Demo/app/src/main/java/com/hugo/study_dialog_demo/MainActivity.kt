package com.hugo.study_dialog_demo

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.bearever.async.chain.AsyncChain
import com.bearever.async.chain.core.AsyncChainRunnable
import com.bearever.async.chain.core.AsyncChainTask
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.hugo.study_dialog_demo.databinding.ActivityMainBinding
import com.hugo.study_dialog_demo.proxy.CallBox
import com.hugo.study_dialog_demo.proxy.Callback
import com.hugo.study_dialog_demo.proxy.OnItemChildListener
import com.hugo.study_dialog_demo.task.ActionChain
import com.jaeger.library.StatusBarUtil
import im.yixin.ui.dialog.CommonDialog

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var actionChain: ActionChain
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ImmersionBar.with(this)
            .transparentStatusBar()
            .init()

        StatusBarUtil.setTranslucent(this)

        findViewById<View>(R.id.tv1).setOnClickListener {
            // 不使用activity 弹出dialog
            showNoTokenDialog()
            ToastUtils.showShort("点击了")
        }
        findViewById<View>(R.id.show_tv).setOnClickListener {
//            showTipDialog()
            showViewDialog()
        }

        binding.rootView.setOnTouchListener { v, event ->
            LogUtils.e("-->> " + event)
            dismissAnimation()
            return@setOnTouchListener false
        }


        /*actionChain = ActionChain()
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
        actionChain.notifyAction()*/
        LogUtils.e("-->>任务链开始 ")


        AsyncChain.with(object : AsyncChainRunnable<String, String>() {
            override fun run(task: AsyncChainTask<String, String>?) {

            }

        }).go()

        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setView(R.layout.layout_dialog)
            .create()

        /*var viewLayer = ViewLayer.Builder()
            .setContentView(R.layout.layout_dialog)
            .initView {

            }.create()
        viewLayer.show()*/

        binding.tv2.setOnClickListener {
            LogUtils.e("-->>点击了tv2")
//            showDialog()
            CallBox.getService(Callback::class.java).call("msg " + System.currentTimeMillis())
//            CallBox.getService(OnItemChildListener::class.java).register()
        }

        CallBox.register(Callback::class.java, object : Callback {
            override fun call(event: String) {
                LogUtils.e("-->>", "回调1=$event")
            }

        })

        CallBox.register(Callback::class.java, object : Callback {
            override fun call(event: String) {
                LogUtils.e("-->>", "回调2=$event")
            }

        })
    }

    private fun showNoTokenDialog() {
        //  java.lang.IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity. 需要设置theme
        var alertDialog: AlertDialog = AlertDialog.Builder(
            AppDelegate.getInstance().application,
            R.style.Theme_AppCompat_Light_Dialog_Alert
        )
            .setTitle("这是标题")
            .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }

            })
            .create()
//        alertDialog.window!!.setType(TYPE_SYSTEM_ALERT)
        alertDialog.window!!.setType(TYPE_APPLICATION_OVERLAY)
        alertDialog.show()
    }

    private fun showDialog() {
        var build = AlertDialog.Builder(this, R.style.MyDialog)
        build.setView(R.layout.layout_dialog2)
        var dialog = build.create()
        // show之前，可以对宽度进行修改吗？
        dialog.window?.decorView?.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
        var attributes = dialog.window?.attributes!!
        attributes.width = ScreenUtils.getScreenWidth()
        dialog.window!!.attributes = attributes

        dialog.show()
        // show之后，可以对宽度进行修改

        // 可以findview 针对view进行修改
    }

    fun showAnimation() {
        var alphaAnimation = AlphaAnimation(0f, 1f)

        var scaleAnimation = ScaleAnimation(
            0.8f,
            1f,
            0.8f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )


        var animationSet = AnimationSet(false)
        binding.dialogIv.animation = animationSet

        animationSet.addAnimation(alphaAnimation)
        animationSet.addAnimation(scaleAnimation)
        animationSet.duration = 100
        animationSet.start()
    }

    fun dismissAnimation() {
        var alphaAnimation = AlphaAnimation(1f, 0f)

        var scaleAnimation = ScaleAnimation(
            1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )

        var animationSet = AnimationSet(false)
        // 监听需要设置在  binding.dialogIv.startAnimation(animationSet) 之前
        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.dialogIv.visibility = View.GONE
                LogUtils.e("-->>onAnimationEnd")
            }

            override fun onAnimationStart(animation: Animation?) {
                LogUtils.e("-->>开始")
            }

        })

        animationSet.addAnimation(alphaAnimation)
        animationSet.addAnimation(scaleAnimation)
        animationSet.duration = 100

        binding.dialogIv.startAnimation(animationSet)
    }

    private fun showViewDialog() {
        binding.dialogIv.visibility = View.VISIBLE
        var layoutParams: ViewGroup.MarginLayoutParams =
            binding.dialogIv.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, ConvertUtils.dp2px(100f), 0, 0)
        binding.dialogIv.layoutParams = layoutParams
        showAnimation()
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