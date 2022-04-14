package com.meta.portal.sdk.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

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
