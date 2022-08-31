/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.Toolbar;

public class TopAppBar extends Toolbar {

  public TopAppBar(Context context) {
    super(context);
  }

  public TopAppBar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public TopAppBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }
}
