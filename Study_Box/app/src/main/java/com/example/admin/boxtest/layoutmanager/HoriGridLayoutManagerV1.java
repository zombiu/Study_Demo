package com.example.admin.boxtest.layoutmanager;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 3行横向排列的列表
 * 已实现缓存复用功能
 */
public class HoriGridLayoutManagerV1 extends RecyclerView.LayoutManager {
    //水平方向上的偏移量
    private int horizontalScrollOffset = 0;
    //总宽度
    private int totalWidth = 0;
    private Activity mActivity;
    //屏幕宽度
    private int screenWidth;

    long temp;

    //保存所有的Item的上下左右的偏移量信息
    private SparseArray<Rect> allItemFrames = new SparseArray<>();

    public HoriGridLayoutManagerV1(Activity activity) {
        mActivity = activity;
        DisplayMetrics displayMetrics = mActivity.getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        temp = System.currentTimeMillis();
        //如果没有item，直接返回
        if (getItemCount() <= 0) {
            return;
        }
        // 跳过preLayout，preLayout主要用于支持动画
        if (state.isPreLayout()) {
            return;
        }
        //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        detachAndScrapAttachedViews(recycler);
        //定义竖直方向的偏移量
        int offsetY = 0;
        int offsetX = 0;
        totalWidth = 0;
        //第一次循环，计算出每个item所在的位置，并保存起来
        for (int i = 0; i < getItemCount(); i++) {
            //这里就是从缓存里面取出
            View view = recycler.getViewForPosition(i);
            //对view进行重新布局，每一个item不能填满整个屏幕宽度
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = (int) (screenWidth * 0.93);
            //将View加入到RecyclerView中
            ////多次addView在notifyDataSetChanged刷新时，会造成重影现象
//            addView(view);
            //对子View进行测量
            measureChildWithMargins(view, 0, 0);
            //把宽高拿到，宽高都是包含ItemDecorate的尺寸
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);
            //三排就换行
            if (i % 3 == 0 && i != 0) {
                //竖着排完3个以后，x坐标要偏移一个width的宽度
                //x轴偏移
                offsetX += width;
                //y轴偏移
                offsetY = 0;
            }
            Rect frame = allItemFrames.get(i);
            if (frame == null) {
                frame = new Rect();
            }
            //          左       上           右               下
            frame.set(offsetX, offsetY, width + offsetX, height + offsetY);
            // 将当前的Item的Rect边界数据保存
            allItemFrames.put(i, frame);
            //最后，将View布局       左      上             右                       下
//            layoutDecorated(view, offsetX, offsetY, width + offsetX, height + offsetY);
            //将竖直方向偏移量增大height
            offsetY += height;
            if (i % 3 == 0) {
                //计算总的宽度
                totalWidth += width;
            }
        }

        //如果所有子View的高度和没有填满RecyclerView的高度，
        // 则将高度设置为RecyclerView的高度
        totalWidth = Math.max(totalWidth, getHorizontalSpace());
        recycleAndFillItems(recycler, state);
        Log.i("-->>", "滑动重绘 耗时为 " + (System.currentTimeMillis() - temp));
    }

    /**
     * 回收不需要的Item，并且将需要显示的Item从缓存中取出
     */
    private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 跳过preLayout，preLayout主要用于支持动画
        if (state.isPreLayout()) {
            return;
        }
        // 当前scroll offset状态下的显示区域
        // 左上右下
        Rect displayFrame = new Rect(horizontalScrollOffset, 0, horizontalScrollOffset + getHorizontalSpace(), getVerticalSpace());

        /**
         * 将滑出屏幕的Items回收到Recycle缓存中
         */
        Rect childFrame = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childFrame.left = getDecoratedLeft(child);
            childFrame.top = getDecoratedTop(child);
            childFrame.right = getDecoratedRight(child);
            childFrame.bottom = getDecoratedBottom(child);
            //如果Item没有在显示区域，就说明需要回收
            if (!Rect.intersects(displayFrame, childFrame)) {
                //回收掉滑出屏幕的View
                removeAndRecycleView(child, recycler);

            }
        }
        //第二次循环，将出现在显示区域的view绘制出来
        //重新显示需要出现在屏幕的子View
        for (int i = 0; i < getItemCount(); i++) {
            //如果item显示在当前屏幕中
            if (Rect.intersects(displayFrame, allItemFrames.get(i))) {

                View scrap = recycler.getViewForPosition(i);
                //对view进行重新布局，每一个item不能填满整个屏幕宽度
                ViewGroup.LayoutParams layoutParams = scrap.getLayoutParams();
                layoutParams.width = (int) (screenWidth * 0.93);
                measureChildWithMargins(scrap, 0, 0);
                //将View加入到RecyclerView中
                addView(scrap);

                Rect frame = allItemFrames.get(i);
                //将这个item布局出来
                layoutDecorated(scrap,
                        frame.left - horizontalScrollOffset,
                        frame.top,
                        frame.right - horizontalScrollOffset,
                        frame.bottom);

            }
        }
    }

    /**
     * 设置可以横向滑动
     *
     * @return
     */
    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //先detach掉所有的子View
        detachAndScrapAttachedViews(recycler);
        //实际要滑动的距离
        int travel = dx;
        //如果滑动到最左边
        if (horizontalScrollOffset + dx < 0) {
            travel = -horizontalScrollOffset;
            //如果滑动到最右边
        } else if (horizontalScrollOffset + dx > totalWidth - getHorizontalSpace()) {
            travel = totalWidth - getHorizontalSpace() - horizontalScrollOffset;
        }
        //将水平方向的偏移量+travel
        horizontalScrollOffset += travel;
        // 平移容器内的item
        offsetChildrenHorizontal(-travel);
        //每次移动，都进行重新绘制
        recycleAndFillItems(recycler, state);
//        Log.i("-->>", "距离最右边的偏移量 :" + horizontalScrollOffset);
        return travel;
    }

    /**
     * 获取RecyclerView在垂直方向上的可用空间，即去除了padding后的高度
     *
     * @return
     */
    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    /**
     * 获取RecyclerView在水平方向上的可用空间，即去除了padding后的宽度
     *
     * @return
     */
    private int getHorizontalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    private String getTime() {
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
