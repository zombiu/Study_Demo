package com.example.study_notification

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hjq.toast.ToastUtils

class MainActivity : AppCompatActivity() {
    private var BACKGROUND_WINDOWS_PERMISS_CODE = 1009
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        var canBackgroundStart = UIDelegate.canBackgroundStart(this)
        Log.e("-->>", "是否可以后台启动 $canBackgroundStart")
        ToastBox.show("toast一下")
        ToastUtils.show("我是吐司")
    }

    fun clickView(view: View) {
        //开启后台权限
        request()
    }

    fun request() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val xiaomiBackGroundIntent = Intent()
            xiaomiBackGroundIntent.action = "miui.intent.action.APP_PERM_EDITOR"
            xiaomiBackGroundIntent.addCategory(Intent.CATEGORY_DEFAULT)
            xiaomiBackGroundIntent.putExtra("extra_pkgname", this.packageName)
            startActivityForResult(xiaomiBackGroundIntent, BACKGROUND_WINDOWS_PERMISS_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("-->>", "requestCode =${requestCode} resultCode=${resultCode} + ${data.toString()}")

        var canBackgroundStart = UIDelegate.canBackgroundStart(this)
        Log.e("-->>", "是否可以后台启动 $canBackgroundStart")
    }
}