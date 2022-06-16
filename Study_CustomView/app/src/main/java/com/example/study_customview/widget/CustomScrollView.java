package com.example.study_customview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.ViewCompat;

import com.blankj.utilcode.util.LogUtils;

public class CustomScrollView extends FrameLayout {
    private OverScroller scroller;
    private int downY;
    private int prevScrollY;
    private VelocityTracker mVelocityTracker;

    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;

    public CustomScrollView(Context context) {
        this(context, null);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        scroller = new OverScroller(getContext());

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        initVelocityTrackerIfNotExists();
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(event);
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                if (!scroller.isFinished()) {
                    abortAnimatedScroll();
                }

                downY = (int) event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int offsetY = (int) (downY - event.getY() + prevScrollY);
                scrollTo(0, offsetY);
                break;
            }
            case MotionEvent.ACTION_UP: {
                prevScrollY = getScrollY();

                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity();
                LogUtils.e("-->>计算的速度=" + initialVelocity);
                fling(-initialVelocity);
                break;
            }
        }
        return true;
    }


    @Override
    public void computeScroll() {
        // 先判断mScroller滚动是否完成
        if (scroller.computeScrollOffset()) {
            LogUtils.e("-->>滑动");
            // 这里调用View的scrollTo()完成实际的滚动
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            // 必须调用该方法，否则不一定能看到滚动效果
//            invalidate();
            ViewCompat.postInvalidateOnAnimation(this);
            prevScrollY = scroller.getCurrY();
        }

    }

    public void fling(int velocityY) {
        if (getChildCount() > 0) {

            scroller.fling(getScrollX(), getScrollY(), // start
                    0, velocityY, // velocities
                    0, 0, // x
                    Integer.MIN_VALUE, Integer.MAX_VALUE, // y
                    0, 0); // overscroll
            runAnimatedScroll(false);
        }
    }

    /**
     * @param participateInNestedScrolling 是否参与嵌套滚动
     */
    private void runAnimatedScroll(boolean participateInNestedScrolling) {
        /*if (participateInNestedScrolling) {
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH);
        } else {
            stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
        }*/
        prevScrollY = getScrollY();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void abortAnimatedScroll() {
        scroller.abortAnimation();
//        stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
    }
}
