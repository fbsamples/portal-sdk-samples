package com.meta.portal.sdk.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class CompanionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_companion, container, false);
        return view;
    }
    
    public static CompanionFragment newInstance() {
        CompanionFragment companionFragment = new CompanionFragment();
        return companionFragment;
    }

}
