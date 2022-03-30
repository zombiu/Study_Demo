package com.example.study_mediacodec.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 */
public class AudioDecoder2 implements Runnable {
    private final String TAG = "-->>" + this.getClass().getName();

    private static String VIDEO = "video/";
    private static String AUDIO = "audio/";

    private MediaExtractor extractor;
    private static MediaCodec decoder;
    private MediaFormat mediaFormat;
    private boolean isEOS;
    private static long TIME_OUT_US = 1000L;

    public void init(String path) {
        extractor = new MediaExtractor();
        try {
            extractor.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        mediaFormat = chooseVideoTrack(extractor, AUDIO);
        if (mediaFormat == null) {
            return;
        }
        LogUtils.e("-->>" + GsonUtils.toJson(mediaFormat));
        initAudioParams(mediaFormat);
        try {
            decoder = createCodec(mediaFormat, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  {"mMap":{"max-bitrate":64000,"sample-rate":44100,"track-id":1,"file-format":"audio/aac","mime":"audio/mp4a-latm","profile":2,"language":"","aac-profile":2,"bitrate":64000,"durationUs":3575726,"aac-format-adif":0,"is-adts":1,"channel-count":2,"max-input-size":8192,"csd-0":{"hb":[18,16],"isReadOnly":false,"offset":0,"_elementSizeShift":0,"address":0,"capacity":2,"limit":2,"position":0}}}
    private void initAudioParams(MediaFormat mediaFormat) {
        mediaFormat.setString(MediaFormat.KEY_MIME, "audio/mp4a-latm");
        mediaFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
        mediaFormat.setInteger(MediaFormat.KEY_SAMPLE_RATE, 0);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 96000);
        mediaFormat.setInteger(MediaFormat.KEY_IS_ADTS, 1);
        mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, 0);
    }

    @Override
    public void run() {
        decoder.start();
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int sampleRate = mediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        int channelCount = mediaFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
//        int CHANNEL_TYPE = AudioFormat.CHANNEL_IN_MONO;
//        if (channelCount != 1) {
//            CHANNEL_TYPE = AudioFormat.CHANNEL_OUT_STEREO;
//        }
        //  声道数，分为单声道（AudioFormat.CHANNEL_IN_MONO）和立体声（AudioFormat.CHANNEL_STEREO）
        int bufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioTrack audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
        Log.e("-->>", "查询音频mediaFormat=" + GsonUtils.toJson(mediaFormat));
        int state = audioTrack.getState();
        Log.e("-->>", "查询音频渲染器状态=" + state);
        audioTrack.play();

        long startTime = 0l;
        boolean first = false;
        while (!isEOS) {
            if (!first) {
                first = true;
                startTime = System.currentTimeMillis();
            }

            int inputBufferId = decoder.dequeueInputBuffer(0);
            if (inputBufferId >= 0) {
                ByteBuffer buffer = decoder.getInputBuffer(inputBufferId);
                // 清除缓冲区，否则将得到与最后一个缓冲区相同的缓冲区
                buffer.clear();
                // 把指定通道中的数据按偏移量读取到ByteBuffer中
                int sampleSize = extractor.readSampleData(buffer, 0);
                if (sampleSize < 0) {
                    decoder.queueInputBuffer(inputBufferId, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    isEOS = true;
                } else {
                    long sampleTime = extractor.getSampleTime();
                    decoder.queueInputBuffer(inputBufferId, 0, sampleSize, sampleTime, 0);
                    extractor.advance();
                }
            }

            int outIndex = decoder.dequeueOutputBuffer(bufferInfo, TIME_OUT_US);
            Log.i(TAG, "run: outIndex=" + outIndex);
            switch (outIndex) {
                case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                    Log.i(TAG, "run: new format: " + decoder.getOutputFormat());
                    break;
                case MediaCodec.INFO_TRY_AGAIN_LATER:
                    Log.i(TAG, "run: try later");
                    break;
                case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                    Log.i(TAG, "run: output buffer changed");
                    break;
                default: {
                    ByteBuffer outputBuffer = decoder.getOutputBuffer(outIndex);
                    // 这里可以使用  short array  一个音频采样如果是16bit的话正好就是一个short的大小
                    byte[] chunk = new byte[bufferInfo.size];
                    // 将解码后的音频数据 拷贝到 chunk中
                    outputBuffer.get(chunk);

                    // PTS全称：Presentation Time Stamp(以微秒为单位)。用于标示解码后的视频帧什么时候被显示出来。 在没有B帧的情况下，DTS和PTS的输出顺序是一样的，一旦存在B帧，PTS和DTS则会不同。
                    // 如果解码过快，这里控制一下播放速度，跟pts保持一致
                    long sleepTime = bufferInfo.presentationTimeUs / 1000 - (System.currentTimeMillis() - startTime);
                    if (sleepTime > 0) {
                        // 特别奇怪 如果这里休眠 质量会有点问题
                        /*try {
                            Log.e("-->>", "audio 休眠时间 =" + sleepTime);
                            Thread.sleep(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    }
                    audioTrack.write(chunk, bufferInfo.offset, bufferInfo.offset + bufferInfo.size);
                    decoder.releaseOutputBuffer(outIndex, true);
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

    private MediaFormat chooseVideoTrack(MediaExtractor extractor, String mimePrefix) {
        int count = extractor.getTrackCount();
        for (int i = 0; i < count; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            LogUtils.e(GsonUtils.toJson(format));
            if (format.getString(MediaFormat.KEY_MIME).startsWith(mimePrefix)) {
                extractor.selectTrack(i);//选择轨道
                return format;
            }
        }
        return null;
    }

    private MediaCodec createCodec(MediaFormat format, Surface surface) throws IOException {
        // 用于 MediaCodecList(int)仅枚举适用于常规（缓冲区到缓冲区）解码或编码的编解码器。
//        MediaCodecList codecList = new MediaCodecList(MediaCodecList.REGULAR_CODECS);
        String mime = format.getString(MediaFormat.KEY_MIME);
        LogUtils.e(mime);
        // 创建Decode解码器
        MediaCodec codec = MediaCodec.createDecoderByType(mime);
        codec.configure(format, surface, null, 0);
        return codec;
    }
}
