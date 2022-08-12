package com.meta.portal.sdk.app.fbns;

import android.content.Context;

import com.meta.portal.sdk.app.data.FbnsData;

import java.util.ArrayList;
import java.util.List;

public class FbnsHelper {
    
    final List<FbnsData> mFbnsData = new ArrayList<>();
    
    private final Context mContext;
    private Callback mCallback;

    public FbnsHelper(Context context) {
        mContext = context.getApplicationContext();
        initFbnsData();
    }

    public void setFinished(final int step, final boolean finished) {
        FbnsStorage.setFinished(mContext, step, finished);
        mFbnsData.get(step).setFinished(finished);
        mCallback.onFbnsDataChanged();
    }

    public List<FbnsData> getFbnsData() {
        return mFbnsData;
    }

    public void setCallback(final Callback callback) {
        mCallback = callback;
    }

    private void initFbnsData() {
        FbnsData fbnsDataContact = new FbnsData();
        fbnsDataContact.setCardTitle("Contact Meta to get your app enabled for FBNS");
        fbnsDataContact.setStep(1);
        fbnsDataContact.setAlertDialogTitle("Key Hash Generation");
        fbnsDataContact.setAlertDialogInformation1("Go to fb.developer.com/1983737boop to generate a hash key.");
        fbnsDataContact.setAlertDialogInformation2("You should receive it within 48 hours at the email you provided.");
        fbnsDataContact.setAlertDialogButton("I have received the hash key");
        fbnsDataContact.setFinished(FbnsStorage.getFinished(mContext, 0));
        fbnsDataContact.setInfoTitle("Contact Meta to get your app enabled for FBNS");
        fbnsDataContact.setInfoText("lorem Ipsum, more details on how to integrate right here.");
        fbnsDataContact.setInfoLink("Learn more at developers.meta.com/19918829");
        mFbnsData.add(fbnsDataContact);

        FbnsData fbnsDataKeyHashIntegrate = new FbnsData();
        fbnsDataKeyHashIntegrate.setCardTitle("Integrate with FBNS client SDK");
        fbnsDataKeyHashIntegrate.setStep(2);
        fbnsDataKeyHashIntegrate.setAlertDialogTitle("Key Hash Generation");
        fbnsDataKeyHashIntegrate.setAlertDialogInformation1("Go to fb.developer.com/1983737boop to generate a hash key.");
        fbnsDataKeyHashIntegrate.setAlertDialogInformation2("You should receive it within 48 hours at the email you provided.");
        fbnsDataKeyHashIntegrate.setAlertDialogButton("I have received the hash key");
        fbnsDataKeyHashIntegrate.setFinished(FbnsStorage.getFinished(mContext, 1));
        fbnsDataKeyHashIntegrate.setInfoTitle("Integrate with FBNS client SDK");
        fbnsDataKeyHashIntegrate.setInfoText("lorem Ipsum, more details on how to integrate right here.");
        fbnsDataKeyHashIntegrate.setInfoLink("Learn more at developers.meta.com/19918829");
        mFbnsData.add(fbnsDataKeyHashIntegrate);

        FbnsData fbnsDataRegister = new FbnsData();
        fbnsDataRegister.setCardTitle("Register a push token");
        fbnsDataRegister.setStep(3);
        fbnsDataRegister.setAlertDialogTitle("Key Hash Generation");
        fbnsDataRegister.setAlertDialogInformation1("Go to fb.developer.com/1983737boop to generate a hash key.");
        fbnsDataRegister.setAlertDialogInformation2("You should receive it within 48 hours at the email you provided.");
        fbnsDataRegister.setAlertDialogButton("I have received the hash key");
        fbnsDataRegister.setFinished(FbnsStorage.getFinished(mContext, 2));
        fbnsDataRegister.setInfoTitle("Register a push token");
        fbnsDataRegister.setInfoText("lorem Ipsum, more details on how to integrate right here.");
        fbnsDataRegister.setInfoLink("Learn more at developers.meta.com/19918829");
        mFbnsData.add(fbnsDataRegister);

        FbnsData fbnsDataReceivePushToken = new FbnsData();
        fbnsDataReceivePushToken.setCardTitle("Send a test message:");
        fbnsDataReceivePushToken.setStep(4);
        fbnsDataReceivePushToken.setAlertDialogTitle("Key Hash Generation");
        fbnsDataReceivePushToken.setAlertDialogInformation1("Go to fb.developer.com/1983737boop to generate a hash key.");
        fbnsDataReceivePushToken.setAlertDialogInformation2("You should receive it within 48 hours at the email you provided.");
        fbnsDataReceivePushToken.setAlertDialogButton("I have received the hash key");
        fbnsDataReceivePushToken.setFinished(FbnsStorage.getFinished(mContext, 3));
        fbnsDataReceivePushToken.setInfoTitle("Send a test message:");
        fbnsDataReceivePushToken.setInfoText("lorem Ipsum, more details on how to integrate right here.");
        fbnsDataReceivePushToken.setInfoLink("Learn more at developers.meta.com/19918829");
        mFbnsData.add(fbnsDataReceivePushToken);
    }
}
