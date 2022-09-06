/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.base;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.Utils;
import com.meta.portal.sdk.app.fbns.FbnsData;
import com.meta.portal.sdk.app.ui.FeatureInfoAnimationController;
import com.meta.portal.sdk.app.ui.TopAppBarAnimationController;

public abstract class FeatureBaseActivity extends BaseActivity implements ActivityCallback {

  private static final int TOP_APP_BAR_FADE_OUT_DELAY = 3000;

  FrameLayout mFeatureInfoContainerBackground;
  RelativeLayout mFeatureInfoContainer;
  TextView mFeatureInfoHeader;
  TextView mFeatureInfoText;
  Button mFeatureInfoCloseButton;
  ImageButton mBackButton;

  FrameLayout mFeatureInfoContainerListBackground;
  RelativeLayout mFeatureInfoListContainer;
  TextView mFeatureInfoListHeader;
  TextView mFeatureInfoListText;
  Button mFeatureInfoListCloseButton;
  TextView mFeatureInfoListLink;

  Toolbar mTopAppBar;
  FrameLayout mTopAppBarBackground;

  private FeatureInfoAnimationController mFeatureInfoAnimationController;
  private FeatureInfoAnimationController mFeatureInfoListAnimationController;

  private TopAppBarAnimationController mTopAppBarAnimationController;

  private final Handler mHandler = new Handler();

  private final Runnable mTopAppBarFadeOutRunnable =
      new Runnable() {
        @Override
        public void run() {
          mTopAppBarAnimationController.startTopAppBarViewOutAnimation();
        }
      };

  private boolean mFeatureInfoShowing = true;
  private boolean mFeatureInfoListShowing = false;

  private boolean mDesignModeOn = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (topAppBarTransparent()) {
      setContentView(R.layout.activity_feature);
    } else {
      setContentView(R.layout.activity_feature_top_app_bar_on_top);
    }
    mFeatureInfoContainerBackground = (FrameLayout) findViewById(R.id.feature_container_background);
    mFeatureInfoCloseButton = (Button) findViewById(R.id.feature_info_close_button);
    mFeatureInfoContainer = (RelativeLayout) findViewById(R.id.feature_info_container);
    mFeatureInfoHeader = (TextView) findViewById(R.id.feature_info_header);
    mFeatureInfoText = (TextView) findViewById(R.id.feature_info_text);
    mBackButton = (ImageButton) findViewById(R.id.back_button);

    mFeatureInfoContainerListBackground =
        (FrameLayout) findViewById(R.id.feature_container_list_background);
    mFeatureInfoListCloseButton = (Button) findViewById(R.id.feature_info_list_close_button);
    mFeatureInfoListContainer = (RelativeLayout) findViewById(R.id.feature_info_list_container);
    mFeatureInfoListHeader = (TextView) findViewById(R.id.feature_info_list_header);
    mFeatureInfoListText = (TextView) findViewById(R.id.feature_info_list_text);
    mFeatureInfoListLink = (TextView) findViewById(R.id.feature_info_list_link);

    mFeatureInfoHeader.setText(getFeatureInfoHeaderResId());
    mFeatureInfoText.setText(getFeatureInfoTextResId());

