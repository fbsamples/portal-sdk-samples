package com.meta.portal.sdk.app.fbns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.data.FbnsData;

public class ListViewHolderRegisterFbns extends RecyclerView.ViewHolder {
    private final TextView mFeatureNumber;
    private final TextView mFeatureTitle;
    private final ImageButton mInfoButton;
    private final Button mRequestButton;
    
    private FbnsData mFeature;

    private final ListViewHolderFbns.OnInfoButtonClickedListener mOnInfoButtonClickedListener;
    
    public ListViewHolderRegisterFbns(final View view,
                                      final ListViewHolderFbns.OnInfoButtonClickedListener onInfoButtonClickedListener) {
        super(view);
        mOnInfoButtonClickedListener = onInfoButtonClickedListener;
        mFeatureNumber = (TextView) view.findViewById(R.id.feature_number);
        mFeatureTitle = (TextView) view.findViewById(R.id.feature_title);
        mInfoButton = (ImageButton) view.findViewById(R.id.info_button);
        mRequestButton = (Button) view.findViewById(R.id.request_token_button);

        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ReferenceApp ListViewHolderFbns onInfoButtonClicked");
                mOnInfoButtonClickedListener.onInfoButtonClicked(mFeature);
            }
        });

        mRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    
    public void bind(FbnsData fbnsData) {
        mFeature = fbnsData;
        mFeatureNumber.setText(String.valueOf(fbnsData.getStep()));
        mFeatureTitle.setText(fbnsData.getCardTitle());
    }
    
    private @DrawableRes int getBackgroundResource(boolean finished) {
        if (finished) {
            return R.drawable.ic_checked;
        } else {
            return R.drawable.ic_unchecked;
        }
    } 
    
    public interface OnListItemClickedListener {
        void onListItemClicked(final FbnsData feature);
    }
    
    public static ListViewHolderRegisterFbns newInstance(
            final ViewGroup parent,
            final ListViewHolderFbns.OnInfoButtonClickedListener onInfoButtonClickedListener) {
        return new ListViewHolderRegisterFbns(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_register_fbns, parent, false),
                onInfoButtonClickedListener);
    }
}
