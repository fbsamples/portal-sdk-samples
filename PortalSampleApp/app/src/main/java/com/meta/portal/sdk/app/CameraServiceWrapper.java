package com.meta.portal.sdk.app;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;
import androidx.annotation.GuardedBy;
import com.facebook.portal.smartcamera.client.base.api.common.ModeSpec;
import com.facebook.portal.smartcamera.client.base.api.common.SmartCameraAccessException;
import com.facebook.portal.smartcamera.client.base.api.common.base.Subscriber;
import com.facebook.portal.smartcamera.client.base.api.common.concurrent.AsyncResult;
import com.facebook.portal.smartcamera.client.base.api.common.concurrent.Result;
import com.facebook.portal.smartcamera.client.base.api.common.concurrent.ResultCallback;
import com.facebook.portal.smartcamera.client.base.api.control.ControlConnection;
import com.facebook.portal.smartcamera.client.base.api.control.SmartCameraControlConnectionFactory;
import com.facebook.portal.smartcamera.client.base.api.metadata.MetadataBuffer;
import com.facebook.portal.smartcamera.client.base.api.metadata.MetadataConnection;
import com.facebook.portal.smartcamera.client.base.api.metadata.SmartCameraMetadataConnectionFactory;
import com.facebook.portal.smartcamera.client.base.api.metadata.Topic;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;

public class CameraServiceWrapper {

    public interface ModeChangeListener {
        void onModeChanged(@Nullable ModeSpec modeSpec);
    }

    public interface ConnectionListener {
        void onControlConnectionResult(boolean success);

        void onMetadataConnectionResult(boolean success);
    }

    private static final String TAG = CameraServiceWrapper.class.getSimpleName();

    private final ReadWriteLock mDestroyedLock = new ReentrantReadWriteLock();

    private final ExecutorService mExecutorService = Executors.newCachedThreadPool();
    private final ModeChangeListener mModeChangeListener;
    private final ConnectionListener mConnectionListener;
    private RectF mCropRect = new RectF();
    private float mAspectRatio = 1f;

    @GuardedBy("mDestroyedLock")
    private boolean mDestroyed;

    private final List<? extends Topic.Frame<?>> mFrameTopics =
            Arrays.asList(Topic.Frame.Crop.key(), Topic.Frame.FullFovAspectRatio.key());

    private final SmartCameraControlConnectionFactory mSmartCameraControlConnectionFactory;
    private final SmartCameraMetadataConnectionFactory mSmartCameraMetadataConnectionFactory;

    private final AtomicReference<Result<ControlConnection>> mControlConnection =
            new AtomicReference<>(
                    Result.<ControlConnection>failure(new IllegalStateException("uninitialized")));
    private final AtomicReference<Result<ControlConnection.ControlSession>> mControlSession =
            new AtomicReference<>(
                    Result.<ControlConnection.ControlSession>failure(
                            new IllegalStateException("uninitialized")));
    private final AtomicReference<Result<MetadataConnection>> mMetadataConnection =
            new AtomicReference<>(
                    Result.<MetadataConnection>failure(new IllegalStateException("uninitialized")));

    public CameraServiceWrapper(
            Context context,
            ModeChangeListener modeChangeListener,
            ConnectionListener connectionListner) {
        mSmartCameraControlConnectionFactory = new SmartCameraControlConnectionFactory(context);
        mSmartCameraMetadataConnectionFactory = new SmartCameraMetadataConnectionFactory(context);
        mModeChangeListener = modeChangeListener;
        mConnectionListener = connectionListner;
    }

    private final Subscriber<ModeSpec> mModeSpecSubscriber =
            new Subscriber<ModeSpec>() {
                @Override
                public void onUpdate(@Nullable ModeSpec value) {
                    mModeChangeListener.onModeChanged(value);
                }
            };

    private final Subscriber<MetadataBuffer> mFrameMetadataSubscriber =
            new Subscriber<MetadataBuffer>() {
                @Override
                public void onUpdate(@Nullable MetadataBuffer value) {
                    if (value == null) {
                        return;
                    }
                    if (value.containsTopic(Topic.Frame.Crop.key())) {
                        mCropRect = Topic.Frame.Crop.read(value);
                    }
                    if (value.containsTopic(Topic.Frame.FullFovAspectRatio.key())) {
                        mAspectRatio = Topic.Frame.FullFovAspectRatio.read(value);
                    }
                }
            };

