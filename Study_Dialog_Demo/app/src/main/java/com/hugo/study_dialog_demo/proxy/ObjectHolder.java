package com.hugo.study_dialog_demo.proxy;

import com.blankj.utilcode.util.LogUtils;

import java.lang.reflect.Method;

public class ObjectHolder {
    Object observer;
    Method method;

    public ObjectHolder(Object object) {
        this.observer = object;
    }

    public Object getObserver() {
        return observer;
    }

    public Method getMethod(Method originalMethod) throws NoSuchMethodException {
        if (method == null) {
            method = observer.getClass().getMethod(originalMethod.getName(), originalMethod.getParameterTypes());
            LogUtils.e("-->>", method.getName() + ((method != null) ? "创建成功" : "创建失败"));
        }
        return method;
    }
}
