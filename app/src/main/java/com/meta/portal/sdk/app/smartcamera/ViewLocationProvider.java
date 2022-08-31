/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.smartcamera;

import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewTreeObserver;

public class ViewLocationProvider {

  private View mView;
  int xOffset = 0;
  int yOffset = 0;

  public ViewLocationProvider(View view) {
    mView = view;
    view.getViewTreeObserver()
        .addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {
              @Override
              public void onGlobalLayout() {
                calculateOffset();
                mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
              }
            });
  }

  private void calculateOffset() {
    xOffset = (int) getViewLocation().x;
    yOffset = (int) getViewLocation().y;
  }

  public RectF getViewRect() {
    return new RectF(mView.getLeft(), mView.getTop(), mView.getRight(), mView.getBottom());
  }

  public PointF getViewLocation() {
    int[] location = new int[2];
    mView.getLocationOnScreen(location);
    return new PointF(location[0] - xOffset, location[1] - yOffset);
  }
}
