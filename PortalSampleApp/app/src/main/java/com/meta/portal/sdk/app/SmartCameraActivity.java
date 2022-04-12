package com.meta.portal.sdk.app;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

public class SmartCameraActivity extends FeatureBaseActivity {

    protected @LayoutRes
    int getFeatureLayoutResId() {
        return R.layout.activity_smartcamera;
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.smart_camera_feature_info_header;
    }

    protected @StringRes int getFeatureInfoTextResId() {
        return R.string.smart_camera_feature_info_text;
    }

}
