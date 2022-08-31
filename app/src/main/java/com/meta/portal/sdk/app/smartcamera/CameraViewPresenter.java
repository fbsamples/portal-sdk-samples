/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.smartcamera;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;
import android.util.Pair;
import android.view.TextureView;
import com.facebook.portal.smartcamera.client.base.api.common.ModeSpec;

public class CameraViewPresenter
    implements CameraPresenter,
        CameraServiceWrapper.ConnectionListener,
        ManualExperienceController.ManualModeListener {

  private static final String TAG = "CameraPresenter";

  private boolean mRetriedControlConnection = false;
  private boolean mRetriedMetadataConnection = false;
  private boolean mControlConnectionFailed = false;
  private boolean mMetadataConnectionFailed = false;

  private final CameraServiceWrapper mCameraServiceWrapper;
  private final ManualExperienceController mManualExperienceController;
  private final TextureView mTextureView;
  private final ViewLocationProvider mViewLocationProvider;

  public CameraViewPresenter(Context context, TextureView textureView) {
    mCameraServiceWrapper = new CameraServiceWrapper(context, this);
    mManualExperienceController = new ManualExperienceController(textureView, true, this);
    mTextureView = textureView;
    mViewLocationProvider = new ViewLocationProvider(mTextureView);
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
  }

  @Override
  public void onMetadataConnectionResult(final boolean success) {
    mMetadataConnectionFailed = !success;
    if (mMetadataConnectionFailed) {
      retryMetadataConnection();
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
  public void onManualModeClicked() {
    RectF oldRect = mViewLocationProvider.getViewRect();
    RectF cropRect = getCropRect();
    ModeSpec.Fixed mode =
        FixedModeUtil.getFixedMode(cropRect, oldRect, new Pair<Float, Float>(0f, 0f), 1f, true);
    setFixedMode(mode);
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
}
