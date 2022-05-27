package com.example.study_customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.study_customview.R;

public class CameraRotateView extends View {
    private int width = ConvertUtils.dp2px(150);
    private int OFFSET = ConvertUtils.dp2px(100);
    private Camera camera = new Camera();
    private Paint paint = new Paint();

    private Bitmap bitmap;

    private int rotateAngle = 45;

    private int cameraRotate;

    public CameraRotateView(Context context) {
        this(context, null);
    }

    public CameraRotateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraRotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bitmap = getBitMap(width);
        // 旋转x轴
        camera.rotate(60, 0, 0);

        camera.setLocation(0, 0, -6 * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 对canvas进行变换时，需要从下到上反着构思变换逻辑 ，倒着写才方便理解
        canvas.save();
        canvas.translate((OFFSET + width / 2), (OFFSET + width / 2));
        canvas.rotate(-rotateAngle);
//        camera.applyToCanvas(canvas);
        // 旋转之后 裁切范围需要 扩大一部分区域
        canvas.clipRect(-width, -width,  width, 0);
        canvas.rotate(rotateAngle);
        canvas.translate(-(OFFSET + width / 2), -(OFFSET + width / 2));
        canvas.drawBitmap(bitmap, OFFSET, OFFSET, paint);
        canvas.restore();


        // 对canvas进行变换时，需要从下到上反着构思变换逻辑 ，倒着写才方便理解
        canvas.save();
        canvas.translate((OFFSET + width / 2), (OFFSET + width / 2));
        canvas.rotate(-rotateAngle);
        camera.applyToCanvas(canvas);
        canvas.clipRect(-width, 0,  width, width);
        canvas.rotate(rotateAngle);
        canvas.translate(-(OFFSET + width / 2), -(OFFSET + width / 2));
        canvas.drawBitmap(bitmap, OFFSET, OFFSET, paint);
        canvas.restore();
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

    public int getRotateAngle() {
        return rotateAngle;
    }

    public void setRotateAngle(int rotateAngle) {
        this.rotateAngle = rotateAngle;
        invalidate();
    }

    public int getCameraRotate() {
        return cameraRotate;
    }

    public void setCameraRotate(int cameraRotate) {
        this.cameraRotate = cameraRotate;
        invalidate();
    }
}
