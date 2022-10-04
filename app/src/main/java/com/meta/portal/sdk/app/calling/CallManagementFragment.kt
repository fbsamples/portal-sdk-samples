/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.calling

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.facebook.portal.systemstate.SystemStateClient
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import android.media.AudioManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview

import com.meta.portal.sdk.app.R
import com.meta.portal.sdk.app.Utils

import com.facebook.portal.calling.external.PortalCalling
import com.facebook.portal.calling.external.PortalVCCallListener
import com.facebook.portal.calling.model.VCCallParticipant
import com.facebook.portal.calling.model.VCCall
import com.facebook.portal.calling.model.VCCallParametersExtraKey
import com.facebook.portal.calling.model.VCCallDirection
import com.facebook.portal.calling.model.VCCallStartParameters
import com.facebook.portal.calling.model.VCContact
import java.util.UUID


private const val PERMISSIONS_REQUEST_CODE_CAMERA = 10
private const val PERMISSIONS_REQUEST_CODE_MICROPHONE = 11
private val PERMISSIONS_REQUIRED_CAMERA = arrayOf(Manifest.permission.CAMERA)
private val PERMISSIONS_REQUIRED_MICROPHONE = arrayOf(Manifest.permission.RECORD_AUDIO)

class CallFragment : Fragment() {

    private var callUiContainer: RelativeLayout? = null

    private var fragmentCameraView: ViewGroup? = null
    private var viewFinder: PreviewView? = null

    private var avatarView: ImageView? = null

    private var cameraButton: ImageButton? = null
    private var microphoneButton: ImageButton? = null

    private var hangUpButton: ImageButton? = null

    private var designModeLayoutContainer: RelativeLayout? = null

    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private lateinit var privacyStateClient: SystemStateClient

    private lateinit var callingManager: CallingManager

    private var isCallActive = false

    private var callControlsLayoutContainer: LinearLayout? = null

    var callType = false;

    private val listener: SystemStateClient.SystemStateListener =
        object : SystemStateClient.SystemStateListener {
            override fun onCameraStateChanged(enabled: Boolean) {
                Log.d(TAG, "Camera State $enabled")
                cameraButton?.setSelected(!enabled)
                if(enabled) {
                    callingManager.unmuteCamera()
                }else{
                    callingManager.unmuteCamera()
                }
            }

