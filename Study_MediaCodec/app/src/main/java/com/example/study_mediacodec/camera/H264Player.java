package com.example.study_mediacodec.camera;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import com.blankj.utilcode.util.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

public class H264Player implements Runnable {
    private static long TIMEOUT_US = 10_000;
    // H264 格式
    public static final String VIDEO_MIME_TYPE = "video/avc";
    private MediaCodec mediaCodec;
    // 解码过程
    private boolean isProgress;

    private String path;

    public H264Player() {

    }

    public void init(Context context, String path, Surface surface, int width, int height, int fps) {
        this.path = path;
        try {
            mediaCodec = MediaCodec.createDecoderByType(VIDEO_MIME_TYPE);
            MediaFormat format = MediaFormat.createVideoFormat(VIDEO_MIME_TYPE, height, width);
            //设置帧数
            format.setInteger(MediaFormat.KEY_FRAME_RATE, fps);
            // 0 解码模式
            mediaCodec.configure(format, surface, null, 0);
        } catch (IOException e) {
            LogUtils.e("-->>" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        decodeH264();
    }

    private void decodeH264() {
        byte[] h264Bytes = getH264Bytes(path);
        if (h264Bytes == null || h264Bytes.length == 0) {
            return;
        }
        mediaCodec.start();
        isProgress = true;
        int startIndex = 0;
        int dataLength = h264Bytes.length;
        LogUtils.e("-->>数据总长度=" + dataLength);
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        long startTime = System.currentTimeMillis() * 1000;
        while (isProgress) {
            if (startIndex >= dataLength) {
                break;
            }
            //+2的目的跳过pps和sps  找到下一帧的起始坐标
            int nextFrameStart = findByFrame(h264Bytes, startIndex + 2, dataLength);
            int decodeInputIndex = mediaCodec.dequeueInputBuffer(TIMEOUT_US);
            if (decodeInputIndex >= 0) {
                ByteBuffer byteBuffer = mediaCodec.getInputBuffer(decodeInputIndex);
                byteBuffer.clear();
                // nextFrameStart= -1时也需要额外处理
                if (nextFrameStart == -1) {
                    nextFrameStart = dataLength;
                }
                int frameLength = nextFrameStart - startIndex;
                LogUtils.e("-->>当前帧长度=" + frameLength + " , startIndex=" + startIndex + " , nextFrameStart=" + nextFrameStart);
                byteBuffer.put(h264Bytes, startIndex, frameLength);
                //将数据提交给dsp芯片解码
                mediaCodec.queueInputBuffer(decodeInputIndex, 0, frameLength, 0, 0);
                startIndex = nextFrameStart;
            } else {
                LogUtils.e("-->>decodeInputIndex=" + decodeInputIndex);
                continue;
            }
            int decodeOutIndex = mediaCodec.dequeueOutputBuffer(info, TIMEOUT_US);
            Log.e("-->>", "从MediaCodec.BufferInfo获取到的时间戳=" + info.presentationTimeUs);
            if (decodeOutIndex >= 0) {
                mediaCodec.releaseOutputBuffer(decodeOutIndex, true);
            } else {
                LogUtils.e("decodeOutIndex=" + decodeOutIndex);
            }
        }
    }

    private byte[] getH264Bytes(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
            byte[] byteBuffer = new byte[1024 * 8];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            while ((len = dataInputStream.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                bos.write(byteBuffer, 0, len);
            }
            return bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 找到下一帧
     */
    private int findByFrame(byte[] bytes, int start, int totalSize) {
        for (int i = start; i < totalSize - 4; i++) {
            //0x 00 00 00 01
            if (bytes[i] == 0x00 && bytes[i + 1] == 0x00 && bytes[i + 2] == 0x00 && bytes[i + 3] == 0x01) {
                return i;
            }
        }
        return -1;
    }
}
