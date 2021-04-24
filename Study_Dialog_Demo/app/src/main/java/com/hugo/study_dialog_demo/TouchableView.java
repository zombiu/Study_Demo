package com.hugo.study_dialog_demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;

public class TouchableView extends FrameLayout {
    private OnTouchListener onTouchListener;

    public TouchableView(@NonNull Context context) {
        super(context, null);
    }

    public TouchableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public TouchableView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (onTouchListener != null) {
            onTouchListener.onTouch(this, ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }
}
