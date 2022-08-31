/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.smartcamera;

import android.graphics.RectF;
import android.util.Pair;
import com.facebook.portal.smartcamera.client.base.api.common.ModeSpec;

public class FixedModeUtil {

  public FixedModeUtil() {}

  private static final float MIN_SCALE_FACTOR = 0.4f;
  private static final float MAX_SCALE_FACTOR = 1.0f;

  public static ModeSpec.Fixed getFixedMode(
      RectF crop,
      RectF viewRect,
      Pair<Float, Float> offset,
      float scaleFactor,
      boolean isMirrored) {

    float xOffset = viewRect.left - offset.first;
    float yOffset = viewRect.top + offset.second;
    float width = viewRect.width() / crop.width();
    float height = viewRect.height() / crop.height();

    float centerX;
    if (isMirrored) {
      centerX =
          crop.left + xOffset / width / scaleFactor + crop.width() - crop.width() / 2 / scaleFactor;
    } else {
      centerX = crop.left - xOffset / width / scaleFactor + crop.width() / 2 / scaleFactor;
    }
    float centerY =
        crop.top - yOffset / height / scaleFactor + (crop.bottom - crop.top) / 2 / scaleFactor;

    RectF rect = getMoveableRect(crop, scaleFactor);
    centerX = clamp(centerX, rect.left, rect.right);
    centerY = clamp(centerY, rect.top, rect.bottom);

    float scale = Math.max(crop.width(), crop.height()) / scaleFactor;
    scale = clamp(scale, MIN_SCALE_FACTOR, MAX_SCALE_FACTOR);

    return ModeSpec.Fixed.newBuilder()
        .setCameraFrameCrop(centerX, centerY, scale)
        .setAutoCameraFaceMetering()
        .build();
  }

  private static RectF getMoveableRect(RectF crop, float newScale) {
    float newCenterX = crop.width() / newScale / 2;
    float newCenterY = crop.height() / newScale / 2;
    RectF rect = new RectF();
    rect.left = newCenterX;
    rect.right = 1 - newCenterX;
    rect.top = newCenterY;
    rect.bottom = 1 - newCenterY;
    return rect;
  }

  public static float clamp(float val, float min, float max) {
    return Math.max(min, Math.min(max, val));
  }
}
