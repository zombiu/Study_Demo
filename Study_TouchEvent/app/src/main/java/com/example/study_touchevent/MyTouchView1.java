package com.example.study_touchevent;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.InputStream;

/**
 * 自定义触摸反馈的view
 * 单点触控
 */
public class MyTouchView1 extends View {
    private Paint paint = new Paint();
    private Bitmap bitmap;

    private int bitmapOffsetX;
    private int bitmapOffsetY;

    private int offsetX;
    private int offsetY;

    private int downX;
    private int downY;

    public MyTouchView1(Context context) {
        this(context, null);
    }

    public MyTouchView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTouchView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dlam);
        Log.e("-->>", "执行init" + bitmap);
//        InputStream inputStream = getResources().openRawResource(R.mipmap.ic_launcher);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmapOffsetX = (getMeasuredWidth() - bitmap.getWidth()) / 2;
        bitmapOffsetY = (getMeasuredHeight() - bitmap.getHeight()) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, bitmapOffsetX, bitmapOffsetY, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getAction();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {
                Log.e("-->>", "ACTION_DOWN");
                downX = (int) event.getX();
                downY = (int) event.getY();
                // 需要返回true 告诉父view  我要消费这个事件
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.e("-->>", "ACTION_MOVE");
                // 这里计算出 与上次move相比 手指移动的偏移量
                int x = (int) (event.getX() - downX);
                int y = (int) (event.getY() - downY);
                // 这里计算出 整个图片的偏移量
                bitmapOffsetX += x;
                bitmapOffsetY += y;
                // 更新此次事件的x y
                downX = (int) event.getX();
                downY = (int) event.getY();
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {

                break;
            }
        }
        return super.onTouchEvent(event);
    }
}
