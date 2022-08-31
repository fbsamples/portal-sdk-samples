/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;

public class Utils {
  public static boolean isTvDevice(Context context) {
    UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
    if (uiModeManager == null) {
      return false;
    }
    boolean isTvDevice =
        (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION);
    return isTvDevice;
  }
}
