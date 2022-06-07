// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.portal.sdk.app.smartcamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.facebook.portal.smartcamera.SmartCameraEditor;
import com.meta.portal.sdk.app.R;

public class CameraActivity extends Activity implements CameraEditorLauncher {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    Log.i("CameraActivity", "onCreate: " + getIntent().toString());
    if (null == savedInstanceState) {
      getFragmentManager()
          .beginTransaction()
          .replace(R.id.container, new CameraFragment())
          .commit();
    }
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    Log.w("CameraActivity", "onNewIntent: " + intent.toString());
  }

  @Override
  public void onLaunchClicked() {
    SmartCameraEditor.launchSmartCameraEditor(getBaseContext());
  }
}
