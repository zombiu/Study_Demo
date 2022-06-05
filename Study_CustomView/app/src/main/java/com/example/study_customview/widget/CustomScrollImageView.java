package com.example.study_customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.study_customview.R;

public class CustomScrollImageView extends View {

    private Bitmap bitmap;
    private Paint paint = new Paint();

    private int originalOffsetX;
    private int originalOffsetY;

    private float downX;
    private float downY;

    // 支持惯性滑动  跟Scroller的区别  OverScroller可以划过界 OverScroller松手时会有个初始速度
    private OverScroller scroller = new OverScroller(getContext());

    private VelocityTracker mVelocityTracker;

    private FlingRunnable flingRunnable = new FlingRunnable();

    public CustomScrollImageView(Context context) {
        this(context, null);
    }

    public CustomScrollImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        bitmap = getBitmap(ConvertUtils.dp2px(100));
        mVelocityTracker = VelocityTracker.obtain();
    }

    public Bitmap getBitmap(int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 很奇怪 这里传入 R.mipmap.ic_launcher 会解码失败 是因为webp的原因吗？
        BitmapFactory.decodeResource(getResources(), R.drawable.ic_avatar, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(getResources(), R.drawable.ic_avatar, options);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // 手指按下 之后 移动的偏移
//                float moveX = event.getX() - downX;
//                int scrollX = (int) (originalOffsetX - moveX);
//                int scrollY = (int) (originalOffsetY - event.getY() + downY);
//                scrollTo(scrollX, scrollY);
                break;
            }
            case MotionEvent.ACTION_UP: {
                originalOffsetX = getScrollX();
                originalOffsetY = getScrollY();
//                mVelocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
//                // 开始坐标 一个用来辅助计算的坐标 有点不好理解 一般是中心点 这里使用的是中心点， 因为最开始 我们计算滑动偏移量时，就是以中心点为参考原点
//                scroller.fling(0, 0, (int) mVelocityTracker.getXVelocity(), (int) mVelocityTracker.getYVelocity(), 0, ConvertUtils.dp2px(500),
//                        0, ConvertUtils.dp2px(500));
//                postOnAnimation(flingRunnable);

                scroller.startScroll(-100,-100,-300,-300);
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
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
        }

    }

    private class FlingRunnable implements Runnable {

        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                scrollTo(scroller.getCurrX(), scroller.getCurrY());
                postOnAnimation(this);
            }
        }
    }
}
