package com.meta.portal.sdk.app.fbns;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.data.FbnsData;

import java.util.ArrayList;
import java.util.List;

public class FeatureCardAdapterFbns extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_ITEM = 2;
    
    private FbnsDataCardAdapterListener mFbnsDataCardAdapterListener;
    
    private final CardViewHolderFbns.OnListItemClickedListener mOnListItemClicked;

    private List<FbnsData> mFbnsData = new ArrayList<>();
    
    public FeatureCardAdapterFbns() {
        mOnListItemClicked = new CardViewHolderFbns.OnListItemClickedListener() {
            @Override
            public void onListItemClicked(final FbnsData feature) {
                mFbnsDataCardAdapterListener.onListItemClicked(feature);
            } 
        };
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CardViewHolderFbns.newInstance(parent, mOnListItemClicked);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ITEM_VIEW_TYPE_ITEM:
                ((CardViewHolderFbns) holder).bind(mFbnsData.get(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mFbnsData.size();
    }

    public void setData(List<FbnsData> features) {
        mFbnsData.clear();
        mFbnsData.addAll(features);
        notifyDataSetChanged();
    }

    public void setFbnsDataAdapterListener(final FbnsDataCardAdapterListener featureCardAdapterListener) {
        mFbnsDataCardAdapterListener = featureCardAdapterListener;
    }

}


