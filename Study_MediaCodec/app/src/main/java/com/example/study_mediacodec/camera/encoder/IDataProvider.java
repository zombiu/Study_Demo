package com.example.study_mediacodec.camera.encoder;

import java.util.concurrent.BlockingQueue;

public interface IDataProvider{

    default BlockingQueue<byte[]> getDataQueue() {
        return null;
    }

    default void queueData(byte[] data) {

    }

    default byte[] dequeueData() {
        return null;
    }

}
