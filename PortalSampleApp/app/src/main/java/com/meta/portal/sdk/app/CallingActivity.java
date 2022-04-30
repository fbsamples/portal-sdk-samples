package com.meta.portal.sdk.app;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

public class CallingActivity extends FeatureBaseActivity {

    protected Fragment getFragment() {
        return CallingFragment.newInstance();
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.calling_feature_info_header;
    }

    protected @StringRes
    int getFeatureInfoTextResId() {
        return R.string.calling_feature_info_text;
    }

}
