package com.example.study_letterindexlist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.study_letterindexlist.R;


public class LetterIndexView extends View {

    private OnTouchingLetterChangedListener listener;

    private String[] letters = new String[]{
            "↑", "☆", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
    };

    private Paint mPaint;

    private float offset;

    private boolean hit;

    private int normalColor;

    private int touchColor;

    private Drawable hintDrawable;

//    private int stringArrayId = R.array.letter_list;

    private int charHeight;
    private int charMargin;
    //一个字母占用的宽高
    private int mCellWidth;
    private float mCellHeight;
    //用于记录当前触摸的索引值
    private int mTouchIndex = 0;

    private boolean isClearSelected = false;

    public LetterIndexView(Context paramContext) {
        this(paramContext, null);
    }

    public LetterIndexView(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public LetterIndexView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        this.mPaint = new Paint();
        this.offset = 0.0F;
        this.hit = false;
        this.normalColor = Color.GRAY;
        this.touchColor = Color.WHITE;

//        hintDrawable = paramContext.getResources().getDrawable(R.drawable.contact_letter_view_hit_point);
//        hintDrawable.setBounds(0, 0, hintDrawable.getIntrinsicWidth(), hintDrawable.getIntrinsicHeight());

        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setFakeBoldText(true);
        mPaint.setColor(normalColor);
        mPaint.setTextSize(ConvertUtils.dp2px(11));
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        charHeight = (int) Math.floor(fontMetrics.descent - fontMetrics.ascent);
        charMargin = ConvertUtils.dp2px(2);

//        letters = paramContext.getResources().getStringArray(stringArrayId);
        //单个字母高度固定 15dp
        charHeight = ConvertUtils.dp2px(15);
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.listener = onTouchingLetterChangedListener;
    }

    public void setLetters(String[] letters) {
        this.letters = letters;
    }

    public void setNormalColor(int color) {
        this.normalColor = color;
        mPaint.setColor(normalColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = -1;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //   y值/每个单元格的高度 = 当前单元格的索引
                index = (int) (event.getY() / mCellHeight);
                if (index >= 0 && index < letters.length) {
                    //优化 如果索引没有变化，就不触发invalidate
                    if (index == mTouchIndex) {
                        if (listener != null) {
                            listener.onHit(letters[index]);
                        }
                        return true;
                    }
                    mTouchIndex = index;
                    if (listener != null) {
                        listener.onHit(letters[index]);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                onCancel();
                break;
            default:
                break;
        }
        invalidate();//重新调用onDraw方法实现选中的字母更改颜色
        return true;
    }

    /*public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            hit = true;
            setBackgroundColor(getResources().getColor(R.color.contact_letter_idx_bg));
            mPaint.setColor(touchColor);
            onHit(event.getY());
            break;
        case MotionEvent.ACTION_MOVE:
            onHit(event.getY());
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            onCancel();
            break;
        }
        invalidate();
        return true;
    }*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量自身高度
        int mLettersHeight = (int) (letters.length * charHeight);
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), mLettersHeight);
    }

    protected void onDraw(Canvas canvas) {
        /*super.onDraw(canvas);
        float startY = 0;
        //计算所有字母索引占用的高度
        float lettersLength = letters.length * charHeight + (letters.length - 1) * charMargin;
        //绘制字母的起始y坐标
        if (lettersLength <= getHeight()) {
            startY = (getHeight() - lettersLength) / 2;
        }
    
        float letterPosY;
        float halfWidth = getWidth() / 2;
        for (int i = 0; i < letters.length; ++i) {
            letterPosY = charHeight * (i+1) + charMargin*i;
            canvas.drawText(letters[i], halfWidth, startY + letterPosY, mPaint);
        }
        if (hit) {
            int halfDrawWidth = hintDrawable.getIntrinsicWidth() / 2;
            float translateX = halfWidth - halfDrawWidth;
            float halfDrawHeight = hintDrawable.getIntrinsicHeight() / 2;
            float translateY = offset - halfDrawHeight;
            canvas.save();
            canvas.translate(translateX, translateY);
            hintDrawable.draw(canvas);
            canvas.restore();
        }*/

        for (int i = 0; i < letters.length; i++) {
            String text = letters[i];
            //计算坐标
            //x坐标为单元格宽度的一半 减去 文字宽度的一半
            int x = (int) (mCellWidth / 2.0f - mPaint.measureText(text) / 2.0f);

            Rect bounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            //文本的高度
            int textHeight = bounds.height();

            //y坐标为单元格高度的一半 + 文字高度的一半 + 上面有多少个单元格的高度(index * mCellHeight)
            int y = (int) (mCellHeight / 2.0f + textHeight / 2.0f + i * mCellHeight);
            //被选择到的字母 绘制圆
            if (mTouchIndex == i) {
                //圆心坐标
                float centerX = mCellWidth * 0.5f;
                float centerY = mTouchIndex * mCellHeight + mCellHeight * 0.5f;
                //半径
                float radius = bounds.height() * 0.5f + ConvertUtils.dp2px(3);

                if(isClearSelected){
                    //重置字母颜色
                    mPaint.setColor(getResources().getColor(android.R.color.black));
                    //绘制文本A-Z，此处的x，y坐标是字母左上方的坐标
                    canvas.drawText(text, x, y, mPaint);
                }else{
                    //绘制蓝色的圆
                    mPaint.setColor(getResources().getColor(R.color.color_3275EE));
                    canvas.drawCircle(centerX, centerY, radius, mPaint);
                    //绘制白色的字母
                    mPaint.setColor(getResources().getColor(R.color.white));
                    canvas.drawText(text, x, y, mPaint);
                }
            } else {
                //重置字母颜色
                mPaint.setColor(getResources().getColor(android.R.color.black));
                //绘制文本A-Z，此处的x，y坐标是字母左上方的坐标
                canvas.drawText(text, x, y, mPaint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //获取单元格的宽度和高度
        mCellWidth = getMeasuredWidth();
        mCellHeight = getMeasuredHeight() * 1.0f / letters.length;
    }

    private void onHit(float offset) {
        this.offset = offset;
        if (hit && listener != null) {
            float lettersLength = letters.length * charHeight + (letters.length - 1) * charMargin;
            float startX = 0;
            if (lettersLength <= getHeight()) {
                startX = (getHeight() - lettersLength) / 2;
            }
            if (offset < startX || offset > lettersLength + startX) {
                return;
            }

            int index = (int) ((offset - startX) / lettersLength * letters.length);
            index = Math.max(index, 0);
            index = Math.min(index, letters.length - 1);
            String str = letters[index];
            listener.onHit(str);
        }
    }

    private void onCancel() {
        hit = false;
        setBackgroundColor(Color.TRANSPARENT);
        mPaint.setColor(this.normalColor);
        refreshDrawableState();

        if (listener != null) {
            listener.onCancel();
        }
    }

    public void updateLetterIndex(String letter){
        for (int i = 0; i < letters.length; i++) {
            if (TextUtils.equals(letter,letters[i])){
                mTouchIndex = i;
                invalidate();
                break;
            }
        }
    }

    public void clearSelected(boolean isClear){
        isClearSelected = isClear;
        invalidate();
    }

    public interface OnTouchingLetterChangedListener {
        void onHit(String letter);

        void onCancel();
    }

}
