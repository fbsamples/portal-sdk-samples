package com.meta.portal.sdk.app;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FeatureCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_HEADER_1 = 0;
    private static final int ITEM_VIEW_TYPE_HEADER_2 = 1;
    private static final int ITEM_VIEW_TYPE_ITEM = 2;
    
    private static final int HEADER_COUNT = 2;
    
    private FeatureCardAdapterListener mFeatureCardAdapterListener;
    
    private final CardViewHolder.OnListItemClickedListener mOnListItemClicked;

    private List<Feature> mFeatures = new ArrayList<>();
    
    private String mHeaderFirst;
    private String mHeaderSecond;

    private boolean isHeader1(int position) {
        return position == 0;
    }

    private boolean isHeader2(int position) {
        return position == 1;
    }
    
    public FeatureCardAdapter() {
        mOnListItemClicked = new CardViewHolder.OnListItemClickedListener() {
            @Override
            public void onListItemClicked(final Feature feature) {
                mFeatureCardAdapterListener.onListItemClicked(feature);
            } 
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER_1) {
            return HeaderFirstViewHolder.newInstance(parent);
        } else if (viewType == ITEM_VIEW_TYPE_HEADER_2) {
            return HeaderSecondViewHolder.newInstance(parent);
        } else {
            return CardViewHolder.newInstance(parent, mOnListItemClicked);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ITEM_VIEW_TYPE_HEADER_1:
                ((HeaderFirstViewHolder) holder).bind(mHeaderFirst);
                break;
            case ITEM_VIEW_TYPE_HEADER_2:
                ((HeaderSecondViewHolder) holder).bind(mHeaderSecond);
                break;
            case ITEM_VIEW_TYPE_ITEM:
                ((CardViewHolder) holder).bind(mFeatures.get(position - HEADER_COUNT));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader1(position)) {
            return ITEM_VIEW_TYPE_HEADER_1;
        } else if (isHeader2(position)) {
            return ITEM_VIEW_TYPE_HEADER_2;
        } else {
            return ITEM_VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mFeatures.size() + HEADER_COUNT;
    }
    
    public void setHeaderFirst(final String headerFirst) {
        mHeaderFirst = headerFirst;
    }

    public void setHeaderSecond(final String headerSecond) {
        mHeaderSecond = headerSecond;
    }

    public void setData(List<Feature> features) {
        mFeatures.clear();
        mFeatures.addAll(features);
        notifyDataSetChanged();
    }

    public void setFeatureCardAdapterListener(final FeatureCardAdapterListener featureCardAdapterListener) {
        mFeatureCardAdapterListener = featureCardAdapterListener;
    }

    public interface FeatureCardAdapterListener {
        void onListItemClicked(final Feature feature);
    }

}


