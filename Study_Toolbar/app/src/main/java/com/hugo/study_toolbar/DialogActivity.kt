package com.hugo.study_toolbar

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.hugo.study_toolbar.databinding.ActivityDialogBinding
import com.hugo.study_toolbar.widget.FullScreenDialogFragment
import com.hugo.study_toolbar.widget.PermissionDialog
import com.hugo.study_toolbar.widget.StatusBarManager
import im.yixin.b.qiye.common.util.PermissionKit

class DialogActivity : AppCompatActivity() {
    lateinit var binding: ActivityDialogBinding

    companion object {
        fun go(context: Context) {
            context.startActivity(Intent(context, DialogActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPermission.setOnClickListener {
            PermissionKit.requestPermission(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), null
            )
//            var dialog = PermissionDialog.newInstance(null, null)
//            dialog.show(this)

//            var fullScreenDialogFragment = FullScreenDialogFragment()
//            fullScreenDialogFragment.show(supportFragmentManager, "full")
        }

        StatusBarManager.transparentStatusBar(this)
        StatusBarManager.modifyLightStatusBar(this, true)
    }

}