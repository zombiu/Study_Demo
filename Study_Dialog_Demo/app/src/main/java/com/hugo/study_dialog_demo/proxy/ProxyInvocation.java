package com.hugo.study_dialog_demo.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

class ProxyInvocation implements InvocationHandler {
    Object proxyService;
    private Class cType;
    private Set<ObjectHolder> observers = new HashSet<>();


    public ProxyInvocation(Class interfaceType) {
        this.cType = interfaceType;
        this.proxyService = Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        for (ObjectHolder objectHolder : observers) {
            if (cType.isInstance(objectHolder.getObserver())) {
                try {
                    Method observerMethod = objectHolder.getMethod(method);
                    Object invoke = observerMethod.invoke(objectHolder.getObserver(), args);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public synchronized void addObserver(Object observer) {
        ObjectHolder objectHolder = new ObjectHolder(observer);
        observers.add(objectHolder);
    }
}
