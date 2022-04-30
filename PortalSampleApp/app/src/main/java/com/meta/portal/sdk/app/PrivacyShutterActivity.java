package com.meta.portal.sdk.app;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

public class PrivacyShutterActivity extends FeatureBaseActivity {

    protected Fragment getFragment() {
        return PrivacyShutterFragment.newInstance();
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.privacy_screen_feature_info_header;
    }

    protected @StringRes int getFeatureInfoTextResId() {
        return R.string.privacy_screen_feature_info_text;
    }

}
