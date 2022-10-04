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
import androidx.recyclerview.widget.RecyclerView;
import com.meta.portal.sdk.app.R;

public class FbnsFooterViewHolder extends RecyclerView.ViewHolder {

  public FbnsFooterViewHolder(View itemView) {
    super(itemView);
  }

  public void bind() {}

  public static FbnsFooterViewHolder newInstance(final ViewGroup parent) {
    return new FbnsFooterViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.fbns_footer, parent, false));
  }
}
