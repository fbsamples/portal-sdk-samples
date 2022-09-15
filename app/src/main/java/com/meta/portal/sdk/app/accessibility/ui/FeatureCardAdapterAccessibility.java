/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.accessibility.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.data.ListData;
import com.meta.portal.sdk.app.fbns.ui.FbnsHeaderSecondViewHolder;

import java.util.ArrayList;
import java.util.List;

public class FeatureCardAdapterAccessibility extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final int ITEM_VIEW_TYPE_HEADER_1 = 0;
  private static final int ITEM_VIEW_TYPE_HEADER_2 = 1;
  private static final int ITEM_VIEW_TYPE_FOOTER = 2;
  private static final int ITEM_VIEW_TYPE_ITEM = 3;

  private static final int HEADER_COUNT = 2;
  private static final int FOOTER_COUNT = 1;

  private AccessibilityUiListener mAccessibilityUiListener;

  private List<ListData> mAccessibilityData = new ArrayList<>();

  private String mHeaderFirst;
  private String mHeaderSecond;
  private String mFooter;
  private String mLink;

  private boolean isHeader1(int position) {
    return position == 0;
  }

  private boolean isHeader2(int position) {
    return position == 1;
  }

  private boolean isFooter(int position) {
    return position == 6;
  }

  public FeatureCardAdapterAccessibility() {}

  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM_VIEW_TYPE_HEADER_1) {
      return AccessibilityHeaderFirstViewHolder.newInstance(parent);
    } else if (viewType == ITEM_VIEW_TYPE_HEADER_2) {
      return AccessibilityHeaderSecondViewHolder.newInstance(parent);
    } else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
      return AccessibilityFooterViewHolder.newInstance(parent);
    } else {
      return ListViewHolderAccessibility.newInstance(parent, mAccessibilityUiListener);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    switch (holder.getItemViewType()) {
      case ITEM_VIEW_TYPE_HEADER_1:
        ((AccessibilityHeaderFirstViewHolder) holder).bind(mHeaderFirst);
        break;
      case ITEM_VIEW_TYPE_HEADER_2:
        ((AccessibilityHeaderSecondViewHolder) holder).bind(mHeaderSecond);
        break;
      case ITEM_VIEW_TYPE_FOOTER:
        ((AccessibilityFooterViewHolder) holder).bind(mFooter, mLink);
        break;
      case ITEM_VIEW_TYPE_ITEM:
        ((ListViewHolderAccessibility) holder).bind(mAccessibilityData.get(position - HEADER_COUNT));
        break;
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (isHeader1(position)) {
      return ITEM_VIEW_TYPE_HEADER_1;
    } else if (isHeader2(position)) {
      return ITEM_VIEW_TYPE_HEADER_2;
    } else if (isFooter(position)) {
      return ITEM_VIEW_TYPE_FOOTER;
    } else {
      return ITEM_VIEW_TYPE_ITEM;
    }
  }

  @Override
  public int getItemCount() {
    return mAccessibilityData.size() + HEADER_COUNT + FOOTER_COUNT;
  }

  public void setHeaderFirst(final String headerFirst) {
    mHeaderFirst = headerFirst;
  }

  public void setHeaderSecond(final String headerSecond) {
    mHeaderSecond = headerSecond;
  }

  public void setFooter(final String footer) {
    mFooter = footer;
  }

  public void setLink(final String link) {
    mLink = link;
  }

  public void setData(List<ListData> features) {
    mAccessibilityData.clear();
    mAccessibilityData.addAll(features);
    notifyDataSetChanged();
  }

  public void setInfoButtonClickedListener(final AccessibilityUiListener accessibilityUiListener) {
    mAccessibilityUiListener = accessibilityUiListener;
  }
}
