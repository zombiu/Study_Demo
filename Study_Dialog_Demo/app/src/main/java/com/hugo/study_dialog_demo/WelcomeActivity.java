package com.hugo.study_dialog_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.blankj.utilcode.util.LogUtils;
import com.hugo.study_dialog_demo.algo._707_设计链表_双向链表;
import com.hugo.study_dialog_demo.algo.link.LRUCache;
import com.hugo.study_dialog_demo.algo.queue.MyCircularDeque;
import com.hugo.study_dialog_demo.algo.queue.MyCircularQueue;
import com.hugo.study_dialog_demo.algo.stack._150_逆波兰表达式求值;
import com.hugo.study_dialog_demo.databinding.ActivityWelcomeBinding;
import com.hugo.study_dialog_demo.utils.MyViewModel;

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


        binding.goMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
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

        //        ViewModelProviders弃用后最新用法
        MyViewModel viewModelWithliveData = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MyViewModel.class);

        MyCircularDeque myCircularDeque = new MyCircularDeque(52);
        myCircularDeque.insertFront(80);
        myCircularDeque.insertFront(27);
        myCircularDeque.deleteLast();
    }
}