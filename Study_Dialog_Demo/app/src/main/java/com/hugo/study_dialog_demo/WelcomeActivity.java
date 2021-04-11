package com.hugo.study_dialog_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.blankj.utilcode.util.LogUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.hugo.study_dialog_demo.databinding.ActivityWelcomeBinding;

/**
 * 闪屏页处理
 */
public class WelcomeActivity extends AppCompatActivity {

    ActivityWelcomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LogUtils.e("-->>onCreate");


        binding.goMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
            }
        });

        //第一种方式 闪屏页防止背景被拉伸的处理
        getWindow().setBackgroundDrawableResource(R.drawable.bg_welcome);
//        getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);

        //第二种方式 配合沉浸式 可以达到与getWindow().setBackgroundDrawableResource(R.drawable.bg_welcome);相同的效果，
        /*ImmersionBar.with(this)
                .transparentStatusBar()
                .init();
        binding.getRoot().setBackgroundResource(R.drawable.bg_welcome);*/
    }
}