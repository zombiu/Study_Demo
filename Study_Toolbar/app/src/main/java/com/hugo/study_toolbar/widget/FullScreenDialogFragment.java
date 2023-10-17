package com.hugo.study_toolbar.widget;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.hugo.study_toolbar.R;
import com.hugo.study_toolbar.databinding.ActivityMain2Binding;

/**
 * 沉浸式 全屏DialogFragment
 */
public class FullScreenDialogFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //        <!--关键点1-->
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // 添加动画
        getDialog().getWindow().setWindowAnimations(R.style.AnimTop);
        return inflater.inflate(R.layout.permission_dialog_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        <!--关键点2-->
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        // 2. 需要设置 Activity 的 Window 的 FLAG_TRANSLUCENT_STATUS 标志，以确保 Activity 的布局与状态栏重叠
        Window window = getDialog().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    public void onStart() {
        super.onStart();
        // 1. 在 DialogFragment 中设置 Window 的 FLAG_LAYOUT_NO_LIMITS 标志，以使 DialogFragment 的布局可以延伸到状态栏区域
        Window window = getDialog().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}