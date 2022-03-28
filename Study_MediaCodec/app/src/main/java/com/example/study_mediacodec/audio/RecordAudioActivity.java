package com.example.study_mediacodec.audio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.study_mediacodec.R;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecordAudioActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_record_audio;
    private Button btn_stop_record_audio;
    private Button btn_play_audio;

    private AudioRecorder audioRecorder;

    private ExecutorService executorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);

        init();
    }

    private void init() {
        btn_record_audio = findViewById(R.id.btn_record_audio);
        btn_stop_record_audio = findViewById(R.id.btn_stop_record_audio);
        btn_play_audio = findViewById(R.id.btn_play_audio);

        btn_record_audio.setOnClickListener(this::onClick);
        btn_stop_record_audio.setOnClickListener(this::onClick);
        btn_play_audio.setOnClickListener(this::onClick);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        executorService = Executors.newFixedThreadPool(2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_record_audio: {
                audioRecorder = new AudioRecorder();
                audioRecorder.init();
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        String accFile = getExternalCacheDir().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".acc";
                        audioRecorder.startEncode(accFile);
                    }
                });

                executorService.submit(audioRecorder);
                break;
            }
            case R.id.btn_stop_record_audio: {
                audioRecorder.stop();
                break;
            }
            case R.id.btn_play_audio: {

                break;
            }
        }
    }
}