package com.meta.portal.sdk.app.smartcamera;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.meta.portal.sdk.app.base.FeatureBaseActivity;
import com.meta.portal.sdk.app.R;

public class SmartCameraActivity extends FeatureBaseActivity {

    private Fragment mFragment;

    protected Fragment getFragment() {
        mFragment = SmartCameraFragment.Companion.newInstance();
        return mFragment;
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.smart_camera_feature_info_header;
    }

    protected @StringRes
    int getFeatureInfoTextResId() {
        return R.string.smart_camera_feature_info_text;
    }

    protected void updateDebugModeLayoutContainerVisibility(boolean visible) {
        ((SmartCameraFragment) mFragment).updateDebugModeLayoutContainerVisibility(visible);
    }

}
