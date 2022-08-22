package com.meta.portal.sdk.app.fbns;

public class FbnsData {
  private String mCardTitle;
  private int mStep;

  private String mInfoTitle;
  private String mInfoText;
  private String mInfoLink;

  private String mValueText;

  private STEP_TYPE mType;

  public void setCardTitle(final String screenName) {
    mCardTitle = screenName;
  }

  public void setStep(final int step) {
    mStep = step;
  }

  public void setInfoTitle(final String infoTitle) {
    mInfoTitle = infoTitle;
  }

  public void setInfoText(final String infoText) {
    mInfoText = infoText;
  }

  public void setInfoLink(final String infoLink) {
    mInfoLink = infoLink;
  }

  public void setValueText(final String valueText) {
    mValueText = valueText;
  }

  public void setType(final STEP_TYPE type) {
    mType = type;
  }

  public String getCardTitle() {
    return mCardTitle;
  }

  public int getStepIndex() {
    return mStep;
  }

  public String getInfoTitle() {
    return mInfoTitle;
  }

  public String getInfoText() {
    return mInfoText;
  }

  public String getInfoLink() {
    return mInfoLink;
  }

  public String getValueText() {
    return mValueText;
  }

  public STEP_TYPE getStepType() {
    return mType;
  }

  public enum STEP_TYPE {
    DEFAULT,
    REGISTER_TOKEN,
    SEND_MSG,
    RECEIVED_MSG
  }
}
