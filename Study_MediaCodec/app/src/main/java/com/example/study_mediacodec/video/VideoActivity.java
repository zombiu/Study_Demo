package com.example.study_mediacodec.video;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.study_mediacodec.R;

import java.io.IOException;
import java.nio.ByteBuffer;

public class VideoActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final String TAG = "-->>";
    private MediaExtractor extractor;
    private boolean isEOS;
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initView();
        createMediaExtractor();
        displayDecoders();
    }

    private void initView() {
        SurfaceView surface_view = findViewById(R.id.surface_view);
        surface_view.getHolder().addCallback(this);
    }

    private void createMediaExtractor() {
        extractor = new MediaExtractor();
        try {
            String path = getExternalFilesDir("") + "/yazi.mp4";
            extractor.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dumpFormat(extractor);
    }

    private void dumpFormat(MediaExtractor extractor) {
        int count = extractor.getTrackCount();
        Log.i(TAG, "playVideo: track count: " + count);
        for (int i = 0; i < count; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            Log.i(TAG, "playVideo: track " + i + ":" + getTrackInfo(format));
        }
    }

    private String getTrackInfo(MediaFormat format) {
        String info = format.getString(MediaFormat.KEY_MIME);
        if (info.startsWith("audio/")) {
            info += " samplerate: " + format.getInteger(MediaFormat.KEY_SAMPLE_RATE)
                    + ", channel count:" + format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        } else if (info.startsWith("video/")) {
            info += " size:" + format.getInteger(MediaFormat.KEY_WIDTH) + "x" + format.getInteger(MediaFormat.KEY_HEIGHT);
        }
        return info;
    }

    private void displayDecoders() {
        MediaCodecList list = new MediaCodecList(MediaCodecList.REGULAR_CODECS);//REGULAR_CODECS参考api说明
        MediaCodecInfo[] codecs = list.getCodecInfos();
        for (MediaCodecInfo codec : codecs) {
            if (codec.isEncoder())
                continue;
            Log.i(TAG, "displayDecoders: " + codec.getName());
        }
    }

    private MediaFormat chooseVideoTrack(MediaExtractor extractor) {
        int count = extractor.getTrackCount();
        for (int i = 0; i < count; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            if (format.getString(MediaFormat.KEY_MIME).startsWith("video/")) {
                extractor.selectTrack(i);//选择轨道
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaFormat selTrackFmt = chooseVideoTrack(extractor);
                try {
                    MediaCodec codec = createCodec(selTrackFmt, surfaceHolder.getSurface());
                    codec.start();
                    while (!isEOS) {
                        int inputBufferId = codec.dequeueInputBuffer(0);
                        if (inputBufferId >= 0) {
                            ByteBuffer buffer = codec.getInputBuffer(inputBufferId);
                            int sampleSize = extractor.readSampleData(buffer, 0);
                            if (sampleSize < 0) {
                                codec.queueInputBuffer(inputBufferId, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                                isEOS = true;
                            } else {
                                long sampleTime = extractor.getSampleTime();
                                codec.queueInputBuffer(inputBufferId, 0, sampleSize, sampleTime, 0);
                                extractor.advance();
                            }
                        }

                        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                        int outIndex = codec.dequeueOutputBuffer(bufferInfo, 10000);
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
                            default:
                                codec.releaseOutputBuffer(outIndex, true);
                                break;
                        }

                        if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            Log.i(TAG, "OutputBuffer BUFFER_FLAG_END_OF_STREAM");
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}