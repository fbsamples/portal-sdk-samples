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
        FbnsData fbnsDataIntegrate = new FbnsData();
        fbnsDataIntegrate.setCardTitle("Integrate with FBNS \nclient SDK");
        fbnsDataIntegrate.setStep(0);
        fbnsDataIntegrate.setAlertDialogTitle("Key Hash Generation");
        fbnsDataIntegrate.setAlertDialogInformation1("Go to fb.developer.com/1983737boop to generate a hash key.");
        fbnsDataIntegrate.setAlertDialogInformation2("You should receive it within 48 hours at the email you provided.");
        fbnsDataIntegrate.setAlertDialogButton("I have received the hash key");
        fbnsDataIntegrate.setFinished(FbnsStorage.getFinished(mContext, 0));
        mFbnsData.add(fbnsDataIntegrate);

        FbnsData fbnsDataKeyHashGeneration = new FbnsData();
        fbnsDataKeyHashGeneration.setCardTitle("Key hash generation");
        fbnsDataKeyHashGeneration.setStep(1);
        fbnsDataKeyHashGeneration.setAlertDialogTitle("Key Hash Generation");
        fbnsDataKeyHashGeneration.setAlertDialogInformation1("Go to fb.developer.com/1983737boop to generate a hash key.");
        fbnsDataKeyHashGeneration.setAlertDialogInformation2("You should receive it within 48 hours at the email you provided.");
        fbnsDataKeyHashGeneration.setAlertDialogButton("I have received the hash key");
        fbnsDataKeyHashGeneration.setFinished(FbnsStorage.getFinished(mContext, 1));
        mFbnsData.add(fbnsDataKeyHashGeneration);

        FbnsData fbnsDataRegister = new FbnsData();
        fbnsDataRegister.setCardTitle("Register and obtain \nbackend token");
        fbnsDataRegister.setStep(2);
        fbnsDataRegister.setAlertDialogTitle("Key Hash Generation");
        fbnsDataRegister.setAlertDialogInformation1("Go to fb.developer.com/1983737boop to generate a hash key.");
        fbnsDataRegister.setAlertDialogInformation2("You should receive it within 48 hours at the email you provided.");
        fbnsDataRegister.setAlertDialogButton("I have received the hash key");
        fbnsDataRegister.setFinished(FbnsStorage.getFinished(mContext, 2));
        mFbnsData.add(fbnsDataRegister);

        FbnsData fbnsDataReceivePushToken = new FbnsData();
        fbnsDataReceivePushToken.setCardTitle("Receive push token \nand add it to app \ncode");
        fbnsDataReceivePushToken.setStep(3);
        fbnsDataReceivePushToken.setAlertDialogTitle("Key Hash Generation");
        fbnsDataReceivePushToken.setAlertDialogInformation1("Go to fb.developer.com/1983737boop to generate a hash key.");
        fbnsDataReceivePushToken.setAlertDialogInformation2("You should receive it within 48 hours at the email you provided.");
        fbnsDataReceivePushToken.setAlertDialogButton("I have received the hash key");
        fbnsDataReceivePushToken.setFinished(FbnsStorage.getFinished(mContext, 3));
        mFbnsData.add(fbnsDataReceivePushToken);

        FbnsData fbnsDataAuthenticate = new FbnsData();
        fbnsDataAuthenticate.setCardTitle("Authenticate the \ntoken with \nFB.developer.com \nservice");
        fbnsDataAuthenticate.setStep(4);
        fbnsDataAuthenticate.setAlertDialogTitle("Key Hash Generation");
        fbnsDataAuthenticate.setAlertDialogInformation1("Go to fb.developer.com/1983737boop to generate a hash key.");
        fbnsDataAuthenticate.setAlertDialogInformation2("You should receive it within 48 hours at the email you provided.");
        fbnsDataAuthenticate.setAlertDialogButton("I have received the hash key");
        fbnsDataAuthenticate.setFinished(FbnsStorage.getFinished(mContext, 4));
        mFbnsData.add(fbnsDataAuthenticate);

        FbnsData fbnsDataReceivePushSippet = new FbnsData();
        fbnsDataReceivePushSippet.setCardTitle("Receive a push with code sippet to add");
        fbnsDataReceivePushSippet.setStep(5);
        fbnsDataReceivePushSippet.setAlertDialogTitle("Key Hash Generation");
        fbnsDataReceivePushSippet.setAlertDialogInformation1("Go to fb.developer.com/1983737boop to generate a hash key.");
        fbnsDataReceivePushSippet.setAlertDialogInformation2("You should receive it within 48 hours at the email you provided.");
        fbnsDataReceivePushSippet.setAlertDialogButton("I have received the hash key");
        fbnsDataReceivePushSippet.setFinished(FbnsStorage.getFinished(mContext, 5));
        mFbnsData.add(fbnsDataReceivePushSippet);

        FbnsData fbnsDataReceivePushSetup = new FbnsData();
        fbnsDataReceivePushSetup.setCardTitle("Voila! Receive a \npush to confirm \nsetup of service!");
        fbnsDataReceivePushSetup.setStep(6);
        fbnsDataReceivePushSetup.setAlertDialogTitle("Key Hash Generation");
        fbnsDataReceivePushSetup.setAlertDialogInformation1("Go to fb.developer.com/1983737boop to generate a hash key.");
        fbnsDataReceivePushSetup.setAlertDialogInformation2("You should receive it within 48 hours at the email you provided.");
        fbnsDataReceivePushSetup.setAlertDialogButton("I have received the hash key");
        fbnsDataReceivePushSetup.setFinished(FbnsStorage.getFinished(mContext, 6));
        mFbnsData.add(fbnsDataReceivePushSetup);
    }
}
