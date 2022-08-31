/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class ButtonAnimationControllerTv {

  private static final String TAG = ButtonAnimationControllerTv.class.getSimpleName();

  private static final int BUTTON_FADE_IN_DURATION = 200;
  private static final int BUTTON_FADE_OUT_DURATION = 200;

  private final View mModeButtons;
  private final View mMoveButtons;
  private final View mZoomButtons;
  private final View mDoneButton;

  private ObjectAnimator mModeButtonsFadeInAnimator;
  private ObjectAnimator mMoveButtonsFadeInAnimator;
  private ObjectAnimator mZoomButtonsFadeInAnimator;
  private ObjectAnimator mDoneButtonFadeInAnimator;

  private ObjectAnimator mModeButtonsFadeOutAnimator;
  private ObjectAnimator mMoveButtonsFadeOutAnimator;
  private ObjectAnimator mZoomButtonsFadeOutAnimator;
  private ObjectAnimator mDoneButtonFadeOutAnimator;

  private AnimatorSet mMoveButtonsFadeInAnimatorSet;
  private AnimatorSet mMoveButtonsFadeOutAnimatorSet;

  public ButtonAnimationControllerTv(
      View modeButtons, View moveButtons, View zoomButtons, View doneButton) {
    mModeButtons = modeButtons;
    mMoveButtons = moveButtons;
    mZoomButtons = zoomButtons;
    mDoneButton = doneButton;
  }

  public void startMoveButtonsInAnimation() {
    startMoveButtonsFadeInAnimation();
  }

  public void startModeButtonsInAnimation() {
    startModeButtonsFadeInAnimation();
  }

  public void startMoveButtonsFadeInAnimation() {
    if ((mModeButtonsFadeInAnimator != null && mModeButtonsFadeInAnimator.isRunning())
        || (mMoveButtonsFadeInAnimator != null && mMoveButtonsFadeInAnimator.isRunning())
        || (mZoomButtonsFadeInAnimator != null && mZoomButtonsFadeInAnimator.isRunning())
        || (mDoneButtonFadeInAnimator != null && mDoneButtonFadeInAnimator.isRunning())
        || (mModeButtonsFadeOutAnimator != null && mModeButtonsFadeOutAnimator.isRunning())
        || (mMoveButtonsFadeOutAnimator != null && mMoveButtonsFadeOutAnimator.isRunning())
        || (mZoomButtonsFadeOutAnimator != null && mZoomButtonsFadeOutAnimator.isRunning())
        || (mDoneButtonFadeOutAnimator != null && mDoneButtonFadeOutAnimator.isRunning())) {
      return;
    }
    mMoveButtons.setVisibility(View.VISIBLE);
    mZoomButtons.setVisibility(View.VISIBLE);
    mDoneButton.setVisibility(View.VISIBLE);
    mMoveButtons.setAlpha(0);
    mZoomButtons.setAlpha(0);
    mDoneButton.setAlpha(0);

    mModeButtonsFadeOutAnimator = ObjectAnimator.ofFloat(mModeButtons, "alpha", 0f);
    mModeButtonsFadeOutAnimator.setDuration(BUTTON_FADE_OUT_DURATION);
    mModeButtonsFadeOutAnimator.addListener(
        new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            mMoveButtonsFadeInAnimator = ObjectAnimator.ofFloat(mMoveButtons, "alpha", 1.0f);
            mMoveButtonsFadeInAnimator.setDuration(BUTTON_FADE_IN_DURATION);
            mZoomButtonsFadeInAnimator = ObjectAnimator.ofFloat(mZoomButtons, "alpha", 1.0f);
            mZoomButtonsFadeInAnimator.setDuration(BUTTON_FADE_IN_DURATION);
            mDoneButtonFadeInAnimator = ObjectAnimator.ofFloat(mDoneButton, "alpha", 1.0f);
            mDoneButtonFadeInAnimator.setDuration(BUTTON_FADE_IN_DURATION);

            mMoveButtonsFadeInAnimatorSet = new AnimatorSet();

            mMoveButtonsFadeInAnimatorSet.playTogether(
                mMoveButtonsFadeInAnimator, mZoomButtonsFadeInAnimator, mDoneButtonFadeInAnimator);

            mMoveButtonsFadeInAnimatorSet.addListener(
                new AnimatorListenerAdapter() {
                  @Override
                  public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mModeButtons.setVisibility(View.GONE);
                  }
                });

            mMoveButtonsFadeInAnimatorSet.start();
          }
        });
    mModeButtonsFadeOutAnimator.start();
  }

  public void startModeButtonsFadeInAnimation() {
    if ((mModeButtonsFadeInAnimator != null && mModeButtonsFadeInAnimator.isRunning())
        || (mMoveButtonsFadeInAnimator != null && mMoveButtonsFadeInAnimator.isRunning())
        || (mZoomButtonsFadeInAnimator != null && mZoomButtonsFadeInAnimator.isRunning())
        || (mDoneButtonFadeInAnimator != null && mDoneButtonFadeInAnimator.isRunning())
        || (mModeButtonsFadeOutAnimator != null && mModeButtonsFadeOutAnimator.isRunning())
        || (mMoveButtonsFadeOutAnimator != null && mMoveButtonsFadeOutAnimator.isRunning())
        || (mZoomButtonsFadeOutAnimator != null && mZoomButtonsFadeOutAnimator.isRunning())
        || (mDoneButtonFadeOutAnimator != null && mDoneButtonFadeOutAnimator.isRunning())) {
      return;
    }
    mModeButtons.setVisibility(View.VISIBLE);
    mModeButtons.setAlpha(0);

    mMoveButtonsFadeOutAnimator = ObjectAnimator.ofFloat(mMoveButtons, "alpha", 0f);
    mMoveButtonsFadeOutAnimator.setDuration(BUTTON_FADE_IN_DURATION);
    mZoomButtonsFadeOutAnimator = ObjectAnimator.ofFloat(mZoomButtons, "alpha", 0f);
    mZoomButtonsFadeOutAnimator.setDuration(BUTTON_FADE_IN_DURATION);
    mDoneButtonFadeOutAnimator = ObjectAnimator.ofFloat(mDoneButton, "alpha", 0f);
    mDoneButtonFadeOutAnimator.setDuration(BUTTON_FADE_IN_DURATION);

    mMoveButtonsFadeOutAnimatorSet = new AnimatorSet();

    mMoveButtonsFadeOutAnimatorSet.playTogether(
        mMoveButtonsFadeOutAnimator, mZoomButtonsFadeOutAnimator, mDoneButtonFadeOutAnimator);

    mMoveButtonsFadeOutAnimatorSet.addListener(
        new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            mModeButtonsFadeInAnimator = ObjectAnimator.ofFloat(mModeButtons, "alpha", 1.0f);
            mModeButtonsFadeInAnimator.setDuration(BUTTON_FADE_IN_DURATION);

            mModeButtonsFadeInAnimator.start();
            mMoveButtons.setVisibility(View.GONE);
            mZoomButtons.setVisibility(View.GONE);
            mDoneButton.setVisibility(View.GONE);
          }
        });

    mMoveButtonsFadeOutAnimatorSet.start();
  }
}
