package com.meta.portal.sdk.app;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

public class HandRaiseActivity extends FeatureBaseActivity {

    protected @LayoutRes
    int getFeatureLayoutResId() {
        return R.layout.activity_handraise;
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.handraise_feature_info_header;
    }

    protected @StringRes int getFeatureInfoTextResId() {
        return R.string.handraise_feature_info_text;
    }

}
