// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.portal.sdk.app.smartcamera;

public interface CameraPresenter {
  void start();

  void stop();

  void onDefaultModeClicked();

  void onDeskModeClicked();

  void onZoomInClicked();

  void onZoomOutClicked();

  void onScrollUpClicked();

  void onScrollDownClicked();

  void onScrollLeftClicked();

  void onScrollRightClicked();

  void onLaunchEditorClicked();

  public interface CameraView {
    void setModeText(String modeText);

    void setFailureText(String failureText);

    void showFailureText(boolean show);

    void showModeText(boolean show);
  }
}
