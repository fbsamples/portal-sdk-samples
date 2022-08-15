package com.meta.portal.sdk.app.fbns;

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
import com.meta.portal.sdk.app.data.FbnsData;

public class ListViewHolderSendFbns extends RecyclerView.ViewHolder {
    private final TextView mFeatureNumber;
    private final TextView mFeatureTitle;
    private final ImageButton mInfoButton;
    private final Button mSendButton;
    private final ImageView mSendIcon;
    private final TextView mSendStatus;
    private final EditText mSendMessage;
    
    private FbnsData mFeature;
    private FbnsHelper mFbnsHelper;

    private final Context mContext;

    private final ListViewHolderFbns.OnInfoButtonClickedListener mOnInfoButtonClickedListener;
    
    public ListViewHolderSendFbns(final View view,
                                  final ListViewHolderFbns.OnInfoButtonClickedListener onInfoButtonClickedListener) {
        super(view);
        mContext = view.getContext();
        mOnInfoButtonClickedListener = onInfoButtonClickedListener;
        mFeatureNumber = (TextView) view.findViewById(R.id.feature_number);
        mFeatureTitle = (TextView) view.findViewById(R.id.feature_title);
        mInfoButton = (ImageButton) view.findViewById(R.id.info_button);
        mSendButton = (Button) view.findViewById(R.id.send_message_button);
        mSendIcon = (ImageView) view.findViewById(R.id.send_icon);
        mSendStatus = (TextView) view.findViewById(R.id.send_status);
        mSendMessage = (EditText) view.findViewById(R.id.send_message);

        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ReferenceApp ListViewHolderFbns onInfoButtonClicked");
                mOnInfoButtonClickedListener.onInfoButtonClicked(mFeature);
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFbnsHelper.sendMessageButtonClicked(mSendMessage.getText().toString());
            }
        });
    }
    
    public void bind(FbnsData fbnsData, FbnsHelper fbnsHelper) {
        mFeature = fbnsData;
        mFbnsHelper = fbnsHelper;
        mFeatureNumber.setText(String.valueOf(fbnsData.getStep()));
        mFeatureTitle.setText(fbnsData.getCardTitle());
        mSendIcon.setImageResource(fbnsHelper.getSendStatusIcon());
        mSendIcon.setColorFilter(ContextCompat.getColor(mContext, fbnsHelper.getSendStatusColor()),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        mSendStatus.setText(fbnsHelper.getSendStatus());
        mSendStatus.setTextColor(ContextCompat.getColor(mContext, fbnsHelper.getSendStatusColor()));
    }
    
    public interface OnListItemClickedListener {
        void onListItemClicked(final FbnsData feature);
    }
    
    public static ListViewHolderSendFbns newInstance(
            final ViewGroup parent,
            final ListViewHolderFbns.OnInfoButtonClickedListener onInfoButtonClickedListener) { 
        return new ListViewHolderSendFbns(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_send_fbns, parent, false),
                onInfoButtonClickedListener);
    }
}
