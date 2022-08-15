package com.meta.portal.sdk.app.fbns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.data.FbnsData;

public class ListViewHolderFbns extends RecyclerView.ViewHolder {
    private final TextView mFeatureNumber;
    private final TextView mFeatureTitle;
    private final ImageButton mInfoButton;
    
    private FbnsData mFeature;
    
    private final OnListItemClickedListener mOnListItemClickedListener;
    
    private final OnInfoButtonClickedListener mOnInfoButtonClickedListener;

    public ListViewHolderFbns(
            final View view, 
            final OnListItemClickedListener onListItemClickedListener, 
            final OnInfoButtonClickedListener onInfoButtonClickedListener) {
        super(view);
        mOnListItemClickedListener = onListItemClickedListener;
        mOnInfoButtonClickedListener = onInfoButtonClickedListener;
        mFeatureNumber = (TextView) view.findViewById(R.id.feature_number);
        mFeatureTitle = (TextView) view.findViewById(R.id.feature_title);
        mInfoButton = (ImageButton) view.findViewById(R.id.info_button);
        
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnListItemClickedListener.onListItemClicked(mFeature);
            }
        });

        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ReferenceApp ListViewHolderFbns onInfoButtonClicked");
                mOnInfoButtonClickedListener.onInfoButtonClicked(mFeature);
            }
        });
        
    }
    
    public void bind(FbnsData fbnsData) {
        mFeature = fbnsData;
        mFeatureNumber.setText(String.valueOf(fbnsData.getStep()));
        mFeatureTitle.setText(fbnsData.getCardTitle());
    }
    
    public interface OnListItemClickedListener {
        void onListItemClicked(final FbnsData feature);
    }

    public interface OnInfoButtonClickedListener {
        void onInfoButtonClicked(final FbnsData fbnsData);
    }
    
    public static ListViewHolderFbns newInstance(
            final ViewGroup parent, 
            final OnListItemClickedListener onListItemClickedListener,
            final OnInfoButtonClickedListener onInfoButtonClickedListener) { 
        return new ListViewHolderFbns(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_fbns, parent, false),
                onListItemClickedListener, onInfoButtonClickedListener);
    }
}
