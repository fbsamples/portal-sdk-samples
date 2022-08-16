package com.meta.portal.sdk.app.fbns.ui;

import com.meta.portal.sdk.app.fbns.FbnsData;

public interface FbnsUiListener {
    void onInfoButtonClicked(FbnsData fbnsData);
    void onActionButtonClicked(FbnsData fbnsData);
}
