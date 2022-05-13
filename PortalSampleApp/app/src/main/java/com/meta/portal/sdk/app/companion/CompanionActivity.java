package com.meta.portal.sdk.app.companion;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.meta.portal.sdk.app.base.FeatureBaseActivity;
import com.meta.portal.sdk.app.R;

public class CompanionActivity extends FeatureBaseActivity {

    protected Fragment getFragment() {
        return CompanionFragment.newInstance();
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.companion_feature_info_header;
    }

    protected @StringRes int getFeatureInfoTextResId() {
        return R.string.companion_feature_info_text;
    }

    protected void updateDebugModeLayoutContainerVisibility(boolean visible) {
    }

}
