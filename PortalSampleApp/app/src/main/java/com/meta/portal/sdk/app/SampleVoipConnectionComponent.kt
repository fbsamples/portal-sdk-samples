// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.portal.sdk.app

import android.content.ComponentName
import android.content.Context

object SampleVoipConnectionComponent {
  fun getComponentName(context: Context) =
      ComponentName(
          context,
          "com.meta.portal.sdk.app.SampleVoipConnectionService")
}
