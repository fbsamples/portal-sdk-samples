package com.meta.portal.sdk.app.data;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

public class Feature {
    
    private String mScreenName;
    private String mClassNameTitle;
    private @DrawableRes int mBackgroundResourceId;
    private @ColorInt int mBackgroundColor;
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

    public void setBackgroundColor(final @ColorInt int color) {
        mBackgroundColor = color;
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

    public @ColorInt int getBackgroundResourceColor() {
        return mBackgroundColor;
    }

    public Class<?> getClassName() {
        return mClassName;
    }

}
