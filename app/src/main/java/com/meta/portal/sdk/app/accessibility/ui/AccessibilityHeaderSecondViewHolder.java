/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.accessibility.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.R;

public class AccessibilityHeaderSecondViewHolder extends RecyclerView.ViewHolder {

  private final TextView mHeader;

  public AccessibilityHeaderSecondViewHolder(View itemView) {
    super(itemView);
    mHeader = (TextView) itemView.findViewById(R.id.header_text);
  }

  public void bind(final String header) {
    mHeader.setText(header);
  }

  public static AccessibilityHeaderSecondViewHolder newInstance(final ViewGroup parent) {
    return new AccessibilityHeaderSecondViewHolder(
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.accessibility_header_second, parent, false));
  }
}
