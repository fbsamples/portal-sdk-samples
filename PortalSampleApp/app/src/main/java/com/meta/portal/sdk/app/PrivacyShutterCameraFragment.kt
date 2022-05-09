/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.meta.portal.sdk.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.window.WindowManager
import com.facebook.portal.systemstate.SystemStateClient
import com.meta.portal.sdk.app.databinding.FragmentCameraBinding
import com.meta.portal.sdk.app.databinding.PrivacyShutterCameraUiContainerBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import android.media.AudioManager

private const val PERMISSIONS_REQUEST_CODE_CAMERA = 10
private const val PERMISSIONS_REQUEST_CODE_MICROPHONE = 11
private val PERMISSIONS_REQUIRED_CAMERA = arrayOf(Manifest.permission.CAMERA)
private val PERMISSIONS_REQUIRED_MICROPHONE = arrayOf(Manifest.permission.RECORD_AUDIO)

class PrivacyShutterCameraFragment : Fragment() {

    private var _fragmentCameraBinding: FragmentCameraBinding? = null

    private val fragmentCameraBinding get() = _fragmentCameraBinding!!

    private var privacyShutterCameraUiContainerBinding: PrivacyShutterCameraUiContainerBinding? = null

    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var windowManager: WindowManager

    // Recording Info
    private var mRecordingSampler: RecordingSampler? = null

    private lateinit var privacyStateClient: SystemStateClient

    private val listener: SystemStateClient.SystemStateListener =
        object : SystemStateClient.SystemStateListener {
            override fun onCameraStateChanged(enabled: Boolean) {
                Log.d(TAG, "Camera State $enabled")
                privacyShutterCameraUiContainerBinding?.cameraButton?.setSelected(!enabled)

            }

            override fun onMicrophoneStateChanged(enabled: Boolean) {
                Log.d(TAG, "Microphone State $enabled")
                privacyShutterCameraUiContainerBinding?.microphoneButton?.setSelected(!enabled)

            }
        }

