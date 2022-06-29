package com.meta.portal.sdk.app.fbns;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.base.FeatureBaseActivity;

public class FbnsActivity extends FeatureBaseActivity {

    protected Fragment getFragment() {
        return FbnsFragment.newInstance();
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.fbns_feature_info_header;
    }

    protected @StringRes int getFeatureInfoTextResId() {
        return R.string.fbns_feature_info_text;
    }

    protected void updateDebugModeLayoutContainerVisibility(boolean visible) {
    }

    protected void setFeatureInfoShowing(boolean showing) {
    }

}
