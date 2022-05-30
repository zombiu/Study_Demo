package com.example.study_customview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TagLayout extends ViewGroup {
    private List<Rect> rectList = new ArrayList<>();

    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 当前行已使用的宽度
        int lineWidthUse = 0;
        // 当前行最大高度
        int lineHeightUse = 0;

        int maxHeight = 0;

        int maxWidth = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

            if (widthMode != MeasureSpec.UNSPECIFIED && lineWidthUse + child.getMeasuredWidth() > widthSize) {
                //换行，重置 行高 行宽等参数   并且重新对item进行测量
                lineWidthUse = 0;
                // 换行的时候 计算一下 现在
                maxHeight += lineHeightUse;
                // 重置当前行最大高度
                lineHeightUse = 0;
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

            }

            if (i >= rectList.size()) {
                rectList.add(new Rect());
            }
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int marginHorizontal = layoutParams.getMarginStart() + layoutParams.getMarginEnd();
            int marginVertical = layoutParams.topMargin + layoutParams.bottomMargin;
            // 计算该子view的摆放位置和宽高  todo 需要处理一下margin
            rectList.get(i).set(lineWidthUse, maxHeight, lineWidthUse + child.getMeasuredWidth() + marginHorizontal,
                    maxHeight + child.getMeasuredHeight() + marginVertical);
            // 计算当前行占用的 最大宽度
            lineWidthUse += child.getMeasuredWidth() + marginHorizontal;
            // 计算当前行 占用的最大高度
            lineHeightUse = Math.max(lineHeightUse, child.getMeasuredHeight() + marginVertical);

            // 计算最大宽度，父view需要以此计算自己的宽度
            maxWidth = Math.max(maxWidth, lineWidthUse);
        }


        int selfWidth = maxWidth;
        int selfHeight = maxHeight + lineHeightUse;
        setMeasuredDimension(selfWidth, selfHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            Rect rect = rectList.get(i);
            View child = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            // 布局时 去除margin
            child.layout(rect.left + layoutParams.getMarginStart(), rect.top + layoutParams.topMargin, rect.right - layoutParams.getMarginEnd(), rect.bottom - layoutParams.bottomMargin);
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
