package com.meta.portal.sdk.app.fbns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.data.FbnsData;

public class CardViewHolderFbns extends RecyclerView.ViewHolder {
    private final CardView mCardView;
    private final TextView mFeatureTitle;
    private final ImageView mFinished;
    
    private FbnsData mFeature;
    
    private final OnListItemClickedListener mOnListItemClickedListener;

    public CardViewHolderFbns(final View view, final OnListItemClickedListener onListItemClickedListener) {
        super(view);
        mOnListItemClickedListener = onListItemClickedListener;
        mCardView = (CardView) view.findViewById(R.id.card_view);
        mFeatureTitle = (TextView) view.findViewById(R.id.feature_title);
        mFinished = (ImageView) view.findViewById(R.id.checkbox);
        
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnListItemClickedListener.onListItemClicked(mFeature);
            }
        });
    }
    
    public void bind(FbnsData fbnsData) {
        mFeature = fbnsData;
        mFeatureTitle.setText(fbnsData.getCardTitle());
        mFinished.setBackgroundResource(getBackgroundResource(fbnsData.getFinished()));
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
    
    public static CardViewHolderFbns newInstance(
            final ViewGroup parent, 
            final OnListItemClickedListener onListItemClickedListener) { 
        return new CardViewHolderFbns(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_fbns, parent, false),
                onListItemClickedListener);
    }
}
