package com.hugo.study_richtext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.hugo.study_richtext.databinding.ActivityClickSpanBinding;
import com.hugo.study_richtext.kit.LinkMoveMentMehtodEx;

public class ClickSpanActivity extends AppCompatActivity {

    private ActivityClickSpanBinding binding;
    private String text = "这个用户的昵称是碧海鱼龙，一个外包仔。这个是用来换行测试用的，看看多少个字可以换行哟！";
    private int start = 8;
    private int end = 12;


    public static void start(Context context) {
        context.startActivity(new Intent(context, ClickSpanActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClickSpanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    /**
     * 小米手机有个特性 有点恶心，点击下面的带颜色的文字，会触发拖拽效果
     */
    private void initView() {
        final SpannableStringBuilder builder = new SpannableStringBuilder(text);
        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                LogUtils.e("-->>点击了话题");
                // 这里必须post一下，不然没有选中效果
                widget.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.richEt.setSelection(start, end);
                    }
                });
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        builder.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int color = getResources().getColor(R.color.colorPrimary);
        builder.setSpan(new ForegroundColorSpan(color), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.richEt.setText(builder);


        binding.richEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("-->>点击了文本");
            }
        });

//        binding.richEt.setMovementMethod(LinkMovementMethod.getInstance());
        binding.richEt.setMovementMethod(new LinkMoveMentMehtodEx());
    }
}