    private val displayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    /** Blocking camera operations are performed using this executor */
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        privacyStateClient = SystemStateClient(activity)
    }

    override fun onResume() {
        super.onResume()
        // Make sure that all permissions are still present, since the
        // user could have removed them while the app was in paused state.
        if (!hasPermissionsCamera(requireContext())) {
            // Request camera-related permissions
            requestPermissions(PERMISSIONS_REQUIRED_CAMERA, PERMISSIONS_REQUEST_CODE_CAMERA)
        } else if (!hasPermissionsMicrophone(requireContext())) {
            // Request microphone-related permissions
            requestPermissions(PERMISSIONS_REQUIRED_MICROPHONE, PERMISSIONS_REQUEST_CODE_MICROPHONE)
        }
        privacyStateClient.registerListener(listener)
    }

    override fun onPause() {
        super.onPause()
        privacyStateClient.unregisterListener(listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        privacyStateClient.destroy()
    }

    override fun onDestroyView() {
        _fragmentCameraBinding = null
        super.onDestroyView()

        // Shut down our background executor
        cameraExecutor.shutdown()

        mRecordingSampler?.release();
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _fragmentCameraBinding = FragmentCameraBinding.inflate(inflater, container, false)
        return fragmentCameraBinding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        //Initialize WindowManager to retrieve display metrics
        windowManager = WindowManager(view.context)

        // Wait for the views to be properly laid out
        fragmentCameraBinding.viewFinder.post {

            // Build UI controls
            updateCameraUi()

            // Set up the camera and its use cases
            setUpCamera()
        }

    }

    /**
     * Inflate camera controls and update the UI manually upon config changes to avoid removing
     * and re-adding the view finder from the view hierarchy; this provides a seamless rotation
     * transition on devices that support it.
     *
     * NOTE: The flag is supported starting in Android 8 but there still is a small flash on the
     * screen for devices that run Android 9 or below.
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Rebind the camera with the updated display metrics
        bindCameraUseCases()

    }

    /** Initialize CameraX, and prepare to bind the camera use cases  */
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {

            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Select lensFacing depending on the available cameras
            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            // Build and bind the camera use cases
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    /** Declare and bind preview, capture and analysis use cases */
    private fun bindCameraUseCases() {

        // Get screen metrics used to setup camera for full screen resolution
        val metrics = windowManager.getCurrentWindowMetrics().bounds
        Log.d(TAG, "Screen metrics: ${metrics.width()} x ${metrics.height()}")

        val screenAspectRatio = aspectRatio(metrics.width(), metrics.height())
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

        val rotation = fragmentCameraBinding.viewFinder.display.rotation

        // CameraProvider
        val cameraProvider = cameraProvider
                ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        // Preview
        preview = Preview.Builder()
                // We request aspect ratio but no resolution
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation
                .setTargetRotation(rotation)
                .build()

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(fragmentCameraBinding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    /**
     *  [androidx.camera.core.ImageAnalysis.Builder] requires enum value of
     *  [androidx.camera.core.AspectRatio]. Currently it has values of 4:3 & 16:9.
     *
     *  Detecting the most suitable ratio for dimensions provided in @params by counting absolute
     *  of preview ratio to one of the provided values.
     *
     *  @param width - preview width
     *  @param height - preview height
     *  @return suitable aspect ratio
     */
    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    /** Method used to re-draw the camera UI controls, called every time configuration changes. */
    private fun updateCameraUi() {

        // Remove previous UI if any
        privacyShutterCameraUiContainerBinding?.root?.let {
            fragmentCameraBinding.root.removeView(it)
        }

        privacyShutterCameraUiContainerBinding = PrivacyShutterCameraUiContainerBinding.inflate(
                LayoutInflater.from(requireContext()),
                fragmentCameraBinding.root,
                true
        )

        privacyShutterCameraUiContainerBinding?.cameraButton?.setOnClickListener {
            it.setSelected(!it.isSelected())

            if (it.isSelected()) {
                //Handle selected state change
                val cameraProvider = cameraProvider
                        ?: throw IllegalStateException("Camera initialization failed.")
                cameraProvider.unbindAll()
            } else {
                //Handle de-select state change
                // Rebind the camera with the updated display metrics
                bindCameraUseCases()
            }

        }

        privacyShutterCameraUiContainerBinding?.microphoneButton?.setOnClickListener {
            it.setSelected(!it.isSelected())
            val audioManager = requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (it.isSelected()) {
                //Handle selected state change
                if (audioManager.isMicrophoneMute == false) {
                    audioManager.isMicrophoneMute = true
                }

            } else {
                //Handle de-select state change
                if (audioManager.isMicrophoneMute == true) {
                    audioManager.isMicrophoneMute = false
                }
            }

        }

        if (hasPermissionsMicrophone(requireContext())) {
            mRecordingSampler = RecordingSampler()
            mRecordingSampler?.link(privacyShutterCameraUiContainerBinding?.visualizer)
            mRecordingSampler?.startRecording();
        }

    }

    /** Returns true if the device has an available back camera. False otherwise */
    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    /** Returns true if the device has an available front camera. False otherwise */
    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE_CAMERA) {
            if (!(PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull())) {
                activity?.onBackPressed()
            }
        }
        if (requestCode == PERMISSIONS_REQUEST_CODE_MICROPHONE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                mRecordingSampler = RecordingSampler()
                mRecordingSampler?.link(privacyShutterCameraUiContainerBinding?.visualizer)
                mRecordingSampler?.startRecording();
            } else {
                activity?.onBackPressed()
            }
        }
    }

    /** Convenience method used to check if all permissions required by this app are granted */
    fun hasPermissionsCamera(context: Context) = PERMISSIONS_REQUIRED_CAMERA.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    /** Convenience method used to check if all permissions required by this app are granted */
    fun hasPermissionsMicrophone(context: Context) = PERMISSIONS_REQUIRED_MICROPHONE.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {

        private const val TAG = "PrivacyShutterCameraFragment"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        fun newInstance(): PrivacyShutterCameraFragment? {
            return PrivacyShutterCameraFragment()
        }

    }

}
