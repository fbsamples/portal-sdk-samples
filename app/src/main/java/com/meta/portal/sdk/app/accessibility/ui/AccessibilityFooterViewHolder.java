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

public class AccessibilityFooterViewHolder extends RecyclerView.ViewHolder {

  private final TextView mFooter;
  private final TextView mLink;

  public AccessibilityFooterViewHolder(View itemView) {
    super(itemView);
    mFooter = (TextView) itemView.findViewById(R.id.footer_text);
    mLink = (TextView) itemView.findViewById(R.id.link);
  }

  public void bind(final String footer, final String link) {
    mFooter.setText(footer);
    mLink.setText(link);
  }

  public static AccessibilityFooterViewHolder newInstance(final ViewGroup parent) {
    return new AccessibilityFooterViewHolder(
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.accessibility_footer, parent, false));
  }
}
