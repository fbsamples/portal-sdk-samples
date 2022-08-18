package com.meta.portal.sdk.app.fbns.ui;

import androidx.annotation.DrawableRes;

import com.meta.portal.sdk.app.fbns.FbnsData;

public interface FbnsUiListener {
    void onInfoButtonClicked(FbnsData fbnsData);
    void onActionButtonClicked(FbnsData fbnsData);
    String getSendStatus();
    @DrawableRes int getSendStatusIcon();
    int getSendStatusColor();
    boolean getMessageSent();
}
