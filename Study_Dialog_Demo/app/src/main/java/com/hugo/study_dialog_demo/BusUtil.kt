package com.hugo.study_dialog_demo

import android.os.Message
import org.greenrobot.eventbus.EventBus

/**
 * eventbus 辅助类
 */
object BusUtil {

    /**
     * 注册
     */
    fun register(subscriber: Any) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber)
        }
    }


    /**
     * 解除注册
     */
    fun unregister(subscriber: Any) {
        EventBus.getDefault().unregister(subscriber)
    }

    /**
     * 生成一个事件
     */
    fun getEvent(int: Int, obj: Any? = null): Message {
        var message = Message.obtain()
        message.what = int
        obj?.let {
            message.obj = obj
        }
        return message
    }

    /**
     * 发送一个事件
     */
    fun post(int: Int, obj: Any? = null) {
        var event = getEvent(int, obj)
        EventBus.getDefault().post(event)
    }

    /**
     * 发送一个事件
     */
    fun post(obj: Any) {
        EventBus.getDefault().post(obj)
    }
}