    public void start() {
        mDestroyedLock.writeLock().lock();
        try {
            mDestroyed = false;
        } finally {
            mDestroyedLock.writeLock().unlock();
        }
        setupControlConnection();
        setupMetadataConnection();
    }

    public void setupControlConnection() {
        mDestroyedLock.readLock().lock();
        try {
            if (mDestroyed) {
                Log.i(TAG, "Skipping setupControlConnection since destroyed");
                return;
            }
            AsyncResult<ControlConnection> controlConnection =
                    mSmartCameraControlConnectionFactory.connect();
            controlConnection.addListener(
                    new ResultCallback<ControlConnection>() {
                        @Override
                        public void onResult(Result<ControlConnection> result) {
                            mDestroyedLock.readLock().lock();
                            try {
                                if (mDestroyed) {
                                    closeControlConnection();
                                    Log.i(TAG, "Skipping control connection result since destroyed");
                                    return;
                                }
                                Log.i(TAG, "SCS control connection opened");
                                mControlConnection.set(result);
                                if (result.isSuccessful()) {
                                    mConnectionListener.onControlConnectionResult(true);
                                    Log.i(TAG, "SCS control connection success");
                                    setControlConnectionCloseMonitoring(result.getValue());
                                    mControlSession.set(
                                            Result.<ControlConnection.ControlSession>failure(
                                                    new IllegalStateException("initialization pending")));
                                    setupControlSession(result.getValue());
                                } else {
                                    mConnectionListener.onControlConnectionResult(false);
                                    Log.e(TAG, "SCS control connection failed", result.getThrowable());
                                }
                            } finally {
                                mDestroyedLock.readLock().unlock();
                            }
                        }
                    },
                    mExecutorService);
        } finally {
            mDestroyedLock.readLock().unlock();
        }
    }

    private void setupControlSession(ControlConnection controlConnection) {
        mDestroyedLock.readLock().lock();
        try {
            if (mDestroyed) {
                Log.i(TAG, "Skipping setupControlSession since destroyed");
                return;
            }
            controlConnection
                    .subscribeControlAvailability()
                    .addSubscriber(
                            new Subscriber<Boolean>() {
                                @Override
                                public void onUpdate(Boolean value) {
                                    mDestroyedLock.readLock().lock();
                                    try {
                                        if (mDestroyed) {
                                            return;
                                        }
                                        if (value == Boolean.TRUE) {
                                            try {
                                                ControlConnection.ControlSession controlSession =
                                                        mControlConnection.get().getValue().requestControls();
                                                if (controlSession != null) {
                                                    Result<ControlConnection.ControlSession> successfulControlSessionResult =
                                                            Result.success(controlSession);
                                                    mControlSession.set(successfulControlSessionResult);
                                                    setControlSessionMonitoring(controlSession);
                                                } else {
                                                    Log.e(TAG, "SCS requestControls returns null unexpectedly");
                                                }
                                            } catch (SmartCameraAccessException e) {
                                                Log.e(TAG, "Unable to request control from SCS ", e);
                                            }
                                        } else {
                                            Log.e(TAG, "SCS subscribeControlAvailability, onUpdate invoked with false");
                                        }
                                    } finally {
                                        mDestroyedLock.readLock().unlock();
                                    }
                                }
                            },
                            mExecutorService,
                            true);
        } catch (SmartCameraAccessException e) {
            Log.e(TAG, "subscribeControlAvailability throws exception", e);
        } finally {
            mDestroyedLock.readLock().unlock();
        }
    }

