package com.meta.portal.sdk.app.privacyshutter;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RecordingSampler {

    private static final int RECORDING_SAMPLE_RATE = 44100;

    private AudioRecord mAudioRecord;
    private boolean mIsRecording;
    private int mBufSize;

    private int mSamplingInterval = 100;
    private Timer mTimer;

    private List<VisualizerView> mVisualizerViews = new ArrayList<>();

    public RecordingSampler() {
        initAudioRecord();
    }
    
    public void link(VisualizerView visualizerView) {
        mVisualizerViews.add(visualizerView);
    }

    @SuppressLint("MissingPermission")
    private void initAudioRecord() {
        int bufferSize = AudioRecord.getMinBufferSize(
                RECORDING_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
        );

        mAudioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                RECORDING_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
        );

        if (mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            mBufSize = bufferSize;
        }
    }
    
    public void startRecording() {
        mTimer = new Timer();
        mAudioRecord.startRecording();
        mIsRecording = true;
        runRecording();
    }
    
    public void stopRecording() {
        mIsRecording = false;
        mTimer.cancel();

        if (mVisualizerViews != null && !mVisualizerViews.isEmpty()) {
            for (int i = 0; i < mVisualizerViews.size(); i++) {
                mVisualizerViews.get(i).receive(0);
            }
        }
    }

    private void runRecording() {
        final byte buf[] = new byte[mBufSize];
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // stop recording
                if (!mIsRecording) {
                    mAudioRecord.stop();
                    return;
                }
                mAudioRecord.read(buf, 0, mBufSize);

                int decibel = calculateDecibel(buf);
                if (mVisualizerViews != null && !mVisualizerViews.isEmpty()) {
                    for (int i = 0; i < mVisualizerViews.size(); i++) {
                        mVisualizerViews.get(i).receive(decibel);
                    }
                }
                
            }
        }, 0, mSamplingInterval);
    }

    private int calculateDecibel(byte[] buf) {
        int sum = 0;
        for (int i = 0; i < mBufSize; i++) {
            sum += Math.abs(buf[i]);
        }
        // avg 10-50
        return sum / mBufSize;
    }

    /**
     * release member object
     */
    public void release() {
        stopRecording();
        mAudioRecord.release();
        mAudioRecord = null;
        mTimer = null;
    }

}
