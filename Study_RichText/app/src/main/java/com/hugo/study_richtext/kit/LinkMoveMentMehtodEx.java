package com.hugo.study_richtext.kit;

import android.os.Build;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.textclassifier.TextLinks;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

public class LinkMoveMentMehtodEx extends LinkMovementMethod {

    /**
     * 这里需要过滤一下长按事件
     * @param widget
     * @param buffer
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        // 因为TextView没有点击事件，所以点击TextView的非富文本时，super.onTouchEvent()返回false；
        // 此时可以让TextView的父容器执行点击事件；
        boolean isConsume = super.onTouchEvent(widget, buffer, event);
        if (!isConsume && event.getAction() == MotionEvent.ACTION_UP) {
            /*ViewParent parent = widget.getParent();
            if (parent instanceof ViewGroup) {
                // 获取被点击控件的父容器，让父容器执行点击；
                ((ViewGroup) parent).performClick();
            }*/
            widget.performClick();
        }
        return isConsume;
    }
}
