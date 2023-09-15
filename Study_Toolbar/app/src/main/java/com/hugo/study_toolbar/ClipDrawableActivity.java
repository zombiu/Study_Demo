package com.hugo.study_toolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ClipDrawableActivity extends AppCompatActivity {

    public static void go(Context context) {
        Intent intent = new Intent(context, ClipDrawableActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_drawable);

        SeekBar mSeekbar = findViewById(R.id.seekbar);
        ImageView mImageShow = findViewById(R.id.iv_show);
        TextView tv_info = findViewById(R.id.tv_info);


        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = seekBar.getMax();
                double scale = (double)progress/(double)max;
                ClipDrawable drawable = (ClipDrawable) mImageShow.getBackground();
                drawable.setLevel((int) (10000*scale));
                tv_info.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}