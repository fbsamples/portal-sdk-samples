package com.meta.portal.sdk.app;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

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

}
