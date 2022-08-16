package com.meta.portal.sdk.app.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.data.Feature;

public class CardViewHolderTv extends RecyclerView.ViewHolder {
    private final CardView mCardView;
    private final TextView mFeatureName;
    private final TextView mClassNameTitle;

    private final FrameLayout mBorderView;
    private final FrameLayout mFeatureIcon;

    private Feature mFeature;

    private final OnListItemClickedListener mOnListItemClickedListener;

  public CardViewHolderTv(
      final View view, final OnListItemClickedListener onListItemClickedListener) {
    super(view);
    mOnListItemClickedListener = onListItemClickedListener;
    mCardView = (CardView) view.findViewById(R.id.card_view_tv);
    mFeatureName = (TextView) view.findViewById(R.id.feature_name);
    mClassNameTitle = (TextView) view.findViewById(R.id.class_name_title);

    mBorderView = (FrameLayout) view.findViewById(R.id.card_border);
    mFeatureIcon = (FrameLayout) view.findViewById(R.id.feature_icon);

    view.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            mOnListItemClickedListener.onListItemClicked(mFeature);
          }
        });

    view.setOnFocusChangeListener(
        new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
              // run scale animation and make it bigger
              Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_in_tv);
              view.startAnimation(anim);
              anim.setFillAfter(true);

              mBorderView.setAlpha(1.0f);
              Animation animBorder =
                  AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in);
              mBorderView.startAnimation(animBorder);
              animBorder.setAnimationListener(
                  new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                      mBorderView.setAlpha(1.0f);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                  });
              animBorder.setFillAfter(true);
            } else {
              // run scale animation and make it smaller
              Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_out_tv);
              view.startAnimation(anim);
              anim.setFillAfter(true);

              Animation animBorder =
                  AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_out);
              mBorderView.startAnimation(animBorder);
              animBorder.setFillAfter(true);
            }
          }
        });
  }

  public void bind(Feature feature) {
    mFeature = feature;
    mCardView.setCardBackgroundColor(feature.getBackgroundResourceColor());
    mFeatureIcon.setBackgroundResource(feature.getBackgroundResourceId());
    mFeatureName.setText(feature.getScreenName());
    mClassNameTitle.setText(feature.getClassNameTitle());
  }

  public interface OnListItemClickedListener {
    void onListItemClicked(final Feature feature);
  }

  public static CardViewHolderTv newInstance(
      final ViewGroup parent, final OnListItemClickedListener onListItemClickedListener) {
    return new CardViewHolderTv(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_tv, parent, false),
        onListItemClickedListener);
  }
}
