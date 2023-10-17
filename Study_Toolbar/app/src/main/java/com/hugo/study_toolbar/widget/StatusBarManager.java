package com.hugo.study_toolbar.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class StatusBarManager {
    private static final int TAG_KEY_HAVE_SET_PADDING_TOP = -101;

    /**
     * 标题栏背景图片需要沉浸式
     * 原理，首先使全透明状态栏，然后再给titlebar或者需要占用状态栏的布局设置一个状态栏高度的padding top
     *
     * @param activity
     * @param needOffsetView
     */
    public static void useStatusBar(Activity activity, View needOffsetView) {
        //设置状态栏全透明 setTransparent方法里面设置 fitsSystemWindows为true ，造成title的bg不能绘制到状态栏里面
//        StatusBarUtil.setTransparent(activity);
        transparentStatusBar(activity);
        if (needOffsetView != null) {
            Object haveSetOffset = needOffsetView.getTag(TAG_KEY_HAVE_SET_PADDING_TOP);
            if (haveSetOffset != null && (Boolean) haveSetOffset) {
                return;
            }
            //设置高度
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
            layoutParams.height = layoutParams.height + getStatusBarHeight();
            needOffsetView.setLayoutParams(layoutParams);
            //设置padding
            int paddinTop = needOffsetView.getPaddingTop() + getStatusBarHeight();
            needOffsetView.setPadding(needOffsetView.getPaddingLeft(), paddinTop, needOffsetView.getRight(), needOffsetView.getBottom());
            needOffsetView.setTag(TAG_KEY_HAVE_SET_PADDING_TOP, true);
        }
    }

    /**
     * 使状态栏完全透明
     * 如果没有效果，检查根布局是否设置了
     *     android:clipToPadding="true"
     *     android:fitsSystemWindows="true"
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加此flag，表明会Window负责系统bar的background 绘制，绘制透明背景的系统bar（状态栏和导航栏），然后用getStatusBarColor()和getNavigationBarColor()的颜色填充相应的区域
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            //4.4-5.0以下设置全透明状态栏 Android 4.4 新增了一个重要的属性 FLAG_TRANSLUCENT_STATUS
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void modifyLightStatusBar(Activity activity, boolean light) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (light) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 浅色状态栏 会改变状态栏文字颜色为深色
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }else {
                //todo
            }
        }
    }

    public static void setStatusBarColor(Activity activity, int color) {
        //因为不是所有的系统都可以设置颜色的，在4.4以下就不可以
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(activity.getResources().getColor(color));
        }
    }

    public static int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
