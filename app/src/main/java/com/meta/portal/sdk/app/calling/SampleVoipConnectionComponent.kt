/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.calling

import android.content.ComponentName
import android.content.Context

object SampleVoipConnectionComponent {
  fun getComponentName(context: Context) =
      ComponentName(
          context,
          "com.meta.portal.sdk.app.calling.SampleVoipConnectionService")
}
