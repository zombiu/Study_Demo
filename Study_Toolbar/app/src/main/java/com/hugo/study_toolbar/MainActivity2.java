package com.hugo.study_toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.os.Bundle;
import android.view.MenuItem;

import com.hugo.study_toolbar.databinding.ActivityMain2Binding;

/**
 * 自定义ActionProvider
 * https://blog.csdn.net/yanzhenjie1003/article/details/51902796
 */
public class MainActivity2 extends AppCompatActivity {
    ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        //ensureMenuView toolbar内部会生成一个ActionMenuView

//        getMenuInflater().inflate();
    }
}