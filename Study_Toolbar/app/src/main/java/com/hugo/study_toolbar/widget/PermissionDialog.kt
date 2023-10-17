package com.hugo.study_toolbar.widget

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.ConvertUtils
import com.hugo.study_toolbar.R


class PermissionDialog : DialogFragment() {
    val TAG: String = this::class.java.simpleName

    private var title: String? = null
    private var content: String? = null

    companion object {
        const val TITLE = "title"
        const val CONTENT = "content"

        fun newInstance(title: String?, content: String?): PermissionDialog {
            var permissionDialog = PermissionDialog()
            var arguments = Bundle()
            arguments.putString(TITLE, title)
            arguments.putString(CONTENT, content)

            permissionDialog.arguments = arguments
            return permissionDialog
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
        // 3.设置style
//        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen)

    }

    fun parseArguments() {
        if (arguments == null) {
            return
        }
        title = arguments?.getString(TITLE)
        content = arguments?.getString(CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 添加动画
        dialog!!.window!!.setWindowAnimations(R.style.AnimTop)
        //    app:cardElevation="0dp" cardview去黑边
//        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.permission_dialog_layout, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var window = dialog!!.window!!
        /*val decorView = dialog!!.window!!.decorView

        val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            decorView,
            PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.0f),
            PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.0f),
            PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f),
            PropertyValuesHolder.ofFloat("translationY", -100f, 0f),
        )
        scaleDown.setDuration(2000);
        scaleDown.start();*/
//        AnimationKit.slideToUp(view)
//        MeasureUtils.measureWrapContent(view)
//        LogUtils.e("-->>", "view 高=" + view.measuredHeight)
//        var objectAnimatorY =
//            ObjectAnimator.ofFloat(view, "translationY", -(view.measuredHeight.toFloat() + StatusBarManager.getStatusBarHeight()), StatusBarManager.getStatusBarHeight().toFloat())
//        objectAnimatorY.setDuration(2000)
//        objectAnimatorY.start()


        // 2. 需要设置 Activity 的 Window 的 FLAG_TRANSLUCENT_STATUS 标志，以确保 Activity 的布局与状态栏重叠
        window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        dialog!!.window!!.attributes.windowAnimations = R.style.AnimTop
    }

    override fun onStart() {
        super.onStart()
        initWindowParams()
    }

    private fun initWindowParams() {
        val window = dialog!!.window
        if (window != null) {
            val lp = window.attributes
            //调节灰色背景透明度[0-1]，默认0.5f
//            lp.dimAmount = 0.5f
            val widthPixels = resources.displayMetrics.widthPixels
            lp.width = widthPixels - ConvertUtils.dp2px(20f) * 2
//            lp.height = ConvertUtils.dp2px(140f)
            lp.y = StatusBarManager.getStatusBarHeight()
            window.attributes = lp
            window.setGravity(Gravity.TOP)

            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


            val decorView = window.decorView
            decorView.background = ColorDrawable(Color.TRANSPARENT)


            // 1. 在 DialogFragment 中设置 Window 的 FLAG_LAYOUT_NO_LIMITS 标志，以使 DialogFragment 的布局可以延伸到状态栏区域
            val window = dialog!!.window
            window!!.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        }
        isCancelable = true //设置点击外部是否消失

    }

    fun show(activity: FragmentActivity) {
        try {
            show(activity.supportFragmentManager, TAG)
        } catch (e: Exception) {

        }
    }

}

interface OnDialogClickListener {
    fun onViewClick(dialogFragment: DialogFragment, view: View)
}