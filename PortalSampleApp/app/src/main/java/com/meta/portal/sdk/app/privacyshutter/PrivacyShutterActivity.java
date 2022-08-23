package com.meta.portal.sdk.app.privacyshutter;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.base.ActivityCallback;
import com.meta.portal.sdk.app.base.FeatureBaseActivity;

public class PrivacyShutterActivity extends FeatureBaseActivity {

  private Fragment mFragment;

  protected Fragment getFragment() {
    mFragment = PrivacyShutterCameraFragment.Companion.newInstance();
    return mFragment;
  }

  protected @StringRes int getFeatureInfoHeaderResId() {
    return R.string.privacy_screen_feature_info_header;
  }

  protected @StringRes int getFeatureInfoTextResId() {
    return R.string.privacy_screen_feature_info_text;
  }

  protected void updateDebugModeLayoutContainerVisibility(boolean visible) {
    ((PrivacyShutterCameraFragment) mFragment).updateDebugModeLayoutContainerVisibility(visible);
  }

  protected void setFeatureInfoShowing(boolean showing) {
    ((PrivacyShutterCameraFragment) mFragment).setFeatureInfoShowing(showing);
  }

  protected void setActivityCallback(ActivityCallback activityCallback) {}

  protected boolean topAppBarTransparent() {
    return true;
  }

}
