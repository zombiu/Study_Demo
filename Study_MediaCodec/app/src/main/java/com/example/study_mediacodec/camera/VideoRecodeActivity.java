package com.example.study_mediacodec.camera;

import static android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX;
import static android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.study_mediacodec.R;
import com.example.study_mediacodec.camera.encoder.AudioEncoder;
import com.example.study_mediacodec.camera.encoder.AudioRecorder;
import com.example.study_mediacodec.camera.encoder.VideoEncoder2;
import com.example.study_mediacodec.databinding.ActivityVideoRecodeBinding;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VideoRecodeActivity extends AppCompatActivity implements Camera.PreviewCallback {
    private ActivityVideoRecodeBinding binding;

    private Camera camera;
    private Camera.Parameters cameraParameters;
    private int cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
    private int displayOrientation = 0;
    public int DEFAULT_WIDTH = 1280;
    public int DEFAULT_HEIGHT = 720;
    public int previewWidth;
    public int previewHeight;
    public int frameRate;
    private String savePath;
    private Mp4Recorder mp4Recorder;

    private SurfaceHolder surfaceHolder;
    private ExecutorService executorService;

    private VideoEncoder2 videoEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoRecodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AudioEncoder audioEncoder = new AudioEncoder();
                videoEncoder = new VideoEncoder2();
                mp4Recorder = new Mp4Recorder(VideoRecodeActivity.this, audioEncoder, videoEncoder);
                mp4Recorder.startRecording();
            }
        });
        binding.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp4Recorder != null) {
                    mp4Recorder.stopRecording();
                    mp4Recorder = null;
                }
            }
        });

        binding.btnGoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayVideoActivity.start(VideoRecodeActivity.this, savePath);
            }
        });

        executorService = Executors.newFixedThreadPool(1);

        surfaceHolder = binding.surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
