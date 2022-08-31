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
import android.view.ViewTreeObserver;
import android.view.animation.PathInterpolator;

public class FeatureInfoAnimationController {

  private static final String TAG = FeatureInfoAnimationController.class.getSimpleName();

  private static final int FEATURE_VIEW_ANIMATION_X_IN_DURATION = 300;
  private static final int FEATURE_VIEW_ANIMATION_X_OUT_DURATION = 200;

  private final View mFeatureInfoContainer;
  private final View mFeatureInfoContainerBackground;

  private int mFeatureInfoViewDefaultX;
  private int mFeatureInfoViewWidth;

  private ObjectAnimator mStartFeatureInfoViewInXAnimatorFirst;
  private ObjectAnimator mStartFeatureInfoViewInXAnimator;
  private ObjectAnimator mStartFeatureInfoViewOutXAnimator;

  public FeatureInfoAnimationController(View featureInfoView, View featureInfoContainerBackground) {
    mFeatureInfoContainer = featureInfoView;
    mFeatureInfoContainerBackground = featureInfoContainerBackground;
  }

  public void startFeatureInfoViewInAnimation() {
    mFeatureInfoContainer.setVisibility(View.VISIBLE);
    mFeatureInfoContainerBackground.setVisibility(View.VISIBLE);
    startFeatureInfoViewInFirstAnimation();
  }

  public void startFeatureInfoViewOutAnimation() {
    startFeatureInfoViewSlideOutAnimation();
  }

  private void startFeatureInfoViewInFirstAnimation() {
    if ((mStartFeatureInfoViewInXAnimatorFirst != null
        && mStartFeatureInfoViewInXAnimatorFirst.isRunning())) {
      return;
    }

    mFeatureInfoContainer
        .getViewTreeObserver()
        .addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {
              @Override
              public void onGlobalLayout() {
                mFeatureInfoContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                mFeatureInfoViewDefaultX = Math.round(mFeatureInfoContainer.getX());
                mFeatureInfoViewWidth = mFeatureInfoContainer.getWidth();

                mStartFeatureInfoViewInXAnimatorFirst =
                    ObjectAnimator.ofFloat(
                        mFeatureInfoContainer,
                        "x",
                        mFeatureInfoViewDefaultX + mFeatureInfoViewWidth);
                mStartFeatureInfoViewInXAnimatorFirst.setDuration(0);
                mStartFeatureInfoViewInXAnimatorFirst.addListener(
                    new AnimatorListenerAdapter() {
                      @Override
                      public void onAnimationEnd(Animator animation) {
                        startFeatureInfoViewSlideInAnimation();
                      }
                    });

                mStartFeatureInfoViewInXAnimatorFirst.start();
              }
            });
  }

  private void startFeatureInfoViewSlideInAnimation() {
    if ((mStartFeatureInfoViewInXAnimator != null
        && mStartFeatureInfoViewInXAnimator.isRunning())) {
      return;
    }
    mStartFeatureInfoViewInXAnimator =
        ObjectAnimator.ofFloat(mFeatureInfoContainer, "x", mFeatureInfoViewDefaultX);
    mStartFeatureInfoViewInXAnimator.setDuration(FEATURE_VIEW_ANIMATION_X_IN_DURATION);
    mStartFeatureInfoViewInXAnimator.setInterpolator(
        new PathInterpolator(0.25f, 0.10f, 0.00f, 1.00f));
    mStartFeatureInfoViewInXAnimator.start();
  }

  private void startFeatureInfoViewSlideOutAnimation() {
    if ((mStartFeatureInfoViewOutXAnimator != null
        && mStartFeatureInfoViewOutXAnimator.isRunning())) {
      return;
    }
    mStartFeatureInfoViewOutXAnimator =
        ObjectAnimator.ofFloat(
            mFeatureInfoContainer, "x", mFeatureInfoViewDefaultX + mFeatureInfoViewWidth);
    mStartFeatureInfoViewOutXAnimator.setDuration(FEATURE_VIEW_ANIMATION_X_OUT_DURATION);
    mStartFeatureInfoViewOutXAnimator.setInterpolator(
        new PathInterpolator(0.45f, 0.10f, 0.11f, 1.00f));
    mStartFeatureInfoViewOutXAnimator.addListener(
        new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            mFeatureInfoContainer.setX(mFeatureInfoViewDefaultX);
            mFeatureInfoContainer.setVisibility(View.GONE);
            mFeatureInfoContainerBackground.setVisibility(View.GONE);
          }
        });
    mStartFeatureInfoViewOutXAnimator.start();
  }
}
