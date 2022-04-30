package com.meta.portal.sdk.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class PrivacyShutterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_privacy, container, false);
        return view;
    }
    
    public static PrivacyShutterFragment newInstance() {
        PrivacyShutterFragment privacyShutterFragment = new PrivacyShutterFragment();
        return privacyShutterFragment;
    }

}
