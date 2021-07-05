package com.example.study_flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.study_flow.databinding.ActivityCancelCoroutineBinding
import com.example.study_flow.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.coroutines.suspendCoroutine

/**
 * Context 用于配置协程的属性
 *
 */
class CancelCoroutineActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var binding: ActivityCancelCoroutineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelCoroutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var launch = launch {
            // 我们可以在协程的调用树中利用协程库中提供的 yield 、 suspendCancellableCoroutine 等函数，在异步任务的「间隙」中自动插入对协程状态的检查，并通过异常机制回退整个协程的调用栈，实现取消协程更加方便
            // suspendCancellableCoroutine 就是专门用来将回调函数转换成协程的作用域
            // 使用 suspendCancellableCoroutine 和 suspendCoroutine 可以将回调函数转换为协程
            suspendCancellableCoroutine<Unit> { cont ->
                cont.invokeOnCancellation {
                    Log.e("-->>", "被取消 ${it?.message}")
                }
                log("suspendCancellableCoroutine 结束")
                // 很神奇，这里可以控制父协程是否继续执行
                cont.resume(Unit,null)
            }
            log("suspendCancellableCoroutine 结束之后")
            delay(2000)
            Log.e("-->>", "结束")
        }

        // CoroutineContext.Key都是同名的伴生对象 比如 CoroutineName("测试")的 key就是 CoroutineName伴生对象
        // 用来获取元素的 CoroutineName 其实是 CoroutineName 这个类的伴生对象（companion object
        lifecycleScope.launch(CoroutineName("测试")) {
            var coroutineName = coroutineContext[CoroutineName]
            log(" $coroutineName")
            suspendCoroutine<Unit> {
            }
            // 从上下文中获取 job
            var job = coroutineContext[Job]

            ensureActive()
        }

        binding.tv1.setOnClickListener {
            launch.cancel()
            Log.e("-->>", "点击取消")
        }


        lifecycleScope.launch {
            coroutineScope {
                log("开始delay")
                delay(3000)
                log("结束delay")
            }
            test2()
            log("launch结束")
        }
    }

    private fun log(msg: String = "") {
        Log.e("-->>", msg)
    }

    /**
     * 自定义耗时挂起函数，必须要使其内部支持取消
     * 除了根协程，不要使用reture去取消协程
     */
    suspend fun test1() {
        for (i in 0 .. 10) {
            ensureActive()
            // 下面这些写法也可以
            // if (!isActive) throw CancellationException()
            // yield()
            delay(2000)
        }

        /*withContext(Dispatchers.Default) {

        }
        coroutineScope {

        }

        launch {  }

        async {

        }*/
    }

    /**
     * 定义在 CoroutineScope 上的扩展函数提供了这样的约定（Convention）：这个函数会立即返回，但是函数会开启异步任务，可以理解为这个函数内的子程序和调用方的的代码并发执行。
     * suspend 函数提供的约定：调用这个函数不会阻塞线程，函数内的子程序执行完毕以后函数才会返回，控制流回到调用方。suspend 函数不应该有开启异步任务的副作用。
     */
    suspend fun test2() = withContext(Dispatchers.IO) {
        delay(3000)
        log("test2结束")
    }
}