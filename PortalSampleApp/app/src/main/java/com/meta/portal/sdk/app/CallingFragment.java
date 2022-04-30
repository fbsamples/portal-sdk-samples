package com.meta.portal.sdk.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

public class CallingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calling, container, false);
        return view;
    }
    
    public static CallingFragment newInstance() {
        CallingFragment callingFragment = new CallingFragment();
        return callingFragment;
    }

}
