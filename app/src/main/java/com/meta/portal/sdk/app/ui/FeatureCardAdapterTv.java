/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.ui;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.meta.portal.sdk.app.data.Feature;
import java.util.ArrayList;
import java.util.List;

public class FeatureCardAdapterTv extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final int ITEM_VIEW_TYPE_ITEM = 2;

  private FeatureCardAdapterListener mFeatureCardAdapterListener;

  private final CardViewHolderTv.OnListItemClickedListener mOnListItemClicked;

  private List<Feature> mFeatures = new ArrayList<>();

  public FeatureCardAdapterTv() {
    mOnListItemClicked =
        new CardViewHolderTv.OnListItemClickedListener() {
          @Override
          public void onListItemClicked(final Feature feature) {
            mFeatureCardAdapterListener.onListItemClicked(feature);
          }
        };
  }

  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return CardViewHolderTv.newInstance(parent, mOnListItemClicked);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    switch (holder.getItemViewType()) {
      case ITEM_VIEW_TYPE_ITEM:
        ((CardViewHolderTv) holder).bind(mFeatures.get(position));
        break;
    }
  }

  @Override
  public int getItemViewType(int position) {
    return ITEM_VIEW_TYPE_ITEM;
  }

  @Override
  public int getItemCount() {
    return mFeatures.size();
  }

  public void setData(List<Feature> features) {
    mFeatures.clear();
    mFeatures.addAll(features);
    notifyDataSetChanged();
  }

  public void setFeatureCardAdapterListener(
      final FeatureCardAdapterListener featureCardAdapterListener) {
    mFeatureCardAdapterListener = featureCardAdapterListener;
  }
}
