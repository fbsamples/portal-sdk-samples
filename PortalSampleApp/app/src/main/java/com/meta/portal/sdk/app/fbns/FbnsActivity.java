package com.meta.portal.sdk.app.fbns;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.base.ActivityCallback;
import com.meta.portal.sdk.app.base.FeatureBaseActivity;

public class FbnsActivity extends FeatureBaseActivity {

  private Fragment mFragment;

  protected Fragment getFragment() {
    mFragment = FbnsFragment.newInstance();
    return mFragment;
  }

  protected @StringRes int getFeatureInfoHeaderResId() {
    return R.string.fbns_feature_info_header;
  }

  protected @StringRes int getFeatureInfoTextResId() {
    return R.string.fbns_feature_info_text;
  }

  protected void updateDebugModeLayoutContainerVisibility(boolean visible) {}

  protected void setFeatureInfoShowing(boolean showing) {
    ((FbnsFragment) mFragment).setFeatureInfoShowing(showing);
  }

  protected void setActivityCallback(ActivityCallback activityCallback) {
    ((FbnsFragment) mFragment).setActivityCallback(activityCallback);
  }
}