//                surfaceHolder.removeCallback(this);
                releaseCamera();
            }
        });

        openCamera(cameraFacing);
    }

    private void startRecording() {
        AudioRecorder audioRecorder = new AudioRecorder();
        AudioEncoder audioEncoder = new AudioEncoder();
        audioEncoder.init();
        audioEncoder.setDataProvider(audioRecorder);

    }

    //     java.lang.RuntimeException: Camera is being used after Camera.release() was called
    //        at android.hardware.Camera.setHasPreviewCallback(Native Method)
    // 这里需要    camera.setPreviewCallback(null);
    private void releaseCamera() {
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
    }

    private void openCamera(int cameraFacing) {
        if (!supportCameraFacing(cameraFacing)) {

            return;
        }
        camera = Camera.open(cameraFacing);
        setParameters(camera);
        // 设置预览数据回调
        // 每产生一帧都要开辟一个新的buffer，进行存储帧数据，这样不断开辟和回收内存，GC会很频繁，效率很低
        // 使用mCamera.setPreviewCallbackWithBuffer(this)方法代替
        /**
         * 使用步骤：
         *
         * 1、先设置回调:
         *
         * mCamera.setPreviewCallbackWithBuffer(this)
         *
         * 2、增加buffer:
         *
         * mCamera.addCallbackBuffer(new byte[((previewWidth * previewHeight) * ImageFormat.getBitsPerPixel(ImageFormat.NV21)) / 8])
         * ————————————————
         * 版权声明：本文为CSDN博主「天空还是那么蓝」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
         * 原文链接：https://blog.csdn.net/w958796636/article/details/51038389
         */
        camera.setPreviewCallback(this);
    }

    private boolean supportCameraFacing(int cameraFacing) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == cameraFacing) {
                return true;
            }
        }
        return false;
    }

    private void startPreview() {
        try {
            //设置实时预览
            camera.setPreviewDisplay(surfaceHolder);
            //开始预览
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void initParameters(final Camera camera) {
        cameraParameters = camera.getParameters();
        cameraParameters.setPreviewFormat(ImageFormat.NV21); //default

        if (isSupportFocus(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (isSupportFocus(Camera.Parameters.FOCUS_MODE_AUTO)) {
            cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        //设置预览图片大小
        setPreviewSize();
        //设置图片大小
        setPictureSize();

        camera.setParameters(cameraParameters);
    }*/

    //由于相机预览默认是横屏的，还需要根据实际情况设置为竖屏或者横屏
    private void setCameraDisplayOrientation() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraFacing, cameraInfo);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();  //自然方向
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        //cameraInfo.orientation 图像传感方向
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (cameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (cameraInfo.orientation - degrees + 360) % 360;
        }

        displayOrientation = result;
        //相机预览方向
        camera.setDisplayOrientation(result);
    }

    private void setParameters(Camera camera) {
        cameraParameters = camera.getParameters();
        // 设置预览输出的格式 , 这里是 NV21 所有的相机都支持, 是 YUV420 的一种
        cameraParameters.setPreviewFormat(ImageFormat.NV21);

        //设置预览的大小，Camera 预览的大小（分辨率）只支持内置的几种 getSupportedPreviewSizes 录像
        Camera.Size previewSize = getBestSize(DEFAULT_WIDTH, DEFAULT_HEIGHT, cameraParameters.getSupportedPreviewSizes());
        LogUtils.e("预览画面=" + GsonUtils.toJson(previewSize));
        cameraParameters.setPreviewSize(previewSize.width, previewSize.height);
        //如果使用截图接口，还需要设置截图大小（分辨率） 拍照
        Camera.Size pictureSize = getBestSize(DEFAULT_WIDTH, DEFAULT_HEIGHT, cameraParameters.getSupportedPreviewSizes());
        cameraParameters.setPictureSize(pictureSize.width, pictureSize.height);

        //设置支持的聚焦模式
        if (supportFocus(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        previewWidth = previewSize.width;
        previewHeight = previewSize.height;

        // 需要使用硬件支持的分辨率进行 codec处理 不然 录的视频 会重影绿屏的现象
        DEFAULT_WIDTH = previewWidth;
        DEFAULT_HEIGHT = previewHeight;

        //set fps range.
        int defminFps = 0;
        int defmaxFps = 0;
        List<int[]> supportedPreviewFpsRange = cameraParameters.getSupportedPreviewFpsRange();
        for (int[] fps : supportedPreviewFpsRange) {
            if (defminFps <= fps[PREVIEW_FPS_MIN_INDEX] && defmaxFps <= fps[PREVIEW_FPS_MAX_INDEX]) {
                defminFps = fps[PREVIEW_FPS_MIN_INDEX];
                defmaxFps = fps[PREVIEW_FPS_MAX_INDEX];
            }
        }
        //设置相机预览帧率
        cameraParameters.setPreviewFpsRange(defminFps, defmaxFps);
        frameRate = defmaxFps / 1000;
        LogUtils.e("-->>帧率=" + defmaxFps);

        setCameraDisplayOrientation();

        camera.setParameters(cameraParameters);
    }

    private boolean supportFocus(String focus) {
        if (camera == null) {
            return false;
        }
        List<String> focusModes = camera.getParameters().getSupportedFocusModes();
        if (focusModes.contains(focus)) {
            return true;
        }
        return false;
    }

    private Camera.Size getBestSize(int width, int height, List<Camera.Size> sizes) {
        for (int i = 0; i < sizes.size(); i++) {
            Camera.Size size = sizes.get(i);
            if (width == size.width) {
                return size;
            }
        }
        return sizes.get(0);
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
//        LogUtils.e("回调视频nv21数据 " + bytes.length);
        // 将原始视频数据 交给视频编码器处理
        if (videoEncoder != null) {
            videoEncoder.putFrameData(bytes);
        }
    }

    @Override
    protected void onDestroy() {
        // 多次调用 releaseCamera会有问题
//        releaseCamera();
        super.onDestroy();
    }
}