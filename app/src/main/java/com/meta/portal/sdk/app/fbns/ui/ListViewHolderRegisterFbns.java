/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.fbns.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;
import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.fbns.FbnsData;

public class ListViewHolderRegisterFbns extends RecyclerView.ViewHolder {
  private static final String TAG = "ListViewHolderRegisterFBNS";

  private final TextView mFeatureNumber;
  private final TextView mFeatureTitle;
  private final ImageButton mInfoButton;
  private final Button mRequestButton;
  private final TextView mPushToken;

  private FbnsData mFeature;

  private final FbnsUiListener mFbnsUiListener;

  public ListViewHolderRegisterFbns(final View view, final FbnsUiListener fbnsUiListener) {
    super(view);
    mFbnsUiListener = fbnsUiListener;
    mFeatureNumber = (TextView) view.findViewById(R.id.feature_number);
    mFeatureTitle = (TextView) view.findViewById(R.id.feature_title);
    mInfoButton = (ImageButton) view.findViewById(R.id.info_button);
    mRequestButton = (Button) view.findViewById(R.id.request_push_token_button);
    mPushToken = (TextView) view.findViewById(R.id.push_token);

    mInfoButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            mFbnsUiListener.onInfoButtonClicked(mFeature);
          }
        });

    mRequestButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            mFbnsUiListener.onActionButtonClicked(mFeature);
          }
        });
  }

  public void bind(FbnsData fbnsData) {
    mFeature = fbnsData;
    mFeatureNumber.setText(String.valueOf(fbnsData.getStepIndex()));
    mFeatureTitle.setText(fbnsData.getCardTitle());
    mPushToken.setText(fbnsData.getValueText());
  }

  private @DrawableRes int getBackgroundResource(boolean finished) {
    if (finished) {
      return R.drawable.ic_checked;
    } else {
      return R.drawable.ic_unchecked;
    }
  }

  public static ListViewHolderRegisterFbns newInstance(
      final ViewGroup parent, final FbnsUiListener mFbnsUiListener) {
    return new ListViewHolderRegisterFbns(
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_item_register_fbns, parent, false),
        mFbnsUiListener);
  }
}