            override fun onMicrophoneStateChanged(enabled: Boolean) {
                Log.d(TAG, "Microphone State $enabled")
                microphoneButton?.setSelected(!enabled)
                if(enabled) {
                    callingManager.unmuteMic()
                }else{
                    callingManager.muteMic()
                }
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
        callingManager = CallingManager()
        callingManager.init(requireContext())
        callType = activity!!.intent.getBooleanExtra("incoming_call",false)
        callingManager.startCall(callType)
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
        super.onDestroyView()

        // Shut down our background executor
        cameraExecutor.shutdown()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        fragmentCameraView = inflater.inflate(R.layout.fragment_call_screen, container, false) as ViewGroup
        viewFinder = fragmentCameraView?.findViewById(R.id.view_finder)
        return fragmentCameraView as View
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    fun setupCallScreen(){
        isCallActive = true
        activity!!.runOnUiThread(Runnable {
            // Wait for the views to be properly laid out
            viewFinder?.post {
                // Build UI controls
                updateUi()
                // Set up the camera and its use cases
                setUpCamera()
            }
        })
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
        val metrics = resources.displayMetrics

        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

        val rotation = viewFinder?.display?.rotation

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
                .setTargetRotation(rotation!!)
                .build()

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(viewFinder?.surfaceProvider)
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
    private fun updateUi() {
        callUiContainer = LayoutInflater.from(requireContext()).inflate(
                R.layout.call_ui_container,
                fragmentCameraView) as RelativeLayout

        avatarView = callUiContainer?.findViewById(R.id.avatar_view)

        cameraButton = callUiContainer?.findViewById(R.id.camera_button)

        microphoneButton = callUiContainer?.findViewById(R.id.microphone_button)

        hangUpButton = callUiContainer?.findViewById(R.id.hang_up_button)


        designModeLayoutContainer = callUiContainer?.findViewById(
                 R.id.design_mode_layout_container)

        callControlsLayoutContainer = callUiContainer?.findViewById(
            R.id.call_controls)


        cameraButton?.setOnClickListener {
            it.setSelected(!it.isSelected())

            if (it.isSelected()) {
                //Handle selected state change
                val cameraProvider = cameraProvider
                    ?: throw IllegalStateException("Camera initialization failed.")
                cameraProvider.unbindAll()
                viewFinder?.visibility = View.GONE
                callingManager.muteCamera()
            } else {
                //Handle de-select state change
                // Rebind the camera with the updated display metrics
                bindCameraUseCases()
                viewFinder?.visibility = View.VISIBLE
                callingManager.unmuteCamera()
            }

        }

        microphoneButton?.setOnClickListener {
            it.setSelected(!it.isSelected())
            val audioManager = requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (it.isSelected()) {
                //Handle selected state change
                if (audioManager.isMicrophoneMute == false) {
                    audioManager.isMicrophoneMute = true
                    callingManager.muteMic()
                }

            } else {
                //Handle de-select state change
                if (audioManager.isMicrophoneMute == true) {
                    audioManager.isMicrophoneMute = false
                    callingManager.unmuteMic()
                }
            }

        }

        hangUpButton?.setOnClickListener {
            it.setSelected(!it.isSelected())

            if (it.isSelected()) {
                isCallActive = false
                callingManager.endCall()
                callingManager.deinit()
                activity!!.finish()
            } else {

            }
        }

       updateVisibility()
    }

    private fun updateVisibility() {
        if (!isCallActive) {
            viewFinder?.visibility = View.GONE
            callControlsLayoutContainer?.visibility = View.GONE
            avatarView?.visibility = View.INVISIBLE

        } else {
            viewFinder?.visibility = View.VISIBLE
            callControlsLayoutContainer?.visibility = View.VISIBLE
            avatarView?.visibility = View.VISIBLE
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
        if (requestCode == PERMISSIONS_REQUEST_CODE_CAMERA ||
                requestCode == PERMISSIONS_REQUEST_CODE_MICROPHONE) {
            if (!(PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull())) {
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

    fun updateDesignModeLayoutContainerVisibility(visible: Boolean) {
        if (visible) {
            designModeLayoutContainer?.visibility =
                View.VISIBLE
        } else {
            designModeLayoutContainer?.visibility =
                View.GONE
        }
    }

    fun setFeatureInfoShowing(showing: Boolean) {
        if (showing) {
            cameraButton?.setEnabled(false)
            microphoneButton?.setEnabled(false)
        } else {
            cameraButton?.setEnabled(true)
            microphoneButton?.setEnabled(true)
        }
    }

    fun onBackPressed() {
        if (isCallActive) {
            isCallActive = false
            callingManager.endCall()
            callingManager.deinit()
        }
    }

    companion object {

        private const val TAG = "OutgoingCallFragment"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        private const val APP_PACKAGE_NAME = "com.meta.portal.sdk.app"

        private val SELF_ID = UUID.randomUUID().toString()
        private const val SELF_DISPLAY_NAME = "Alice"
        private val SELF_PROFILE_ICON = Icon.createWithResource(APP_PACKAGE_NAME, R.drawable.avatar_3)
        private val CONTACT_A_ID = UUID.randomUUID().toString()
        private const val CONTACT_A_DISPLAY_NAME = "Bob"
        private val CONTACT_A_PROFILE_ICON = Icon.createWithResource(APP_PACKAGE_NAME, R.drawable.avatar_7)
        private val CONTACT_B_ID = UUID.randomUUID().toString()
        private const val CONTACT_B_DISPLAY_NAME = "John"
        private val CONTACT_B_PROFILE_ICON = Icon.createWithResource(APP_PACKAGE_NAME, R.drawable.avatar_5)

        fun newInstance(): CallFragment? {
            return CallFragment()
        }

    }

    @SuppressLint("SetTextI18n", "ToastMayNotBeVisibleToUser")
    inner class CallingManager {

        private var portalCalling = PortalCalling.getInstance()

        private var activeCall: String? = null
        private var incomingCall: String? = null

        private val portalVCCallListener =
            object : PortalVCCallListener {
                override fun onCreateIncomingCallFailed(call: VCCall) {
                    if (call.id != incomingCall) {
                        return
                    }

                    incomingCall = null
                    writeCallInfo()
                }

                override fun onCreateIncomingCall(call: VCCall) {
                    if (call.id != incomingCall) {
                        return
                    }
                }

                override fun onCreateOutgoingCallFailed(call: VCCall) {
                    if (call.id != activeCall) {
                        return
                    }

                    activeCall = null
                    writeCallInfo()
                }

                override fun onCreateOutgoingCall(call: VCCall) {
                    if (call.id != activeCall) {
                        return
                    }
                    portalCalling.setCallConnectionActive(call.id)
                    setupCallScreen()
                    writeCallInfo()
                }

                override fun onAnswer(call: VCCall, videoState: Int) {
                    portalCalling.setCallConnectionActive(call.id)
                    portalCalling.answerCall(incomingCall)
                    activeCall = incomingCall
                    incomingCall = null
                    setupCallScreen()
                    writeCallInfo()
                }

                override fun onReject(call: VCCall) {
                    portalCalling.rejectCall(incomingCall)
                    incomingCall = null
                    activity!!.finish()
                    writeCallInfo()
                }

                override fun onDisconnect(call: VCCall) = endCall()

                override fun onMuteMicrophone(call: VCCall) = writeCallInfo()

                override fun onUnmuteMicrophone(call: VCCall) = writeCallInfo()

                override fun onEnableMicrophone(call: VCCall) = writeCallInfo()

                override fun onDisableMicrophone(call: VCCall) = writeCallInfo()

                override fun onMuteCamera(call: VCCall) = writeCallInfo()

                override fun onUnmuteCamera(call: VCCall) = writeCallInfo()

                override fun onEnableCamera(call: VCCall) = writeCallInfo()

                override fun onDisableCamera(call: VCCall) = writeCallInfo()

                override fun onRaiseHand(call: VCCall?) = Unit

                override fun onLowerHand(call: VCCall?) = Unit

                override fun onCallAudioRouteChanged(callAudioRoute: Int) = Unit
            }

        fun init(context: Context)  {
            portalCalling.init(
                context,
                "Portal SDK Reference App",
                R.mipmap.ic_launcher,
                SampleVoipConnectionComponent.getComponentName(context),
                portalVCCallListener,
                true)

            portalCalling.registerService()
        }

        fun startCall(isIncoming: Boolean){
            if (isIncoming) receiveIncomingCall()
            else startOutgoingCall()
        }

        fun startOutgoingCall() {
            val extras = companionExtras()
            val callStartParameters =
                VCCallStartParameters.newBuilder()
                    .setDirection(VCCallDirection.OUTGOING)
                    .setSelf(createSelfParticipant())
                    .setContacts(createContactListForOutgoingCall())
                    .setExtras(extras)
                    .build()
            val call = portalCalling.createCall(callStartParameters)
            activeCall = portalCalling.activeCall?.id
        }

        fun receiveIncomingCall() {
            val extras =
                Bundle().apply {
                    putString(VCCallParametersExtraKey.BUNDLE_KEY_INCOMING_CALL_TITLE, "1:1-Bob/Alice")
                }
            val callStartParameters =
                VCCallStartParameters.newBuilder()
                    .setDirection(VCCallDirection.INCOMING)
                    .setSelf(createSelfParticipant())
                    .setContacts(createContactListForIncomingCall())
                    .setExtras(extras)
                    .build()
            val call = portalCalling.createCall(callStartParameters)

            incomingCall = portalCalling.incomingCall?.id
            writeCallInfo()
        }


        private fun companionExtras() =
            Bundle().apply {
                putString(
                    "call_context",
                    "{\"join_conference_name\":\"ROOM:4186655701460350\",\"link_url\":\"https://fb.workplace.com/groupcall/LINK:vsktrfnslswn/\"}")
                putString(
                    VCCallParametersExtraKey.CALL_URL,
                    "https://fb.workplace.com/groupcall/LINK:vsktrfnslswn")
            }

        fun endCall() {
            activeCall?.let { portalCalling.endCall(it) }
        }

        fun muteMic() {
            activity!!.runOnUiThread(Runnable {
                portalCalling.muteMicrophone();
            })
        }

        fun unmuteMic() {
            activity!!.runOnUiThread(Runnable {
                portalCalling.unmuteMicrophone();
            })
        }

        fun muteCamera() {
            activity!!.runOnUiThread(Runnable {
                portalCalling.muteCamera();
            })
        }

        fun unmuteCamera() {
            activity!!.runOnUiThread(Runnable {
                portalCalling.unmuteCamera();
            })

        }

        fun deinit(){
            portalCalling.unregisterService()
        }

        private fun writeCallInfo() {

        }

        private fun createSelfParticipant(): VCCallParticipant {
            return VCCallParticipant.newBuilder()
                .setUserId(SELF_ID)
                .setDisplayName(SELF_DISPLAY_NAME)
                // On next SDK push, set the icon
                // .setProfileIcon(SELF_PROFILE_ICON)
                .build()
        }

        private fun createContactListForOutgoingCall(): List<VCContact> {
            // On next SDK push, use this constructor
            val contact = VCContact(CONTACT_A_ID, CONTACT_A_DISPLAY_NAME, CONTACT_A_PROFILE_ICON)
            return listOf(contact)
        }

        private fun createContactListForIncomingCall(): List<VCContact> {
            // On next SDK push, use this constructor
            val contact = VCContact(CONTACT_B_ID, CONTACT_B_DISPLAY_NAME, CONTACT_B_PROFILE_ICON)
            return listOf(contact)
        }
    }

}
