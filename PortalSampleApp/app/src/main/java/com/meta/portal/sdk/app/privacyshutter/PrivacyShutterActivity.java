package com.meta.portal.sdk.app.privacyshutter;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.meta.portal.sdk.app.base.FeatureBaseActivity;
import com.meta.portal.sdk.app.R;

public class PrivacyShutterActivity extends FeatureBaseActivity {

    protected Fragment getFragment() {
        return PrivacyShutterCameraFragment.Companion.newInstance();
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.privacy_screen_feature_info_header;
    }

    protected @StringRes int getFeatureInfoTextResId() {
        return R.string.privacy_screen_feature_info_text;
    }

}
