package com.meta.portal.sdk.app.data;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

public class FbnsData {
    private String mCardTitle;
    private boolean mFinished;
    private int mStep;
    private String mAlertDialogTitle;
    private String mAlertDialogInformation1;
    private String mAlertDialogInformation2;
    private String mAlertDialogButton;

    public void setCardTitle(final String screenName) {
        mCardTitle = screenName;
    }

    public void setFinished(final boolean finished) {
        mFinished = finished;
    }

    public void setStep(final int step) {
        mStep = step;
    }

    public void setAlertDialogTitle(final String alertDialogTitle) {
        mAlertDialogTitle = alertDialogTitle;
    }

    public void setAlertDialogInformation1(final String alertDialogInformation1) {
        mAlertDialogInformation1 = alertDialogInformation1;
    }

    public void setAlertDialogInformation2(final String alertDialogInformation2) {
        mAlertDialogInformation2 = alertDialogInformation2;
    }

    public void setAlertDialogButton(final String alertDialogButton) {
        mAlertDialogButton = alertDialogButton;
    }

    public String getCardTitle() {
        return mCardTitle;
    }

    public boolean getFinished() {
        return mFinished;
    }

    public int getStep() {
        return mStep;
    }

    public String getAlertDialogTitle() {
        return mAlertDialogTitle;
    }

    public String getAlertDialogInformation1() {
        return mAlertDialogInformation1;
    }

    public String getAlertDialogInformation2() {
        return mAlertDialogInformation2;
    }

    public String getAlertDialogButton() {
        return mAlertDialogButton;
    }

}
