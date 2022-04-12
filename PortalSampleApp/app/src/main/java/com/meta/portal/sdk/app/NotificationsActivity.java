package com.meta.portal.sdk.app;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

public class NotificationsActivity extends FeatureBaseActivity {

    protected @LayoutRes
    int getFeatureLayoutResId() {
        return R.layout.activity_notifications;
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.notification_feature_info_header;
    }

    protected @StringRes int getFeatureInfoTextResId() {
        return R.string.notification_feature_info_text;
    }

}
