package com.hugo.study_dialog_demo.proxy;

public class CallBox {

    public static <T> T getService(Class<T> interfaceType) {
        return BoxCore.getInstance().getService(interfaceType);
    }


    public static void register(Class cls, Object func) {
        BoxCore.getInstance().register(cls, func);
    }

    public static void unregister(Class cls, Object func) {
        BoxCore.getInstance().unregister(cls, func);
    }
}
