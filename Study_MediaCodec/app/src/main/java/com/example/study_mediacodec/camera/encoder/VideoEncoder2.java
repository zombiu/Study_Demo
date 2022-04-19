package com.example.study_mediacodec.camera.encoder;

import static android.media.MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV422Flexible;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.text.TextUtils;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.study_mediacodec.camera.Mp4Recorder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class VideoEncoder2 implements Runnable {
    private int type = 0;
    // H264 格式
    public static final String VIDEO_MIME_TYPE = "video/avc";
    public int colorFormat = COLOR_FormatYUV422Flexible;
    private MediaCodec mediaCodec;
    private BlockingQueue<byte[]> mQueue = new LinkedBlockingQueue<>();
    private volatile boolean isRunning;
    private String h264Path;
    private File file;
    private int width;
    private int height;
    // 超时返回时间为10毫秒
    private static long TIMEOUT_US = 10_000;
    private long presentationTimeUs;
    private MediaCodec.BufferInfo bufferInfo;
    private BufferedOutputStream bufferedOutputStream = null;
    private volatile int count = 0;
    private MediaFormat videoFormat;

    private Mp4Recorder mp4Recorder;
    public int trackIndex = -1;
    private IMediaCodecListener mediaCodecListener;
//    public volatile boolean stop;

    public void setMediaCodecListener(IMediaCodecListener mediaCodecListener) {
        this.mediaCodecListener = mediaCodecListener;
    }

    public void init(int width, int height, int fps) {
        LogUtils.e("-->>初始化 " + width + " " + height + " " + fps);
        this.width = width;
        this.height = height;
        if ((width & 1) == 1) {
            width--;
        }
        if ((height & 1) == 1) {
            height--;
        }
//        LogUtils.e("-->>初始化 " + width + " " + height + " " + fps);
        MediaCodecInfo mediaCodecInfo = selectCodecInfo();
        LogUtils.e("carema支持的输出格式=" + GsonUtils.toJson(mediaCodecInfo));
        colorFormat = selectColorFormat(mediaCodecInfo);
//        colorFormat = MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar;
        // 在我的手机上 格式为21 COLOR_FormatYUV420SemiPlanar = 21;
        LogUtils.e("carema支持的输出格式=" + colorFormat);
        // 因为获取到的视频帧数据是逆时针旋转了90度的，所以这里宽高需要对调
        videoFormat = MediaFormat.createVideoFormat(VIDEO_MIME_TYPE, height, width);
        // 如果下面码率填bitRate 会在configure时崩溃 直接坛蜜oom了 为啥不能这么写呢
//        int bitRate = (width * height * 3 / 2) * 8 * fps;
        // 这里不知道为啥 码率填0也可以
        int bitRate = (width * height) * 5;
        // 没有这一行会报错 configureCodec returning error -38
        videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
        videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, fps);
        videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);
        videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);
        LogUtils.e(GsonUtils.toJson(videoFormat));

        try {
            mediaCodec = MediaCodec.createByCodecName(mediaCodecInfo.getName());
            // 配置为编码模式 CONFIGURE_FLAG_ENCODE
            mediaCodec.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMp4Recorder(Mp4Recorder mp4Recorder) {
        this.mp4Recorder = mp4Recorder;
    }

    public void start(String path) {
        this.h264Path = path;
        LogUtils.e("开始录制 地址=" + h264Path);
        try {
            run();
        } catch (Exception e) {
            LogUtils.e("-->>" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stop() {
        synchronized (this) {
            isRunning = false;
            mQueue.clear();
        }
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(h264Path)) {
            isRunning = false;
            return;
        }
        isRunning = true;
        file = new File(h264Path);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 2048);
        } catch (IOException e) {
            e.printStackTrace();
            isRunning = false;
            return;
        }

        mediaCodec.start();

//        bufferInfo = new MediaCodec.BufferInfo();
        // 保存开始时的pts
        presentationTimeUs = System.currentTimeMillis() * 1000;
        while (isRunning) {
            byte[] data = getFrameData();
//            LogUtils.e("获取nv21数据 " + data);
            if (data == null) {
                continue;
            }
            count = 0;
            encodeVideoData(data);
        }

        mediaCodec.stop();
        mediaCodec.release();
        if (mediaCodecListener != null) {
            mediaCodecListener.onStop(type);
        }
    }

    private byte[] getFrameData() {
        if (mQueue.isEmpty()) {
            return null;
        }
        try {
            return mQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void putFrameData(byte[] data) {
        if (data == null || !isRunning) {
            return;
        }
        try {
            mQueue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private MediaCodecInfo selectCodecInfo() {
//        MediaCodecList codecList = new MediaCodecList(MediaCodecList.REGULAR_CODECS);
//        codecList.findDecoderForFormat(format);
        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (!codecInfo.isEncoder()) {
                continue;
            }
            LogUtils.e("格式=" + GsonUtils.toJson(codecInfo));
            String[] types = codecInfo.getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                if (types[j].equalsIgnoreCase(VIDEO_MIME_TYPE)) {
                    return codecInfo;
                }
            }
        }
        return null;
    }

    //查询支持的输入格式
    private int selectColorFormat(MediaCodecInfo codecInfo) {
        if (codecInfo == null) {
            return -1;
        }
        MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(VIDEO_MIME_TYPE);
        int[] colorFormats = capabilities.colorFormats;
        LogUtils.e("支持的颜色格式" + GsonUtils.toJson(colorFormats));
        for (int i = 0; i < colorFormats.length; i++) {
            if (isRecognizedFormat(colorFormats[i])) {
                return colorFormats[i];
            }
        }
        return -1;
    }

    private boolean isRecognizedFormat(int colorFormat) {
        switch (colorFormat) {
            // these are the formats we know how to handle for this test
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar://对应Camera预览格式I420(YV21/YUV420P)
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar: //对应Camera预览格式NV12
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar://对应Camera预览格式NV21
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar: {////对应Camera预览格式YV12
                return true;
            }
            default:
                return false;
        }
    }

    /**
     * camera输出的数据格式 是nv21
     * MediaCodec不支持NV21的数据编码，所以需要先将NV21的数据转码为NV12
     */
    private byte[] _NV21ToNV12(byte[] nv21, int width, int height) {
        byte[] nv12 = new byte[width * height * 3 / 2];
        int frameSize = width * height;
        int i, j;
        System.arraycopy(nv21, 0, nv12, 0, frameSize);
        for (i = 0; i < frameSize; i++) {
            nv12[i] = nv21[i];
        }
        for (j = 0; j < frameSize / 2; j += 2) {
            nv12[frameSize + j - 1] = nv21[j + frameSize];
        }
        for (j = 0; j < frameSize / 2; j += 2) {
            nv12[frameSize + j] = nv21[j + frameSize - 1];
        }
        return nv12;
    }

    /**
     * 此处为顺时针旋转旋转90度
     *
     * @param data        旋转前的数据
     * @param imageWidth  旋转前数据的宽
     * @param imageHeight 旋转前数据的高
     * @return 旋转后的数据
     */
    private byte[] rotateNV290(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i--;
            }
        }
        return yuv;
    }

    private void encodeVideoData(byte[] data) {
        // todo 如果carema输出的不是nv21格式 这里需要进行兼容处理 将carema输出的格式转换为nv12
        //将NV21编码成NV12
        byte[] bytes = _NV21ToNV12(data, width, height);
        //视频顺时针旋转90度
        byte[] nv12 = rotateNV290(bytes, width, height);

        ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();
        int inputIndex = mediaCodec.dequeueInputBuffer(10_000);
        if (inputIndex >= 0) {
            ByteBuffer byteBuffer = inputBuffers[inputIndex];
            byteBuffer.clear();
            byteBuffer.put(nv12);
            long pts = System.currentTimeMillis() * 1000 - presentationTimeUs;
            mediaCodec.queueInputBuffer(inputIndex, 0, nv12.length, pts, 0);
        }

        bufferInfo = new MediaCodec.BufferInfo();
        ByteBuffer[] outputBuffers = mediaCodec.getOutputBuffers();
        int outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_US);
        if (outputIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
            outputBuffers = mediaCodec.getOutputBuffers();
        } else if (outputIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            if (mediaCodecListener != null) {
                mediaCodecListener.outputFormatChange(type, mediaCodec.getOutputFormat());
            }
        }
        while (outputIndex >= 0) {
            count++;
            LogUtils.e("第" + count + "次循环写入");
            ByteBuffer byteBuffer = outputBuffers[outputIndex];
            if (isMediaMuxer()) {
                // 很奇怪 如果要编码成h264文件的话 不能执行下面这个代码 这里判断一下 如果是写入mp4就过滤 写入h264就不过滤
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    bufferInfo.size = 0;
                    LogUtils.e("-->>过滤一下 BUFFER_FLAG_CODEC_CONFIG");
                }
            }
            if (bufferInfo.size != 0) {
                if (isMediaMuxer()) {
                    LogUtils.e("写入视频数据 " + ",写入数据的长度" + bufferInfo.size);
                    // 将视频数据写入  MediaMuxer
                    if (mediaCodecListener != null) {
                        mediaCodecListener.onEncodeFrame(type, byteBuffer, bufferInfo);
                    } else {
                        LogUtils.e("-->>丢失了数据=" + GsonUtils.toJson(bufferInfo));
                    }
                } else {
                    byte[] buffer = new byte[bufferInfo.size];
                    byteBuffer.get(buffer);
                    try {
                        bufferedOutputStream.write(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 消费完后， 释放输出的数组
            mediaCodec.releaseOutputBuffer(outputIndex, false);
            bufferInfo = new MediaCodec.BufferInfo();
            outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_US);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    private boolean isMediaMuxer() {
        return mp4Recorder != null;
    }
}
