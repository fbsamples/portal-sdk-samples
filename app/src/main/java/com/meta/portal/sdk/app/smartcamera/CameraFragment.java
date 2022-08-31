/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.smartcamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.Utils;
import com.meta.portal.sdk.app.ui.ButtonAnimationControllerTv;
import java.util.Collections;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@SuppressLint({"ValidFragment", "MissingPermission"})
public class CameraFragment extends Fragment {

  private static final String TAG = "CameraFragment";
  private static final int REQUEST_CODE = 0;
  private static final int CAMERA_ID = 0;
  public static final int CAMERA_LOCK_TIMEOUT_MS = 2500;

  /** A Semaphore to prevent the app from exiting before closing the camera. */
  private final Semaphore mCameraOpenCloseLock = new Semaphore(1);

  /** A view reference for camera preview. */
  private TextureView mTextureView;

  private View mModeButtons;
  private View mMoveButtons;
  private View mZoomButtons;
  private View mDoneButton;

  private Button mDefaultModeButton;
  private Button mDeskModeButton;
  private Button mManualModeButton;
  private ImageButton mUpButton;
  private ImageButton mDownButton;
  private ImageButton mLeftButton;
  private ImageButton mRightButton;
  private ImageButton mZoomInButton;
  private ImageButton mZoomOutButton;

  private ButtonAnimationControllerTv mButtonAnimationControllerTv;

  private boolean mModeButtonsShowing = true;
  private boolean mDesignInfoShowing = false;

  RelativeLayout mDesignModeLayoutContainer;
  RelativeLayout mDesignModeLayoutContainer1;
  RelativeLayout mDesignModeLayoutContainer2;

  /** A reference to the opened Camera. */
  @Nullable private CameraDevice mCameraDevice;

  /** An additional thread for running tasks that shouldn't block the UI. */
  private @Nullable HandlerThread mBackgroundThread;

  /** Handler for running tasks in the background. */
  private @Nullable Handler mBackgroundHandler;

  /** The Size of camera preview. */
  private Size mPreviewSize;

  @Nullable private Integer mSensorOrientation;

  /** A reference to the current CameraCaptureSession for preview. */
  private @Nullable CameraCaptureSession mPreviewSession;

  CaptureRequest.Builder mPreviewBuilder;

  private CameraPresenter mCameraPresenter;
  private int mWidth;
  private int mHeight;

