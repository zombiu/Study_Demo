package com.hugo.study_toolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class ListViewAnimationActivity extends AppCompatActivity {
    List<ItemEntity> itemEntities = new ArrayList<>();
    private ListAdapter adapter;
    private ListView listView;

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


        listView = (ListView) findViewById(R.id.list);
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

    private void deleteCell(View v, final int position) {
//        itemEntities.remove(position);
//        LogUtils.e("size=" + itemEntities.size());
//        adapter.notifyDataSetChanged();
        /*ViewPropertyAnimator animate = v.animate();
        animate.alpha(0)
                .setDuration(300);
        animate.start();
        animate.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.setAlpha(1f);
                itemEntities.remove(position);
                adapter.setNewData(itemEntities);
                adapter.notifyDataSetChanged();
            }
        });*/
        // 将position后面的 item一起做动画

        AnimatorSet animatorSet = getDeleteAnimation(position);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                remove(position);
                // 动画结束后，恢复ListView所有子View的属性
                for (int i=0; i<listView.getChildCount() ;++i){
                    View v = listView.getChildAt(i);
                    v.setAlpha(1f);
                    v.setTranslationY(0);
                    v.setTranslationX(0);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private AnimatorSet getDeleteAnimation(int position) {
        // 存储所有的Animator，利用AnimatorSet直接播放
        ArrayList<Animator> animators = new ArrayList<Animator>();
        //获取显示的一个view的position
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        View deleteView = listView.getChildAt(position - firstVisiblePosition);
        int viewHeight = deleteView.getHeight();
        int viewWidth = deleteView.getWidth();
        //平移动画
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(deleteView, "translationX", viewWidth);
        translationXAnimator.setDuration(500);
        animators.add(translationXAnimator);

        //透明动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(deleteView, "alpha", 1, 0);
        alphaAnimator.setDuration(500);
        animators.add(alphaAnimator);

        int delay = 500;
        int firstViewToMove = position + 1;
        for (int i = firstViewToMove; i < listView.getChildCount(); ++i) {
            View viewToMove = listView.getChildAt(i);
            ObjectAnimator moveAnimator = ObjectAnimator.ofFloat(viewToMove, "translationY", 0, -viewHeight);
            moveAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            moveAnimator.setStartDelay(delay);

            delay += 100;

            animators.add(moveAnimator);
        }
        //动画集合
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.playTogether(animators);

        return animationSet;

    }

    private void remove(int position){
        if (position < itemEntities.size()){
            itemEntities.remove(position);
        }
        adapter.notifyDataSetChanged();
    }
}