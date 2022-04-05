package com.example.study_mediacodec.camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceHolder;

import com.blankj.utilcode.util.LogUtils;
import com.example.study_mediacodec.R;
import com.example.study_mediacodec.databinding.ActivityPlayVideoBinding;
import com.example.study_mediacodec.video.VideoDecoder;

public class PlayVideoActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private ActivityPlayVideoBinding binding;
    private String path;
    private SurfaceHolder surfaceHolder;
    private VideoDecoder videoDecoder;

    public static void start(Context context, String path) {
        Intent intent = new Intent();
        intent.putExtra("path", path);
        intent.setClass(context, PlayVideoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityPlayVideoBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        initParams();

        initView();
    }

    private void initView() {
        videoDecoder = new VideoDecoder();
        videoDecoder.init(binding.surfaceView.getHolder().getSurface(), path);

        binding.surfaceView.getHolder().addCallback(this);
    }

    private void initParams() {
        path = getIntent().getStringExtra("path");
        LogUtils.e("-->>路径=" + path);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        startDecode();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    public void startDecode() {
//        String path = getExternalFilesDir("") + "/yazi.mp4";
        VideoDecoder videoDecoder = new VideoDecoder();
        videoDecoder.init(binding.surfaceView.getHolder().getSurface(), path);
        new Thread(videoDecoder).start();
    }
}