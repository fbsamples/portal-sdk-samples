/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.fbns;

import android.content.Context;
import com.meta.portal.sdk.app.data.ListData;
import java.util.ArrayList;
import java.util.List;

public class FbnsDataHelper {

  final List<ListData> mFbnsData = new ArrayList<>();
  private final Context mContext;

  public FbnsDataHelper(Context context) {
    mContext = context.getApplicationContext();
    initFbnsData();
  }

  public List<ListData> getFbnsData() {
    return mFbnsData;
  }

  public void updatePushToken(String token) {
    for (ListData data : mFbnsData) {
      if (data.getStepType() == ListData.STEP_TYPE.REGISTER_TOKEN) {
        data.setValueText(token);
      }
    }
  }

  public void updateReceivedMessage(String msg) {
    for (ListData data : mFbnsData) {
      if (data.getStepType() == ListData.STEP_TYPE.RECEIVED_MSG) {
        data.setValueText(msg);
      }
    }
  }

  private void initFbnsData() {
    ListData fbnsDataContact = new ListData();
    fbnsDataContact.setCardTitle("Contact Meta to get your app enabled for FBNS");
    fbnsDataContact.setStep(1);
    fbnsDataContact.setInfoTitle("Contact Meta to get your app enabled for FBNS");
    fbnsDataContact.setInfoText(
        "FBNS is currently in public Beta and is not generally available. "
            + "In order for your App to use FBNS, please reach out to your Meta contact to enable FBNS for your App."
            + " You would need to provide your App's signature hash and package name.  ");
    fbnsDataContact.setInfoLink("Learn more at developers.facebook.com");
    fbnsDataContact.setType(ListData.STEP_TYPE.DEFAULT);
    mFbnsData.add(fbnsDataContact);

    ListData fbnsDataIntegrateSDK = new ListData();
    fbnsDataIntegrateSDK.setCardTitle("Integrate with FBNS client SDK");
    fbnsDataIntegrateSDK.setStep(2);
    fbnsDataIntegrateSDK.setInfoTitle("Integrate with FBNS client SDK");
    fbnsDataIntegrateSDK.setInfoText(
        "FBNS Client SDK abstracts the complexity of integrating with FBNS backend and Graph APIs.\n\n"
            + "The SDK would enable you to "
            + "a)Obtain and Register a Push token"
            + " and b)Provide Callbacks to receive Push messages from your cloud servers through FBNS.");
    fbnsDataIntegrateSDK.setInfoLink("Learn more at developers.facebook.com");
    fbnsDataIntegrateSDK.setType(ListData.STEP_TYPE.DEFAULT);
    mFbnsData.add(fbnsDataIntegrateSDK);

    ListData fbnsDataRegisterToken = new ListData();
    fbnsDataRegisterToken.setCardTitle("Obtain and Register a push token");
    fbnsDataRegisterToken.setStep(3);
    fbnsDataRegisterToken.setInfoTitle("Obtain and Register a push token");
    fbnsDataRegisterToken.setInfoText(
        "Push tokens are unique per app instance and enables your cloud backend to specifically target and App instance. \n"
            + "\nFBNS SDK enables you to obtain and register a push token for an App instance.");
    fbnsDataRegisterToken.setInfoLink("Learn more at developers.facebook.com");
    fbnsDataRegisterToken.setValueText("");
    fbnsDataRegisterToken.setType(ListData.STEP_TYPE.REGISTER_TOKEN);
    mFbnsData.add(fbnsDataRegisterToken);

    ListData fbnsDataSendMsg = new ListData();
    fbnsDataSendMsg.setCardTitle("Send Push messages (JSON Formatted)");
    fbnsDataSendMsg.setStep(4);
    fbnsDataSendMsg.setInfoTitle("Send Push Messages");
    fbnsDataSendMsg.setInfoText(
        "Push Messages are Server originated and delivered to your App through FBNS. \n"
            + "\nPush Messages needs to be JSON formatted for them to be delivered through FBNS. \n"
            + "\nWe have implemented sending push messages from this App to demo the workflow that would typically be implemented on Server");
    fbnsDataSendMsg.setInfoLink("Learn more at developers.facebook.com");
    fbnsDataSendMsg.setType(ListData.STEP_TYPE.SEND_MSG);
    mFbnsData.add(fbnsDataSendMsg);

    ListData fbnsDataRcvdMsg = new ListData();
    fbnsDataRcvdMsg.setCardTitle("Receive a Push Message");
    fbnsDataRcvdMsg.setStep(5);
    fbnsDataRcvdMsg.setInfoTitle("Receive a Push Message");
    fbnsDataRcvdMsg.setInfoText(
        "FBNS Client SDK delivers the Server orginated Push messages to your App through Broadcast Receivers.");
    fbnsDataRcvdMsg.setInfoLink("Learn more at developers.facebook.com");
    fbnsDataRcvdMsg.setValueText("Received message will appear here..");
    fbnsDataRcvdMsg.setType(ListData.STEP_TYPE.RECEIVED_MSG);
    mFbnsData.add(fbnsDataRcvdMsg);
  }
}
