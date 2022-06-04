package com.example.study_customview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.study_customview.R;

public class ScaleImageView extends View {
    private boolean big;
    private float bigScale;
    private float smallScale;
    private int originalOffsetX;
    private int originalOffsetY;
    // 滑动之后的offset
    private int scrollOffsetX;
    private int scrollOffsetY;

    private float factor = 1.5f;
    private Bitmap bitmap;
    private Paint paint = new Paint();
    // 各种手势的探测器
    private GestureDetector gestureDetector = new GestureDetector(getContext(), new ScaleGestureListener());
    // 支持惯性滑动  跟Scroller的区别  OverScroller可以划过界 OverScroller松手时会有个初始速度
    private OverScroller scroller = new OverScroller(getContext());
    // 支持多指触控的探测器
    private ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDoctor());
    private FlingRunnable flingRunnable = new FlingRunnable();

    private ObjectAnimator animator = ObjectAnimator.ofFloat(this, "scaleRatio", 0, 1);

    private float scaleRatio;

    public void setScaleRatio(float scaleRatio) {
        this.scaleRatio = scaleRatio;
        invalidate();
    }

    public ScaleImageView(Context context) {
        this(context, null);
    }

    public ScaleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                super.onAnimationEnd(animation, isReverse);
                /*LogUtils.e("-->>动画结束1");
                // 动画结束时 重置一下 滚动的偏移量
                scrollOffsetX = 0;
                scrollOffsetY = 0;*/
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtils.e("-->>动画结束");
                // 动画结束时 重置一下 滚动的偏移量
//                scrollOffsetX = 0;
//                scrollOffsetY = 0;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        // 需要制定bitmap 宽高
//        bitmap = ImageKit.getBitmap(ConvertUtils.dp2px(200));
        bitmap = getBitMap(ConvertUtils.dp2px(200));

        boolean type = w / bitmap.getWidth() > h / bitmap.getHeight();
        // 如果放大时 高的那一边，会先贴到屏幕的边
        if (type) {
            bigScale = w * 1.0F / bitmap.getWidth() * factor;
            smallScale = h * 1.0F / bitmap.getHeight();
        } else {
            // 如果放大时 宽的那一边，会先贴到屏幕的边
            bigScale = h * 1.0F / bitmap.getHeight() * factor;
            smallScale = w * 1.0F / bitmap.getWidth();
        }
        LogUtils.e("bitmap width=" + bitmap.getWidth(), "bitmap height=" + bitmap.getHeight());
        LogUtils.e("bigScale=" + bigScale, "smallScale=" + smallScale);
        originalOffsetX = (w - bitmap.getWidth()) / 2;
        originalOffsetY = (h - bitmap.getHeight()) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(scrollOffsetX * scaleRatio, scrollOffsetY * scaleRatio);
        // 以view中心点为放缩原点，放缩倍数为 smallScale倍
        float ratio = smallScale + (bigScale - smallScale) * scaleRatio;
        canvas.scale(ratio, ratio, getWidth() / 2, getHeight() / 2);
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint);
    }

    public Bitmap getBitMap(int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 很奇怪 这里传入 R.mipmap.ic_launcher 会解码失败 是因为webp的原因吗？
        BitmapFactory.decodeResource(getResources(), R.drawable.ic_avatar, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(getResources(), R.drawable.ic_avatar, options);
    }

    private class ScaleGestureListener implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            big = !big;
            if (big) {
                // 双击时，计算如果以双击的地方为缩放点的偏移量
                scrollOffsetX = (int) ((e.getX() - getWidth() / 2) * (1 - bigScale / smallScale));
                scrollOffsetY = (int) ((e.getY() - getHeight() / 2) * (1 - bigScale / smallScale));
//                resetScrollOffset();
                animator.start();
            } else {
                animator.reverse();
            }
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        /**
         * @param e1
         * @param e2
         * @param distanceX 前一位置x坐标 - 现在位置x坐标
         * @param distanceY 前一位置y坐标 - 现在位置y坐标
         * @return 除了down方法中的返回值， 其它方法中的返回值 不会影响程序的行为
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (big) {
                LogUtils.e("-->>可偏移的宽度=" + ((getWidth() - bitmap.getWidth()) / 2));
                scrollOffsetX -= distanceX;
                // 这里计算时 ，要把放大的系数算上
                scrollOffsetX = (int) Math.min(scrollOffsetX, (bitmap.getWidth() * bigScale - getWidth()) / 2);
                scrollOffsetX = (int) Math.max(scrollOffsetX, -(bitmap.getWidth() * bigScale - getWidth()) / 2);
                scrollOffsetY -= distanceY;
                scrollOffsetY = (int) Math.min(scrollOffsetY, (bitmap.getHeight() * bigScale - getHeight()) / 2);
                scrollOffsetY = (int) Math.max(scrollOffsetY, -(bitmap.getHeight() * bigScale - getHeight()) / 2);
                invalidate();
            }
            return false;
        }

        public void resetScrollOffset() {
            scrollOffsetX = (int) Math.min(scrollOffsetX, (bitmap.getWidth() * bigScale - getWidth()) / 2);
            scrollOffsetX = (int) Math.max(scrollOffsetX, -(bitmap.getWidth() * bigScale - getWidth()) / 2);
            scrollOffsetY = (int) Math.min(scrollOffsetY, (bitmap.getHeight() * bigScale - getHeight()) / 2);
            scrollOffsetY = (int) Math.max(scrollOffsetY, -(bitmap.getHeight() * bigScale - getHeight()) / 2);
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (big) {
                // 开始坐标 一个用来辅助计算的坐标 有点不好理解 一般是中心点 这里使用的是中心点， 因为最开始 我们计算滑动偏移量时，就是以中心点为参考原点
                scroller.fling(scrollOffsetX, scrollOffsetY, (int) velocityX, (int) velocityY, (int) -(bitmap.getWidth() * bigScale - getWidth()) / 2, (int) (bitmap.getWidth() * bigScale - getWidth()) / 2,
                        (int) -(bitmap.getHeight() * bigScale - getHeight()) / 2, (int) (bitmap.getHeight() * bigScale - getHeight()) / 2);
                postOnAnimation(flingRunnable);
            }
            return true;
        }
    }

    private class FlingRunnable implements Runnable {

        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                // 将快滑时 计算出的滑动到的位置 赋值给偏移坐标
                scrollOffsetX = scroller.getCurrX();
                scrollOffsetY = scroller.getCurrY();
                // 注意 这里需要进行刷新
                invalidate();
                postOnAnimation(this);
            }
        }
    }

    private class ScaleGestureDoctor implements ScaleGestureDetector.OnScaleGestureListener {


        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    }
}
