package com.hugo.study_recyclerview.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.hugo.study_recyclerview.toReactiveStream
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    val keywordLiveData = MutableLiveData<String>()

    val searchResultLiveData = MutableLiveData<String>()

    lateinit var disposable: Disposable

    init {
        disposable = keywordLiveData.toReactiveStream()
            // 在filter中返回true表示发射该元素，返回false表示过滤该数据。
            .filter {
                if (it.isNullOrBlank()) {
                    //为空时，清空搜索结果
                }
                true
            }
            // 去抖动
            .debounce(1000, TimeUnit.MILLISECONDS)
            /*.debounce {
                //连续的输入，时间间隔小于300ms，将不会触发搜索操作
                Flowable.timer(1000, TimeUnit.MILLISECONDS)
            }*/
            .filter {
                it.isNotBlank()
            }
            // 去掉数据源重复的数据,直到数据不跟上一个数据重复 例如 1, 1, 2, 1, 2, 3, 3, 4 打印：1 2 1 2 3 4
            .distinctUntilChanged()
            .switchMap {
                //调用接口进行搜索
                Flowable.just(it)
            }
            .doOnNext {
                //处理搜索接口，并可根据接口返回，控制间隔时间
                /*if (timeOut != it.call_interval) {
                    timeOut = it.call_interval
                }*/
                LogUtils.e("-->>最终结果$it<<  ${it == null}")
            }
//            .autoDisposable(this)
            .subscribe()
    }

    fun search(keyword: String) {

    }

    fun query() {

    }

    override fun onCleared() {
        LogUtils.e("-->>嘿嘿")
        disposable.dispose()
        super.onCleared()
    }
}