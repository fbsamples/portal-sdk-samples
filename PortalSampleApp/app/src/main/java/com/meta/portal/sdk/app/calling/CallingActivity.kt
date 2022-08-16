// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.portal.sdk.app.calling

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.meta.portal.sdk.app.R
import com.facebook.portal.calling.external.PortalCalling
import com.facebook.portal.calling.external.PortalVCCallListener
import com.facebook.portal.calling.model.VCCall
import com.facebook.portal.calling.model.VCCallDirection
import com.facebook.portal.calling.model.VCCallParametersExtraKey
import com.facebook.portal.calling.model.VCCallParticipant
import com.facebook.portal.calling.model.VCCallStartParameters
import com.facebook.portal.calling.model.VCContact
import com.facebook.portal.ui.view.PortalCallingCameraButton
import com.facebook.portal.ui.view.PortalCallingHangupButton
import com.facebook.portal.ui.view.PortalCallingMicrophoneButton
import com.facebook.portal.ui.view.PortalSmartCameraEditorButton
import java.util.UUID

@SuppressLint("SetTextI18n", "ToastMayNotBeVisibleToUser")
class CallingActivity : AppCompatActivity() {

  private var portalCalling = PortalCalling.getInstance()
  private val uiHandler = Handler(Looper.getMainLooper())

  private lateinit var createOutgoingCallButton: Button
  private lateinit var createIncomingCallButton: Button
  private lateinit var incomingCallControls: View
  private lateinit var callingBar: View
  private lateinit var smartCameraButton: PortalSmartCameraEditorButton
  private lateinit var cameraButton: PortalCallingCameraButton
  private lateinit var microphoneButton: PortalCallingMicrophoneButton
  private lateinit var hangupButton: PortalCallingHangupButton
  private lateinit var answerButton: Button
  private lateinit var rejectButton: Button
  private lateinit var updateExtrasButton: Button

  private lateinit var deviceState: TextView
  private lateinit var callInfo: TextView

  private lateinit var telephonyManager: TelephonyManager

  private var activeCall: String? = null
  private var incomingCall: String? = null

