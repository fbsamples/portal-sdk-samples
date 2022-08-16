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
