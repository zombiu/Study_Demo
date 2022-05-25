package com.example.study_customview.xfermode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.study_customview.R;

public class CutView extends View {
    private int width = ConvertUtils.dp2px(150);
    private int height = ConvertUtils.dp2px(150);

    private Paint paint = new Paint();

    private Bitmap circleBitmap;
    private Bitmap squareBitmap;

    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);


    public CutView(Context context) {
        this(context, null);
    }

    public CutView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {

        circleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        paint.setColor(getResources().getColor(R.color.purple_200));
        canvas.drawCircle(ConvertUtils.dp2px(100), ConvertUtils.dp2px(50), ConvertUtils.dp2px(50), paint);

        paint.setColor(getResources().getColor(R.color.teal_700));
        squareBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas squareCanvas = new Canvas(squareBitmap);
        squareCanvas.drawRect(0, ConvertUtils.dp2px(50), ConvertUtils.dp2px(100), ConvertUtils.dp2px(150), paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int count = canvas.saveLayer(0, 0, width, height, null);
        canvas.drawBitmap(circleBitmap, 0, 0, paint);

        paint.setXfermode(xfermode);
        canvas.drawBitmap(squareBitmap, 0, 0, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(count);
    }
}
