package com.example.study_notification;

import android.app.AppOpsManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.shoucai.reslib.utils.UIKit;

import java.lang.reflect.Method;

import static androidx.core.app.NotificationCompat.CATEGORY_MESSAGE;
import static androidx.core.app.NotificationCompat.DEFAULT_ALL;


public class UIDelegate {

    private volatile static UIDelegate uiDelegate;
    private Application application;

    private ViewModelStore viewModelStore;
    private ViewModelProvider viewModelProvider;
    private boolean isBackground = true;

    private static final String CHANNEL_ID = "call";

    public static UIDelegate getInstance() {
        if (uiDelegate == null) {
            synchronized (UIDelegate.class) {
                if (uiDelegate == null) {
                    uiDelegate = new UIDelegate();
                }
            }
        }
        return uiDelegate;
    }

    public void init(Application application) {
        this.application = application;

        viewModelStore = new ViewModelStore();
        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        viewModelProvider = new ViewModelProvider(viewModelStore, factory);

//        LoginViewModel loginViewModel = viewModelProvider.get(LoginViewModel.class);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            public void onForeground() {
                isBackground = false;
                Log.e("-->>", "app进入前台");

            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            public void onBackground() {
                isBackground = true;
                Log.e("-->>", "app进入后台");
                UIKit.INSTANCE.getUIHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        createNotification(application);

                        Intent fullScreenIntent = new Intent(application, CallActivity.class);
                        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(application, 0,
                                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        showNotification(application, "tag", 1009, "标题", "内容", fullScreenPendingIntent);
                    }
                }, 5000);
            }
        });

    }

    public void createNotification(Application application) {
        Intent fullScreenIntent = new Intent(application, CallActivity.class);
        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(application, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(application, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Incoming call")
                        .setContentText("(919) 555-1234")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)

                        // Use a full-screen intent only for the highest-priority alerts where you
                        // have an associated activity that you would like to launch after the user
                        // interacts with the notification. Also, if your app targets Android 10
                        // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                        // order for the platform to invoke this notification.
                        .setFullScreenIntent(fullScreenPendingIntent, true);

        Notification incomingCallNotification = notificationBuilder.build();
        incomingCallNotification.notify();
    }

    /**
     * 渠道创建之后不能修改，所以通知修改了之后，需要卸载重装才能生效
     *
     * @param context
     * @param tag
     * @param id
     * @param title
     * @param content
     * @param pendingIntent
     */
    private void showNotification(Context context, String tag, int id, String title, String content, PendingIntent pendingIntent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "notification";
        //android o 需要设置渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "test message",
                    NotificationManager.IMPORTANCE_HIGH);

            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(CATEGORY_MESSAGE)
                .setDefaults(DEFAULT_ALL);
        //这句是重点 奇怪 setFullScreenIntent后，在定制版 mete9 pro上不能弹出通知横幅  就算注释了 也只有第一次能弹出
        builder.setFullScreenIntent(pendingIntent, true);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);
        builder.setContentText(content);

        notificationManager.notify(tag, id, builder.build());
    }

    /*public boolean canBackgroundStart() {
        AppOpsManager ops = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        try {
            int op = 10021; // >= 23
            Method method = ops.getClass().getMethod("checkOpNoThrow", new Class[]
                    {int.class, int.class, String.class}
            );
            Integer result = (Integer) method.invoke(ops, op, android.os.Process.myUid(), getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            Log.e("WelcomeActivity", "not support", e);
        }
        return false;
    }*/

    //miui后台启动
    public static boolean canBackgroundStart(Context context) {
        AppOpsManager ops = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            int op = 10021; // >= 23
            // ops.checkOpNoThrow(op, uid, packageName)
            Method method = ops.getClass().getMethod("checkOpNoThrow", new Class[]{int.class, int.class, String.class});
            Integer result = (Integer) method.invoke(ops, op, Process.myUid(), context.getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //miui锁屏显示
    public static boolean canShowLockView(Context context) {
        AppOpsManager ops = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            int op = 10020; // >= 23
            // ops.checkOpNoThrow(op, uid, packageName)
            Method method = ops.getClass().getMethod("checkOpNoThrow", new Class[]{int.class, int.class, String.class});
            Integer result = (Integer) method.invoke(ops, op, Process.myUid(), context.getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Application getApplication() {
        return application;
    }
}
