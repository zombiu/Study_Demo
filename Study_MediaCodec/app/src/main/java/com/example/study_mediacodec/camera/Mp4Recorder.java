package com.example.study_mediacodec.camera;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Environment;
import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.study_mediacodec.camera.encoder.AudioEncoder;
import com.example.study_mediacodec.camera.encoder.AudioRecorder;
import com.example.study_mediacodec.camera.encoder.IMediaCodecListener;
import com.example.study_mediacodec.camera.encoder.VideoEncoder2;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Mp4Recorder implements IMediaCodecListener, Runnable {
    private static final String TAG = "-->>Mp4Recoder";
    public static final int VIDEO_TYPE = 0;
    public static final int AUDIO_TYPE = 1;

    private MediaMuxer mediaMuxer;
    private static final SimpleDateFormat mDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
    private static final String DIR_NAME = "MP4";
    private String mp4Path;
    private BlockingQueue<FrameData> dataQueue = new LinkedBlockingQueue<FrameData>();
    private volatile boolean isRecording;
    private AudioEncoder audioEncoder;
    private VideoEncoder2 videoEncoder;
    private Context context;
    private ExecutorService executorService;
    private int trackCount = 0;
    private volatile boolean isVideoAdd;
    private volatile boolean isAudioAdd;
    private int index = 0;
    private int videoTrackIndex = -1;
    private int audioTrackIndex = -1;

    public Mp4Recorder(Context context, AudioEncoder audioEncoder, VideoEncoder2 videoEncoder) {
        this.context = context;
        executorService = Executors.newFixedThreadPool(3);
        // 获取录制的mp4文件路径
        mp4Path = getCaptureFile(context, Environment.DIRECTORY_MOVIES, ".mp4").toString();
        File file = new File(mp4Path);
        LogUtils.e("-->>", "是否存在=" + file.exists());
        this.audioEncoder = audioEncoder;
        this.videoEncoder = videoEncoder;
        try {
            AudioRecorder audioRecorder = new AudioRecorder();
            audioRecorder.init();
            audioEncoder.setDataProvider(audioRecorder);
            audioEncoder.init();
            videoEncoder.init(1280, 960, 30);
            audioEncoder.setMediaCodecListener(this);
            videoEncoder.setMediaCodecListener(this);
            mediaMuxer = new MediaMuxer(mp4Path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            audioEncoder.setMp4Recorder(this);
            videoEncoder.setMp4Recorder(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MediaMuxer getMediaMuxer() {
        return mediaMuxer;
    }

    public String getMp4Path() {
        return mp4Path;
    }

    public void startRecording() {
        // 开启一个线程 去封装视频和音频数据
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                packageFrame();
            }
        }).start();*/
        // 需要一个第一帧编码成功输出后的回调 在回调里才能 mediaMuxer.start()
//        mediaMuxer.start();
        if (videoEncoder != null) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    videoEncoder.start(mp4Path);
                }
            });
        }
        if (audioEncoder != null) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    audioEncoder.startEncode(mp4Path);
                }
            });
        }
    }

    public void stopRecording() {
        isRecording = false;
        if (videoEncoder != null) {
            videoEncoder.stop();
        }
        if (audioEncoder != null) {
            audioEncoder.stop();
        }
    }

    private void packageFrame() {
        while (isRecording) {
            FrameData frameData = dataQueue.poll();
            if (frameData == null) {
                continue;
            }
            boolean keyFrame = (frameData.bufferInfo.flags & MediaCodec.BUFFER_FLAG_KEY_FRAME) != 0;
            Log.e("-->>", frameData.index + "trackIndex:" + frameData.trackIndex + ",writeSampleData:" + keyFrame);
            mediaMuxer.writeSampleData(frameData.trackIndex, frameData.byteBuffer, frameData.bufferInfo);
        }
    }

    /**
     * assign encoder to muxer
     * @param format
     * @return minus value indicate error
     */
    /*package*//* synchronized int addTrack(final MediaFormat format) {
        if (mIsStarted)
            throw new IllegalStateException("muxer already started");
        final int trackIx = mMediaMuxer.addTrack(format);
        if (DEBUG) Log.i(TAG, "addTrack:trackNum=" + mEncoderCount + ",trackIx=" + trackIx + ",format=" + format);
        return trackIx;
    }*/

    /**
     * generate output file
     *
     * @param context
     * @param type    Environment.DIRECTORY_MOVIES / Environment.DIRECTORY_DCIM etc.
     * @param ext     .mp4(.m4a for audio) or .png
     * @return return null when this app has no writing permission to external storage.
     */
    public static final File getCaptureFile(Context context, final String type, final String ext) {
//        final File dir = new File(Environment.getExternalStoragePublicDirectory(type), DIR_NAME);
        final File dir = new File(context.getExternalCacheDir(), DIR_NAME);
        Log.d(TAG, "path=" + dir.toString());
        dir.mkdirs();
        if (dir.canWrite()) {
            return new File(dir, getDateTimeString() + ext);
        }
        return null;
    }

    /**
     * get current date and time as String
     *
     * @return
     */
    private static final String getDateTimeString() {
        final GregorianCalendar now = new GregorianCalendar();
        return mDateTimeFormat.format(now.getTime());
    }

    @Override
    public synchronized void outputFormatChange(int mediaType, MediaFormat mediaFormat) {
        // 需要一个第一帧编码成功输出后的回调 在回调里才能 mediaMuxer.start()
        if (mediaType == VIDEO_TYPE) {
            videoTrackIndex = mediaMuxer.addTrack(mediaFormat);
            LogUtils.e("-->>添加了视频轨道");
            isVideoAdd = true;
        } else if (mediaType == AUDIO_TYPE) {
            audioTrackIndex = mediaMuxer.addTrack(mediaFormat);
            LogUtils.e("-->>添加了音频轨道");
            isAudioAdd = true;
        }
        startMediaMuxer();
    }

    @Override
    public void onStop(int mediaType) {
        synchronized (this) {
//            && audioEncoder.stop
            if (videoEncoder.stop && audioEncoder.stop) {
                isRecording = false;
            }
        }
    }

    @Override
    public void onEncodeFrame(int mediaType, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        int trackIndex = videoTrackIndex;
        if (mediaType == AUDIO_TYPE) {
            trackIndex = audioTrackIndex;
        }
        FrameData frameData = new FrameData(index++, trackIndex, byteBuffer, bufferInfo);
        dataQueue.add(frameData);
    }

    @Override
    public void run() {
        mediaMuxer.start();
        isRecording = true;
        LogUtils.e("-->>开始执行 run");
        while (isRecording || !dataQueue.isEmpty()) {
            FrameData frameData = dataQueue.poll();
            LogUtils.e("-->>取出编码后的数据=" + GsonUtils.toJson(frameData));
            if (frameData == null) {
                continue;
            }
            mediaMuxer.writeSampleData(frameData.trackIndex, frameData.byteBuffer, frameData.bufferInfo);
        }

        LogUtils.e("-->>结束");
        try {
            mediaMuxer.stop();
            mediaMuxer.release();
        }catch (Exception e) {
            LogUtils.e("-->>" + e.getMessage());
            e.printStackTrace();
        }
//        executorService.shutdown();
    }

    public boolean isRecording() {
        return isRecording;
    }

    private synchronized void startMediaMuxer() {
        if (isVideoAdd && isAudioAdd) {
            LogUtils.e("-->> startMediaMuxer");
            executorService.execute(this::run);
        }
    }

    public static class FrameData {
        int index = 0;
        int trackIndex;
        ByteBuffer byteBuffer;
        MediaCodec.BufferInfo bufferInfo;

        public FrameData(int index, int trackIndex, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
            this.index = index;
            this.trackIndex = trackIndex;
            this.byteBuffer = byteBuffer;
            this.bufferInfo = bufferInfo;
            boolean keyFrame = (bufferInfo.flags & MediaCodec.BUFFER_FLAG_KEY_FRAME) != 0;
            Log.e("-->>FrameData", GsonUtils.toJson(this));
        }
    }
}
