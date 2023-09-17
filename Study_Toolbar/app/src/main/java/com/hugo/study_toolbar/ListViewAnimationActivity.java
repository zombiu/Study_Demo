package com.hugo.study_toolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class ListViewAnimationActivity extends AppCompatActivity {
    List<ItemEntity> itemEntities = new ArrayList<>();
    private ListAdapter adapter;

    public static void go(Context context) {
        Intent intent = new Intent(context, ListViewAnimationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_animation);

        for (int i = 0; i < 30; i++) {
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.id = i;
            itemEntities.add(itemEntity);
        }
//        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
//        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
//        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
//        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
//        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
//        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
//        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
//        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
//        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));


        ListView listView = (ListView) findViewById(R.id.list);
        adapter = new ListAdapter(this, listView, itemEntities, itemEntities.size());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.e("移除的item=" + position);
                deleteCell(view, position);
            }
        });
    }

    private void deleteCell(View v, int position) {
//        itemEntities.remove(position);
//        LogUtils.e("size=" + itemEntities.size());
//        adapter.notifyDataSetChanged();
        ViewPropertyAnimator animate = v.animate();
        animate.alpha(0)
                .setDuration(300);
        animate.start();
        animate.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                v.setAlpha(1f);
//                itemEntities.remove(position);
//                adapter.setNewData(itemEntities);
//                adapter.notifyDataSetChanged();

            }
        });
        // 将position后面的 item一起做动画
    }
}