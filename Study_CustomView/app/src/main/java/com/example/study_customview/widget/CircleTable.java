package com.example.study_customview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;

public class CircleTable extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path circlePath = new Path();
    private float radius = ConvertUtils.dp2px(100);
    private float notch = 90;
    private float startAngle = 90 + notch / 2;
    private float sweepAngle = 360 - 90;
    private float pathLength;
    private PathDashPathEffect pathEffect;
    private static float dash_width = ConvertUtils.dp2px(2);
    private static float dash_height = ConvertUtils.dp2px(7);
    private Path dash = new Path();
    private int markCount = 20;

    private int mark = 5;
    private float pointLength = ConvertUtils.dp2px(80);


    private PathMeasure pathMeasure = new PathMeasure();

    public CircleTable(Context context) {
        this(context, null);
    }

    public CircleTable(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleTable(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float left = getWidth() / 2 - radius;
        float top = getHeight() / 2 - radius;
        float right = getWidth() / 2 + radius;
        float bottom = getHeight() / 2 + radius;
        circlePath.addArc(left, top, right, bottom, startAngle, sweepAngle);
        pathMeasure.setPath(circlePath, false);
        pathLength = pathMeasure.getLength();

        dash.reset();
        // 这里从开始到结束 画笔画过的方向 ccw 不影响最终效果
        dash.addRect(0, 0, dash_width, dash_height, Path.Direction.CCW);
        // 注意 这里偏移量和间隔 是反的 ，float advance, float phase
        pathEffect = new PathDashPathEffect(dash, (pathLength - dash_width) / markCount, 0, PathDashPathEffect.Style.MORPH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStrokeWidth(ConvertUtils.dp2px(5));
        canvas.drawPath(circlePath, paint);
        paint.setPathEffect(pathEffect);
        canvas.drawPath(circlePath, paint);
        paint.setPathEffect(null);

        paint.setStrokeWidth(ConvertUtils.dp2px(2));
        // 接下来画指针
        float markAngle = startAngle + (mark / markCount) * sweepAngle;
        float endX = (float) Math.cos(Math.toRadians(markAngle));
        float endY = (float) Math.sin(Math.toRadians(markAngle));
        canvas.drawLine(getWidth() / 2, getHeight() / 2, endX, endY, paint);
    }
}
