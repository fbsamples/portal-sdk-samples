package com.meta.portal.sdk.app;

import androidx.annotation.DrawableRes;

public class Feature {
    
    private String mScreenName;
    private String mClassNameTitle;
    private @DrawableRes int mBackgroundResourceId;
    private Class<?> mClassName;

    public void setScreenName(final String screenName) {
        mScreenName = screenName;
    }

    public void setClassNameTitle(final String classNameTitle) {
        mClassNameTitle = classNameTitle;
    }

    public void setBackgroundResourceId(final @DrawableRes int backgroundResourceId) {
        mBackgroundResourceId = backgroundResourceId;
    }

    public void setClassName(final Class<?> className) {
        mClassName = className;
    }

    public String getScreenName() {
        return mScreenName;
    }

    public String getClassNameTitle() {
        return mClassNameTitle;
    }

    public @DrawableRes int getBackgroundResourceId() {
        return mBackgroundResourceId;
    }

    public Class<?> getClassName() {
        return mClassName;
    }

}
