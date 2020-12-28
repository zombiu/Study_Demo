package com.example.study_flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.study_flow.utils.observeIn
import com.example.study_flow.viewmodel.RoomViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    //需要activity-ktx的支持
    private val roomViewModel: RoomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launchWhenStarted {
            /*roomViewModel.getFlow2().collect {
                Log.e("-->>","接收到的数据 ${it}")
            }*/
            roomViewModel.getFlow2().onEach {
                Log.e("-->>observeIn","接收到的数据 ${it}")
            }.observeIn(this@MainActivity)
        }
    }
}