package com.meta.portal.sdk.app.fbns;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.data.FbnsData;

public class CardViewHolderFbnsTv extends RecyclerView.ViewHolder {
    private final CardView mCardView;
    private final TextView mFeatureTitle;
    private final ImageView mFinished;

    private FbnsData mFeature;

    private final OnListItemClickedListener mOnListItemClickedListener;

    private final FrameLayout mBorderView;

    public CardViewHolderFbnsTv(final View view, final OnListItemClickedListener onListItemClickedListener) {
        super(view);
        mOnListItemClickedListener = onListItemClickedListener;
        mCardView = (CardView) view.findViewById(R.id.card_view);
        mFeatureTitle = (TextView) view.findViewById(R.id.feature_title);
        mFinished = (ImageView) view.findViewById(R.id.checkbox);

        mBorderView = (FrameLayout) view.findViewById(R.id.card_border);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnListItemClickedListener.onListItemClicked(mFeature);
            }
        });

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // run scale animation and make it bigger
                    Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_in_tv);
                    view.startAnimation(anim);
                    anim.setFillAfter(true);

                    mBorderView.setAlpha(1.0f);
                    Animation animBorder = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in);
                    mBorderView.startAnimation(animBorder);
                    animBorder.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mBorderView.setAlpha(1.0f);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                    });
                    animBorder.setFillAfter(true);
                } else {
                    // run scale animation and make it smaller
                    Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_out_tv);
                    view.startAnimation(anim);
                    anim.setFillAfter(true);

                    Animation animBorder = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_out);
                    mBorderView.startAnimation(animBorder);
                    animBorder.setFillAfter(true);
                }
            }
        });
    }

    public void bind(FbnsData fbnsData) {
        mFeature = fbnsData;
        mFeatureTitle.setText(fbnsData.getCardTitle());
        mFinished.setBackgroundResource(getBackgroundResource(fbnsData.getFinished()));
        mBorderView.setAlpha(0.0f);
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

    public static CardViewHolderFbnsTv newInstance(
            final ViewGroup parent,
            final OnListItemClickedListener onListItemClickedListener) {
        return new CardViewHolderFbnsTv(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_fbns_tv, parent, false),
                onListItemClickedListener);
    }
}
