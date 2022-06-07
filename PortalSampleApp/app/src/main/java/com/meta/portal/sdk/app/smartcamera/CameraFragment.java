// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.portal.sdk.app.smartcamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.meta.portal.sdk.app.R;

import java.util.Collections;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@SuppressLint({"ValidFragment", "MissingPermission"})
public class CameraFragment extends Fragment implements CameraPresenter.CameraView {

  private static final String TAG = "CameraFragment";
  private static final int REQUEST_CODE = 0;
  private static final int CAMERA_ID = 0;
  public static final int CAMERA_LOCK_TIMEOUT_MS = 2500;

  /** A Semaphore to prevent the app from exiting before closing the camera. */
  private final Semaphore mCameraOpenCloseLock = new Semaphore(1);

  /** A view reference for camera preview. */
  private TextureView mTextureView;

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

  private CameraEditorLauncher mCameraEditorLauncher;
  private CameraPresenter mCameraPresenter;
  private int mWidth;
  private int mHeight;
  private TextView mCurrentModeText;
  private TextView mFailureText;

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

  private final View.OnClickListener setDefaultMode =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mCameraPresenter.onDefaultModeClicked();
        }
      };

  private final View.OnClickListener setDeskMode =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mCameraPresenter.onDeskModeClicked();
        }
      };

  private final View.OnClickListener launchCameraEditor =
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          mCameraPresenter.onLaunchEditorClicked();
        }
      };

  private final View.OnClickListener scale =
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

  private final View.OnClickListener scroll =
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

  @Override
  public void setModeText(final String modeText) {
    mCurrentModeText.post(
        new Runnable() {
          @Override
          public void run() {
            mCurrentModeText.setText(modeText);
          }
        });
  }

  @Override
  public void setFailureText(final String failureText) {
    mFailureText.post(
        new Runnable() {
          @Override
          public void run() {
            mFailureText.setText(failureText);
          }
        });
  }

  @Override
  public void showModeText(final boolean show) {
    mCurrentModeText.post(
        new Runnable() {
          @Override
          public void run() {
            mCurrentModeText.setVisibility(show ? View.VISIBLE : View.GONE);
          }
        });
  }

  @Override
  public void showFailureText(final boolean show) {
    mFailureText.post(
        new Runnable() {
          @Override
          public void run() {
            mFailureText.setVisibility(show ? View.VISIBLE : View.GONE);
          }
        });
  }

  @Override
  public void onAttach(Activity activity) {
    mCameraEditorLauncher = (CameraEditorLauncher) activity;
    super.onAttach(activity);
  }

  @RequiresApi(api = Build.VERSION_CODES.P)
  @Override
  public void onViewCreated(final View view, Bundle savedInstanceState) {
    mTextureView = view.findViewById(R.id.texture);
    mCurrentModeText = view.requireViewById(R.id.currentMode);
    mFailureText = view.requireViewById(R.id.failureText);
    mCameraPresenter =
        new CameraViewPresenter(getActivity(), this, mTextureView, mCameraEditorLauncher);
    Button defaultModeButton = view.requireViewById(R.id.defaultMode);
    Button deskModeButton = view.requireViewById(R.id.deskMode);
    Button upButton = view.requireViewById(R.id.up);
    Button downButton = view.requireViewById(R.id.down);
    Button leftButton = view.requireViewById(R.id.left);
    Button rightButton = view.requireViewById(R.id.right);
    Button zoomInButton = view.requireViewById(R.id.zoomIn);
    Button zoomOutButton = view.requireViewById(R.id.zoomOut);
    defaultModeButton.setOnClickListener(setDefaultMode);
    deskModeButton.setOnClickListener(setDeskMode);
    upButton.setOnClickListener(scroll);
    downButton.setOnClickListener(scroll);
    leftButton.setOnClickListener(scroll);
    rightButton.setOnClickListener(scroll);
    zoomInButton.setOnClickListener(scale);
    zoomOutButton.setOnClickListener(scale);
    view.requireViewById(R.id.launch_editor).setOnClickListener(launchCameraEditor);
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
}
