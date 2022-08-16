// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.portal.sdk.app.smartcamera;

public interface CameraPresenter {
  void start();

  void stop();

  void onDefaultModeClicked();

  void onDeskModeClicked();

  void onManualModeClicked();

  void onZoomInClicked();

  void onZoomOutClicked();

  void onScrollUpClicked();

  void onScrollDownClicked();

  void onScrollLeftClicked();

  void onScrollRightClicked();
}
