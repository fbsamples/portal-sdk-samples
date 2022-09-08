/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.fbns;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.Utils;
import com.meta.portal.sdk.app.base.ActivityCallback;
import com.meta.portal.sdk.app.base.FeatureBaseActivity;

public class FbnsActivity extends FeatureBaseActivity {

  private Fragment mFragment;

  protected Fragment getFragment() {
    mFragment = FbnsFragment.newInstance();
    return mFragment;
  }

  protected @StringRes int getFeatureInfoHeaderResId() {
    return R.string.fbns_feature_info_header;
  }

  protected @StringRes int getFeatureInfoTextResId() {
    return R.string.fbns_feature_info_text;
  }

  protected void updateDesignModeLayoutContainerVisibility(boolean visible) {}

  protected void setFeatureInfoShowing(boolean showing) {
    ((FbnsFragment) mFragment).setFeatureInfoShowing(showing);
  }

  protected void setActivityCallback(ActivityCallback activityCallback) {
    ((FbnsFragment) mFragment).setActivityCallback(activityCallback);
  }

  protected boolean topAppBarTransparent() {
    if (Utils.isTvDevice(this)) {
      return false;
    } else {
      return true;
    }
  }

  protected boolean infoButtonShowing() {
    return true;
  }

  protected boolean designModeButtonShowing() {
    return true;
  }
}