    public void setupMetadataConnection() {
        mDestroyedLock.readLock().lock();
        try {
            if (mDestroyed) {
                Log.i(TAG, "Skipping setupMetadataConnection since destroyed");
                return;
            }
            AsyncResult<MetadataConnection> metadataConnection =
                    mSmartCameraMetadataConnectionFactory.connect();
            metadataConnection.addListener(
                    new ResultCallback<MetadataConnection>() {
                        @Override
                        public void onResult(Result<MetadataConnection> result) {
                            mDestroyedLock.readLock().lock();
                            try {
                                if (mDestroyed) {
                                    closeMetadataConnection();
                                    Log.i(TAG, "Skipping metadata connection result since destroyed");
                                    return;
                                }
                                Log.d(TAG, "SCS metadata connection opened");
                                mMetadataConnection.set(result);
                                if (result.isSuccessful()) {
                                    mConnectionListener.onMetadataConnectionResult(true);
                                    Log.d(TAG, "SCS metadata connection success");
                                    setMetadataConnectionCloseMonitoring(result.getValue());
                                    subscribeToModeChanges(mModeSpecSubscriber);
                                    subscribeToFrameMetadata(
                                            mFrameTopics, Float.POSITIVE_INFINITY, mFrameMetadataSubscriber);
                                } else {
                                    mConnectionListener.onMetadataConnectionResult(false);
                                    Log.e(TAG, "SCS metadata connection failed", result.getThrowable());
                                }
                            } finally {
                                mDestroyedLock.readLock().unlock();
                            }
                        }
                    },
                    mExecutorService);
        } finally {
            mDestroyedLock.readLock().unlock();
        }
    }

    private void setControlConnectionCloseMonitoring(ControlConnection controlConnection) {
        controlConnection
                .getCloseFuture()
                .addListener(
                        new ResultCallback<Void>() {
                            @Override
                            public void onResult(Result<Void> result) {
                                mDestroyedLock.readLock().lock();
                                try {
                                    if (mDestroyed) {
                                        return;
                                    }
                                    mConnectionListener.onControlConnectionResult(false);
                                    mControlConnection.set(
                                            Result.<ControlConnection>failure(
                                                    new IllegalStateException("connection lost - refresh pending")));
                                    setupControlConnection();
                                } finally {
                                    mDestroyedLock.readLock().unlock();
                                }
                            }
                        },
                        mExecutorService);
    }

    private void setControlSessionMonitoring(ControlConnection.ControlSession controlSession) {
        controlSession
                .getCloseFuture()
                .addListener(
                        new ResultCallback<Void>() {
                            @Override
                            public void onResult(Result<Void> result) {
                                mDestroyedLock.readLock().lock();
                                try {
                                    if (mDestroyed) {
                                        return;
                                    }
                                    mControlSession.set(
                                            Result.<ControlConnection.ControlSession>failure(
                                                    new IllegalStateException("control session lost - refresh pending")));
                                    if (mControlConnection.get() != null
                                            && mControlConnection.get().isSuccessful()
                                            && mControlConnection.get().getValue() != null) {
                                        setupControlSession(mControlConnection.get().getValue());
                                    } else {
                                        /*
                                         * It is okay to do nothing here. We are being notified that control session
                                         * has been closed, but we have also lost the control connection in the meanwhile.
                                         * Given that we have monitoring setup on control connection, we will actually
                                         * have a refresh in progress. That refresh will take care of procuring a new
                                         * control connection, which will be followed by procurement of a new control
                                         * session.
                                         */
                                        Log.e(TAG, "Control connection lost");
                                    }
                                } finally {
                                    mDestroyedLock.readLock().unlock();
                                }
                            }
                        },
                        mExecutorService);
    }

    private void setMetadataConnectionCloseMonitoring(MetadataConnection metadataConnection) {
        metadataConnection
                .getCloseFuture()
                .addListener(
                        new ResultCallback<Void>() {
                            @Override
                            public void onResult(Result<Void> result) {
                                mDestroyedLock.readLock().lock();
                                try {
                                    if (mDestroyed) {
                                        return;
                                    }
                                    mConnectionListener.onMetadataConnectionResult(false);
                                    mMetadataConnection.set(
                                            Result.<MetadataConnection>failure(
                                                    new IllegalStateException("Metadata connection lost - refresh pending")));
                                    Log.d(TAG, "SCS metadata connection closed");
                                    setupMetadataConnection();
                                } finally {
                                    mDestroyedLock.readLock().unlock();
                                }
                            }
                        },
                        mExecutorService);
    }

