package com.example.study_mediacodec.audio;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.os.Environment;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.example.study_mediacodec.utils.IOUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AudioRecorder implements Runnable {
    public final static int DEFAULT_INPUT = MediaRecorder.AudioSource.MIC;
    public final static int DEFAULT_SAMPLE_RATE_IN_HZ = 44_100;
    public final static int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    public final static int DEFAULT_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private int MAX_BUFFER_SIZE = 8192;


    private AudioRecord audioRecord;
    private MediaCodec encoder;

    private volatile boolean stop;

    private BlockingQueue<byte[]> mDataQueue = new ArrayBlockingQueue<>(10);

    @SuppressLint("MissingPermission")
    public void init() {
        int minBufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLE_RATE_IN_HZ, DEFAULT_CHANNEL_CONFIG, DEFAULT_ENCODING);
        LogUtils.e("-->>", "最小缓冲区=" + minBufferSize);
        minBufferSize = Math.max(minBufferSize, 2048);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, DEFAULT_SAMPLE_RATE_IN_HZ, DEFAULT_CHANNEL_CONFIG, AudioFormat.ENCODING_PCM_16BIT, minBufferSize);

        try {
            encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);

            //指定创建的MediaCodec类型 单声道
            MediaFormat format = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, DEFAULT_SAMPLE_RATE_IN_HZ, 1);
            format.setString(MediaFormat.KEY_MIME, MediaFormat.MIMETYPE_AUDIO_AAC);
            // 封装可用于编解码器组件的配置文件
            format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            //码率：声音中的比特率是指将模拟声音信号转换成数字声音信号后，单位时间内的二进制数据量，是间接衡量音频质量的一个指标
            // 比特率 也叫码率 指的是每秒传送的数据位数。常见的单位 kbps （k bits per s） 和 mbps (m bits per s) 。帧率越大，每秒传输的帧数越大；分辨率越大，每一帧的内容大小越大；因此帧率越大，分辨率越大，码率就越大。
            format.setInteger(MediaFormat.KEY_BIT_RATE, 96000);//传入的数据最大值，可以修改
            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, MAX_BUFFER_SIZE);
            // MediaCodec.CONFIGURE_FLAG_ENCODE 编码标记
            encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                startEncode("");
            }
        }).start();
        audioRecord.startRecording();
        byte[] buffer = new byte[2048];
        while (!stop) {
            // 将音频数据写入 audioRecord
            int len = audioRecord.read(buffer, 0, 2048);
            if (len > 0) {
                byte[] data = new byte[len];
                System.arraycopy(buffer, 0, data, 0, len);
                // 交给MediaCodec转码为aac文件
                queueData(data);
            }
        }
    }

    public void startEncode(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        encoder.start();
        File mFile = new File(path);
        if (mFile.exists()) {
            mFile.delete();
        }
        FileOutputStream mFileOutputStream = null;
        BufferedOutputStream mBufferedOutputStream = null;
        try {
            mFile.createNewFile();
            mFileOutputStream = new FileOutputStream(mFile);
            mBufferedOutputStream = new BufferedOutputStream(mFileOutputStream, 2048);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] pcmData;
        int inputIndex;
        ByteBuffer inputBuffer;
        ByteBuffer[] inputBuffers = encoder.getInputBuffers();

        int outputIndex;
        ByteBuffer outputBuffer;
        ByteBuffer[] outputBuffers = encoder.getOutputBuffers();

        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        byte[] aacChunk;
        while (!stop || !mDataQueue.isEmpty()) {
            // 获取录制的pcm数据
            pcmData = dequeueData();
            if (pcmData == null) {
                continue;
            }
            inputIndex = encoder.dequeueInputBuffer(10_000);
            if (inputIndex >= 0) {
                inputBuffer = inputBuffers[inputIndex];
                inputBuffer.clear();
                inputBuffer.limit(pcmData.length);
                inputBuffer.put(pcmData);
                encoder.queueInputBuffer(inputIndex, 0, pcmData.length, 0, 0);
            }

            // 获取 输出的 ByteBuffer数组索引
            outputIndex = encoder.dequeueOutputBuffer(bufferInfo, 10_000);
            while (outputIndex >= 0) {
                // 获取输出的 ByteBuffer
                outputBuffer = outputBuffers[outputIndex];
                // position：对于写入模式，表示当前可写入数据的下标，对于读取模式，表示接下来可以读取的数据的下标；
                // 这里是读取模式 从outputBuffer中读取
                outputBuffer.position(bufferInfo.offset);
                // limit：对于写入模式，表示当前可以写入的数组大小，默认为数组的最大长度，对于读取模式，表示当前最多可以读取的数据的位置下标；
                outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                aacChunk = new byte[bufferInfo.size + 7];
                // 需要为adts帧加上头部
                addADTStoPacket(44100, aacChunk, aacChunk.length);
                // 从outputBuffer中将数据读到 aacChunk中
                outputBuffer.get(aacChunk, 7, bufferInfo.size);
                try {
                    // 渲染数据
                    mBufferedOutputStream.write(aacChunk);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 释放outputBuffer
                encoder.releaseOutputBuffer(outputIndex, false);
                outputIndex = encoder.dequeueOutputBuffer(bufferInfo, 10_000);
            }
        }
        try {
            mBufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        IOUtil.close(mBufferedOutputStream);
        IOUtil.close(mFileOutputStream);
        encoder.stop();
        encoder.release();
        encoder = null;
    }

    private byte[] dequeueData() {
        if (mDataQueue.isEmpty()) {
            return null;
        }
        try {
            return mDataQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void queueData(byte[] data) {
        try {
            mDataQueue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        stop = true;
        audioRecord.stop();
        mDataQueue.clear();
    }

    private void addADTStoPacket(int sampleRateType, byte[] packet, int packetLen) {
        int profile = 2;
        int chanCfg = 2;
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (sampleRateType << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }
}
