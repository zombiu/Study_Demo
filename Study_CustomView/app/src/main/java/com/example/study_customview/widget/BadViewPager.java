package com.example.study_customview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class BadViewPager extends ViewGroup {

    private float firstX;

    private Scroller scroller;

    // 移动多少，就认为是发生了滑动行为
    private int slop;

    int sumX = 0;

    // 当前item
    float currItem = 0;

    public BadViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        slop = viewConfiguration.getScaledPagingTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 为每一个子控件测量大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                // 让每个子控件都是屏幕宽度，并且水平布局
                childView.layout(i * childView.getMeasuredWidth(), 0, (i + 1) * childView.getMeasuredWidth(), childView.getMeasuredHeight());
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                firstX = event.getX();
                break;

            case MotionEvent.ACTION_UP:

                currItem = Math.round((float) getScrollX() / getWidth());
                int dx = (int) (currItem * getWidth() - getScrollX());
                scroller.startScroll(getScrollX(), 0, dx, 0);

                // 记录总偏移量
                sumX = (int) (currItem * getWidth());

                invalidate();

                break;

            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int scrollX = sumX + (int) firstX - x;
                scrollTo(scrollX, 0);

                // 限制每个页面的边界
                if (scrollX < 0) {
                    scrollTo(0, 0);
                } else if (scrollX > (getChildCount() - 1) * getWidth()) {
                    scrollTo((getChildCount() - 1) * getWidth(), 0);
                } else {
                    scrollTo(scrollX, 0);
                }

                break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            int currX = scroller.getCurrX();
            scrollTo(currX, 0);
            invalidate();
        }

    }
}
