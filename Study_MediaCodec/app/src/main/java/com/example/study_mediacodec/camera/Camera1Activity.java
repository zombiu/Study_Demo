package com.example.study_mediacodec.camera;

import static android.hardware.Camera.Parameters.PREVIEW_FPS_MAX_INDEX;
import static android.hardware.Camera.Parameters.PREVIEW_FPS_MIN_INDEX;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.study_mediacodec.R;
import com.example.study_mediacodec.camera.encoder.VideoEncoder2;
import com.example.study_mediacodec.databinding.ActivityCamera1Binding;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * API Level	Camera API	Preview View
 * 9-13	        Camera1	SurfaceView
 * 14-20	    Camera1	TextureView
 * 21-23	    Camera2	TextureView
 * 24	        Camera2	SurfaceView
 */
public class Camera1Activity extends AppCompatActivity implements Camera.PreviewCallback {

    private ActivityCamera1Binding binding;

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

    private SurfaceHolder surfaceHolder;
    private ExecutorService executorService;

    private VideoEncoder videoEncoder = new VideoEncoder();
//    private VideoEncoder2 videoEncoder = new VideoEncoder2();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCamera1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 初始化输出路径
                savePath = getExternalCacheDir().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".h264";
                // execute会抛出异常 submit可以在future 中获取异常
                Future<?> submit = executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        videoEncoder.start(savePath);
                    }
                });
                /*try {
                    Object o = submit.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        });
        binding.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoEncoder.stop();
            }
        });

        binding.btnGoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayVideoActivity.start(Camera1Activity.this, savePath);
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

        // setParameters 获取到分辨率 帧率 之后 再init
        videoEncoder.init(DEFAULT_WIDTH, DEFAULT_HEIGHT, frameRate);
    }

    //     java.lang.RuntimeException: Camera is being used after Camera.release() was called
    //        at android.hardware.Camera.setHasPreviewCallback(Native Method)
    // 这里需要    camera.setPreviewCallback(null);
    private void releaseCamera() {
        /*try {
            camera.setPreviewDisplay(null);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
        videoEncoder.putFrameData(bytes);
    }

    @Override
    protected void onDestroy() {
        // 多次调用 releaseCamera会有问题
//        releaseCamera();
        super.onDestroy();
    }
}