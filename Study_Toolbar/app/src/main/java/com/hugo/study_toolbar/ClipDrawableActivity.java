package com.hugo.study_toolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.hugo.study_toolbar.widget.SoundWaveView;

public class ClipDrawableActivity extends AppCompatActivity {
    private ImageView iv_sound_wave;
    private SoundWaveView sound_wave_view;

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

        iv_sound_wave = findViewById(R.id.iv_sound_wave);

        sound_wave_view = findViewById(R.id.sound_wave_view);
        sound_wave_view.setDuration(10);


        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int max = seekBar.getMax();
                double scale = (double) progress / (double) max;
                ClipDrawable drawable = (ClipDrawable) mImageShow.getBackground();
                drawable.setLevel((int) (10000 * scale));
                tv_info.setText(progress + "");

                handleSoundWaveProgress(seekBar, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void handleSoundWaveProgress(SeekBar seekBar, int progress) {
        int max = seekBar.getMax();
        double scale = (double) progress / (double) max;
        LogUtils.e("-->>", iv_sound_wave.getDrawable());
        LayerDrawable layerDrawable = (LayerDrawable) iv_sound_wave.getDrawable();
        Drawable clipDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);
//        ClipDrawable drawable = (ClipDrawable) iv_sound_wave.getBackground();
        clipDrawable.setLevel((int) (10000 * scale));
    }
}