    if (infoButtonShowing()) {
      if (!Utils.isTvDevice(this)) {
        mFeatureInfoContainerBackground.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                    mFeatureInfoAnimationController.startFeatureInfoViewOutAnimation();
                    mFeatureInfoShowing = false;
                    startTopAppBarFadeOutAnimation();
                  }
                });
      }
    }

    if (Utils.isTvDevice(this)) {
      mFeatureInfoContainerListBackground.setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  mFeatureInfoListAnimationController.startFeatureInfoViewOutAnimation();
                  mFeatureInfoListShowing = false;
                }
              });
    }
    if (infoButtonShowing()) {
      mFeatureInfoCloseButton.setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  mFeatureInfoAnimationController.startFeatureInfoViewOutAnimation();
                  mFeatureInfoShowing = false;
                  if (!Utils.isTvDevice(FeatureBaseActivity.this)) {
                    startTopAppBarFadeOutAnimation();
                  }
                  if (Utils.isTvDevice(FeatureBaseActivity.this)) {
                    setFeatureInfoShowing(mFeatureInfoShowing);
                  }
                }
              });
    }

    if (mFeatureInfoListCloseButton != null) {
      mFeatureInfoListCloseButton.setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  mFeatureInfoListAnimationController.startFeatureInfoViewOutAnimation();
                  mFeatureInfoListShowing = false;
                  if (!Utils.isTvDevice(FeatureBaseActivity.this)) {
                    startTopAppBarFadeOutAnimation();
                  }
                }
              });
    }

    mFeatureInfoAnimationController =
        new FeatureInfoAnimationController(mFeatureInfoContainer, mFeatureInfoContainerBackground);

    mFeatureInfoListAnimationController =
        new FeatureInfoAnimationController(
            mFeatureInfoListContainer, mFeatureInfoContainerListBackground);

    if (infoButtonShowing()) {
      mFeatureInfoContainer
              .getViewTreeObserver()
              .addOnGlobalLayoutListener(
                      new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                          mFeatureInfoContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                          mFeatureInfoAnimationController.startFeatureInfoViewInAnimation();
                        }
                      });
    }

    mTopAppBar = (Toolbar) findViewById(R.id.top_app_bar);
    setSupportActionBar(mTopAppBar);

    if (Utils.isTvDevice(this)) {
      if (!topAppBarTransparent()) {
        mTopAppBar.setBackgroundResource(R.color.top_app_bar_background_fbns);
      }
    }

    mTopAppBarBackground = (FrameLayout) findViewById(R.id.top_app_bar_container);

    if (!Utils.isTvDevice(this)) {
      mTopAppBarBackground.setOnTouchListener(
          new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
              mTopAppBarAnimationController.startTopAppBarViewInAnimation(
                  new TopAppBarAnimationController.AnimationFinishedCallback() {
                    @Override
                    public void animationFinished() {
                      FeatureBaseActivity.this.startTopAppBarFadeOutAnimation();
                    }
                  });
              return false;
            }
          });
    }

    mTopAppBarAnimationController = new TopAppBarAnimationController(mTopAppBar);

    ImageButton infoButton = (ImageButton) findViewById(R.id.info_button);

    if (!infoButtonShowing()) {
        infoButton.setVisibility(View.INVISIBLE);
        mFeatureInfoContainer.setVisibility(View.INVISIBLE);
        mFeatureInfoContainerBackground.setVisibility(View.INVISIBLE);
        startTopAppBarFadeOutAnimation();
    }

    if (infoButtonShowing()) {
      infoButton.setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  mFeatureInfoAnimationController.startFeatureInfoViewInAnimation();
                  mFeatureInfoShowing = true;
                  if (Utils.isTvDevice(FeatureBaseActivity.this)) {
                    setFeatureInfoShowing(mFeatureInfoShowing);
                  }
                }
              });
    }

    Button designModeButton = (Button) findViewById(R.id.design_mode_button);

    if (!designModeButtonShowing()) {
        designModeButton.setVisibility(View.INVISIBLE);
    }

    designModeButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (!mDesignModeOn) {
              Spannable designModeText = new SpannableString("Design mode: ON");
              designModeText.setSpan(
                  new ForegroundColorSpan(Color.GREEN), 12, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
              designModeButton.setText(designModeText);
              mDesignModeOn = true;
              updateDesignModeLayoutContainerVisibility(true);
            } else {
              designModeButton.setText("Design mode: OFF");
              mDesignModeOn = false;
              updateDesignModeLayoutContainerVisibility(false);
            }
          }
        });

    if (!Utils.isTvDevice(this)) {
      TextView featureName = (TextView) findViewById(R.id.feature_name);
      featureName.setText(getFeatureInfoHeaderResId());
    }

    if (Utils.isTvDevice(this)) {
      mBackButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          finish();
        }
      });
    }

    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.add(R.id.fragment_container, getFragment()).commit();

    setActivityCallback(this);
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (!Utils.isTvDevice(this)) {
      if (!mFeatureInfoShowing) {
        startTopAppBarFadeOutAnimation();
      }
    }
    if (Utils.isTvDevice(FeatureBaseActivity.this)) {
      setFeatureInfoShowing(mFeatureInfoShowing);
    }
  }

  private void startTopAppBarFadeOutAnimation() {
    mHandler.removeCallbacks(mTopAppBarFadeOutRunnable);
    mHandler.postDelayed(mTopAppBarFadeOutRunnable, TOP_APP_BAR_FADE_OUT_DELAY);
  }

  @Override
  public void onInfoButtonClicked(FbnsData fbnsData) {
    mFeatureInfoListHeader.setText(fbnsData.getInfoTitle());
    mFeatureInfoListText.setText(fbnsData.getInfoText());
    mFeatureInfoListLink.setText(fbnsData.getInfoLink());

    mFeatureInfoContainerListBackground.setVisibility(View.VISIBLE);
    mFeatureInfoListContainer.setVisibility(View.VISIBLE);

    mFeatureInfoListAnimationController.startFeatureInfoViewInAnimation();
    mFeatureInfoListShowing = true;
  }

  @Override
  public void updateSystemUiVisibility() {
    return;
  }

  protected abstract Fragment getFragment();

  protected abstract @StringRes int getFeatureInfoHeaderResId();

  protected abstract @StringRes int getFeatureInfoTextResId();

  protected abstract void updateDesignModeLayoutContainerVisibility(boolean visible);

  protected abstract void setFeatureInfoShowing(boolean showing);

  protected abstract void setActivityCallback(ActivityCallback activityCallback);

  protected abstract boolean topAppBarTransparent();

  protected abstract boolean infoButtonShowing();

  protected abstract boolean designModeButtonShowing();
}
