package com.example.study_notification

import android.app.Application
import android.view.Gravity.BOTTOM
import com.blankj.utilcode.util.ConvertUtils
import com.hjq.toast.ToastUtils

class App :Application() {

    override fun onCreate() {
        super.onCreate()
        UIDelegate.getInstance().init(this)

        // 在 Application 中初始化
        ToastUtils.init(this);

        ToastUtils.getToast().setGravity(BOTTOM, 0, ConvertUtils.dp2px(60f));
    }
}