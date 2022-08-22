package com.meta.portal.sdk.app.fbns;

import android.content.Context;
import android.util.Log;
import androidx.annotation.Nullable;
import com.facebook.portal.fbns.AbstractFbnsBroadcastReceiver;

/** This is an example of standalone FBNS Notifications Receiver */
public class FbnsNotificationsReceiverGlobal extends AbstractFbnsBroadcastReceiver {

  public static final String TAG = "FbnsNotificationsRcvrG";

  public FbnsNotificationsReceiverGlobal() {
    super(FbnsConstants.ACCESS_TOKEN, FbnsConstants.APP_ID);
  }

  @Override
  protected void onMessage(Context context, @Nullable String payload) {
    Log.i(TAG, "onMessage: " + payload);
  }

  @Override
  protected void onRegisterSuccess(Context context, String token) {
    Log.i(TAG, "onRegistered: " + token);
    context
        .getSharedPreferences("portal_sdk_ref_app", Context.MODE_PRIVATE)
        .edit()
        .putString("fbns_push_token", token)
        .apply();
  }

  @Override
  protected void onRegisterFailed(Context context, String traceId) {
    Log.e(TAG, "Error trace id: " + traceId);
    // Toast.makeText(getContext(), "Push Token Registration Failed", Toast.LENGTH_SHORT);
  }

  @Override
  protected void onUnregistered(Context context) {
    Log.d(TAG, "onUnregistered");
  }

  @Override
  protected void onRegistrationError(Context context, String error) {
    Log.e(TAG, "onRegistrationError");
    // Toast.makeText(getContext(), "Push Token Registration Failed", Toast.LENGTH_SHORT);
  }
}
