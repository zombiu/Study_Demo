package com.example.study_notification;

import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ToastBox {

    public static void show(String content) {
        Toast toast = Toast.makeText(UIDelegate.getInstance().getApplication(), content, Toast.LENGTH_SHORT);
        showSystemToast(toast);
    }

    /**
     * 显示系统Toast
     */
    private static void showSystemToast(Toast toast){
        try{
            Method getServiceMethod = Toast.class.getDeclaredMethod("getService");
            getServiceMethod.setAccessible(true);

            final Object iNotificationManager = getServiceMethod.invoke(null);
            Class iNotificationManagerCls = Class.forName("android.app.INotificationManager");
            Object iNotificationManagerProxy = Proxy.newProxyInstance(toast.getClass().getClassLoader(), new Class[]{iNotificationManagerCls}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    // 强制使用系统Toast
                    // 华为p20 pro上为enqueueToastEx
                    if("enqueueToast".equals(method.getName())
                            || "enqueueToastEx".equals(method.getName())){
                        args[0] = "android";
                    }
                    return method.invoke(iNotificationManager, args);
                }
            });
            Field sServiceFiled = Toast.class.getDeclaredField("sService");
            sServiceFiled.setAccessible(true);
            sServiceFiled.set(null, iNotificationManagerProxy);
            toast.show();

        }catch (Exception e){
            e.printStackTrace();
            Log.e("-->>异常",e.getMessage());
        }
    }
}
