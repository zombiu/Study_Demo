package com.hugo.study_recyclerview

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import io.reactivex.rxjava3.android.MainThreadDisposable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableEmitter
import io.reactivex.rxjava3.core.Scheduler

class LiveDatas {


}

fun <T> LiveData<T>.toReactiveStream(
    observerScheduler: Scheduler = AndroidSchedulers.mainThread()
): Flowable<T> = Flowable
    .create({ emitter: FlowableEmitter<T> ->
        val observer = Observer<T> { data ->
            data?.let {
                emitter.onNext(it)
            }
        }
        observeForever(observer)

        emitter.setCancellable {
            object : MainThreadDisposable() {
                override fun onDispose() {
                    LogUtils.e("-->>onDispose")
                    removeObserver(observer)
                }
            }
        }
    }, BackpressureStrategy.LATEST)
    .subscribeOn(AndroidSchedulers.mainThread())
    .observeOn(observerScheduler)