/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.calling

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.meta.portal.sdk.app.R



class CallLaunchFragment : Fragment() {

    var mCallLaunchContainer: ViewGroup? = null
    var mOutgoingCallButton: Button? = null
    var mIncomingCallButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mCallLaunchContainer = inflater.inflate(R.layout.fragment_calling, container, false) as ViewGroup
        mOutgoingCallButton = mCallLaunchContainer?.findViewById(R.id.outgoing_call_button);
        mIncomingCallButton = mCallLaunchContainer?.findViewById(R.id.incoming_call_button);
        return mCallLaunchContainer as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mOutgoingCallButton!!.setOnClickListener {
            val intent = Intent(activity, CallManagementActivity::class.java).apply {
                putExtra("incoming_call", false)
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
        mIncomingCallButton!!.setOnClickListener {
            val intent = Intent(activity, CallManagementActivity::class.java).apply {
                putExtra("incoming_call", true)
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        }
    }

    companion object {

        fun newInstance(): CallLaunchFragment? {
            return CallLaunchFragment()
        }

    }
}
