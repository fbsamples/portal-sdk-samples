package com.meta.portal.sdk.app;

public class Feature {
    
    private final String mScreenName;
    private final String mDemoModeTitle;
    private final Class<?> mClassName;
    
    public Feature(final String screenName, final String demoModeTitle, final Class<?> className) {
        mScreenName = screenName;
        mDemoModeTitle = demoModeTitle;
        mClassName = className;
    }

    public String getScreenName() {
        return mScreenName;
    }

    public String getDemoModeTitle() {
        return mDemoModeTitle;
    }

    public Class<?> getClassName() {
        return mClassName;
    }

}
