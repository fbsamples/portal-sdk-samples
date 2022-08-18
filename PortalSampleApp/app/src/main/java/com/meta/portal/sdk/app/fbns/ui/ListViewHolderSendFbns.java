package com.meta.portal.sdk.app.fbns.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.fbns.FbnsData;

public class ListViewHolderSendFbns extends RecyclerView.ViewHolder {
    private final TextView mFeatureNumber;
    private final TextView mFeatureTitle;
    private final ImageButton mInfoButton;
    private final EditText mMsgText;
    private final Button mSendButton;
    private final ImageView mSendIcon;
    private final TextView mSendStatus;
    
    private FbnsData mFeature;

    private final FbnsUiListener mFbnsUiListener;

    private final Context mContext;
    
    public ListViewHolderSendFbns(final View view,
                                  final FbnsUiListener fbnsUiListener) {
        super(view);
        mContext = view.getContext();
        mFbnsUiListener = fbnsUiListener;
        mFeatureNumber = (TextView) view.findViewById(R.id.feature_number);
        mFeatureTitle = (TextView) view.findViewById(R.id.feature_title);
        mInfoButton = (ImageButton) view.findViewById(R.id.info_button);
        mMsgText = (EditText) view.findViewById(R.id.send_message_text);
        mSendButton = (Button) view.findViewById(R.id.send_message_button);
        mSendIcon = (ImageView) view.findViewById(R.id.send_icon);
        mSendStatus = (TextView) view.findViewById(R.id.send_status);

        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFbnsUiListener.onInfoButtonClicked(mFeature);
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFeature.setValueText(mMsgText.getText().toString());
                mFbnsUiListener.onActionButtonClicked(mFeature);
            }
        });
    }
    
    public void bind(FbnsData fbnsData, FbnsUiListener fbnsUiListener) {
        mFeature = fbnsData;
        mFeatureNumber.setText(String.valueOf(fbnsData.getStepIndex()));
        mFeatureTitle.setText(fbnsData.getCardTitle());
        if (fbnsUiListener.getMessageSent()) {
            mSendIcon.setVisibility(View.VISIBLE);
            mSendStatus.setVisibility(View.VISIBLE);
            mSendIcon.setImageResource(fbnsUiListener.getSendStatusIcon());
            mSendIcon.setColorFilter(ContextCompat.getColor(mContext, fbnsUiListener.getSendStatusColor()),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            mSendStatus.setText(fbnsUiListener.getSendStatus());
            mSendStatus.setTextColor(ContextCompat.getColor(mContext, fbnsUiListener.getSendStatusColor()));
        } else {
            mSendIcon.setVisibility(View.INVISIBLE);
            mSendStatus.setVisibility(View.INVISIBLE);
        }
    }
    
    private @DrawableRes int getBackgroundResource(boolean finished) {
        if (finished) {
            return R.drawable.ic_checked;
        } else {
            return R.drawable.ic_unchecked;
        }
    } 

    public static ListViewHolderSendFbns newInstance(
            final ViewGroup parent,
            final FbnsUiListener fbnsUiListener) {
        return new ListViewHolderSendFbns(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_send_fbns, parent, false),
                fbnsUiListener);
    }
}
