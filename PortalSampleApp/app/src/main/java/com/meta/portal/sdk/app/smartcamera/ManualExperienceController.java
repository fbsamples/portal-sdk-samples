// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.portal.sdk.app.smartcamera;

import android.graphics.RectF;
import android.util.Pair;
import android.view.View;
import com.facebook.portal.smartcamera.client.base.api.common.ModeSpec;

public class ManualExperienceController {

  public interface ManualModeListener {
    RectF getCropRect();

    float getAspectRatio();

    void setFixedMode(ModeSpec.Fixed fixed);
  }

  private static final float OFFSET = 100f;
  private static final float SCALE_FACTOR_ZOOM_IN = 1.1f;
  private static final float SCALE_FACTOR_ZOOM_OUT = 1f / SCALE_FACTOR_ZOOM_IN;

  private final ViewLocationProvider mViewLocationProvider;
  private final boolean mIsMirrored;
  private final ManualModeListener mManualModeListener;

  public ManualExperienceController(
      View view, boolean isMirrored, ManualModeListener manualModeListener) {
    mViewLocationProvider = new ViewLocationProvider(view);
    mIsMirrored = isMirrored;
    mManualModeListener = manualModeListener;
  }

  public void scrollUp() {
    scroll(new Pair<Float, Float>(0f, OFFSET));
  }

  public void scrollDown() {
    scroll(new Pair<Float, Float>(0f, -OFFSET));
  }

  public void scrollRight() {
    scroll(new Pair<Float, Float>(OFFSET / mManualModeListener.getAspectRatio(), 0f));
  }

  public void scrollLeft() {
    scroll(new Pair<Float, Float>(-OFFSET / mManualModeListener.getAspectRatio(), 0f));
  }

  public void zoomIn() {
    zoom(SCALE_FACTOR_ZOOM_IN);
  }

  public void zoomOut() {
    zoom(SCALE_FACTOR_ZOOM_OUT);
  }

  private void zoom(float scaleFactor) {
    RectF oldRect = mViewLocationProvider.getViewRect();
    RectF cropRect = mManualModeListener.getCropRect();
    ModeSpec.Fixed mode =
        FixedModeUtil.getFixedMode(
            cropRect, oldRect, new Pair<Float, Float>(0f, 0f), scaleFactor, mIsMirrored);
    mManualModeListener.setFixedMode(mode);
  }

  private void scroll(Pair<Float, Float> offset) {
    RectF oldRect = mViewLocationProvider.getViewRect();
    RectF cropRect = mManualModeListener.getCropRect();
    ModeSpec.Fixed mode = FixedModeUtil.getFixedMode(cropRect, oldRect, offset, 1f, mIsMirrored);
    mManualModeListener.setFixedMode(mode);
  }
}
