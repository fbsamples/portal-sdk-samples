package com.meta.portal.sdk.app.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.meta.portal.sdk.app.R;

public class NotificationsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        return view;
    }
    
    public static NotificationsFragment newInstance() {
        NotificationsFragment notificationsFragment = new NotificationsFragment();
        return notificationsFragment;
    }

}
