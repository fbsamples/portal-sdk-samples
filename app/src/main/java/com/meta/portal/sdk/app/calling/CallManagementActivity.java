/*
 * Copyright 2022-present, Meta Platforms, Inc. and affiliates
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */

package com.meta.portal.sdk.app.calling;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.base.ActivityCallback;
import com.meta.portal.sdk.app.base.BaseActivity;
import com.meta.portal.sdk.app.base.FeatureBaseActivity;

public class CallManagementActivity extends BaseActivity {

    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_mgmt);
        mFragment = CallFragment.Companion.newInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, mFragment, null)
                .commit();
    }
}
