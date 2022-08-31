/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.fbns;

import com.meta.portal.sdk.app.BuildConfig;

public class FbnsConstants {

  public static final String APP_ID = BuildConfig.APP_ID;
  public static final String CLIENT_TOKEN = BuildConfig.CLIENT_TOKEN;
  public static final String ACCESS_TOKEN = APP_ID + "|" + CLIENT_TOKEN;
  public static final String APP_SECRET = BuildConfig.APP_SECRET;

  public static final String EXTRA_LOG_DATA = "data";
  public static final String EXTRA_LOG_TIME = "time";
  public static final String EXTRA_PAYLOAD = "payload";
  public static final String PREF_TOKEN = "fbns_token";
  public static final String PREF_KEY_TOKEN = "token";
  public static final String PREF_SENDER_ACCESS_TOKEN = "sender_access_token";
}
