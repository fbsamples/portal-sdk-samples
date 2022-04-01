package com.meta.portal.sdk.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CardViewHolder extends RecyclerView.ViewHolder {
    private final TextView mFeatureName;
    private final TextView mDemoModeTitle;
    
    private Feature mFeature;
    
    private final OnListItemClickedListener mOnListItemClickedListener;

    public CardViewHolder(final View view, final OnListItemClickedListener onListItemClickedListener) {
        super(view);
        mOnListItemClickedListener = onListItemClickedListener;
        mFeatureName = (TextView) view.findViewById(R.id.feature_name);
        mDemoModeTitle = (TextView) view.findViewById(R.id.demo_mode_title);
        
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnListItemClickedListener.onListItemClicked(mFeature);
            }
        });
    }
    
    public void bind(Feature feature) {
        mFeature = feature;
        mFeatureName.setText(feature.getScreenName());
        mDemoModeTitle.setText(feature.getDemoModeTitle());
    }
    
    public interface OnListItemClickedListener {
        void onListItemClicked(final Feature feature);
    }
    
    public static CardViewHolder newInstance(
            final ViewGroup parent, 
            final OnListItemClickedListener onListItemClickedListener) { 
        return new CardViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false),
                onListItemClickedListener);
    }
}
