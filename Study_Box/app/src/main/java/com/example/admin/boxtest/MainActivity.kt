package com.example.admin.boxtest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.example.admin.boxtest.adapter.ModMyAppAdapter
import com.example.admin.boxtest.adapter.TopMultipleItemRvAdapter
import com.example.admin.boxtest.entity.NormalMultipleEntity
import com.example.admin.boxtest.layoutmanager.*
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.fragment_mod_mygame.*
import kotlinx.android.synthetic.main.fragment_rank.*
import kotlinx.android.synthetic.main.fragment_recommend.*
import kotlinx.android.synthetic.main.grid_layout.view.*
import kotlinx.android.synthetic.main.web_new_item.*

class MainActivity : AppCompatActivity(), PagingScrollHelper.onPageChangeListener {
    var scrollHelper = PagingScrollHelper()
    lateinit var adapter: ModMyAppAdapter
    lateinit var horiAdapter: TopMultipleItemRvAdapter
    override fun onPageChange(index: Int) {

    }

    var tagList = mutableListOf("无限道具", "无敌", "秒杀")
    val datas = NormalMultipleEntity.getNormalMultipleEntities()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_mod_mygame)
        setContentView(R.layout.fragment_recommend)
//        ScreenAdapter.setCustomDensity(this,this.application)
//        setContentView(R.layout.fragment_rank)
        // Example of a call to a native method
//        ScreenAdapter.setCustomDensity(this,this.application)

        initRecyclerView()

//        initView()

//        initMultipleRecyclerView()

//        initPage()

//        initHorizontalRecyclerView()
    }

    private fun initHorizontalRecyclerView() {
        //游戏推荐
        rv_recommend_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_recommend_list.adapter = ModMyAppAdapter(R.layout.mod_recycle_item_game_recommend, NormalMultipleEntity.getNormalMultipleEntities())
    }

    private fun initPage() {

        var horizontalPageLayoutManager = HorizontalPageLayoutManager(3, 4)

        rv_game_list.layoutManager = horizontalPageLayoutManager
//        rv_game_list.addItemDecoration(PagingItemDecoration(this,horizontalPageLayoutManager))
        adapter = ModMyAppAdapter(R.layout.mod_recycle_item_add_game, NormalMultipleEntity.getNormalMultipleEntities())

        //设置拖拽相关
        var itemDragAndSwipeCallback = ItemDragAndSwipeCallback(adapter)
        var itemTouchHelper = ItemTouchHelper(itemDragAndSwipeCallback)
        itemTouchHelper.attachToRecyclerView(rv_game_list)

        /*adapter.enableDragItem(itemTouchHelper)
        adapter.setOnItemDragListener(object : OnItemDragListener{
            override fun onItemDragMoving(source: RecyclerView.ViewHolder?, from: Int, target: RecyclerView.ViewHolder?, to: Int) {
            }

            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
            }

        })*/

        //设置adapter
        rv_game_list.adapter = adapter
        scrollHelper.setUpRecycleView(rv_game_list)
        scrollHelper.setOnPageChangeListener(this)
        scrollHelper.updateLayoutManger()
        scrollHelper.scrollToPosition(0)
        rv_game_list.isHorizontalScrollBarEnabled = true

        //长按事件
        /*adapter.setOnItemLongClickListener { adapter, view, position ->
            Log.i("-->>","第$position 个item被长按")
            Toast.makeText(this,"第$position 个item被长按",Toast.LENGTH_SHORT).show()
            return@setOnItemLongClickListener true
        }*/

        adapter.setOnItemChildLongClickListener { adapter, view, position ->

            Log.i("-->>","第$position 个item被长按")
            Toast.makeText(this,"第$position 个item被长按",Toast.LENGTH_SHORT).show()
            view.visibility = View.GONE
            false
        }

        //点击事件
        /*adapter.setOnItemClickListener { adapter, view, position ->
            Log.i("-->>","第$position 个item被点击")
            Toast.makeText(this,"第$position 个item被点击",Toast.LENGTH_SHORT).show()
        }*/
    }

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private var xInScreen: Float = 0.toFloat()

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private var yInScreen: Float = 0.toFloat()
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                //获取手指在屏幕上的坐标
                xInScreen = event.rawX
                yInScreen = event.rawY
                Log.i("-->>","x点为$xInScreen y坐标为$yInScreen")
            }
        }
        return super.onTouchEvent(event)
    }

    fun initMultipleRecyclerView() {
        app_fragment_rank_recview.layoutManager = LinearLayoutManager(this)
        app_fragment_rank_recview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        app_fragment_rank_recview.adapter = TopMultipleItemRvAdapter(datas)
    }

    fun initRecyclerView() {
        horiAdapter = TopMultipleItemRvAdapter(datas)
        horiAdapter.setOnItemClickListener { adapter, view, position ->
            horiAdapter.notifyDataSetChanged()
            Log.i("-->>","horiAdapter 进行数据刷新 $position")
        }

        best_new.app_activity_main_recview.layoutManager = HoriGridLayoutManagerV1(this)
        best_new.app_activity_main_recview.adapter = horiAdapter

        best_hot.app_activity_main_recview.layoutManager = MyLayoutManager1()
        best_hot.app_activity_main_recview.adapter = horiAdapter
    }

    fun initView() {
        var inflater = LayoutInflater.from(this)
        flowlayout.adapter = object : TagAdapter<String>(tagList) {
            override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                var view = inflater.inflate(R.layout.item_textview, flowlayout, false) as TextView
                view.text = t
                return view
            }

        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
//            System.loadLibrary("native-lib")
        }
    }
}
