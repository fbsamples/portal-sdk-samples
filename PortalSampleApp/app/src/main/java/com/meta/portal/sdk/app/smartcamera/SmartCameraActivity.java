package com.meta.portal.sdk.app.smartcamera;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.meta.portal.sdk.app.base.FeatureBaseActivity;
import com.meta.portal.sdk.app.R;

public class SmartCameraActivity extends FeatureBaseActivity {

    protected Fragment getFragment() {
        return SmartCameraFragment.Companion.newInstance();
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.smart_camera_feature_info_header;
    }

    protected @StringRes
    int getFeatureInfoTextResId() {
        return R.string.smart_camera_feature_info_text;
    }

}
