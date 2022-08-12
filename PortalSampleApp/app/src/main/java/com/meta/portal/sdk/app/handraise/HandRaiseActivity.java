package com.meta.portal.sdk.app.handraise;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.meta.portal.sdk.app.base.ActivityCallback;
import com.meta.portal.sdk.app.base.FeatureBaseActivity;
import com.meta.portal.sdk.app.R;

public class HandRaiseActivity extends FeatureBaseActivity {

    protected Fragment getFragment() {
        return HandRaiseFragment.newInstance();
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.handraise_feature_info_header;
    }

    protected @StringRes int getFeatureInfoTextResId() {
        return R.string.handraise_feature_info_text;
    }

    protected void updateDebugModeLayoutContainerVisibility(boolean visible) {
    }

    protected void setFeatureInfoShowing(boolean showing) {
    }

    protected void setActivityCallback(ActivityCallback activityCallback) {
    }

}
