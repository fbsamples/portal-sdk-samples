package com.meta.portal.sdk.app;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;
import android.view.TextureView;
import androidx.annotation.Nullable;
import com.facebook.portal.smartcamera.client.base.api.common.ModeSpec;

public class CameraViewPresenter
        implements CameraPresenter,
        CameraServiceWrapper.ModeChangeListener,
        CameraServiceWrapper.ConnectionListener,
        ManualExperienceController.ManualModeListener {

    private static final String TAG = "CameraPresenter";

    private boolean mRetriedControlConnection = false;
    private boolean mRetriedMetadataConnection = false;
    private boolean mControlConnectionFailed = false;
    private boolean mMetadataConnectionFailed = false;

    private final CameraServiceWrapper mCameraServiceWrapper;
    private final ManualExperienceController mManualExperienceController;
    private final CameraView mCameraView;
    private final CameraEditorLauncher mCameraEditorLauncher;

    public CameraViewPresenter(
            Context context,
            CameraView cameraView,
            TextureView textureView,
            CameraEditorLauncher cameraEditorLauncher) {
        mCameraServiceWrapper = new CameraServiceWrapper(context, this, this);
        mManualExperienceController = new ManualExperienceController(textureView, true, this);
        mCameraView = cameraView;
        mCameraEditorLauncher = cameraEditorLauncher;
    }

    @Override
    public void onModeChanged(@Nullable ModeSpec modeSpec) {
        if (modeSpec == null) {
            mCameraView.setModeText("Current Mode: null");
            return;
        }
        String modeText = String.format("Current Mode: %s", modeSpec.getClass().getSimpleName());
        if (modeSpec instanceof ModeSpec.Fixed) {
            modeText = modeText.concat(getFixedModeText((ModeSpec.Fixed) modeSpec));
        }
        mCameraView.setModeText(modeText);
    }

    private void retryControlConnection() {
        if (!mRetriedControlConnection) {
            Log.i(TAG, "Retrying ControlConnection...");
            mCameraServiceWrapper.setupControlConnection();
            mRetriedControlConnection = true;
        }
    }

    private void retryMetadataConnection() {
        if (!mRetriedMetadataConnection) {
            Log.i(TAG, "Retrying MetadataConnection...");
            mCameraServiceWrapper.setupMetadataConnection();
            mRetriedMetadataConnection = true;
        }
    }

    @Override
    public void onControlConnectionResult(final boolean success) {
        mControlConnectionFailed = !success;
        if (mControlConnectionFailed) {
            retryControlConnection();
        }
        updateFailureText();
    }

    @Override
    public void onMetadataConnectionResult(final boolean success) {
        mMetadataConnectionFailed = !success;
        if (mMetadataConnectionFailed) {
            retryMetadataConnection();
        }
        updateFailureText();
    }

    private void updateFailureText() {
        // if no connection failure, then hide failure text and show mode text
        if (!(mControlConnectionFailed || mMetadataConnectionFailed)) {
            mCameraView.showFailureText(false);
            mCameraView.showModeText(true);
            return;
        }
        // display failure text and hide mode text
        mCameraView.showModeText(false);
        mCameraView.showFailureText(true);
        if (mControlConnectionFailed && mMetadataConnectionFailed) {
            mCameraView.setFailureText("ControlConnection and MetadataConnection Failed!");
        } else if (mControlConnectionFailed) {
            mCameraView.setFailureText("ControlConnection Failed!");
        } else if (mMetadataConnectionFailed) {
            mCameraView.setFailureText("MetadataConnection Failed!");
        }
    }

    @Override
    public RectF getCropRect() {
        return mCameraServiceWrapper.getCropRect();
    }

    @Override
    public float getAspectRatio() {
        return mCameraServiceWrapper.getAspectRatio();
    }

    @Override
    public void setFixedMode(ModeSpec.Fixed fixedMode) {
        mCameraServiceWrapper.setModeToFixed(fixedMode);
    }

    @Override
    public void start() {
        mCameraServiceWrapper.start();
    }

    @Override
    public void stop() {
        mCameraServiceWrapper.destroy();
    }

    @Override
    public void onDefaultModeClicked() {
        mCameraServiceWrapper.setModeToDefault();
    }

    @Override
    public void onDeskModeClicked() {
        mCameraServiceWrapper.setModeToDesk();
    }

    @Override
    public void onZoomInClicked() {
        mManualExperienceController.zoomIn();
    }

    @Override
    public void onZoomOutClicked() {
        mManualExperienceController.zoomOut();
    }

    @Override
    public void onScrollUpClicked() {
        mManualExperienceController.scrollUp();
    }

    @Override
    public void onScrollDownClicked() {
        mManualExperienceController.scrollDown();
    }

    @Override
    public void onScrollLeftClicked() {
        mManualExperienceController.scrollLeft();
    }

    @Override
    public void onScrollRightClicked() {
        mManualExperienceController.scrollRight();
    }

    @Override
    public void onLaunchEditorClicked() {
        mCameraEditorLauncher.onLaunchClicked();
    }

    private static String getFixedModeText(final ModeSpec.Fixed modeSpec) {
        float x = modeSpec.getRelativeCameraCropCenterX();
        float y = modeSpec.getRelativeCameraCropCenterY();
        float scale = modeSpec.getRelativeCameraCropScale();
        return String.format(" x = %f, y = %f, scale = %f", x, y, scale);
    }
}

