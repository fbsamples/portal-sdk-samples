package com.meta.portal.sdk.app;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

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

}
