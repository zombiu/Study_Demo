package com.example.study_customview.widget;

import android.content.Context;
import android.os.Build;
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
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.LogUtils;
import com.example.study_customview.utils.MeasureUtils;

public class CustomScrollView extends FrameLayout {
    private OverScroller scroller;
    private int downY;
    private int prevScrollY;
    private VelocityTracker mVelocityTracker;

    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;

    private boolean isScrolling;

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /*if (getChildCount() > 0) {
            final View child = getChildAt(0);
            int widthPadding = 0;
            int heightPadding = 0;
            final int targetSdkVersion = getContext().getApplicationInfo().targetSdkVersion;
            final FrameLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();
            *//*if (targetSdkVersion >= Build.VERSION_CODES.M) {
                widthPadding = getPaddingLeft() + mPaddingRight + lp.leftMargin + lp.rightMargin;
                heightPadding = mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin;
            } else {
                widthPadding = mPaddingLeft + mPaddingRight;
                heightPadding = mPaddingTop + mPaddingBottom;
            }*//*

            final int desiredHeight = getMeasuredHeight() - heightPadding;
            if (child.getMeasuredHeight() < desiredHeight) {
                final int childWidthMeasureSpec = getChildMeasureSpec(
                        widthMeasureSpec, widthPadding, lp.width);
                final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        desiredHeight, MeasureSpec.EXACTLY);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }*/

        // 对子view进行测量
        if (getChildCount() > 0) {
//            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec((1 << 30) - 1, MeasureSpec.AT_MOST);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            // 对子view进行测量 宽度使用父view的宽度 高度不限制  todo 这里没有处理父布局padding 子布局margin时的情况
            getChildAt(0).measure(widthMeasureSpec, childHeightMeasureSpec);
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

                /*final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity();*/
                // 处理fling
                /*if ((Math.abs(initialVelocity) >= mMinimumVelocity)) {
                    LogUtils.e("-->>计算的速度=" + initialVelocity);
                    // todo fling 结束 也需要处理一下回弹
                    fling(-initialVelocity);
                } else if (scroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, 0)) {
                    // 处理回弹
                    ViewCompat.postInvalidateOnAnimation(this);
                }*/
                // startX 和startY很好解释，是起始坐标，minX，maxX，minY，maxY 这4个坐标构成了一个矩形
                //该方法返回一个boolean,假如View移动到起始位置时，有部分或者全部位于矩形之外则返回true，反之返回false
                // 如果我们在返回true时调用invalidate()方法那么View的computeScroll方法将被调用，View将会滚动，到什么位置停止？在View完全进入矩形的时候。
                /*if (scroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, 0)) {
                    // 处理回弹
                    ViewCompat.postInvalidateOnAnimation(this);
                }*/
                // 不使用scroller.springBack的回弹效果
//                smoothScrollTo(0, 0);

                // 划到头了 继续划 需要回弹
                if (getScrollY() < 0) {
                    springBack(0, 0);
                } else if (getScrollY() > getScrollRange()) {
                    // 滑到尾了 继续划， 需要回弹
                    springBack(0, getScrollRange());
                } else {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker.getYVelocity();
                    // 处理fling
                    if ((Math.abs(initialVelocity) >= mMinimumVelocity)) {
                        LogUtils.e("-->>计算的速度=" + initialVelocity);
                        // todo fling 结束 也需要处理一下回弹
                        fling(-initialVelocity);
                    } else if (scroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, 0)) {
                        // 处理回弹
                        ViewCompat.postInvalidateOnAnimation(this);
                    }
                    isScrolling = true;
                }
                break;
            }
        }
        return true;
    }

    boolean needSpringBack() {
        return getScrollY() < 0 || getScrollY() > getScrollRange();
    }

    int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            FrameLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();
            // view的实际高度 + margin
            int childSize = child.getHeight() + lp.topMargin + lp.bottomMargin;
            // 父view的高度 - padding
            int parentSpace = getHeight() - getPaddingTop() - getPaddingBottom();
            scrollRange = Math.max(0, childSize - parentSpace);
        }
        return scrollRange;
    }

    private void springBack(int finalX, int finalY) {
        // 开始位置是 已经滑到的当前位置，dx和dy表示需要滑动到的位置
        int dy = finalY - getScrollY();
        scroller.startScroll(getScrollX(), getScrollY(), 0, dy);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void smoothScrollTo(int finalX, int finalY) {
        int dx = finalX - scroller.getFinalX();
        int dy = finalY - prevScrollY;
        smoothScrollBy(dx, dy);
    }

    private void smoothScrollBy(int dx, int dy) {
        // 开始位置是 已经滑到的当前位置，dx和dy表示需要滑动到的位置
        scroller.startScroll(getScrollX(), prevScrollY, dx, dy);
        ViewCompat.postInvalidateOnAnimation(this);
    }


    @Override
    public void computeScroll() {
        // 先判断mScroller滚动是否完成
        /*if (scroller.computeScrollOffset()) {
            // 这里调用View的scrollTo()完成实际的滚动
            scrollTo(0, scroller.getCurrY());
            // 必须调用该方法，否则不一定能看到滚动效果
//            invalidate();
            ViewCompat.postInvalidateOnAnimation(this);
            prevScrollY = scroller.getCurrY();
        }*/

        // 滑动
        if (isScrolling) {
            if (scroller.computeScrollOffset()) {
                // 这里调用View的scrollTo()完成实际的滚动
                scrollTo(0, scroller.getCurrY());
                // 必须调用该方法，否则不一定能看到滚动效果
//            invalidate();
                ViewCompat.postInvalidateOnAnimation(this);
                prevScrollY = scroller.getCurrY();
                if (prevScrollY <= -300 || prevScrollY >= getScrollRange() + 300) {
                    isScrolling = false;
                    scroller.abortAnimation();

                    // 划到头了 继续划 需要回弹
                    if (getScrollY() < 0) {
                        springBack(0, 0);
                    } else if (getScrollY() > getScrollRange()) {
                        // 滑到尾了 继续划， 需要回弹
                        springBack(0, getScrollRange());
                    }
                }
            } else {
                // 划到头了 继续划 需要回弹
                if (getScrollY() < 0) {
                    springBack(0, 0);
                } else if (getScrollY() > getScrollRange()) {
                    // 滑到尾了 继续划， 需要回弹
                    springBack(0, getScrollRange());
                }
            }
        } else {
            // 回弹
            if (scroller.computeScrollOffset()) {
                // 这里调用View的scrollTo()完成实际的滚动
                scrollTo(0, scroller.getCurrY());
                // 必须调用该方法，否则不一定能看到滚动效果
//            invalidate();
                ViewCompat.postInvalidateOnAnimation(this);
                prevScrollY = scroller.getCurrY();
            }
        }
    }

    public void fling(int velocityY) {
        if (getChildCount() > 0) {
            // 以up时的 scrollX scrollY为开始坐标
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
//        prevScrollY = getScrollY();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void abortAnimatedScroll() {
        scroller.abortAnimation();
//        stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return true;
    }
}
