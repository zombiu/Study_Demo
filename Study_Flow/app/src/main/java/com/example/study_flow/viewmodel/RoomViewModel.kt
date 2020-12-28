package com.example.study_flow.viewmodel

import androidx.lifecycle.*
import com.example.study_flow.data.Repository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class RoomViewModel : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val mResult: LiveData<String> = liveData {
        val string = getMessage("LiveData Ktx")
//        emit(string)
    }

    fun getMessage(message: String) {
        uiScope.launch {
            val deferred = async(Dispatchers.IO) {
                delay(2000)
                "post $message"
            }
            mMessage.value = deferred.await()
        }
    }


    val mMessage: MutableLiveData<String> = MutableLiveData()


    fun getMessageByViewModel() {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) {
//                getMessage("ViewModel Ktx")
                delay(2000)
                return@async "123"
            }
            mMessage.value = deferred.await()
        }
    }

    /**
     * 绑定生命周期
     */
    fun getMessageByLifeCycle(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            val deferred = async(Dispatchers.IO) {
                getMessage("LifeCycle Ktx")
                "456"
            }
            mMessage.value = deferred.await()
        }
    }

    fun getFlow2(): Flow<Int> {
        return Repository.getFlow2()
    }
}