package im.yixin.b.qiye.common.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.util.Consumer
import androidx.fragment.app.FragmentActivity
import com.hugo.study_toolbar.widget.PermissionFragment

object PermissionKit {
    private var permissionDialogText: HashMap<String, PermissionInfo> = HashMap()

    init {
        permissionDialogText.apply {
            put(
                Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionInfo(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    "存储权限使用说明",
                    "APP需要获取您的存储权限，以便于您正常使用该功能"
                )
            )
            put(
                Manifest.permission.CAMERA, PermissionInfo(
                    Manifest.permission.CAMERA,
                    "摄像头权限使用说明",
                    "APP需要获取您的存储权限，以便于您正常使用该功能"
                )
            )
            put(
                Manifest.permission.RECORD_AUDIO, PermissionInfo(
                    Manifest.permission.RECORD_AUDIO,
                    "录音存储权限使用说明",
                    "APP需要获取您的存储权限，以便于您正常使用该功能"
                )
            )
        }
    }

    fun getPermissionInfo(permission: String): PermissionInfo? {
        return permissionDialogText[permission]
    }

    fun requestPermission(
        activity: FragmentActivity?,
        permissions: Array<String>,
        accept: Consumer<Boolean>?
    ) {
        if (activity == null || activity.isFinishing) {
            return
        }
        val list: List<String> = checkNoPermission(activity!!, permissions)
        if (list.isEmpty()) {
            accept?.accept(true)
            return
        }
        val fragment: PermissionFragment = acquireFragment(activity!!)
        fragment.setConsumer(accept)
        fragment.requestPermission(list)
    }

    private fun checkNoPermission(activity: Activity, permissions: Array<String>): List<String> {
        val list: MutableList<String> = ArrayList()
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                list.add(permission)
            }
        }
        return list
    }

    private fun acquireFragment(activity: FragmentActivity): PermissionFragment {
        val fragmentManager = activity.supportFragmentManager
        var fragment = fragmentManager.findFragmentByTag(PermissionFragment.KEY_TAG)
        if (fragment == null) {
            fragment = PermissionFragment()
            fragmentManager.beginTransaction().add(fragment, PermissionFragment.KEY_TAG)
                .commitAllowingStateLoss()
        }
        return fragment as PermissionFragment
    }

    data class PermissionInfo(val permission: String, val title: String, val content: String)
}