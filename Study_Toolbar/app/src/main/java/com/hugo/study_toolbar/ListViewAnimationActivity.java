package com.hugo.study_toolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListViewAnimationActivity extends AppCompatActivity {

    public static void go(Context context) {
        Intent intent = new Intent(context, ListViewAnimationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_animation);

        List<Drawable> drawables = new ArrayList<>();
        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));
        drawables.add(getResources().getDrawable(R.mipmap.ic_launcher));


        ListView listView = (ListView)findViewById(R.id.list);
        ListAdapter adapter = new ListAdapter(this,listView,drawables,300);
        listView.setAdapter(adapter);
    }
}