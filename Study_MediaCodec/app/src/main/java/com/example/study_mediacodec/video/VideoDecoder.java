package com.example.study_mediacodec.video;

import android.media.MediaCodec;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;

public class VideoDecoder implements Runnable {
    private static final String TAG = "-->>" + VideoDecoder.class.getName();

    private MediaExtractor extractor;
    private MediaCodec codec;
    private boolean isEOS;
    private Surface surface;
    private static long TIME_OUT_US = 1000L;
    // 自己维护的 播放速度的 时间戳
    private long curPlayTimestamp = -1;
    private long curSimpleTimestamp = 0;


    public void init(Surface surface, String videoPath) {
        this.surface = surface;
        extractor = new MediaExtractor();
        try {
            extractor.setDataSource(videoPath);
            MediaFormat mediaFormat = chooseVideoTrack(extractor);
            codec = createCodec(mediaFormat, surface);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        codec.start();
        long startTime = 0l;
        boolean first = false;
        while (!isEOS) {
            // 初始化 时间戳
            if (curPlayTimestamp == -1L) {
                curPlayTimestamp = System.currentTimeMillis();
            }

            int inputBufferId = codec.dequeueInputBuffer(0);
            if (inputBufferId >= 0) {
                ByteBuffer buffer = codec.getInputBuffer(inputBufferId);
                int sampleSize = extractor.readSampleData(buffer, 0);
                if (sampleSize < 0) {
                    //如果数据已经取完，压入数据结束标志：MediaCodec.BUFFER_FLAG_END_OF_STREAM
                    codec.queueInputBuffer(inputBufferId, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    isEOS = true;
                } else {
                    long sampleTime = extractor.getSampleTime();
                    Log.e("-->>", "从提取器获取到的时间戳=" + sampleTime);
                    // 保存当前数据帧的 时间戳
                    curSimpleTimestamp = sampleTime;
                    // 通知提取下一帧数据
                    extractor.advance();
                    codec.queueInputBuffer(inputBufferId, 0, sampleSize, sampleTime, 0);
                }
            }

            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outIndex = codec.dequeueOutputBuffer(bufferInfo, TIME_OUT_US);
            Log.i(TAG, "run: outIndex=" + outIndex);
            switch (outIndex) {
                case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                    Log.i(TAG, "run: new format: " + codec.getOutputFormat());
                    break;
                case MediaCodec.INFO_TRY_AGAIN_LATER:
                    Log.i(TAG, "run: try later");
                    break;
                case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                    Log.i(TAG, "run: output buffer changed");
                    break;
                default: {
                    if (!first) {
                        first = true;
                        startTime = System.currentTimeMillis();
                    }
                    Log.e("-->>", "从MediaCodec.BufferInfo获取到的时间戳=" + bufferInfo.presentationTimeUs);
                    // PTS全称：Presentation Time Stamp(以微秒为单位)。用于标示解码后的视频帧什么时候被显示出来。 在没有B帧的情况下，DTS和PTS的输出顺序是一样的，一旦存在B帧，PTS和DTS则会不同。
                    // 如果解码过快，这里控制一下播放速度，跟pts保持一致
                    long sleepTime = bufferInfo.presentationTimeUs / 1000 - (System.currentTimeMillis() - startTime);
                    if (sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    codec.releaseOutputBuffer(outIndex, true);
                    break;
                }
            }

            if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                isEOS = true;
                Log.i(TAG, "OutputBuffer BUFFER_FLAG_END_OF_STREAM");
                break;
            }
        }
    }

    private MediaFormat chooseVideoTrack(MediaExtractor extractor) {
        int count = extractor.getTrackCount();
        for (int i = 0; i < count; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            if (format.getString(MediaFormat.KEY_MIME).startsWith("video/")) {
                //选择轨道
                extractor.selectTrack(i);
                return format;
            }
        }
        return null;
    }

    private MediaCodec createCodec(MediaFormat format, Surface surface) throws IOException {
        MediaCodecList codecList = new MediaCodecList(MediaCodecList.REGULAR_CODECS);
        MediaCodec codec = MediaCodec.createByCodecName(codecList.findDecoderForFormat(format));
        codec.configure(format, surface, null, 0);
        return codec;
    }
}
