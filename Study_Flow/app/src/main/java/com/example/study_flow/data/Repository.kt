package com.example.study_flow.data

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

object Repository {
    //SharedFlow是一种流，它允许在多个收集器之间共享自己，因此对于所有同时收集器，只有一个流有效地运行（实现）。
    //shareIn运算符，用于将任何Flow转换为SharedFlow
    fun getFlow2() = LocalDataSource.getFlow2().shareIn(
        ProcessLifecycleOwner.get().lifecycleScope,//
        SharingStarted.WhileSubscribed(),//使Flow仅在订阅者数量从0变为1时才开始共享（实现），并在订阅者数量从1变为0时停止共享
        1)//新订阅者将在订阅时立即获得最后发出的值,这里的replay是指发送最近的多少个数据

    //如果确实需要像使用LiveData一样使用.value访问Flow的状态，则我们可以使用StateFlow，它是一种专用的受限SharedFlow
    fun getFlow3() = LocalDataSource.getFlow2().stateIn(
        ProcessLifecycleOwner.get().lifecycleScope,//
        SharingStarted.WhileSubscribed(),//使Flow仅在订阅者数量从0变为1时才开始共享（实现），并在订阅者数量从1变为0时停止共享
        1)
}