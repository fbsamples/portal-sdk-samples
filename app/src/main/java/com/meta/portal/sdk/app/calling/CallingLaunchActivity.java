/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.calling;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.meta.portal.sdk.app.base.ActivityCallback;
import com.meta.portal.sdk.app.base.FeatureBaseActivity;
import com.meta.portal.sdk.app.R;

public class CallingLaunchActivity extends FeatureBaseActivity {

    private Fragment mFragment;

    protected Fragment getFragment() {
        mFragment = CallLaunchFragment.Companion.newInstance();
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

    }

    protected void setFeatureInfoShowing(boolean showing) {

    }

    @Override
    protected void setActivityCallback(ActivityCallback activityCallback) { }

    protected boolean topAppBarTransparent() {
        return true;
    }

    protected boolean infoButtonShowing() {
        return true;
    }

    protected boolean designModeButtonShowing() {
        return false;
    }

}
