package com.meta.portal.sdk.app;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

public class CompanionActivity extends FeatureBaseActivity {

    protected @LayoutRes
    int getFeatureLayoutResId() {
        return R.layout.activity_companion;
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.companion_feature_info_header;
    }

    protected @StringRes int getFeatureInfoTextResId() {
        return R.string.companion_feature_info_text;
    }

}
