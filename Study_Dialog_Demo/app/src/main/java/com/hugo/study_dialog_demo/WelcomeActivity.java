package com.hugo.study_dialog_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.hugo.study_dialog_demo.algo.dp._213_打家劫舍_II;
import com.hugo.study_dialog_demo.algo.dp._5_最长回文子串;
import com.hugo.study_dialog_demo.algo.tree.TreeNode;
import com.hugo.study_dialog_demo.algo.tree._101_对称二叉树;
import com.hugo.study_dialog_demo.algo.tree._138_复制带随机指针的链表;
import com.hugo.study_dialog_demo.databinding.ActivityWelcomeBinding;
import com.hugo.study_dialog_demo.ui.section1.SectionActivity;
import com.hugo.study_dialog_demo.ui.section2.Section2Activity;
import com.hugo.study_dialog_demo.utils.MyViewModel;

//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;

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

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        binding.goMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
        });

        binding.goAppList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, SectionActivity.class));
            }
        });

        binding.goAppList2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, Section2Activity.class));
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

        /*TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(3);
        TreeNode treeNode4 = new TreeNode(4);
        TreeNode treeNode5 = new TreeNode(5);
        TreeNode treeNode7 = new TreeNode(7);

        treeNode1.setLeft(treeNode2);
        treeNode1.setRight(treeNode3);
        treeNode2.setLeft(treeNode4);
        treeNode2.setRight(treeNode5);
        treeNode3.setRight(treeNode7);

        _958_二叉树的完全性检验.Solution solution = new _958_二叉树的完全性检验.Solution();
        LogUtils.e("-->>" + solution.isCompleteTree(treeNode1));*/

//        BasicHttpParams basicHttpParams = new BasicHttpParams();
//        DefaultHttpClient httpClient = new DefaultHttpClient();
        String s = new String();
        try {
            // https://blog.csdn.net/u013270444/article/details/99318797 从Android P开始，org.apache.http.legacy 库将从 bootclasspath 中删除。 https://www.cnblogs.com/renhui/p/9798335.html
            Class<?> aClass = s.getClass().getClassLoader().loadClass("org.apache.http.params.BasicHttpParams");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        _138_复制带随机指针的链表.Node node1 = new _138_复制带随机指针的链表.Node(3);
        _138_复制带随机指针的链表.Node node2 = new _138_复制带随机指针的链表.Node(3);
        _138_复制带随机指针的链表.Node node3 = new _138_复制带随机指针的链表.Node(3);
        node1.setNext(node2);
        node2.setNext(node3);
        node2.setRandom(node1);

        _138_复制带随机指针的链表.Solution solution1 = new _138_复制带随机指针的链表.Solution();
        solution1.copyRandomList(node1);

        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(2);
        TreeNode treeNode4 = new TreeNode(3);
        TreeNode treeNode5 = new TreeNode(4);
        TreeNode treeNode7 = new TreeNode(4);
        TreeNode treeNode8 = new TreeNode(3);
        treeNode1.setLeft(treeNode2);
        treeNode1.setRight(treeNode3);
        treeNode2.setLeft(treeNode4);
        treeNode2.setRight(treeNode5);
        treeNode3.setLeft(treeNode7);
        treeNode3.setRight(treeNode8);
        _101_对称二叉树.Solution solution2 = new _101_对称二叉树.Solution();
        solution2.isSymmetric(treeNode1);

        _213_打家劫舍_II.Solution solution = new _213_打家劫舍_II.Solution();
        int[] arr = new int[]{1,2,3,1};
        solution.rob(arr);

        _5_最长回文子串.Solution solution3 = new _5_最长回文子串.Solution();
        Log.e("-->>","--" + solution3.longestPalindrome("aacabdkacaa"));
    }
}