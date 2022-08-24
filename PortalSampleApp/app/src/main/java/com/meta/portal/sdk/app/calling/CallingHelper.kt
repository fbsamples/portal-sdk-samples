// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.portal.sdk.app.calling

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import com.meta.portal.sdk.app.R
import com.facebook.portal.calling.external.PortalCalling
import com.facebook.portal.calling.external.PortalVCCallListener
import com.facebook.portal.calling.model.VCCall
import com.facebook.portal.calling.model.VCCallDirection
import com.facebook.portal.calling.model.VCCallParametersExtraKey
import com.facebook.portal.calling.model.VCCallParticipant
import com.facebook.portal.calling.model.VCCallStartParameters
import com.facebook.portal.calling.model.VCContact
import java.util.UUID

@SuppressLint("SetTextI18n", "ToastMayNotBeVisibleToUser")
class CallingHelper {

    private var portalCalling = PortalCalling.getInstance()

    private lateinit var telephonyManager: TelephonyManager

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


    fun init(context: Context)  {
        portalCalling.init(
            context,
            "Portal SDK Reference App",
            R.mipmap.ic_launcher,
            SampleVoipConnectionComponent.getComponentName(context),
            portalVCCallListener,
            true)
        telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        portalCalling.registerService()
    }

    fun stop() {
        activeCall?.let { portalCalling.endCall(it) }
    }

    fun startCall() {
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

        incomingCall = portalCalling.incomingCall?.id
        writeCallInfo()
    }

    private fun answerCall() {
        portalCalling.answerCall(incomingCall)
        activeCall = incomingCall
        incomingCall = null
        writeCallInfo()
    }

    private fun rejectCall() {
        portalCalling.rejectCall(incomingCall)
        incomingCall = null
        writeCallInfo()
    }

    private fun endCall() {
        activeCall = null
        writeCallInfo()
    }

    private fun writeCallInfo() {

    }

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
