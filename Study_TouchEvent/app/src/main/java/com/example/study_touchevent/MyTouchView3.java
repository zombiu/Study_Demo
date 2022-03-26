package com.example.study_touchevent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 自定义触摸反馈的view
 * 多点触控
 */
public class MyTouchView3 extends View {
    private Paint paint = new Paint();
    private Bitmap bitmap;

    private int bitmapOffsetX;
    private int bitmapOffsetY;

    private int downX;
    private int downY;

    public MyTouchView3(Context context) {
        this(context, null);
    }

    public MyTouchView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTouchView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        int actionMasked = event.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {
                Log.e("-->>", "ACTION_DOWN " + event.getActionIndex());
                // 计算事件中心点
                int focusX = 0;
                int focusY = 0;
                for (int i = 0; i < event.getPointerCount(); i++) {
                    focusX += event.getX(i);
                    focusY += event.getY(i);
                }
                focusX = focusX / event.getPointerCount();
                focusY = focusY / event.getPointerCount();


                downX = focusX;
                downY = focusY;
                // 需要返回true 告诉父view  我要消费这个事件
                return true;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                Log.e("-->>", "ACTION_POINTER_DOWN pointer index=");
                // 更新此次事件的x y
                // 计算事件中心点
                int focusX = 0;
                int focusY = 0;
                for (int i = 0; i < event.getPointerCount(); i++) {
                    focusX += event.getX(i);
                    focusY += event.getY(i);
                }
                downX = focusX / event.getPointerCount();
                downY = focusY / event.getPointerCount();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // 重要：move事件里面 getActionIndex 为 0  这里不能使用getActionIndex去进行判断
//                Log.e("-->>", "ACTION_MOVE x=" + event.getX() + ", y=" + event.getY());
                // 计算事件中心点
                int focusX = 0;
                int focusY = 0;
                for (int i = 0; i < event.getPointerCount(); i++) {
                    focusX += event.getX(i);
                    focusY += event.getY(i);
                }
                focusX = focusX / event.getPointerCount();
                focusY = focusY / event.getPointerCount();

                // 这里计算出 整个图片的偏移量
                bitmapOffsetX += focusX - downX;
                bitmapOffsetY += focusY - downY;

                downX = focusX;
                downY = focusY;
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                Log.e("-->>", "ACTION_UP x=" + event.getX() + ", y=" + event.getY());
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                // 某些事件 可能连续触发 比如 ACTION_POINTER_UP 还需要兼容
                Log.e("-->>", "ACTION_POINTER_UP getActionIndex=" + event.getActionIndex());
                // 获取 对应索引的id
//                int pointerId = event.getPointerId(actionIndex);
//                int pointerIndex = event.findPointerIndex(pointerId);


                Log.e("-->>", "ACTION_POINTER_UP 之后处理事件的索引=");
                break;
            }
        }
        return super.onTouchEvent(event);
    }
}
