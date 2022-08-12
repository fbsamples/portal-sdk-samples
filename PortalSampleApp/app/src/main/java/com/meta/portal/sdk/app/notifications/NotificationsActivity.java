package com.meta.portal.sdk.app.notifications;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.meta.portal.sdk.app.base.ActivityCallback;
import com.meta.portal.sdk.app.base.FeatureBaseActivity;
import com.meta.portal.sdk.app.R;

public class NotificationsActivity extends FeatureBaseActivity {

    protected Fragment getFragment() {
        return NotificationsFragment.newInstance();
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.notification_feature_info_header;
    }

    protected @StringRes int getFeatureInfoTextResId() {
        return R.string.notification_feature_info_text;
    }

    protected void updateDebugModeLayoutContainerVisibility(boolean visible) {
    }

    protected void setFeatureInfoShowing(boolean showing) {
    }

    protected void setActivityCallback(ActivityCallback activityCallback) {
    }

}
