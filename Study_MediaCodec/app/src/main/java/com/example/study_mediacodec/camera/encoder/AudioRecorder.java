package com.example.study_mediacodec.camera.encoder;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AudioRecorder implements Runnable, IDataProvider {

    public final static int DEFAULT_INPUT = MediaRecorder.AudioSource.MIC;
    public final static int DEFAULT_SAMPLE_RATE_IN_HZ = 44_100;
    public final static int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    public final static int DEFAULT_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private int MAX_BUFFER_SIZE = 8192;

    // 未编码的pcm数据
    private BlockingQueue<byte[]> mDataQueue = new LinkedBlockingQueue<>();

    private AudioRecord audioRecord;

//    private volatile boolean stop;

    private volatile boolean isRunning;

    public AudioRecorder() {

    }

    @SuppressLint("MissingPermission")
    public void init() {
        int minBufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLE_RATE_IN_HZ, DEFAULT_CHANNEL_CONFIG, DEFAULT_ENCODING);
        LogUtils.e("-->>", "最小缓冲区=" + minBufferSize);
        minBufferSize = Math.min(minBufferSize, 2048);
        audioRecord = new AudioRecord(DEFAULT_INPUT, DEFAULT_SAMPLE_RATE_IN_HZ, DEFAULT_CHANNEL_CONFIG, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);
    }

    @Override
    public void run() {
        isRunning = true;
        LogUtils.e("-->>开始录制 startRecording");
        audioRecord.startRecording();
        byte[] buffer = new byte[2048];
        while (isRunning) {
            // 将音频数据写入 audioRecord
            int len = audioRecord.read(buffer, 0, buffer.length);
            Log.e("-->>", "录制的音频长度=" + len);
            if (len > 0) {
                byte[] data = new byte[len];
                System.arraycopy(buffer, 0, data, 0, len);
                // 交给MediaCodec转码为aac文件
                queueData(data);
            }
        }

        audioRecord.stop();
        audioRecord.release();
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public BlockingQueue<byte[]> getDataQueue() {
        return mDataQueue;
    }

    @Override
    public void queueData(byte[] data) {
        try {
            mDataQueue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] dequeueData() {
        try {
            return mDataQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
