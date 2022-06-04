package com.example.study_customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.study_customview.R;

public class CustomTouchView extends View {
    private Bitmap bitmap;
    private Paint paint = new Paint();

    private int originalOffsetX;
    private int originalOffsetY;

    private float downX;
    private float downY;

    private int pointerId;


    public CustomTouchView(Context context) {
        this(context, null);
    }

    public CustomTouchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        bitmap = getBitmap(ConvertUtils.dp2px(200));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint);
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
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                // 需要减去 上一次的偏移量
                downX = event.getX() - originalOffsetX;
                downY = event.getY() - originalOffsetY;
                // 记录需要去处理滑动事件的那个手指
                pointerId = event.getPointerId(0);
                return true;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                //  又落下了一个手指 事件需要交给最新的手指去处理 记录需要去处理滑动事件的手指
                int actionIndex = event.getActionIndex();
                pointerId = event.getPointerId(actionIndex);
                // 触摸位置 需要减去 上一次的偏移量
                downX = event.getX(actionIndex) - originalOffsetX;
                downY = event.getY(actionIndex) - originalOffsetY;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int pointerIndex = event.findPointerIndex(pointerId);
                originalOffsetX = (int) (event.getX(pointerIndex) - downX);
                originalOffsetY = (int) (event.getY(pointerIndex) - downY);

//                downX = event.getX(pointerIndex);
//                downY = event.getY(pointerIndex);
                invalidate();
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                int actionIndex = event.getActionIndex();
                int newIndex = 0;
                // 需要处理手指抬起时 手指index变动的场景 并将事件重新交给其它手指去处理
                if (event.getPointerId(actionIndex) == pointerId) {
                    int pointerIndex = event.findPointerIndex(pointerId);
                    if (pointerIndex == event.getPointerCount() - 1) {
                        newIndex = event.getPointerCount() - 2;
                        // 切换处理move事件的手指时 需要把两个触摸点直接的差值减去
                        /*float eventX = event.getX(newIndex);
                        float eventY = event.getY(newIndex);

                        downX = event.getX(actionIndex);
                        downY = event.getY(actionIndex);*/
                    } else {
                        newIndex = event.getPointerCount() - 1;
                    }

                    pointerId = event.getPointerId(newIndex);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {

                break;
            }
        }
        return false;
    }
}
