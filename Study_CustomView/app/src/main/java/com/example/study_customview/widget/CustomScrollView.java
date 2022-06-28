package com.example.study_customview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

import com.blankj.utilcode.util.LogUtils;
import com.elvishew.xlog.XLog;

public class CustomScrollView extends FrameLayout implements NestedScrollingParent3, NestedScrollingChild3 {
    private OverScroller scroller;
    private int lastY;
    private int prevScrollY;
    private VelocityTracker mVelocityTracker;

    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;

    private boolean isFiling;

    private NestedScrollingParentHelper mParentHelper;
    private NestedScrollingChildHelper mChildHelper;

    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];

    private boolean isBeginMove;
    private int lastScrollerY;

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

        mParentHelper = new NestedScrollingParentHelper(this);
        mChildHelper = new NestedScrollingChildHelper(this);

        // 必须设置 并且重写
        setNestedScrollingEnabled(true);
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
//            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec((1 << 30) - 1, MeasureSpec.AT_MOST);
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
                isFiling = false;
                if (!scroller.isFinished()) {
                    abortAnimatedScroll();
                }

                lastY = (int) event.getY();
                //  开始嵌套滑动了
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int offsetY = (int) (lastY - event.getY() + prevScrollY);
                int deltaY = (int) (lastY - event.getY());
                // 这里需要过滤一下 滑动临界值
                if (!isBeginMove && Math.abs(deltaY) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                    isBeginMove = true;
                }

                if (isBeginMove) {
                    // 这个是已经滑动的距离
                    final int oldY = getScrollY();
                    // 这个是可以滑动的范围
                    final int range = getScrollRange();

                    // 在child滑动之前，先将事件交给parent去滑动
                    if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset, ViewCompat.TYPE_TOUCH)) {
                        // 减去parent已经消耗的部分
                        deltaY -= mScrollConsumed[1];
                    }

                    // 每次滑动 都对lastY进行更新
                    lastY = (int) (event.getY() - mScrollOffset[1]);

                    /*---------------------------------*/
                    // child去消费滑动
                    // 这个是这次手指移动应该滑动的距离
                    int newScrollY = oldY + deltaY;
                    // 如果是在嵌套滑动组件中，这里需要设置不能over滑动，需要限制一下滑动范围
                    boolean clampedY = false;
                    if (true || hasNestedScrollingParent(ViewCompat.TYPE_TOUCH)) {
                        final int left = 0;
                        final int right = 0;
                        final int top = 0;
                        final int bottom = range;

                        if (newScrollY > bottom) {
                            newScrollY = bottom;
                            clampedY = true;
                        } else if (newScrollY < top) {
                            newScrollY = top;
                            clampedY = true;
                        }
                    }

                    /*if (clampedY && !hasNestedScrollingParent(ViewCompat.TYPE_NON_TOUCH)) {
                        scroller.springBack(0, newScrollY, 0, 0, 0, getScrollRange());
                    }*/

                    // todo 为了用户体验 这里应该加一下 划过头之后的阻尼滑动
                    scrollTo(0, newScrollY);

                    // 滚动完之后 再次交给自己的parent去滚动
                    // 计算这一次滑动 真实滑动了多少距离
                    final int scrolledDeltaY = getScrollY() - oldY;
                    // 手指滑动的距离 - 真实滑动的距离 = 剩下还有多少距离未滑动 可以交给父view去滑动
                    final int unconsumedY = deltaY - scrolledDeltaY;

                    /*---------------------------------*/
                    // 重置一下滑动的距离，方便再次使用
                    mScrollConsumed[1] = 0;
                    // 最后 再询问parent是否需要滑动
                    dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, mScrollOffset, ViewCompat.TYPE_TOUCH, mScrollConsumed);

                    // 再次更新 lastY
                    lastY -= mScrollOffset[1];
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                XLog.e("-->>MotionEvent.ACTION_UP scrollY=" + getScrollY() + " 滑动范围=" + getScrollRange());
                prevScrollY = getScrollY();

                /*final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getYVelocity();*/
                // 处理fling
                /*if ((Math.abs(initialVelocity) >= mMinimumVelocity)) {
                    LogUtils.e("-->>计算的速度=" + initialVelocity);
                    //  fling 结束 也需要处理一下回弹
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
                    if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                        LogUtils.e("-->>计算的速度=" + initialVelocity);
                        //  fling 结束 也需要处理一下回弹
                        fling(-initialVelocity);
                    } else if (scroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, getScrollRange())) {
                        // 处理回弹
                        ViewCompat.postInvalidateOnAnimation(this);
                    }
                    isFiling = true;
                }

                // 这次事件结束 进行善后处理
                isBeginMove = false;
                stopNestedScroll(ViewCompat.TYPE_TOUCH);
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

    public void springBackOverScrollOffset(int offsetX, int offsetY) {
        // 划到头了，继续划offsetY的距离后需要回弹
        if (getScrollY() < 0 - offsetY) {
            springBack(0, 0);
        } else if (getScrollY() > getScrollRange() + offsetY) {
            // 滑到尾了 继续划， 需要回弹
            springBack(0, getScrollRange());
        }
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


    /*@Override
    public void computeScroll() {
        // 先判断mScroller滚动是否完成
        *//*if (scroller.computeScrollOffset()) {
            // 这里调用View的scrollTo()完成实际的滚动
            scrollTo(0, scroller.getCurrY());
            // 必须调用该方法，否则不一定能看到滚动效果
//            invalidate();
            ViewCompat.postInvalidateOnAnimation(this);
            prevScrollY = scroller.getCurrY();
        }*//*

        // 滑动
        if (isFiling) {
            if (scroller.computeScrollOffset()) {
                // 这里调用View的scrollTo()完成实际的滚动
                scrollTo(0, scroller.getCurrY());
                // 必须调用该方法，否则不一定能看到滚动效果
//            invalidate();
                ViewCompat.postInvalidateOnAnimation(this);
                prevScrollY = scroller.getCurrY();
                if (prevScrollY <= -300 || prevScrollY >= getScrollRange() + 300) {
                    isFiling = false;
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
                isFiling = false;
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
    }*/

    @Override
    public void computeScroll() {
        if (scroller.isFinished()) {
            return;
        }
        scroller.computeScrollOffset();
        final int y = scroller.getCurrY();
        // 这一次fling的距离 - 上一次fling的距离 = 这一次需要滑动的距离
        int unconsumed = y - lastScrollerY;
        lastScrollerY = y;

        mScrollConsumed[1] = 0;
        dispatchNestedPreScroll(0, unconsumed, mScrollConsumed, null, ViewCompat.TYPE_NON_TOUCH);
        unconsumed -= mScrollConsumed[1];

        final int range = getScrollRange();


        /*---------------------------------*/
        final int oldScrollY = getScrollY();
        // child去消费滑动
        // 这个是这次手指移动应该滑动的距离
        int newScrollY = oldScrollY + unconsumed;
        // 如果是在嵌套滑动组件中，这里需要设置不能over滑动，需要限制一下滑动范围
        if (true || hasNestedScrollingParent(ViewCompat.TYPE_NON_TOUCH)) {
            final int left = 0;
            final int right = 0;
            final int top = 0;
            final int bottom = range;

            if (newScrollY > bottom) {
                newScrollY = bottom;
            } else if (newScrollY < top) {
                newScrollY = top;
            }
        }

        // todo 为了用户体验 这里应该加一下 划过头之后的阻尼滑动
        scrollTo(0, newScrollY);

        // 滚动完之后 再次交给自己的parent去滚动
        // 计算这一次滑动 真实滑动了多少距离
        final int scrolledDeltaY = getScrollY() - oldScrollY;
        // 手指滑动的距离 - 真实滑动的距离 = 剩下还有多少距离未滑动 可以交给父view去滑动
        unconsumed = unconsumed - scrolledDeltaY;

        mScrollConsumed[1] = 0;
        dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumed, mScrollOffset,
                ViewCompat.TYPE_NON_TOUCH, mScrollConsumed);
        unconsumed -= mScrollConsumed[1];


        if (!scroller.isFinished()) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            abortAnimatedScroll();
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
            runAnimatedScroll(true);
        }
    }

    /**
     * @param participateInNestedScrolling 是否参与嵌套滚动
     */
    private void runAnimatedScroll(boolean participateInNestedScrolling) {
        if (participateInNestedScrolling) {
            // 注意 这里保存的是 fling事件相关信息
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH);
        } else {
            stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
        }
        lastScrollerY = getScrollY();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void abortAnimatedScroll() {
        scroller.abortAnimation();
        stopNestedScroll(ViewCompat.TYPE_NON_TOUCH);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return true;
    }


    /*--------------------------------------------------------------当CustomScrollView作为嵌套滑动的parent时------------------------------------------------------------------------------*/

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        // 只支持竖向滑动
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        // 接受嵌套滑动时，这里记录下信息
        mParentHelper.onNestedScrollAccepted(child, target, axes, type);
        // 如果此view可以当做嵌套滑动的child 这里需要开启嵌套滑动
