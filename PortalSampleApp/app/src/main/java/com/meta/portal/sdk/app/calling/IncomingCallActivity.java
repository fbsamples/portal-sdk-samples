package com.meta.portal.sdk.app.calling;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.base.ActivityCallback;
import com.meta.portal.sdk.app.base.FeatureBaseActivity;

public class IncomingCallActivity extends FeatureBaseActivity {

    private Fragment mFragment;

    protected Fragment getFragment() {
        mFragment = IncomingCallFragment.Companion.newInstance();
        return mFragment;
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.calling_feature_info_header;
    }

    protected @StringRes int getFeatureInfoTextResId() {
        return R.string.calling_feature_info_text;
    }

    protected void updateDebugModeLayoutContainerVisibility(boolean visible) {
        ((OutgoingCallFragment) mFragment).updateDebugModeLayoutContainerVisibility(visible);
    }

    protected void setFeatureInfoShowing(boolean showing) {
        ((OutgoingCallFragment) mFragment).setFeatureInfoShowing(showing);
    }

    protected void setActivityCallback(ActivityCallback activityCallback) {}

    protected boolean topAppBarTransparent() {
        return true;
    }

}
