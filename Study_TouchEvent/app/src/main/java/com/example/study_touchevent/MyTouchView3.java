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

    private int actionIndex;

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
                Log.e("-->>", "ACTION_DOWN");
                downX = (int) event.getX();
                downY = (int) event.getY();
                // 需要返回true 告诉父view  我要消费这个事件
                return true;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                // 保存多点触控场景下，此事件的手指 索引
                actionIndex = event.getActionIndex();
                Log.e("-->>", "pointer index=" + actionIndex);
                // 更新此次事件的x y
                downX = (int) event.getX(actionIndex);
                downY = (int) event.getY(actionIndex);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // 重要：move事件里面 getActionIndex 为 0  这里不能使用getActionIndex去进行判断
//                Log.e("-->>", "ACTION_MOVE x=" + event.getX() + ", y=" + event.getY());
                // 这里计算出 与上次move相比 手指移动的偏移量
                int x = (int) (event.getX(actionIndex) - downX);
                int y = (int) (event.getY(actionIndex) - downY);
                // 这里计算出 整个图片的偏移量
                bitmapOffsetX += x;
                bitmapOffsetY += y;
                // 更新此次事件的x y
                downX = (int) event.getX(actionIndex);
                downY = (int) event.getY(actionIndex);
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                Log.e("-->>", "ACTION_UP x=" + event.getX() + ", y=" + event.getY());
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                Log.e("-->>", "ACTION_UP pointerCount=" + event.getPointerCount());
                if (event.getActionIndex() == actionIndex) {
                    // 获取 对应索引的id
//                    int pointerId = event.getPointerId(actionIndex);
//                    int pointerIndex = event.findPointerIndex(pointerId);
                    // 当前处理事件的手指 抬起时，需要计算剩下手指中，应该去处理事件的手指 的索引
                    if (actionIndex == event.getPointerCount() - 1) {
                        actionIndex = actionIndex - 1;
                    } else {
                        actionIndex = actionIndex - 2;
                    }

                    // 注意 这里需要重置一下 偏移量到 新手指上
                    downX = (int) event.getX(actionIndex);
                    downY = (int) event.getY(actionIndex);
                }
                break;
            }
        }
        return super.onTouchEvent(event);
    }
}
