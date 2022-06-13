package com.example.study_customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;
import android.widget.Scroller;

import com.blankj.utilcode.util.LogUtils;

public class CustomViewPager extends ViewGroup {
    private int downX;
    private int downY;

    private Paint paint = new Paint();

    private int originalOffsetX;
    private int originalOffsetY;

    // 支持惯性滑动  跟Scroller的区别  OverScroller可以划过界 OverScroller松手时会有个初始速度
    private OverScroller scroller = new OverScroller(getContext());
//    private Scroller scroller = new Scroller(getContext());

    private VelocityTracker mVelocityTracker;

    public CustomViewPager(Context context) {
        this(context, null);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int useWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.layout(useWidth, 0, useWidth + child.getMeasuredWidth(), child.getMeasuredHeight());
            useWidth += child.getMeasuredWidth();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                downX = (int) event.getX();
//                downY = (int) event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // 手指按下 之后 移动的偏移
                float moveX = event.getX() - downX;
                int scrollX = (int) (originalOffsetX - moveX);
//                int scrollY = (int) (originalOffsetY - event.getY() + downY);
                // 限制一下 scrollX的最大最小偏移
                scrollX = Math.max(scrollX, 0);
                scrollX = Math.min(scrollX, getWidth() * 2);
                scrollTo(scrollX, 0);
                break;
            }
            case MotionEvent.ACTION_UP: {
//                originalOffsetX = getScrollX();
                int scrollX = getScrollX();
                LogUtils.e("-->>up事件 scroll x=" + scrollX + "--view宽度=" + getWidth());
//                round 表示"四舍五入"  需要将结果转换为 float
                int index = Math.round(getScrollX() / (float) getWidth());
                int dx = index * getWidth() - scrollX;

                originalOffsetX = index * getWidth();
                LogUtils.e("-->>up事件 dx=" + dx + " ---- index=" + index + " ---计算索引=" + "---------result=");

                // 注意了 这里选取的是 up事件时  scrollx的坐标
                // startScroll 中的dx和dy：代表相对于参考点要移动的位置  startX和startY表示参考点
                scroller.startScroll(scrollX, 0, dx, 0);
                invalidate();
                break;
            }

        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
            invalidate();
//            originalOffsetX = scroller.getCurrX();
//            LogUtils.e("-->>computeScroll scroll x=" + originalOffsetX);
//            originalOffsetY = scroller.getCurrY();
        }
    }
}
