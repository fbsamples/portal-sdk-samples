package com.meta.portal.sdk.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class HandRaiseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_handraise, container, false);
        return view;
    }
    
    public static HandRaiseFragment newInstance() {
        HandRaiseFragment handRaiseFragment = new HandRaiseFragment();
        return handRaiseFragment;
    }

}
