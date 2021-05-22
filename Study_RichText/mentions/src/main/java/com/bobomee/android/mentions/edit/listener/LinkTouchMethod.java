package com.bobomee.android.mentions.edit.listener;

import android.text.Layout;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;

public class LinkTouchMethod implements View.OnTouchListener {
    long longClickDelay = ViewConfiguration.getLongPressTimeout();
    long startTime = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            startTime = System.currentTimeMillis();
        }
        TextView tv = (TextView) v;
        CharSequence text = tv.getText();
        if (text instanceof Spanned) {
            if (action == MotionEvent.ACTION_UP) {
                // 避免长按和点击冲突，如果超过300毫秒，认为是在长按，不执行点击操作
                if (System.currentTimeMillis() - startTime > longClickDelay) {
                    LogUtils.e("-->>认为是长按");
                    return false;
                }
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= tv.getTotalPaddingLeft();
                y -= tv.getTotalPaddingTop();

                x += tv.getScrollX();
                y += tv.getScrollY();

                Layout layout = tv.getLayout();
                // 获取y坐标所在行数
                int line = layout.getLineForVertical(y);
                // 获取所在行数 x坐标的偏移量
                int off = layout.getOffsetForHorizontal(line, x);
                LogUtils.e("-->>line=" + line);
                ClickableSpan[] link = ((Spanned) text).getSpans(off, off, ClickableSpan.class);
                if (link.length != 0) {
                    if (x < layout.getLineWidth(line) && x > 0) {
                        link[0].onClick(tv);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