  private val broadcastReceiver =
      object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
          when (telephonyManager.callState) {
            TelephonyManager.CALL_STATE_IDLE -> deviceState.text = "CALL_STATE_IDLE"
            TelephonyManager.CALL_STATE_OFFHOOK -> deviceState.text = "CALL_STATE_OFFHOOK"
            TelephonyManager.CALL_STATE_RINGING -> deviceState.text = "CALL_STATE_RINGING"
          }
        }
      }

  private val portalVCCallListener =
      object : PortalVCCallListener {
        override fun onCreateIncomingCallFailed(call: VCCall) {
          if (call.id != incomingCall) {
            return
          }

          incomingCall = null
          writeCallInfo()
          showIncomingCallFailureToast()
        }

        override fun onCreateIncomingCall(call: VCCall) {
          if (call.id != incomingCall) {
            return
          }

          incomingCallControls.visibility = View.VISIBLE
        }

        override fun onCreateOutgoingCallFailed(call: VCCall) {
          if (call.id != activeCall) {
            return
          }

          activeCall = null
          writeCallInfo()
          showOutgoingCallFailureToast()
        }

        override fun onCreateOutgoingCall(call: VCCall) {
          if (call.id != activeCall) {
            return
          }

          setButtonsVisibility(true)
          uiHandler.postDelayed(
              {
                portalCalling.setCallConnectionActive(activeCall)
                writeCallInfo()
              },
              500L)
        }

        override fun onAnswer(call: VCCall, videoState: Int) = answerCall()

        override fun onReject(call: VCCall) = rejectCall()

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

  override fun onCreate(savedInstanceState: Bundle?) {
    Log.i(TAG, "onCreate")
    super.onCreate(savedInstanceState)
    portalCalling.init(
        this,
        "Portal SDK Reference App",
        R.mipmap.ic_launcher,
        SampleVoipConnectionComponent.getComponentName(this),
        portalVCCallListener,
        true)
    setContentView(R.layout.activity_calling)
    createOutgoingCallButton = requireViewById(R.id.create_outgoing_call)
    createIncomingCallButton = requireViewById(R.id.create_incoming_call)
    incomingCallControls = requireViewById(R.id.incoming_call_controls)
    callingBar = requireViewById(R.id.calling_bar)
    smartCameraButton = requireViewById(R.id.smart_camera_button)
    cameraButton = requireViewById(R.id.camera_button)
    microphoneButton = requireViewById(R.id.microphone_button)
    hangupButton = requireViewById(R.id.hangup_button)
    answerButton = requireViewById(R.id.answer)
    rejectButton = requireViewById(R.id.reject)
    deviceState = requireViewById(R.id.device_state)
    callInfo = requireViewById(R.id.call_info)
    updateExtrasButton = requireViewById(R.id.update_extras)

    telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    createOutgoingCallButton.setOnClickListener { startCall() }
    createIncomingCallButton.setOnClickListener { receiveIncomingCall() }
    answerButton.setOnClickListener { answerCall() }
    rejectButton.setOnClickListener { rejectCall() }
    updateExtrasButton.setOnClickListener {
      val extras =
          Bundle().apply {
            putString(VCCallParametersExtraKey.CALL_URL, "https://facebook.com")
            putString(VCCallParametersExtraKey.CALL_TITLE, "Sample App call")
          }
      try {
        portalCalling.updateActiveCallExtras(extras)
      } catch (e: IllegalStateException) {
        Log.e(TAG, "Error updating extras", e)
      }
    }
    registerReceiver(broadcastReceiver, IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED))
    portalCalling.registerService()
  }

  override fun onBackPressed() {
    activeCall?.let { portalCalling.endCall(it) }
    super.onBackPressed()
  }

  override fun onDestroy() {
    Log.i(TAG, "onDestroy")
    portalCalling.unregisterService()
    unregisterReceiver(broadcastReceiver)
    super.onDestroy()
  }

  private fun startCall() {
    val extras = companionExtras()
    val callStartParameters =
        VCCallStartParameters.newBuilder()
            .setDirection(VCCallDirection.OUTGOING)
            .setSelf(createSelfParticipant())
            .setContacts(createContactListForOutgoingCall())
            .setExtras(extras)
            .build()
    val call = portalCalling.createCall(callStartParameters)
    if (call == null) {
      showOutgoingCallFailureToast()
    }
    activeCall = portalCalling.activeCall?.id
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

  private fun receiveIncomingCall() {
    val extras =
        Bundle().apply {
          putString(VCCallParametersExtraKey.BUNDLE_KEY_INCOMING_CALL_TITLE, "Sample Meeting Title")
        }
    val callStartParameters =
        VCCallStartParameters.newBuilder()
            .setDirection(VCCallDirection.INCOMING)
            .setSelf(createSelfParticipant())
            .setContacts(createContactListForIncomingCall())
            .setExtras(extras)
            .build()
    val call = portalCalling.createCall(callStartParameters)
    if (call == null) {
      showIncomingCallFailureToast()
    }

    incomingCall = portalCalling.incomingCall?.id
    writeCallInfo()
  }

  private fun answerCall() {
    incomingCallControls.visibility = View.GONE
    setButtonsVisibility(true)
    portalCalling.answerCall(incomingCall)
    activeCall = incomingCall
    incomingCall = null
    writeCallInfo()

    uiHandler.postDelayed(
        {
          portalCalling.setCallConnectionActive(activeCall)
          writeCallInfo()
        },
        500L)
  }

  private fun rejectCall() {
    incomingCallControls.visibility = View.GONE
    portalCalling.rejectCall(incomingCall)
    incomingCall = null
    writeCallInfo()
  }

  private fun endCall() {
    setButtonsVisibility(false)
    activeCall = null
    writeCallInfo()
  }

  private fun writeCallInfo() {
    val builder = StringBuilder()
    builder
        .append("Incoming Call: ")
        .append(portalCalling.incomingCall)
        .append("\nActive Call: ")
        .append(portalCalling.activeCall)

    callInfo.text = builder.toString()
  }

  private fun showOutgoingCallFailureToast() =
      Toast.makeText(this@CallingActivity, "Create outgoing call failed", Toast.LENGTH_LONG).show()

  private fun showIncomingCallFailureToast() =
      Toast.makeText(this@CallingActivity, "Create incoming call failed", Toast.LENGTH_LONG).show()

  private fun createSelfParticipant(): VCCallParticipant {
    return VCCallParticipant.newBuilder()
        .setUserId(SELF_ID)
        .setDisplayName(SELF_DISPLAY_NAME)
        .build()
  }

  private fun createContactListForOutgoingCall(): List<VCContact> {
    return listOf(VCContact(CONTACT_A_ID, CONTACT_A_DISPLAY_NAME))
  }

  private fun createContactListForIncomingCall(): List<VCContact> {
    return listOf(VCContact(CONTACT_B_ID, CONTACT_B_DISPLAY_NAME))
  }

  private fun setButtonsVisibility(isVisible: Boolean) {
    callingBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    smartCameraButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    cameraButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    microphoneButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    hangupButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    updateExtrasButton.visibility = if (isVisible) View.VISIBLE else View.GONE
  }

  companion object {
    private const val TAG = "CallingActivity"
    private val SELF_ID = UUID.randomUUID().toString()
    private const val SELF_DISPLAY_NAME = "Homer Simpson"
    private val CONTACT_A_ID = UUID.randomUUID().toString()
    private const val CONTACT_A_DISPLAY_NAME = "Marge Simpson"
    private val CONTACT_B_ID = UUID.randomUUID().toString()
    private const val CONTACT_B_DISPLAY_NAME = "Bart Simpson"
  }
}
