package com.meta.portal.sdk.app.fbns;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class FbnsDataHelper {

    final List<FbnsData> mFbnsData = new ArrayList<>();
    private final Context mContext;

    public FbnsDataHelper(Context context) {
        mContext = context.getApplicationContext();
        initFbnsData();
    }

    public List<FbnsData> getFbnsData() {
        return mFbnsData;
    }

    public void updatePushToken(String token){
        for (FbnsData data:mFbnsData) {
            if(data.getStepType() == FbnsData.STEP_TYPE.REGISTER_TOKEN){
                data.setValueText(token);
            }
        }
    }

    public void updateReceivedMessage(String msg){
        for (FbnsData data:mFbnsData) {
            if(data.getStepType() == FbnsData.STEP_TYPE.RECEIVED_MSG){
                data.setValueText(msg);
            }
        }
    }

    private void initFbnsData() {
        FbnsData fbnsDataContact = new FbnsData();
        fbnsDataContact.setCardTitle("Contact Meta to get your app enabled for FBNS");
        fbnsDataContact.setStep(1);
        fbnsDataContact.setInfoTitle("Contact Meta to get your app enabled for FBNS");
        fbnsDataContact.setInfoText("lorem Ipsum, more details on how to integrate right here.");
        fbnsDataContact.setInfoLink("Learn more at developers.meta.com/19918829");
        fbnsDataContact.setType(FbnsData.STEP_TYPE.DEFAULT);
        mFbnsData.add(fbnsDataContact);

        FbnsData fbnsDataIntegrateSDK = new FbnsData();
        fbnsDataIntegrateSDK.setCardTitle("Integrate with FBNS client SDK");
        fbnsDataIntegrateSDK.setStep(2);
        fbnsDataIntegrateSDK.setInfoTitle("Integrate with FBNS client SDK");
        fbnsDataIntegrateSDK.setInfoText("lorem Ipsum, more details on how to integrate right here.");
        fbnsDataIntegrateSDK.setInfoLink("Learn more at developers.meta.com/19918829");
        fbnsDataIntegrateSDK.setType(FbnsData.STEP_TYPE.DEFAULT);
        mFbnsData.add(fbnsDataIntegrateSDK);

        FbnsData fbnsDataRegisterToken = new FbnsData();
        fbnsDataRegisterToken.setCardTitle("Register a push token");
        fbnsDataRegisterToken.setStep(3);
        fbnsDataRegisterToken.setInfoTitle("Register a push token");
        fbnsDataRegisterToken.setInfoText("lorem Ipsum, more details on how to integrate right here.");
        fbnsDataRegisterToken.setInfoLink("Learn more at developers.meta.com/19918829");
        fbnsDataRegisterToken.setValueText("");
        fbnsDataRegisterToken.setType(FbnsData.STEP_TYPE.REGISTER_TOKEN);
        mFbnsData.add(fbnsDataRegisterToken);

        FbnsData fbnsDataSendMsg = new FbnsData();
        fbnsDataSendMsg.setCardTitle("Send a test message:");
        fbnsDataSendMsg.setStep(4);
        fbnsDataSendMsg.setInfoTitle("Send a test message:");
        fbnsDataSendMsg.setInfoText("lorem Ipsum, more details on how to integrate right here.");
        fbnsDataSendMsg.setInfoLink("Learn more at developers.meta.com/19918829");
        fbnsDataSendMsg.setType(FbnsData.STEP_TYPE.SEND_MSG);
        mFbnsData.add(fbnsDataSendMsg);

        FbnsData fbnsDataRcvdMsg = new FbnsData();
        fbnsDataRcvdMsg.setCardTitle("Received Message:");
        fbnsDataRcvdMsg.setStep(5);
        fbnsDataRcvdMsg.setInfoTitle("Received Message:");
        fbnsDataRcvdMsg.setInfoText("lorem Ipsum, more details on how to integrate right here.");
        fbnsDataRcvdMsg.setInfoLink("Learn more at developers.meta.com/19918829");
        fbnsDataRcvdMsg.setValueText("Received message will appear here..");
        fbnsDataRcvdMsg.setType(FbnsData.STEP_TYPE.RECEIVED_MSG);
        mFbnsData.add(fbnsDataRcvdMsg);
    }
}
