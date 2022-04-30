package com.meta.portal.sdk.app;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

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

}
