package com.hugo.study_toolbar.widget;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.LogUtils;

import java.util.Collections;
import java.util.List;

import im.yixin.b.qiye.common.util.PermissionKit;

public class PermissionFragment extends Fragment {
    public static final String KEY_TAG = "PermissionFragment";
    private ActivityResultLauncher resultLauncher;
    private Consumer<Boolean> consumer;
    private int requestIndex = 0;
    private List<String> permissions = Collections.emptyList();
    private PermissionDialog permissionDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置为 true，表示 configuration change 的时候，fragment 实例不会重新创建
        setRetainInstance(true);
        resultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (permissionDialog != null) {
                    permissionDialog.dismiss();
                }
                if (result) {
                    LogUtils.e("-->>", "授权成功");
                    if (requestIndex < permissions.size()) {
                        LogUtils.e("-->>", "请求权限=" + permissions.get(requestIndex));
                        requestPermission(permissions.get(requestIndex));
                    } else {
                        permissionAccept(result);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String s = permissions.get(requestIndex - 1);
                        if (!shouldShowRequestPermissionRationale(s)) {
                            //用户拒绝权限并且系统不再弹出请求权限的弹窗
                            //这时需要我们自己处理，比如自定义弹窗告知用户为何必须要申请这个权限
                            LogUtils.e("-->>", "当前时间=" + System.currentTimeMillis());
                            Log.e("-->>", s + "权限 not granted and should not show rationale");

                        }
                    }
                    LogUtils.e("-->>", "授权失败");
                    permissionAccept(result);
                }
            }
        });
        LogUtils.e("-->>", "fragment onCreate");
        requestPermission(permissions.get(requestIndex));
    }

    public void setConsumer(Consumer<Boolean> consumer) {
        this.consumer = consumer;
    }

    public void requestPermission(List<String> permissions) {
        this.permissions = permissions;
        if (isAdded()) {
            requestPermission(permissions.get(requestIndex));
        }
    }

    public void realRequestPermission(String permissions) {
        nextRequestIndex();
        resultLauncher.launch(permissions);
    }

    private void nextRequestIndex() {
        requestIndex++;
    }

    private void permissionAccept(boolean accept) {
        clear();
        if (consumer != null) {
            consumer.accept(accept);
        }
    }

    public void requestPermission(String permission) {
        PermissionKit.PermissionInfo permissionInfo = PermissionKit.INSTANCE.getPermissionInfo(permission);
        if (permissionInfo == null) {
            permissionAccept(false);
            return;
        }
        permissionDialog = PermissionDialog.Companion.newInstance(null, null);
        permissionDialog.show(getActivity());
        LogUtils.e("-->>", "当前时间=" + System.currentTimeMillis());
        realRequestPermission(permissionInfo.getPermission());
    }

    private void clear() {
        permissions = Collections.emptyList();
        requestIndex = 0;
    }
}
