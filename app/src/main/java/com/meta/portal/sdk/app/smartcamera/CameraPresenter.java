/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

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
