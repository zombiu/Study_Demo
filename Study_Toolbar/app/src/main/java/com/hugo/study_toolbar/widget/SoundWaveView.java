package com.hugo.study_toolbar.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.hugo.study_toolbar.R;

public class SoundWaveView extends FrameLayout {
    private ImageView iv_control;
    private TextView tv_duration;
    private ImageView iv_close;
    private ImageView iv_sound_wave;

    // 音频持续时间
    private int duration;
    private float progress;

    private ObjectAnimator waveAnimator;
    private Drawable clipDrawable;
    private AnimatorListenerAdapter animatorListenerAdapter;

    public SoundWaveView(@NonNull Context context) {
        this(context, null);
    }

    public SoundWaveView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoundWaveView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.sound_wave_view, this);
        iv_control = findViewById(R.id.iv_control);
        tv_duration = findViewById(R.id.tv_duration);
        iv_close = findViewById(R.id.iv_close);
        iv_sound_wave = findViewById(R.id.iv_sound_wave);

        LayerDrawable layerDrawable = (LayerDrawable) iv_sound_wave.getDrawable();
        clipDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress);

        waveAnimator = ObjectAnimator.ofFloat(this, "progress", 1);
        waveAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                LogUtils.e("-->>", "onAnimationStart");
                if (animatorListenerAdapter != null) {
                    animatorListenerAdapter.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtils.e("-->>", "onAnimationEnd " + animation.isStarted());
                iv_control.setImageResource(R.drawable.ic_pause_black);
                close();
                if (animatorListenerAdapter != null) {
                    animatorListenerAdapter.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationPause(Animator animation) {
                LogUtils.e("-->>", "onAnimationPause");
                if (animatorListenerAdapter != null) {
                    animatorListenerAdapter.onAnimationPause(animation);
                }
            }

            @Override
            public void onAnimationResume(Animator animation) {
                LogUtils.e("-->>", "onAnimationResume");
                if (animatorListenerAdapter != null) {
                    animatorListenerAdapter.onAnimationResume(animation);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                LogUtils.e("-->>", "onAnimationCancel");
            }
        });

        iv_control.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // isPaused方法除非在调用pause方法是返回true，其余情况都返回false
                if (waveAnimator.isPaused()) {
                    waveAnimator.resume();
                    iv_control.setImageResource(R.drawable.ic_playing_black);
                } else if (waveAnimator.isStarted()) {
                    // isRunning在延时播放动画上面跟isStarted有区别，如果延时播放动画，在延时时段isRunning将会返回false，其余情况跟isStarted返回值相同
                    waveAnimator.pause();
                    iv_control.setImageResource(R.drawable.ic_pause_black);
                } else {
                    start();
                    iv_control.setImageResource(R.drawable.ic_playing_black);
                }
            }
        });

        iv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                iv_control.setImageResource(R.drawable.ic_pause_black);
            }
        });
    }

    public void setDuration(int duration) {
        close();
        this.duration = duration;
        tv_duration.setText(duration + "″");
    }

    public void setProgress(float progress) {
        this.progress = progress;
        // 对动画进度进行调整
        clipDrawable.setLevel((int) (10000 * progress));

    }

    private void start() {
        if (waveAnimator.isStarted()) {
            return;
        }
        waveAnimator.setDuration(duration * 1000);
        waveAnimator.start();
        LogUtils.e("-->>", "active=" + waveAnimator.isStarted());
    }

    public void close() {
        waveAnimator.cancel();
        clipDrawable.setLevel(0);
        LogUtils.e("-->>", "active=" + waveAnimator.isStarted());
    }

    public void setAnimatorListenerAdapter(AnimatorListenerAdapter animatorListenerAdapter) {
        this.animatorListenerAdapter = animatorListenerAdapter;
    }
}
