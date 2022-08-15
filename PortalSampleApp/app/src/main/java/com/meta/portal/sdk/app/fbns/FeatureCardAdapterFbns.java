package com.meta.portal.sdk.app.fbns;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.data.FbnsData;

import java.util.ArrayList;
import java.util.List;

public class FeatureCardAdapterFbns extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW_TYPE_HEADER_1 = 0;
    private static final int ITEM_VIEW_TYPE_HEADER_2 = 1;
    private static final int ITEM_VIEW_TYPE_ITEM = 2;
    private static final int ITEM_VIEW_TYPE_ITEM_REGISTER = 3;
    private static final int ITEM_VIEW_TYPE_ITEM_SEND = 4;

    private static final int HEADER_COUNT = 2;
    
    private FbnsDataCardAdapterListener mFbnsDataCardAdapterListener;
    
    private InfoButtonClickedListener mInfoButtonClickedListener;
    
    private final ListViewHolderFbns.OnListItemClickedListener mOnListItemClicked;
    
    private final ListViewHolderFbns.OnInfoButtonClickedListener mOnInfoButtonClickedListener;

    private List<FbnsData> mFbnsData = new ArrayList<>();
    private FbnsHelper mFbnsHelper;

    private String mHeaderFirst;
    private String mHeaderSecond;

    private boolean isHeader1(int position) {
        return position == 0;
    }

    private boolean isHeader2(int position) {
        return position == 1;
    }

    private boolean isRegisterListItem(int position) {
        return position == 4;
    }

    private boolean isSendListItem(int position) {
        return position == 5;
    }
    
    public FeatureCardAdapterFbns() {
        mOnListItemClicked = new ListViewHolderFbns.OnListItemClickedListener() {
            @Override
            public void onListItemClicked(final FbnsData feature) {
            } 
        };

        mOnInfoButtonClickedListener = new ListViewHolderFbns.OnInfoButtonClickedListener() {
            @Override
            public void onInfoButtonClicked(final FbnsData fbnsData) {
                System.out.println("ReferenceApp FeatureCardAdapterFbns onInfoButtonClicked");
                mInfoButtonClickedListener.onInfoButtonClicked(fbnsData);
            }
        };
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER_1) {
            return FbnsHeaderFirstViewHolder.newInstance(parent);
        } else if (viewType == ITEM_VIEW_TYPE_HEADER_2) {
            return FbnsHeaderSecondViewHolder.newInstance(parent);
        } else if (viewType == ITEM_VIEW_TYPE_ITEM_REGISTER) {
            return ListViewHolderRegisterFbns.newInstance(parent, mOnInfoButtonClickedListener);
        } else if (viewType == ITEM_VIEW_TYPE_ITEM_SEND) {
            return ListViewHolderSendFbns.newInstance(parent, mOnInfoButtonClickedListener);
        } else {
            return ListViewHolderFbns.newInstance(parent, mOnListItemClicked, mOnInfoButtonClickedListener);
        }
    }
        

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ITEM_VIEW_TYPE_HEADER_1:
                ((FbnsHeaderFirstViewHolder) holder).bind(mHeaderFirst);
                break;
            case ITEM_VIEW_TYPE_HEADER_2:
                ((FbnsHeaderSecondViewHolder) holder).bind(mHeaderSecond);
                break;
            case ITEM_VIEW_TYPE_ITEM:
                ((ListViewHolderFbns) holder).bind(mFbnsData.get(position - HEADER_COUNT));
                break;
            case ITEM_VIEW_TYPE_ITEM_REGISTER:
                ((ListViewHolderRegisterFbns) holder).bind(mFbnsData.get(position - HEADER_COUNT), mFbnsHelper);
                break;
            case ITEM_VIEW_TYPE_ITEM_SEND:
                ((ListViewHolderSendFbns) holder).bind(mFbnsData.get(position - HEADER_COUNT), mFbnsHelper);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader1(position)) {
            return ITEM_VIEW_TYPE_HEADER_1;
        } else if (isHeader2(position)) {
            return ITEM_VIEW_TYPE_HEADER_2;
        } else if (isRegisterListItem(position)) {
            return ITEM_VIEW_TYPE_ITEM_REGISTER;
        } else if (isSendListItem(position)) {
            return ITEM_VIEW_TYPE_ITEM_SEND;
        } else {
            return ITEM_VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mFbnsData.size() + HEADER_COUNT;
    }

    public void setHeaderFirst(final String headerFirst) {
        mHeaderFirst = headerFirst;
    }

    public void setHeaderSecond(final String headerSecond) {
        mHeaderSecond = headerSecond;
    }

    public void setData(List<FbnsData> features) {
        mFbnsData.clear();
        mFbnsData.addAll(features);
        notifyDataSetChanged();
    }

    public void setFbnsHelper(FbnsHelper fbnsHelper) {
        mFbnsHelper = fbnsHelper;
    }

    public void setFbnsDataAdapterListener(final FbnsDataCardAdapterListener featureCardAdapterListener) {
        mFbnsDataCardAdapterListener = featureCardAdapterListener;
    }

    public void setInfoButtonClickedListener(final InfoButtonClickedListener infoButtonClickedListener) {
        mInfoButtonClickedListener = infoButtonClickedListener;
    }

}


