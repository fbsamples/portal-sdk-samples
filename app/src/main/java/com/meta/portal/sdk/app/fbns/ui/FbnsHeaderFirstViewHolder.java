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
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.meta.portal.sdk.app.R;

public class FbnsHeaderFirstViewHolder extends RecyclerView.ViewHolder {

  private final TextView mHeader;

  public FbnsHeaderFirstViewHolder(View itemView) {
    super(itemView);
    mHeader = (TextView) itemView.findViewById(R.id.header_text);
  }

  public void bind(final String header) {
    mHeader.setText(header);
  }

  public static FbnsHeaderFirstViewHolder newInstance(final ViewGroup parent) {
    return new FbnsHeaderFirstViewHolder(
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.fbns_header_first, parent, false));
  }
}
