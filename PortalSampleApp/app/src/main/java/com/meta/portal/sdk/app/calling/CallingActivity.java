package com.meta.portal.sdk.app.calling;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.meta.portal.sdk.app.base.FeatureBaseActivity;
import com.meta.portal.sdk.app.R;

public class CallingActivity extends FeatureBaseActivity {

    protected Fragment getFragment() {
        return CallingFragment.newInstance();
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.calling_feature_info_header;
    }

    protected @StringRes
    int getFeatureInfoTextResId() {
        return R.string.calling_feature_info_text;
    }

}