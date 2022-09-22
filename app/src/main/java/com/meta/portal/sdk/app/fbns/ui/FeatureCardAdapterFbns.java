/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.fbns.ui;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.data.ListData;
import java.util.ArrayList;
import java.util.List;

public class FeatureCardAdapterFbns extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final int ITEM_VIEW_TYPE_HEADER_1 = 0;
  private static final int ITEM_VIEW_TYPE_HEADER_2 = 1;
  private static final int ITEM_VIEW_TYPE_ITEM = 2;
  private static final int ITEM_VIEW_TYPE_ITEM_REGISTER = 3;
  private static final int ITEM_VIEW_TYPE_ITEM_SEND = 4;
  private static final int ITEM_VIEW_TYPE_ITEM_RCV = 5;
  private static final int ITEM_VIEW_TYPE_FOOTER = 6;

  private static final int HEADER_COUNT = 2;
  private static final int FOOTER_COUNT = 1;

  private FbnsUiListener mFbnsUiListener;

  private List<ListData> mFbnsData = new ArrayList<>();

  private String mHeaderFirst;
  private String mHeaderSecond;

  private boolean mIsTvDevice;

  private boolean isHeader1(int position) {
    return position == 0;
  }

  private boolean isHeader2(int position) {
    return position == 1;
  }

  private boolean isRegisterListItem(int position) {
    return position == 4;
  }

  private boolean isSendListItem(int position) {
    return position == 5;
  }

  private boolean isRcvdListItem(int position) {
    return position == 6;
  }

  private boolean isFooter(int position) {
    return position == 7;
  }

  private int getFooterCount() {
    if (mIsTvDevice) {
      return FOOTER_COUNT;
    } else {
      return 0;
    }
  }

  public FeatureCardAdapterFbns(boolean isTvDevice) {
    mIsTvDevice = isTvDevice;
  }

  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM_VIEW_TYPE_HEADER_1) {
      return FbnsHeaderFirstViewHolder.newInstance(parent);
    } else if (viewType == ITEM_VIEW_TYPE_HEADER_2) {
      return FbnsHeaderSecondViewHolder.newInstance(parent);
    } else if (viewType == ITEM_VIEW_TYPE_ITEM_REGISTER) {
      return ListViewHolderRegisterFbns.newInstance(parent, mFbnsUiListener);
    } else if (viewType == ITEM_VIEW_TYPE_ITEM_SEND) {
      return ListViewHolderSendFbns.newInstance(parent, mFbnsUiListener);
    } else if (viewType == ITEM_VIEW_TYPE_ITEM_RCV) {
      return ListViewHolderRcvdFbns.newInstance(parent, mFbnsUiListener);
    } else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
      return FbnsFooterViewHolder.newInstance(parent);
    } else {
      return ListViewHolderFbns.newInstance(parent, mFbnsUiListener);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    switch (holder.getItemViewType()) {
      case ITEM_VIEW_TYPE_HEADER_1:
        ((FbnsHeaderFirstViewHolder) holder).bind(mHeaderFirst);
        break;
      case ITEM_VIEW_TYPE_HEADER_2:
        ((FbnsHeaderSecondViewHolder) holder).bind(mHeaderSecond);
        break;
      case ITEM_VIEW_TYPE_ITEM:
        ((ListViewHolderFbns) holder).bind(mFbnsData.get(position - 2));
        break;
      case ITEM_VIEW_TYPE_ITEM_REGISTER:
        ((ListViewHolderRegisterFbns) holder).bind(mFbnsData.get(position - 2));
        break;
      case ITEM_VIEW_TYPE_ITEM_SEND:
        ((ListViewHolderSendFbns) holder).bind(mFbnsData.get(position - 2));
        break;
      case ITEM_VIEW_TYPE_ITEM_RCV:
        ((ListViewHolderRcvdFbns) holder).bind(mFbnsData.get(position - 2));
        break;
      case ITEM_VIEW_TYPE_FOOTER:
        ((FbnsFooterViewHolder) holder).bind();
        break;
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (isHeader1(position)) {
      return ITEM_VIEW_TYPE_HEADER_1;
    } else if (isHeader2(position)) {
      return ITEM_VIEW_TYPE_HEADER_2;
    } else if (isRegisterListItem(position)) {
      return ITEM_VIEW_TYPE_ITEM_REGISTER;
    } else if (isSendListItem(position)) {
      return ITEM_VIEW_TYPE_ITEM_SEND;
    } else if (isRcvdListItem(position)) {
      return ITEM_VIEW_TYPE_ITEM_RCV;
    } else if (isFooter(position)) {
      return ITEM_VIEW_TYPE_FOOTER;
    } else {
      return ITEM_VIEW_TYPE_ITEM;
    }
  }

  @Override
  public int getItemCount() {
    return mFbnsData.size() + HEADER_COUNT + getFooterCount();
  }

  public void setHeaderFirst(final String headerFirst) {
    mHeaderFirst = headerFirst;
  }

  public void setHeaderSecond(final String headerSecond) {
    mHeaderSecond = headerSecond;
  }

  public void setData(List<ListData> features) {
    mFbnsData.clear();
    mFbnsData.addAll(features);
    notifyDataSetChanged();
  }

  public void setInfoButtonClickedListener(final FbnsUiListener fbnsUiListener) {
    mFbnsUiListener = fbnsUiListener;
  }
}
