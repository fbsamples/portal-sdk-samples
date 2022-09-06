/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.calling;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.base.ActivityCallback;
import com.meta.portal.sdk.app.base.FeatureBaseActivity;

public class CallManagementActivity extends FeatureBaseActivity {

    private Fragment mFragment;

    protected Fragment getFragment() {
        mFragment = CallFragment.Companion.newInstance();
        return mFragment;
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.calling_feature_info_header;
    }

    protected @StringRes int getFeatureInfoTextResId() {
        return R.string.calling_feature_info_text;
    }

    protected void updateDesignModeLayoutContainerVisibility(boolean visible) {
        ((CallFragment) mFragment).updateDesignModeLayoutContainerVisibility(visible);
    }

    protected void setFeatureInfoShowing(boolean showing) {

    }

    @Override
    public void onBackPressed() {
        ((CallFragment) mFragment).onBackPressed();
        super.onBackPressed();
    }

    @Override
    protected void setActivityCallback(ActivityCallback activityCallback) { }

    protected boolean topAppBarTransparent() {
        return true;
    }

    protected boolean infoButtonShowing() {
        return false;
    }

    protected boolean designModeButtonShowing() {
        return true;
    }
}
