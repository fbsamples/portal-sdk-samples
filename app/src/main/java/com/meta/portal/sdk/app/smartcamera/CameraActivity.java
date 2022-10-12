/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.smartcamera;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.base.ActivityCallback;
import com.meta.portal.sdk.app.base.FeatureBaseActivity;

public class CameraActivity extends FeatureBaseActivity {

  private Fragment mFragment;

  protected Fragment getFragment() {
    mFragment = CameraFragment.newInstance();
    return mFragment;
  }

  protected @StringRes int getFeatureInfoHeaderResId() {
    return R.string.smart_camera_manual_feature_info_header;
  }

  protected @StringRes int getFeatureInfoTextResId() {
    return R.string.smart_camera_manual_feature_info_text;
  }

  protected void updateDesignModeLayoutContainerVisibility(boolean visible) {
    ((CameraFragment) mFragment).updateDesignModeLayoutContainerVisibility(visible);
  }

  protected void setFeatureInfoShowing(boolean showing) {
    ((CameraFragment) mFragment).setFeatureInfoShowing(showing);
  }

  protected void setActivityCallback(ActivityCallback activityCallback) {}

  protected boolean topAppBarTransparent() {
    return true;
  }

  protected boolean infoButtonShowing() {
    return true;
  }

  protected boolean designModeButtonShowing() {
    return true;
  }
}
