package com.example.study_customview.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

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
    private float factor = 1.5f;
    private Bitmap bitmap;
    private Paint paint = new Paint();
    // 各种手势的侦测器
    private GestureDetector gestureDetector = new GestureDetector(getContext(), new ScaleGestureListener());

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
        super(context, attrs, 0);
    }

    public ScaleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        // 需要制定bitmap 宽高
//        bitmap = ImageUtils.getBitmap(R.drawable.ic_avatar);
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

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

}
