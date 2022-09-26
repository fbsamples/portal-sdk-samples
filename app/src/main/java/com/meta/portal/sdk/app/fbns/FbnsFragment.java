/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.fbns;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.portal.fbns.AbstractFbnsBroadcastReceiver;
import com.facebook.portal.fbns.FbnsTokenManager;
import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.Utils;
import com.meta.portal.sdk.app.base.ActivityCallback;
import com.meta.portal.sdk.app.data.ListData;
import com.meta.portal.sdk.app.fbns.ui.FbnsUiListener;
import com.meta.portal.sdk.app.fbns.ui.FeatureCardAdapterFbns;

public class FbnsFragment extends Fragment implements FbnsUiListener {

  private static final String TAG = "FbnsFragment";

  private FeatureCardAdapterFbns mFeatureCardAdapter;

  private FbnsDataHelper mFbnsDataHelper;

  RecyclerView mRecyclerView;

  private ActivityCallback mActivityCallback;

  private FbnsTokenManager mFbnsTokenManager;

  FbnsMsgSender mMsgSender;

  FBNSNotificationsReceiver fbnsNotificationsReceiver;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFbnsDataHelper = new FbnsDataHelper(getActivity());
    mFbnsTokenManager =
        new FbnsTokenManager(
            getActivity().getApplicationContext(),
            FbnsConstants.APP_ID,
            FbnsConstants.ACCESS_TOKEN);
    mMsgSender =
        new FbnsMsgSender(
            getActivity().getSharedPreferences(FbnsConstants.APP_ID, Context.MODE_PRIVATE));
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_fbns, container, false);
    return view;
  }

  @RequiresApi(api = Build.VERSION_CODES.P)
  @Override
  public void onViewCreated(final View view, Bundle savedInstanceState) {

    fbnsNotificationsReceiver = new FBNSNotificationsReceiver();
    IntentFilter filter = new IntentFilter();
    filter.addCategory("com.meta.portal.sdk.app");
    filter.addAction("com.facebook.rti.fbns.intent.RECEIVE");
    getActivity().registerReceiver(fbnsNotificationsReceiver, filter);

    mRecyclerView = view.findViewById(R.id.recycler_view);

    mFeatureCardAdapter = new FeatureCardAdapterFbns(Utils.isTvDevice(getActivity()));
    mFeatureCardAdapter.setData(mFbnsDataHelper.getFbnsData());
    mFeatureCardAdapter.setInfoButtonClickedListener(this);
    mFeatureCardAdapter.setHeaderFirst(getString(R.string.fbns_header_first_title));
    mFeatureCardAdapter.setHeaderSecond(getString(R.string.fbns_header_second_title));
    mRecyclerView.setAdapter(mFeatureCardAdapter);

    LinearLayoutManager layoutManager =
        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

    mRecyclerView.setLayoutManager(layoutManager);

    SpacesItemDecorator itemDecorator = new SpacesItemDecorator(10);
    mRecyclerView.addItemDecoration(itemDecorator);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    getActivity().unregisterReceiver(fbnsNotificationsReceiver);
  }

  @Override
  public void onInfoButtonClicked(ListData fbnsData) {
    mActivityCallback.onInfoButtonClicked(fbnsData);
  }

  @Override
  public void onActionButtonClicked(ListData fbnsData) {
    if (fbnsData.getStepType() == ListData.STEP_TYPE.REGISTER_TOKEN) {
      requestAndRegisterPushToken();
    } else if (fbnsData.getStepType() == ListData.STEP_TYPE.SEND_MSG) {
      simulateSendPushMessage(fbnsData.getValueText().toString());
    }
  }

  protected void setFeatureInfoShowing(boolean showing) {}

  public void setActivityCallback(ActivityCallback activityCallback) {
    mActivityCallback = activityCallback;
  }

  public static FbnsFragment newInstance() {
    FbnsFragment fbnsFragment = new FbnsFragment();
    return fbnsFragment;
  }

  public void requestAndRegisterPushToken() {
    try {
      mFbnsTokenManager.register();
    } catch (Exception e) {
      Toast.makeText(getContext(), "Push Token Registration Failed", Toast.LENGTH_SHORT);
      Log.i(TAG, "Register failed.", e);
    }
  }

  public void simulateSendPushMessage(String msg) {
    new Thread(
            new Runnable() {
              @Override
              public void run() {
                mMsgSender.sendNotificationAsync(
                    msg,
                    getActivity()
                        .getSharedPreferences(FbnsConstants.APP_ID, Context.MODE_PRIVATE)
                        .getString(FbnsConstants.PREF_TOKEN, ""));
              }
            })
        .run();
  }

  public class SpacesItemDecorator extends RecyclerView.ItemDecoration {

    private final int space;

    public SpacesItemDecorator(int spaceInPx) {
      this.space = spaceInPx;
    }

    @Override
    public void getItemOffsets(
        Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
      outRect.top = space;
      outRect.bottom = space;
      outRect.left = space;
      outRect.right = space;
    }
  }

  public class FBNSNotificationsReceiver extends AbstractFbnsBroadcastReceiver {

    public FBNSNotificationsReceiver() {
      super(FbnsConstants.ACCESS_TOKEN, FbnsConstants.APP_ID);
    }

    @Override
    protected void onMessage(Context context, @Nullable String payload) {
      Log.i(TAG, "onMessage: " + payload);
      mFbnsDataHelper.updateReceivedMessage(payload);
      mFeatureCardAdapter.setData(mFbnsDataHelper.getFbnsData());
      mFeatureCardAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onRegisterSuccess(Context context, String token) {
      Log.i(TAG, "onRegistered: " + token);
      context
          .getSharedPreferences(FbnsConstants.APP_ID, Context.MODE_PRIVATE)
          .edit()
          .putString(FbnsConstants.PREF_TOKEN, token)
          .apply();
      mFbnsDataHelper.updatePushToken(token);
      mFeatureCardAdapter.setData(mFbnsDataHelper.getFbnsData());
      mFeatureCardAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onRegisterFailed(Context context, String traceId) {
      Log.e(TAG, "Error trace id: " + traceId);
      Toast.makeText(getContext(), "Push Token Registration Failed", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onUnregistered(Context context) {
      Log.d(TAG, "onUnregistered");
    }

    @Override
    protected void onRegistrationError(Context context, String error) {
      Log.e(TAG, "onRegistrationError");
      Toast.makeText(getContext(), "Push Token Registration Failed", Toast.LENGTH_SHORT);
    }
  }
}
