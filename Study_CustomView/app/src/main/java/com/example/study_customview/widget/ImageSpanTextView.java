package com.example.study_customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.study_customview.R;

public class ImageSpanTextView extends View {
    private String content = "昨日，南京市公安局官方微博“平安南京”发布公告称，钱宝实际控制人张小雷因涉嫌违法犯罪于26日向当地警方投案自首。消息一出，迅速引发大量关注。钱宝长期以来都是一个极具争议的互联网金融平台，其年化高达70%的利息使得很多人质疑其安全性，但也引来了大量贪图高息的投资客。虽然唱衰钱宝的声音不断，但是钱宝不可思议地安全运营了足足五年时间，只是最终始终还是没能撑过2017年。目前已有成千上万人血本无归，再现了一次e租宝的惨剧。";
    private TextPaint textPaint = new TextPaint();
    private Paint paint = new Paint();
    private Paint.FontMetrics fontMetrics;

    private int width = ConvertUtils.dp2px(100);
    private int height = ConvertUtils.dp2px(100);

    private Bitmap squareBitmap;

    public ImageSpanTextView(Context context) {
        this(context, null);
    }

    public ImageSpanTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageSpanTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());
        paint.setTextSize(size);
        fontMetrics = paint.getFontMetrics();

        textPaint.setTextSize(size);

        paint.setColor(getResources().getColor(R.color.teal_700));
        squareBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas squareCanvas = new Canvas(squareBitmap);
        squareCanvas.drawRect(0,0, ConvertUtils.dp2px(100), ConvertUtils.dp2px(100), paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(squareBitmap, getWidth() - ConvertUtils.dp2px(100), ConvertUtils.dp2px(80), paint);
        paint.setColor(Color.parseColor("#000000"));
        float[] measuredTextWidth = new float[1];
        int startIndex = 0;
        int count = 0;
        int width = 0;
        int verticalOffset = (int) -paint.getFontMetrics().top;
        while (startIndex < content.length()) {
            /*if (verticalOffset < ConvertUtils.dp2px(50) || verticalOffset + ConvertUtils.dp2px(100) > ConvertUtils.dp2px(50) + ConvertUtils.dp2px(100)) {
                width = getWidth();
            } else {
                width = getWidth() - ConvertUtils.dp2px(100);
            }*/
            count = textPaint.breakText(content, startIndex, content.length(), true, width, measuredTextWidth);
            LogUtils.e("-->>" + count + " content=" + content.subSequence(0, count));
            canvas.drawText(content, startIndex, startIndex + count, 0, verticalOffset, paint);
            startIndex += count;
            verticalOffset += paint.getFontSpacing();
            LogUtils.e("-->>" + GsonUtils.toJson(measuredTextWidth));
        }
    }
}
