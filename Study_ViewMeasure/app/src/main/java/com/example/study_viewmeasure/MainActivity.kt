package com.example.study_viewmeasure

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.example.study_viewmeasure.databinding.ActivityMainBinding
import com.example.study_viewmeasure.databinding.ItemMeetingNotifyBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = NotifyAdapter()

        NumUtils.convert(100000)
        NumUtils.convert(100001)
        NumUtils.convert(100011)
        NumUtils.convert(100111)
        NumUtils.convert(101111)
        NumUtils.convert(1101111)
        NumUtils.convert(105111)
        NumUtils.convert(105511)
        NumUtils.convert(105551)
    }

    fun getNames(): List<String> {
//        return arrayListOf("碧海鱼龙", "181000000", "江湖有鱼", "无妄山海", "元神")
        return arrayListOf("碧海鱼龙", "181000000", "江湖有鱼", )
    }

    inner class NotifyAdapter : RecyclerView.Adapter<NotifyHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyHolder {
            var itemBinding = ItemMeetingNotifyBinding.inflate(layoutInflater)
            return NotifyHolder(itemBinding)
        }

        override fun onBindViewHolder(holder: NotifyHolder, position: Int) {
            holder.bind(getNames())
        }

        override fun getItemCount(): Int {
            return 10
        }


    }

    class NotifyHolder(var binding: ItemMeetingNotifyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(names: List<String>) {
            var stringBuilder = StringBuilder()
            names.forEach {
                stringBuilder.append(it)
                    .append("、")
            }
            binding.members.setText(stringBuilder)
            MeasureUtils.measureWrapContent(binding.members)
            LogUtils.e("-->>手动测量 宽=" + binding.members.measuredWidth)
            binding.members.post {
                LogUtils.e("-->>自动测量 宽=" + binding.members.width)
            }
            /*if (binding.members.measuredWidth <= 0) {
                //这种是最准的
                binding.members.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                    override fun onLayoutChange(
                        v: View,
                        left: Int,
                        top: Int,
                        right: Int,
                        bottom: Int,
                        oldLeft: Int,
                        oldTop: Int,
                        oldRight: Int,
                        oldBottom: Int
                    ) {
                        Log.e(
                            "-->>",
                            "addOnLayoutChangeListener 前缀的宽度为 ${binding.tvMetaMembers!!.measuredWidth}"
                        )
                        Log.e("-->>", "addOnLayoutChangeListener 测量的宽度为 ${v!!.measuredWidth}")
                        binding.members.removeOnLayoutChangeListener(this)
                    }

                })

                binding.root.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                    override fun onLayoutChange(
                        v: View,
                        left: Int,
                        top: Int,
                        right: Int,
                        bottom: Int,
                        oldLeft: Int,
                        oldTop: Int,
                        oldRight: Int,
                        oldBottom: Int
                    ) {
                        LogUtils.e("-->>addOnLayoutChangeListener item测量的高度度为 ${v!!.measuredHeight}")
                    }

                })
            }


            measureItem1()

            measureItem2()*/
        }

        /**
         * 手动测量，有padding/margin时，会有几像素的误差
         */
        private fun measureItem1() {
            var rect = Rect()
            binding.tvMetaMembers.apply {
                paint.getTextBounds(text, 0, text.length, rect)
            }
            //    android:paddingLeft="10dp"
            //    android:paddingRight="10dp"
            //注意手动测量时，需要减去测量布局的padding\margin，可能有几像素的误差
            val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            val heightMeasureSpec: Int =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            binding.root.measure(widthMeasureSpec, heightMeasureSpec)
            LogUtils.e("-->>手动测量的宽度为 ${binding.members.measuredWidth - ConvertUtils.dp2px(10f)}")
        }

        /**
         * 手动计算
         */
        private fun measureItem2() {
            var rect = Rect()
            binding.tvMetaMembers.apply {
                paint.getTextBounds(text, 0, text.length, rect)
            }
            LogUtils.e("-->>前缀的大小为 ${rect}")
            LogUtils.e("-->>前缀的宽度为 ${rect.width()}")
            var itemWidth = ConvertUtils.dp2px(255f)
            LogUtils.e("-->>", "手动计算的宽度为 ${itemWidth - rect.width() - ConvertUtils.dp2px(6f)}")
        }


    }
}