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

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] links = buffer.getSpans(off, off, ClickableSpan.class);

            if (links.length != 0) {
                ClickableSpan link = links[0];
                if (action == MotionEvent.ACTION_UP) {
                    /*if (link instanceof TextLinks.TextLinkSpan) {
                        ((TextLinks.TextLinkSpan) link).onClick(
                                widget, TextLinks.TextLinkSpan.INVOCATION_METHOD_TOUCH);
                    } else {
                        link.onClick(widget);
                    }*/
                    link.onClick(widget);
                    LogUtils.e("-->> link.onClick");
                } else if (action == MotionEvent.ACTION_DOWN) {
                    if (widget.getContext().getApplicationInfo().targetSdkVersion
                            >= Build.VERSION_CODES.P) {
                        // Selection change will reposition the toolbar. Hide it for a few ms for a
                        // smoother transition.
//                        widget.hideFloatingToolbar(HIDE_FLOATING_TOOLBAR_DELAY_MS);
                    }
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link),
                            buffer.getSpanEnd(link));
                    LogUtils.e("-->>选中");
                }
                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }
}
