package com.example.study_mediacodec.video;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

public class CameraToH264 {

    public ArrayBlockingQueue<byte[]> yuv420Queue = new ArrayBlockingQueue<>(10);
    private boolean isRuning;
    private byte[] input;
    private int width;
    private int height;
    private MediaCodec mediaCodec;
    private MediaMuxer mediaMuxer;
    private int mVideoTrack=-1;
    private long nanoTime;


    public void init(int width, int heigth) {
        nanoTime = System.nanoTime();
        this.width = width;
        this.height = heigth;
        MediaFormat videoFormat = MediaFormat.createVideoFormat("video/avc", width, heigth);
        videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);
        videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, width * heigth * 5);
        videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);

        try {
            mediaCodec = MediaCodec.createEncoderByType("video/avc");
            mediaCodec.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mediaCodec.start();

            mediaMuxer = new MediaMuxer("sdcard/aaapcm/camer1.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void putData(byte[] buffer) {
        if (yuv420Queue.size() >= 10) {
            yuv420Queue.poll();
        }
        yuv420Queue.add(buffer);
    }


    public void startEncoder() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isRuning = true;
                while (isRuning) {
                    if (yuv420Queue.size() > 0) {
                        input = yuv420Queue.poll();
                        byte[] yuv420sp = new byte[width * height * 3 / 2];
                        // 必须要转格式，否则录制的内容播放出来为绿屏
                        NV21ToNV12(input, yuv420sp, width, height);
                        input = yuv420sp;
                    } else {
                        input = null;
                    }

                    if (input != null) {
                        ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();
                        ByteBuffer[] outputBuffers = mediaCodec.getOutputBuffers();
                        int inputBufferIndex = mediaCodec.dequeueInputBuffer(0);
                        if (inputBufferIndex >= 0) {
                            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                            inputBuffer.clear();
                            inputBuffer.put(input);
                            mediaCodec.queueInputBuffer(inputBufferIndex, 0, input.length, (System.nanoTime() - nanoTime) / 1000, 0);
                        }

                        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                        int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);

                        if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                            mVideoTrack = mediaMuxer.addTrack(mediaCodec.getOutputFormat());
                            Log.d("mmm", "改变format");
                            if (mVideoTrack >= 0) {
                                mediaMuxer.start();
                                Log.d("mmm", "开始混合");
                            }
                        }
                        while (outputBufferIndex > 0) {
                            ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                            if (mVideoTrack >= 0) {
                                mediaMuxer.writeSampleData(mVideoTrack, outputBuffer, bufferInfo);
                                Log.d("mmm", "正在写入");
                            }

                            mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                            outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
                            if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                                Log.e("mmm", "video end");
                            }
                        }

                    }
                }

                Log.d("mmm", "停止写入");
                mediaMuxer.stop();
                mediaMuxer.release();
                mediaCodec.stop();
                mediaCodec.release();
            }
        }).start();
    }

    public void stop() {
        isRuning = false;
    }


    private void NV21ToNV12(byte[] nv21, byte[] nv12, int width, int height) {
        if (nv21 == null || nv12 == null) return;
        int framesize = width * height;
        int i = 0, j = 0;
        System.arraycopy(nv21, 0, nv12, 0, framesize);
        for (i = 0; i < framesize; i++) {
            nv12[i] = nv21[i];
        }
        for (j = 0; j < framesize / 2; j += 2) {
            nv12[framesize + j - 1] = nv21[j + framesize];
        }
        for (j = 0; j < framesize / 2; j += 2) {
            nv12[framesize + j] = nv21[j + framesize - 1];
        }
    }


}
