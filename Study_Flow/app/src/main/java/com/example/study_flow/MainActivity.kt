package com.example.study_flow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.study_flow.databinding.ActivityMainBinding
import com.example.study_flow.utils.observeIn
import com.example.study_flow.viewmodel.RoomViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

/**
 * 使用纯Flow作为LiveData替代品的主要问题是：

1.Flow是无状态的（并且不能通过.value访问）。
2.Flow是声明性的,一个Flow Builder仅描述Flow是什么，并且仅在收集时才具体化。 并且将为每个收集器有效地实例化新的Flow，这意味着将为每个收集器冗余且重复地运行上游昂贵的数据库访问。
3.Flow本身对Android生命周期一无所知，并且不会在Android生命周期状态发生变化时自动暂停和恢复收集器

作者：RicardoMJiang
链接：https://juejin.cn/post/6906465243564933128
来源：掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    //需要activity-ktx的支持
    private val roomViewModel: RoomViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*lifecycleScope.launchWhenStarted {
            // 末端操作符都是suspend函数，所以需要运行在协程作用域中。
            roomViewModel.getFlow2().collect {
                Log.e("-->>","接收到的数据1 ${it}")
            }
            // onEach：在上游每次emit前调用
            roomViewModel.getFlow2().onEach {
                Log.e("-->>observeIn","接收到的数据 ${it}")
            }.observeIn(this@MainActivity)
        }*/

        lifecycleScope.launchWhenStarted {
            roomViewModel.getFlow3().collect {
                Log.e("-->>","接收到的数据2 ${it}")
            }
            /*roomViewModel.getFlow3().onEach {
                Log.e("-->>observeIn","接收到的数据 ${it}")
            }.observeIn(this@MainActivity)*/

            // lifecycle-runtime-ktx 2.4.0以上才有 repeatOnLifecycle函数
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 处于STARTED状态时会开始收集流，并且在RESUMED状态时保持收集，最终在Fragment进入STOPPED状态时结束收集过程
                roomViewModel.getFlow3().collect {
                    Log.e("-->>","接收到的数据3 ${it}")
                }
            }
        }






        /*lifecycleScope.launchWhenStarted {
            roomViewModel.getFlow2().onEach {
                Log.e("-->>observeIn","接收到的数据 ${it}")
            }.collect()
        }*/


        binding.tv1.setOnClickListener {
//            GlobalScope.cancel()
            startActivity(Intent(this,CancelCoroutineActivity::class.java))
        }
    }
}