  /** Listener handles lifecycle events on a TextureView. */
  private final TextureView.SurfaceTextureListener mSurfaceTextureListener =
      new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(
            SurfaceTexture surfaceTexture, int width, int height) {
          mWidth = width;
          mHeight = height;
          if (getContext().checkSelfPermission(Manifest.permission.CAMERA)
              != PackageManager.PERMISSION_GRANTED) {
            getActivity()
                .requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CODE);
          } else {
            openCamera(width, height);
          }
        }

        @Override
        public void onSurfaceTextureSizeChanged(
            SurfaceTexture surfaceTexture, int width, int height) {
          configureTextureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
          return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {}
      };

  /** Callback for camera status change */
  private final CameraDevice.StateCallback mStateCallback =
      new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice cameraDevice) {
          mCameraDevice = cameraDevice;
          startPreview();
          mCameraOpenCloseLock.release();
          if (null != mTextureView) {
            configureTextureTransform(mTextureView.getWidth(), mTextureView.getHeight());
          }
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
          mCameraOpenCloseLock.release();
          cameraDevice.close();
          mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
          mCameraOpenCloseLock.release();
          cameraDevice.close();
          mCameraDevice = null;
          Activity activity = getActivity();
          if (null != activity) {
            Toast.makeText(
                    activity.getApplicationContext(), R.string.camera_error, Toast.LENGTH_SHORT)
                .show();
            activity.finish();
          }
        }
      };

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_camera_video, container, false);
  }

  private final View.OnClickListener mSetDefaultMode =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mCameraPresenter.onDefaultModeClicked();
        }
      };

  private final View.OnClickListener mSetDeskMode =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mCameraPresenter.onDeskModeClicked();
        }
      };

  private final View.OnClickListener mManualMode =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mCameraPresenter.onManualModeClicked();
          if (Utils.isTvDevice(getActivity())) {
            mButtonAnimationControllerTv.startMoveButtonsInAnimation();
            mModeButtonsShowing = false;
            if (mDesignInfoShowing) {
              updateDesignModeLayoutContainerModeVisibility(true);
            }
          }
        }
      };

  private final View.OnClickListener mScale =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (v.getId() == R.id.zoomIn) {
            mCameraPresenter.onZoomInClicked();
          } else if (v.getId() == R.id.zoomOut) {
            mCameraPresenter.onZoomOutClicked();
          }
        }
      };

  private final View.OnClickListener mScroll =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (v.getId() == R.id.up) {
            mCameraPresenter.onScrollUpClicked();
          } else if (v.getId() == R.id.down) {
            mCameraPresenter.onScrollDownClicked();
          } else if (v.getId() == R.id.left) {
            mCameraPresenter.onScrollLeftClicked();
          } else if (v.getId() == R.id.right) {
            mCameraPresenter.onScrollRightClicked();
          }
        }
      };

  private final View.OnClickListener mDone =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mCameraPresenter.onDefaultModeClicked();
          if (Utils.isTvDevice(getActivity())) {
            mModeButtonsShowing = true;
            mButtonAnimationControllerTv.startModeButtonsInAnimation();
            if (mDesignInfoShowing) {
              updateDesignModeLayoutContainerModeVisibility(true);
            }
          }
        }
      };

  @RequiresApi(api = Build.VERSION_CODES.P)
  @Override
  public void onViewCreated(final View view, Bundle savedInstanceState) {
    mTextureView = view.findViewById(R.id.texture);

    mCameraPresenter = new CameraViewPresenter(getActivity(), mTextureView);
    if (Utils.isTvDevice(getActivity())) {
      mModeButtons = view.requireViewById(R.id.modeButtons);
      mMoveButtons = view.requireViewById(R.id.moveButtons);
      mZoomButtons = view.requireViewById(R.id.zoomButtons);
      mDoneButton = view.requireViewById(R.id.done);
    }
    mDefaultModeButton = view.requireViewById(R.id.defaultMode);
    mDeskModeButton = view.requireViewById(R.id.deskMode);
    mManualModeButton = view.requireViewById(R.id.manualMode);
    mUpButton = view.requireViewById(R.id.up);
    mDownButton = view.requireViewById(R.id.down);
    mLeftButton = view.requireViewById(R.id.left);
    mRightButton = view.requireViewById(R.id.right);
    mZoomInButton = view.requireViewById(R.id.zoomIn);
    mZoomOutButton = view.requireViewById(R.id.zoomOut);
    mDefaultModeButton.setOnClickListener(mSetDefaultMode);
    mDeskModeButton.setOnClickListener(mSetDeskMode);
    mManualModeButton.setOnClickListener(mManualMode);
    mUpButton.setOnClickListener(mScroll);
    mDownButton.setOnClickListener(mScroll);
    mLeftButton.setOnClickListener(mScroll);
    mRightButton.setOnClickListener(mScroll);
    mZoomInButton.setOnClickListener(mScale);
    mZoomOutButton.setOnClickListener(mScale);
    if (Utils.isTvDevice(getActivity())) {
      mDoneButton.setOnClickListener(mDone);
    }

    if (!Utils.isTvDevice(getActivity())) {
      mDesignModeLayoutContainer = view.requireViewById(R.id.design_mode_layout_container);
    } else {
      mDesignModeLayoutContainer1 = view.requireViewById(R.id.design_mode_layout_container_1);
      mDesignModeLayoutContainer2 = view.requireViewById(R.id.design_mode_layout_container_2);
    }
    if (Utils.isTvDevice(getActivity())) {
      mButtonAnimationControllerTv =
          new ButtonAnimationControllerTv(mModeButtons, mMoveButtons, mZoomButtons, mDoneButton);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    startBackgroundThread();
    if (mTextureView.isAvailable()) {
      openCamera(mTextureView.getWidth(), mTextureView.getHeight());
    } else {
      mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
    }
    mCameraPresenter.start();
  }

  @Override
  public void onStop() {
    closeCamera();
    stopBackgroundThread();
    super.onStop();
  }

  public void updateDesignModeLayoutContainerVisibility(boolean visible) {
    if (visible) {
      if (!Utils.isTvDevice(getActivity())) {
        mDesignModeLayoutContainer.setVisibility(View.VISIBLE);
      } else {
        mDesignInfoShowing = true;
        if (mModeButtonsShowing) {
          mDesignModeLayoutContainer1.setVisibility(View.VISIBLE);
        } else {
          mDesignModeLayoutContainer2.setVisibility(View.VISIBLE);
        }
      }
    } else {
      if (!Utils.isTvDevice(getActivity())) {
        mDesignModeLayoutContainer.setVisibility(View.GONE);
      } else {
        mDesignInfoShowing = false;
        if (mModeButtonsShowing) {
          mDesignModeLayoutContainer1.setVisibility(View.GONE);
        } else {
          mDesignModeLayoutContainer2.setVisibility(View.GONE);
        }
      }
    }
  }

  public void updateDesignModeLayoutContainerModeVisibility(boolean visible) {
    if (visible) {
      if (mModeButtonsShowing) {
        mDesignModeLayoutContainer1.setVisibility(View.VISIBLE);
        mDesignModeLayoutContainer2.setVisibility(View.GONE);
      } else {
        mDesignModeLayoutContainer2.setVisibility(View.VISIBLE);
        mDesignModeLayoutContainer1.setVisibility(View.GONE);
      }
    } else {
      if (mModeButtonsShowing) {
        mDesignModeLayoutContainer1.setVisibility(View.GONE);
        mDesignModeLayoutContainer2.setVisibility(View.VISIBLE);
      } else {
        mDesignModeLayoutContainer2.setVisibility(View.GONE);
        mDesignModeLayoutContainer1.setVisibility(View.VISIBLE);
      }
    }
  }

  protected void setFeatureInfoShowing(boolean showing) {
    if (showing) {
      mDefaultModeButton.setEnabled(false);
      mDeskModeButton.setEnabled(false);
      mManualModeButton.setEnabled(false);
      mUpButton.setEnabled(false);
      mDownButton.setEnabled(false);
      mLeftButton.setEnabled(false);
      mRightButton.setEnabled(false);
      mZoomInButton.setEnabled(false);
      mZoomOutButton.setEnabled(false);
    } else {
      mDefaultModeButton.setEnabled(true);
      mDeskModeButton.setEnabled(true);
      mManualModeButton.setEnabled(true);
      mUpButton.setEnabled(true);
      mDownButton.setEnabled(true);
      mLeftButton.setEnabled(true);
      mRightButton.setEnabled(true);
      mZoomInButton.setEnabled(true);
      mZoomOutButton.setEnabled(true);
    }
  }

  /** Starts a background thread and its Handler. */
  private void startBackgroundThread() {
    mBackgroundThread = new HandlerThread("CameraBackground");
    mBackgroundThread.start();
    mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
  }

  /** Stops the background thread and its Handler. */
  private void stopBackgroundThread() {
    if (null == mBackgroundThread) {
      Log.d(TAG, "Already stopped");
      return;
    }
    mBackgroundThread.quitSafely();
    mBackgroundThread = null;
    mBackgroundHandler = null;
  }

  private void openCamera(int width, int height) {
    final Activity activity = getActivity();
    if (null == activity || activity.isFinishing()) {
      return;
    }
    CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
    try {
      Log.d(TAG, "tryAcquire");
      if (!mCameraOpenCloseLock.tryAcquire(CAMERA_LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
        handleCameraError(activity, "Time out waiting to lock camera opening.");
        return;
      }
      String cameraId = manager.getCameraIdList()[CAMERA_ID];

      // Choose the sizes for camera preview and video recording
      final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
      mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
      final StreamConfigurationMap map =
          characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
      if (map == null) {
        handleCameraError(activity, "Cannot get available preview/video sizes");
        return;
      }
      // Take first available size
      mPreviewSize = map.getOutputSizes(SurfaceTexture.class)[0];

      configureTextureTransform(width, height);
      manager.openCamera(cameraId, mStateCallback, null);
    } catch (CameraAccessException e) {
      handleCameraError(activity, "Error accessing the camera");
    } catch (InterruptedException e) {
      handleCameraError(activity, "Interrupted while trying to lock camera opening.");
    }
  }

  private void handleCameraError(Activity activity, String s) {
    Log.e(TAG, s);
    activity.finish();
  }

  private void closeCamera() {
    try {
      mCameraOpenCloseLock.acquire();
      if (null != mCameraDevice) {
        mCameraDevice.close();
        mCameraDevice = null;
      }
      mCameraPresenter.stop();
    } catch (InterruptedException e) {
      handleCameraError(getActivity(), "Interrupted while trying to lock camera closing.");
    } finally {
      mCameraOpenCloseLock.release();
    }
  }

  private void startPreview() {
    if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
      return;
    }
    try {
      closePreviewSession();
      SurfaceTexture texture = mTextureView.getSurfaceTexture();
      assert texture != null;
      texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

      mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

      Surface previewSurface = new Surface(texture);
      mPreviewBuilder.addTarget(previewSurface);

      mCameraDevice.createCaptureSession(
          Collections.singletonList(previewSurface),
          new CameraCaptureSession.StateCallback() {

            @Override
            public void onConfigured(CameraCaptureSession session) {
              mPreviewSession = session;
              updatePreview();
            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {
              Log.e(TAG, " Capture session onConfigureFailed");
            }
          },
          mBackgroundHandler);
    } catch (CameraAccessException e) {
      Log.e(TAG, " Capture request creation failed", e);
    }
  }

  private void closePreviewSession() {
    if (mPreviewSession != null) {
      mPreviewSession.close();
      mPreviewSession = null;
    }
  }

  private void updatePreview() {
    if (null == mCameraDevice || null == mPreviewBuilder) {
      return;
    }
    try {
      mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
      mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
    } catch (CameraAccessException e) {
      Log.e(TAG, "Repeat Capture session exception ", e);
    }
  }

  /**
   * Configures Matrix transformation for TextureView based on display rotation.
   *
   * @param viewWidth
   * @param viewHeight
   */
  private void configureTextureTransform(int viewWidth, int viewHeight) {
    Activity activity = getActivity();
    if (null == mTextureView || null == mPreviewSize || null == activity) {
      return;
    }
    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    Matrix matrix = new Matrix();
    RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
    RectF bufferRect;
    if (mSensorOrientation != null && (mSensorOrientation == 270 || mSensorOrientation == 90)) {
      bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
    } else {
      bufferRect = new RectF(0, 0, mPreviewSize.getWidth(), mPreviewSize.getHeight());
    }
    float centerX = viewRect.centerX();
    float centerY = viewRect.centerY();
    if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
      bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
      matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
      // since width and height have swapped, scale preview to fit new width and height
      float scale;
      if (viewHeight < viewWidth) {
        scale =
            Math.max(
                (float) viewHeight / mTextureView.getWidth(),
                (float) viewHeight / mTextureView.getHeight());
      } else {
        scale =
            Math.max(
                (float) viewWidth / mTextureView.getWidth(),
                (float) viewWidth / mTextureView.getHeight());
      }
      matrix.postScale(scale, scale, centerX, centerY);
      matrix.postRotate(90 * (rotation - 2), centerX, centerY);
    }
    mTextureView.setTransform(matrix);
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(REQUEST_CODE, permissions, grantResults);
    if (requestCode == REQUEST_CODE) {
      openCamera(mWidth, mHeight);
    }
  }

  public static CameraFragment newInstance() {
    CameraFragment cameraFragment = new CameraFragment();
    return cameraFragment;
  }
}
