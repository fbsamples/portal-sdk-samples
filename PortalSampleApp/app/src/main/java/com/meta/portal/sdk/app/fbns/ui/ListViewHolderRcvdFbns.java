package com.meta.portal.sdk.app.fbns.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.fbns.FbnsData;

public class ListViewHolderRcvdFbns extends RecyclerView.ViewHolder {
    private final TextView mFeatureNumber;
    private final TextView mFeatureTitle;
    private final ImageButton mInfoButton;
    private final TextView mMsgText;

    private FbnsData mFeature;

    private final FbnsUiListener mFbnsUiListener;

    public ListViewHolderRcvdFbns(final View view,
                                  final FbnsUiListener fbnsUiListener) {
        super(view);
        mFbnsUiListener = fbnsUiListener;
        mFeatureNumber = (TextView) view.findViewById(R.id.feature_number);
        mFeatureTitle = (TextView) view.findViewById(R.id.feature_title);
        mInfoButton = (ImageButton) view.findViewById(R.id.info_button);
        mMsgText = (TextView) view.findViewById(R.id.rcvd_message_text);

        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFbnsUiListener.onInfoButtonClicked(mFeature);
            }
        });
    }
    
    public void bind(FbnsData fbnsData) {
        mFeature = fbnsData;
        mFeatureNumber.setText(String.valueOf(fbnsData.getStepIndex()));
        mFeatureTitle.setText(fbnsData.getCardTitle());
        mMsgText.setText(fbnsData.getValueText().toString());
    }
    
    private @DrawableRes int getBackgroundResource(boolean finished) {
        if (finished) {
            return R.drawable.ic_checked;
        } else {
            return R.drawable.ic_unchecked;
        }
    } 

    public static ListViewHolderRcvdFbns newInstance(
            final ViewGroup parent,
            final FbnsUiListener fbnsUiListener) {
        return new ListViewHolderRcvdFbns(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rcvd_fbns, parent, false),
                fbnsUiListener);
    }
}
