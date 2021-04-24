package com.hugo.study_dialog_demo.checkbox;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.hugo.study_dialog_demo.UIKit;


import java.lang.reflect.Field;

public class AndroidUtilities {
    public static float density;
    public static Point displaySize = new Point();
    public static DisplayMetrics displayMetrics = new DisplayMetrics();
    public static int statusBarHeight;
    static {
        density = Utils.getApp().getResources().getDisplayMetrics().density;
        statusBarHeight = BarUtils.getStatusBarHeight();
        WindowManager manager = (WindowManager) Utils.getApp().getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            Display display = manager.getDefaultDisplay();
            if (display != null) {
                display.getMetrics(displayMetrics);
                // 获取显示的大小，以像素为单位。
                //  用此方法返回的值不一定代表显示器的实际原始尺寸（本机分辨率）。
                display.getSize(displaySize);
            }
        }
        LogUtils.e(density + "",statusBarHeight);
    }


    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    public static void cancelRunOnUIThread(Runnable runnable) {
        UIKit.INSTANCE.getUIHandler().removeCallbacks(runnable);
    }

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            UIKit.INSTANCE.getUIHandler().post(runnable);
        } else {
            UIKit.INSTANCE.getUIHandler().postDelayed(runnable, delay);
        }
    }

    //展示软键盘
    public static boolean showKeyboard(View view) {
        if (view == null) {
            return false;
        }
        try {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            return inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
//            FileLog.e(e);
        }
        return false;
    }
    //软键盘是否弹出
    public static boolean isKeyboardShowed(View view) {
        if (view == null) {
            return false;
        }
        try {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            return inputManager.isActive(view);
        } catch (Exception e) {
//            FileLog.e(e);
        }
        return false;
    }

    //隐藏软键盘
    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!imm.isActive()) {
                return;
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
//            FileLog.e(e);
        }
    }

    public static boolean isTablet() {
        return false;
    }

    private static Field mAttachInfoField;
    private static Field mStableInsetsField;

    public static int getViewInset(View view) {
        if (view == null || Build.VERSION.SDK_INT < 21 || view.getHeight() == AndroidUtilities.displaySize.y || view.getHeight() == AndroidUtilities.displaySize.y - statusBarHeight) {
            return 0;
        }
        try {
            if (mAttachInfoField == null) {
                mAttachInfoField = View.class.getDeclaredField("mAttachInfo");
                mAttachInfoField.setAccessible(true);
            }
            Object mAttachInfo = mAttachInfoField.get(view);
            if (mAttachInfo != null) {
                if (mStableInsetsField == null) {
                    mStableInsetsField = mAttachInfo.getClass().getDeclaredField("mStableInsets");
                    mStableInsetsField.setAccessible(true);
                }
                Rect insets = (Rect) mStableInsetsField.get(mAttachInfo);
                return insets.bottom;
            }
        } catch (Exception e) {
//            FileLog.e(e);
        }
        return 0;
    }

    public static void shakeView(final View view, final float x, final int num) {
        if (view == null) {
            return;
        }
        if (num == 6) {
            view.setTranslationX(0);
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationX", AndroidUtilities.dp(x)));
        animatorSet.setDuration(50);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                shakeView(view, num == 5 ? 0 : -x, num + 1);
            }
        });
        animatorSet.start();
    }

}
