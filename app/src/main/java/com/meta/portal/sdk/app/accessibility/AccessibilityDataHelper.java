/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.accessibility;

import android.content.Context;
import com.meta.portal.sdk.app.data.ListData;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityDataHelper {

  final List<ListData> mAccessibilityData = new ArrayList<>();
  private final Context mContext;

  public AccessibilityDataHelper(Context context) {
    mContext = context.getApplicationContext();
    initFbnsData();
  }

  public List<ListData> getAccessibilityData() {
    return mAccessibilityData;
  }

  //  public void updatePushToken(String token) {
  //    for (FbnsData data : mFbnsData) {
  //      if (data.getStepType() == FbnsData.STEP_TYPE.REGISTER_TOKEN) {
  //        data.setValueText(token);
  //      }
  //    }
  //  }

  //  public void updateReceivedMessage(String msg) {
  //    for (FbnsData data : mFbnsData) {
  //      if (data.getStepType() == FbnsData.STEP_TYPE.RECEIVED_MSG) {
  //        data.setValueText(msg);
  //      }
  //    }
  //  }

  private void initFbnsData() {
    ListData accessibilityDataLabels = new ListData();
    accessibilityDataLabels.setCardTitle(
        "All screen elements have accessibility labels for screen readers");
    accessibilityDataLabels.setStep(1);
    accessibilityDataLabels.setInfoTitle(
        "All screen elements have accessibility labels for screen readers");
    accessibilityDataLabels.setInfoText(
        "All screen elements have accessibility labels for screen readers");
    accessibilityDataLabels.setType(ListData.STEP_TYPE.DEFAULT);
    accessibilityDataLabels.setInfoLink("Learn more at developers.facebook.com");
    mAccessibilityData.add(accessibilityDataLabels);

    ListData accessibilityDataColor = new ListData();
    accessibilityDataColor.setCardTitle(
        "All user interface elements conform to WCAG 2.1 color ratio requirements");
    accessibilityDataColor.setStep(2);
    accessibilityDataColor.setInfoTitle(
        "All user interface elements conform to WCAG 2.1 color ratio requirements");
    accessibilityDataColor.setInfoText(
        "All user interface elements conform to WCAG 2.1 color ratio requirements");
    accessibilityDataColor.setType(ListData.STEP_TYPE.DEFAULT);
    accessibilityDataColor.setInfoLink("Learn more at developers.facebook.com");
    mAccessibilityData.add(accessibilityDataColor);

    ListData accessibilityDataSpace = new ListData();
    accessibilityDataSpace.setCardTitle(
        "All user interface elements contain touch targets with sufficient space");
    accessibilityDataSpace.setStep(3);
    accessibilityDataSpace.setInfoTitle(
        "All user interface elements contain touch targets with sufficient space");
    accessibilityDataSpace.setInfoText(
        "All user interface elements contain touch targets with sufficient space");
    accessibilityDataSpace.setInfoLink("Learn more at developers.facebook.com");
    mAccessibilityData.add(accessibilityDataSpace);

    ListData accessibilityDataSettings = new ListData();
    accessibilityDataSettings.setCardTitle(
        "Go to Settings > Accessibility and select each of the available settings for font size, contrast, color inversion and correction ensuring your application:\n"
            + "\n  \u2022 Responds to the system accessibility settings"
            + "\n  \u2022 Text is readable"
            + "\n  \u2022 Is functional under each accessibility settings"
            + "\n     configuration");
    accessibilityDataSettings.setStep(4);
    accessibilityDataSettings.setInfoTitle(
        "Go to Settings > Accessibility and select each of the available settings for font size, contrast, color inversion and correction ensuring your application:");
    accessibilityDataSettings.setInfoText(
        "\n  \u2022 Responds to the system"
            + "\n     accessibility settings"
            + "\n  \u2022 Text is readable"
            + "\n  \u2022 Is functional under each"
            + "\n     accessibility settings"
            + "\n     configuration");
    accessibilityDataSettings.setType(ListData.STEP_TYPE.DEFAULT);
    accessibilityDataSettings.setInfoLink("Learn more at developers.facebook.com");
    mAccessibilityData.add(accessibilityDataSettings);
  }
}
