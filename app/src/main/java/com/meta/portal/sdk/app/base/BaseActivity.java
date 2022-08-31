/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.base;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

  @Override
  protected void onResume() {
    super.onResume();
    updateSystemUiVisibility();
  }

  public void updateSystemUiVisibility() {
    return;
  }
}
