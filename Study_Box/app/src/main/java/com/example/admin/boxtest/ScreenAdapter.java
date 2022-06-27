package com.example.admin.boxtest;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * 今日头条适配方案
 */
public class ScreenAdapter {
    //未适配之前的屏幕密度
    private static float sNoncompatDensity;
    //未适配之前的sp
    private static float sNoncompatscaledDensity;

    public static void setCustomDensity(@NonNull Activity activity, @NonNull final Application application) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatscaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    //配置发生改变就重新获取
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatscaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
            //宽维度来适配 360dp宽度的设计图   屏幕密度 / 360
            final float targetDensity = appDisplayMetrics.widthPixels / 360;
            final float targetscaledDensity = targetDensity * (sNoncompatscaledDensity / sNoncompatDensity);
            final int targetDensityDpi = (int) (160 * targetDensity);

            appDisplayMetrics.density = targetDensity;
            appDisplayMetrics.scaledDensity = targetscaledDensity;
            appDisplayMetrics.densityDpi = targetDensityDpi;

            final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
            activityDisplayMetrics.density = targetDensity;
            activityDisplayMetrics.scaledDensity = targetscaledDensity;
            activityDisplayMetrics.densityDpi = targetDensityDpi;

        }
    }
}
