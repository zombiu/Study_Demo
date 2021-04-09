package im.yixin.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.ScreenUtils
import com.hugo.study_dialog_demo.R


/**
 *
 */
class CommonDialog : DialogFragment() {
    lateinit var rootView: View

    var provider: IDialogElementProvider? = null

    var runnable = Runnable {
        activity?.let {
            if (!it.isFinishing) {
                dismiss()
            }
        }

    }

    companion object {
        @JvmStatic
        fun show(
            activity: FragmentActivity,
            tag: String,
            provider: IDialogElementProvider
        ): CommonDialog? {
            if (provider == null) {
                return null
            }
            var userGuideDialog = CommonDialog()
            userGuideDialog.provider = provider
            userGuideDialog.show(activity.supportFragmentManager, tag)
            return userGuideDialog
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MyDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layoutId = provider?.bindLayoutId() ?: R.layout.layout_dialog
        rootView = inflater.inflate(layoutId, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isCancelable = true

        provider?.onViewCreated(this)

        setDialogPosition()

        dialog?.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (!activity!!.isFinishing) {
                    activity?.onBackPressed()
                }
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 5000)
    }

    override fun onDestroyView() {
        Handler(Looper.getMainLooper()).removeCallbacks(runnable)
        super.onDestroyView()
    }

    /*fun setAvatar(url: String) {
        loadImageUrl(null, url,
                context!!.resources.getDimensionPixelOffset(R.dimen.dp_30),
                ImageUtil.AVATAR_STROKE_WIDTH,
                ImageUtil.AVATAR_STROKE_COLOR,
                ImageUtil.AVATAR_DEFAULT,
                null, null)
    }*/

    private fun setDialogPosition() {
        var window = dialog!!.window
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //FLAG_NOT_TOUCH_MODAL作用：即使该window可获得焦点情况下，仍把该window之外的任何event发送到该window之后的其他window
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.BOTTOM)

        var attributes = window!!.attributes
        attributes.width = ScreenUtils.getScreenWidth()

        provider!!.provideDialogPosition(window!!)
    }

    interface IDialogElementProvider {

        fun bindLayoutId(): Int

        fun onViewCreated(dialogFragment: DialogFragment)

        fun provideDialogPosition(window: Window)
    }
}
