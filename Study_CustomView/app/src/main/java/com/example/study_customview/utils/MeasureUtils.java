package com.example.study_customview.utils;

import android.view.View;
import android.view.View.MeasureSpec;

/**
 * 如果要构造在 View 的宽高为 match_parent 时的 MeasureSpec，需要知道 parentSize，而此时因为无法知道 parentSize，所以直接放弃
 */
public class MeasureUtils {

    /**
     * 手动测量 View 的宽高,当 View 的宽高是精确值时
     *
     * @param target     需要测量的 View
     * @param widthSize  宽度
     * @param heightSize 高度
     */
    public void measureSpecificParams(View target, int widthSize, int heightSize) {
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        target.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 手动测量 View 的宽高,当 View 的宽高是 wrap_content 时
     *
     * @param target 需要测量的View
     */
    public static void measureWrapContent(View target) {
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec((1 << 30) - 1, MeasureSpec.AT_MOST);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec((1 << 30) - 1, MeasureSpec.AT_MOST);
        target.measure(widthMeasureSpec, heightMeasureSpec);
    }
}
