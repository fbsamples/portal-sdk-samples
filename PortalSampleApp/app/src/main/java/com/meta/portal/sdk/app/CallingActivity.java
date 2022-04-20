package com.meta.portal.sdk.app;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

public class CallingActivity extends FeatureBaseActivity {

    protected @LayoutRes
    int getFeatureLayoutResId() {
        return R.layout.activity_calling;
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
