package com.hugo.study_dialog_demo.proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class BoxCore {

    private Map<Class<?>, ProxyInvocation> proxyInvocationMap = new ConcurrentHashMap<>();

    private static final class Holder {
        private static final BoxCore BOX_CORE = new BoxCore();
    }

    public static BoxCore getInstance() {
        return Holder.BOX_CORE;
    }


    public <T> T getService(Class<T> interfaceType) {
        // todo 只支持单接口 不支持多重继承的接口？
        ProxyInvocation proxyInvocation = proxyInvocationMap.get(interfaceType);
        if (!interfaceType.isInterface()) {
            throw new IllegalArgumentException(String.format("interfaceType must be a interface , " + "%s is not a interface", interfaceType.getName()));
        }
        if (proxyInvocation == null) {
            proxyInvocation = new ProxyInvocation(interfaceType);
            proxyInvocationMap.put(interfaceType, proxyInvocation);
        }
        return (T) proxyInvocation.proxyService;
    }

    /*public synchronized void register(Object func) {
        if (func == null) {
            return;
        }
        if (!func.getClass().isInterface()) {
            throw new IllegalArgumentException(String.format("interfaceType must be a interface , " + "%s is not a interface", func.getClass().getName()));
        }

    }*/

    public synchronized void register(Class cls, Object func) {
        if (cls == null && func == null) {
            return;
        }
        Class<?> funcClass = func.getClass();
        if (!cls.isInstance(func)) {
            throw new IllegalArgumentException(String.format("func must be %s type , " + "but func type is %s", cls.getName(), funcClass.getName()));
        }
        ProxyInvocation proxyInvocation = proxyInvocationMap.get(cls);
        if (proxyInvocation == null) {
            proxyInvocation = new ProxyInvocation(cls);
            proxyInvocationMap.put(cls, proxyInvocation);
        }
        proxyInvocation.addObserver(func);
    }

    public synchronized void unregister(Class cls, Object func) {
        if (cls == null && func == null) {
            return;
        }
        Class<?> funcClass = func.getClass();
        if (!cls.isInstance(func)) {
            throw new IllegalArgumentException(String.format("func must be %s type , " + "but func type is %s", cls.getName(), funcClass.getName()));
        }
        ProxyInvocation proxyInvocation = proxyInvocationMap.get(cls);
        if (proxyInvocation == null) {
            return;
        }
        proxyInvocation.addObserver(func);
    }
}
