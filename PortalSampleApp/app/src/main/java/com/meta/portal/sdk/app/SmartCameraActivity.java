package com.meta.portal.sdk.app;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

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