//        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        onNestedScrollInternal(dyUnconsumed, type, null);
    }

    /**
     * 注意 fling的时候 也会走到这个方法里
     *
     * @param target
     * @param dxConsumed
     * @param dyConsumed
     * @param dxUnconsumed
     * @param dyUnconsumed
     * @param type
     * @param consumed
     */
    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        onNestedScrollInternal(dyUnconsumed, type, consumed);
    }

    /**
     * dispatchNestedPreScroll(0, unconsumed, mScrollConsumed, null,ViewCompat.TYPE_NON_TOUCH); 也会将fling事件分发到这里
     *
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     * @param type
     */
    //    int dx = mLastMotionY - y;
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        // 如果滑过头了，需要父view先处理滑动 此时child 不能进行滑动了
        if (getScrollY() < 0) {
            onNestedScrollInternal(dy, type, consumed);
        } else if (getScrollY() > getScrollRange()) {
            onNestedScrollInternal(dy, type, consumed);
        }

    }

    /**
     * 在child fling 之前 父view先进行滑动
     * CustomScrollView的逻辑就是 如果已经过度滑动了，那么就交给CustomScrollView进行处理
     *
     * @param target
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
//        if (getScrollY() < 0 || getScrollY() > getScrollRange()) {
//            return true;
//        }
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
//        return super.onNestedFling(target, velocityX, velocityY, consumed);
        /*if (isNestedScrollingEnabled() && mNestedScrollingParent != null) {
            return mNestedScrollingParent.onNestedFling(this, velocityX, velocityY, consumed);
        }*/
        XLog.e("-->>onNestedFling： child已经fling完了 现在轮到parent进行fling了  scrollY=" + getScrollY());
        /*if (getScrollY() <= -300 || getScrollY() >= getScrollRange() + 300) {
            isFiling = false;
            scroller.abortAnimation();

            // 划到头了 继续划 需要回弹
            if (getScrollY() < 0) {
                springBack(0, 0);
            } else if (getScrollY() > getScrollRange()) {
                // 滑到尾了 继续划， 需要回弹
                springBack(0, getScrollRange());
            }
        }*/
        return true;
    }

    private void onNestedScrollInternal(int dyUnconsumed, int type, @Nullable int[] consumed) {
        final int oldScrollY = getScrollY();
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            XLog.e("-->>onNestedScrollInternal--" + dyUnconsumed);
            if (oldScrollY <= -300 || oldScrollY >= getScrollRange() + 300) {
                isFiling = false;
                scroller.abortAnimation();

                springBackOverScrollOffset(0, 300);
                return;
            }
        }

        // 这里需要限制一下
        final int left = 0;
        final int right = 0;
        final int top = 0;
        final int bottom = getScrollRange();
        int i = getScrollY() + dyUnconsumed;
        if (i > bottom) {
            i = bottom;
        } else if (i < top) {
            i = top;
        }

        dyUnconsumed = i - getScrollY();

        // 进行相对滑动
        scrollBy(0, dyUnconsumed);
        // 获取自己消费了多少的滑动距离
        final int myConsumed = getScrollY() - oldScrollY;

        if (consumed != null) {
            consumed[1] += myConsumed;
        }
        final int myUnconsumed = dyUnconsumed - myConsumed;
        mChildHelper.dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null, type, consumed);
    }

    /*--------------------------------------------------------------当CustomScrollView作为嵌套滑动的child时------------------------------------------------------------------------------*/

    /**
     * 作为child时，必须重写setNestedScrollingEnabled，并设置 setNestedScrollingEnabled(true)
     *
     * @param enabled
     */
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type, @NonNull int[] consumed) {
        mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return mChildHelper.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        mChildHelper.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return mChildHelper.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
        return false;
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }
}
