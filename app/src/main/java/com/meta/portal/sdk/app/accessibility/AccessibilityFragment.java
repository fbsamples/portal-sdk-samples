/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.accessibility;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.accessibility.ui.AccessibilityUiListener;
import com.meta.portal.sdk.app.accessibility.ui.FeatureCardAdapterAccessibility;
import com.meta.portal.sdk.app.base.ActivityCallback;
import com.meta.portal.sdk.app.data.ListData;

public class AccessibilityFragment extends Fragment implements AccessibilityUiListener {

  private static final String TAG = "AccessibilityFragment";

  private FeatureCardAdapterAccessibility mFeatureCardAdapter;

  private AccessibilityDataHelper mAccessibilityDataHelper;

  RecyclerView mRecyclerView;

  private ActivityCallback mActivityCallback;

//  private FbnsTokenManager mFbnsTokenManager;

//  FbnsMsgSender mMsgSender;
//
//  FBNSNotificationsReceiver fbnsNotificationsReceiver;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAccessibilityDataHelper = new AccessibilityDataHelper(getActivity());
//    mFbnsTokenManager =
//        new FbnsTokenManager(
//            getActivity().getApplicationContext(),
//            FbnsConstants.APP_ID,
//            FbnsConstants.ACCESS_TOKEN);
//    mMsgSender =
//        new FbnsMsgSender(
//            getActivity().getSharedPreferences(FbnsConstants.APP_ID, Context.MODE_PRIVATE));
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_accessibility, container, false);
    return view;
  }

  @RequiresApi(api = Build.VERSION_CODES.P)
  @Override
  public void onViewCreated(final View view, Bundle savedInstanceState) {

//    fbnsNotificationsReceiver = new FBNSNotificationsReceiver();
//    IntentFilter filter = new IntentFilter();
//    filter.addCategory("com.meta.portal.sdk.app");
//    filter.addAction("com.facebook.rti.fbns.intent.RECEIVE");
//    getActivity().registerReceiver(fbnsNotificationsReceiver, filter);

    mRecyclerView = view.findViewById(R.id.recycler_view);

    mFeatureCardAdapter = new FeatureCardAdapterAccessibility();
    mFeatureCardAdapter.setData(mAccessibilityDataHelper.getAccessibilityData());
    mFeatureCardAdapter.setInfoButtonClickedListener(this);
    mFeatureCardAdapter.setHeaderFirst(getString(R.string.accessibility_header_first_title));
    mFeatureCardAdapter.setHeaderSecond(getString(R.string.accessibility_header_second_title));
    mFeatureCardAdapter.setFooter(getString(R.string.accessibility_footer));
    mFeatureCardAdapter.setLink(getString(R.string.accessibility_link));
    mRecyclerView.setAdapter(mFeatureCardAdapter);

    LinearLayoutManager layoutManager =
        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

    mRecyclerView.setLayoutManager(layoutManager);

    SpacesItemDecorator itemDecorator = new SpacesItemDecorator(10);
    mRecyclerView.addItemDecoration(itemDecorator);
  }

  @Override
  public void onInfoButtonClicked(ListData accessibilityData) {
    mActivityCallback.onInfoButtonClicked(accessibilityData);
  }

  protected void setFeatureInfoShowing(boolean showing) {}

  public void setActivityCallback(ActivityCallback activityCallback) {
    mActivityCallback = activityCallback;
  }

  public static AccessibilityFragment newInstance() {
    AccessibilityFragment accessibilityFragment = new AccessibilityFragment();
    return accessibilityFragment;
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

}