    private void subscribeToModeChanges(Subscriber<ModeSpec> subscriber) {
        if (mMetadataConnection.get().isSuccessful()) {
            MetadataConnection metadataConnection = mMetadataConnection.get().getValue();
            if (metadataConnection != null) {
                try {
                    metadataConnection
                            .subscribeModeChanges()
                            .addSubscriber(subscriber, mExecutorService, true);
                } catch (SmartCameraAccessException e) {
                    Log.e(TAG, "Failed to subsribe to mode changes", e);
                }
            }
        }
    }

    public void destroy() {
        mDestroyedLock.writeLock().lock();
        try {
            mDestroyed = true;
        } finally {
            mDestroyedLock.writeLock().unlock();
        }
        closeControlSession();
        closeControlConnection();
        closeMetadataConnection();
    }

    private void closeControlSession() {
        if (mControlSession.get().isSuccessful()) {
            ControlConnection.ControlSession controlSession = mControlSession.get().getValue();
            if (controlSession != null) {
                controlSession.close();
            }
        }
        mControlSession.set(
                Result.<ControlConnection.ControlSession>failure(
                        new IllegalStateException("Control session has been destroyed")));
    }

    private void closeControlConnection() {
        if (mControlConnection.get().isSuccessful()) {
            ControlConnection controlConnection = mControlConnection.get().getValue();
            if (controlConnection != null) {
                controlConnection.close();
            }
        }
        mControlConnection.set(
                Result.<ControlConnection>failure(
                        new IllegalStateException("Control connection has been destroyed")));
    }

    private void closeMetadataConnection() {
        if (mMetadataConnection.get().isSuccessful()) {
            MetadataConnection metadataConnection = mMetadataConnection.get().getValue();
            if (metadataConnection != null) {
                metadataConnection.close();
            }
        }
        mMetadataConnection.set(
                Result.<MetadataConnection>failure(
                        new IllegalStateException("Metadata connection has been destroyed")));
    }

    private void subscribeToFrameMetadata(
            List<? extends Topic.Frame<?>> topics,
            float maxUpdatesPerSec,
            Subscriber<MetadataBuffer> subscriber) {
        if (mMetadataConnection.get().isSuccessful()) {
            MetadataConnection metadataConnection = mMetadataConnection.get().getValue();
            if (metadataConnection != null) {
                try {
                    metadataConnection
                            .subscribeFrameMetadata(topics, maxUpdatesPerSec)
                            .addSubscriber(subscriber, mExecutorService, true);
                } catch (SmartCameraAccessException e) {
                    Log.e(TAG, "Couldn't subsribe to frame metadata", e);
                }
            }
        }
    }

    public void setModeToDefault() {
        setMode(ModeSpec.DefaultAuto.create());
    }

    public void setModeToDesk() {
        setMode(ModeSpec.Desk.create());
    }

    public void setModeToFixed(ModeSpec.Fixed fixedMode) {
        setMode(fixedMode);
    }

    public @Nullable ModeSpec getMode() {
        if (mMetadataConnection.get().isSuccessful()) {
            MetadataConnection metadataConnection = mMetadataConnection.get().getValue();
            if (metadataConnection != null) {
                try {
                    return metadataConnection.getMode();
                } catch (SmartCameraAccessException e) {
                    Log.e(TAG, "Failed to get Mode", e);
                }
            }
        }
        return null;
    }

    private void setMode(ModeSpec modeSpec) {
        if (mControlSession.get().isSuccessful()) {
            ControlConnection.ControlSession controlSession = mControlSession.get().getValue();
            if (controlSession != null) {
                try {
                    Log.d(TAG, "Setting mode to " + modeSpec.toString());
                    controlSession.setMode(modeSpec);
                } catch (SmartCameraAccessException e) {
                    Log.e(TAG, "Couldn't set mode", e);
                }
            }
        }
    }

    public RectF getCropRect() {
        return mCropRect;
    }

    public float getAspectRatio() {
        return mAspectRatio;
    }
}

