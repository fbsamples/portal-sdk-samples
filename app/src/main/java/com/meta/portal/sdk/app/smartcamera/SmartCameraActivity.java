/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.smartcamera;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.base.BaseActivity;
import com.meta.portal.sdk.app.smartcameraEditor.SmartCameraEditorActivity;

public class SmartCameraActivity extends BaseActivity {

  Button mWithUIButton;
  Button mWithoutUIButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_smart_camera);

    mWithUIButton = (Button) findViewById(R.id.with_ui_button);
    mWithoutUIButton = (Button) findViewById(R.id.without_ui_button);

    mWithUIButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(SmartCameraActivity.this, SmartCameraEditorActivity.class);
            startActivity(intent);
          }
        });

    mWithoutUIButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(SmartCameraActivity.this, CameraActivity.class);
            startActivity(intent);
          }
        });
  }
}
