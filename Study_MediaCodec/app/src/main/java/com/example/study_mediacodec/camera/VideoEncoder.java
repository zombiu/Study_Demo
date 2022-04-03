package com.example.study_mediacodec.camera;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class VideoEncoder implements Runnable {

    private BlockingQueue<byte[]> mQueue = new LinkedBlockingQueue<>();
    private boolean isEncoding;

    @Override
    public void run() {
        isEncoding = true;
    }

    public void putFrameData(byte[] data) {
        if (data == null || !isEncoding) {
            return;
        }
        try {
            mQueue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
