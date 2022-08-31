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
import android.animation.ObjectAnimator;
import android.view.View;

public class TopAppBarAnimationController {

  private static final String TAG = TopAppBarAnimationController.class.getSimpleName();

  private static final int TOP_APP_BAR_FADE_IN_DURATION = 200;
  private static final int TOP_APP_BAR_FADE_OUT_DURATION = 200;

  private final View mTopAppBarView;

  private ObjectAnimator mTopAppBarFadeInXAnimator;
  private ObjectAnimator mTopAppBarFadeOutXAnimator;

  public TopAppBarAnimationController(View topAppBarView) {
    mTopAppBarView = topAppBarView;
  }

  public void startTopAppBarViewInAnimation(AnimationFinishedCallback animationFinishedCallback) {
    startTopAppBarViewFadeInAnimation(animationFinishedCallback);
  }

  public void startTopAppBarViewOutAnimation() {
    startTopAppBarViewFadeOutAnimation();
  }

  private void startTopAppBarViewFadeInAnimation(
      AnimationFinishedCallback animationFinishedCallback) {
    if ((mTopAppBarFadeInXAnimator != null && mTopAppBarFadeInXAnimator.isRunning())) {
      return;
    }
    mTopAppBarView.setVisibility(View.VISIBLE);
    mTopAppBarFadeInXAnimator = ObjectAnimator.ofFloat(mTopAppBarView, "alpha", 1.0f);
    mTopAppBarFadeInXAnimator.setDuration(TOP_APP_BAR_FADE_IN_DURATION);
    mTopAppBarFadeInXAnimator.addListener(
        new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            animationFinishedCallback.animationFinished();
          }
        });
    mTopAppBarFadeInXAnimator.start();
  }

  private void startTopAppBarViewFadeOutAnimation() {
    if ((mTopAppBarFadeOutXAnimator != null && mTopAppBarFadeOutXAnimator.isRunning())) {
      return;
    }
    mTopAppBarFadeOutXAnimator = ObjectAnimator.ofFloat(mTopAppBarView, "alpha", 0f);
    mTopAppBarFadeOutXAnimator.setDuration(TOP_APP_BAR_FADE_OUT_DURATION);
    mTopAppBarFadeOutXAnimator.addListener(
        new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            mTopAppBarView.setVisibility(View.GONE);
          }
        });
    mTopAppBarFadeOutXAnimator.start();
  }

  public interface AnimationFinishedCallback {
    void animationFinished();
  }
}
