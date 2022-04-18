package com.example.study_mediacodec.camera.encoder;

import android.media.MediaCodec;
import android.media.MediaFormat;

import java.nio.ByteBuffer;


public interface IMediaCodecListener {
    void outputFormatChange(int mediaType, MediaFormat mediaFormat);

    void onStop(int mediaType);

    void onEncodeFrame(int mediaType, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo);
}
