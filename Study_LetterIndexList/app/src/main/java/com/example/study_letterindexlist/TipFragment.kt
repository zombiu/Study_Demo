package com.example.study_letterindexlist

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils


/**
 * 如何实现悬浮提示框
 */
class TipFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.dialog_default_style)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tip, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view!!.findViewById<TextView>(R.id.title).setOnClickListener {
            ToastUtils.showLong("点击了dialog")
            dismiss()
        }

        dialog?.setOnKeyListener { dialog, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_BACK){
                LogUtils.e("-->>返回键 KEYCODE_BACK")
                if (!activity!!.isFinishing) {
                    activity!!.onBackPressed()
                    LogUtils.e("-->>activity 退出")
                }
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        //FLAG_NOT_TOUCH_MODAL作用：即使该window可获得焦点情况下，仍把该window之外的任何event发送到该window之后的其他window
        window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val params = window!!.attributes
        params.gravity = Gravity.BOTTOM
        val widthPixels = resources.displayMetrics.widthPixels
//        params.width = widthPixels - DensityUtil.dip2px(AppCache.getContext(), 10f) * 2
        //        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.attributes = params
        val decorView = window.decorView
        decorView.setPadding(100, 100, 100, 0)
        decorView.background = ColorDrawable(Color.TRANSPARENT)
        /*decorView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
//                slideDown()
            }
            true
        }*/
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TipFragment.
         */
        @JvmStatic
        fun newInstance() = TipFragment().apply {
                /*arguments = Bundle().apply {

                }*/
            }
    }
}