package com.example.admin.boxtest.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.admin.boxtest.R;
import com.example.admin.boxtest.entity.NormalMultipleEntity;
import com.example.admin.boxtest.window.MyWindowManager;

import java.util.List;

public class ModMyAppAdapter extends BaseItemDraggableAdapter<NormalMultipleEntity,BaseViewHolder> {
    public ModMyAppAdapter(int layoutResId, @Nullable List<NormalMultipleEntity> data) {
        super(layoutResId, data);
    }

    private float xInScreen;
    private float yInScreen;
    private float xInView;
    private float yInView;
    private float mTop;
    private float mLeft;
    @Override
    protected void convert(BaseViewHolder helper, NormalMultipleEntity item) {
        LinearLayout view = helper.getView(R.id.ll_mod_rv_item);
        helper.addOnLongClickListener(R.id.ll_mod_rv_item);
        View view1 = helper.itemView.findViewById(R.id.ll_mod_rv_item);
        //为item设置监听
        view1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        xInScreen = event.getRawX();
                        yInScreen = event.getRawY();
                        xInView = event.getX();
                        yInView = event.getY();
                        mLeft = xInScreen - xInView;
                        mTop = yInScreen - yInView;
                        Log.i("-->>","x坐标为 " + xInScreen + ", y坐标为 " + yInScreen );
                        Log.i("-->>","x在view内坐标为 " + xInView + ", y在view内坐标为 " + yInView );
                        MyWindowManager.createDrawWindow(v.getContext(), ((int) mLeft), (int) mTop);
                        break;
                }
                return false;
            }
        });

        /*if (helper.getLayoutPosition() % 2 != 0) {
            view.setBackgroundResource(R.color.holo_yellow_dark);
        } else {
            View view1 = helper.getView(R.id.iv_start_game);
            view1.setBackgroundResource(R.color.blue_progress);
        }*/
    }
}
