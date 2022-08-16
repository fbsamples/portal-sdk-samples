package com.meta.portal.sdk.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.data.Feature;

public class CardViewHolder extends RecyclerView.ViewHolder {
  private final CardView mCardView;
  private final TextView mFeatureName;
  private final TextView mClassNameTitle;

  private Feature mFeature;

  private final OnListItemClickedListener mOnListItemClickedListener;

  public CardViewHolder(
      final View view, final OnListItemClickedListener onListItemClickedListener) {
    super(view);
    mOnListItemClickedListener = onListItemClickedListener;
    mCardView = (CardView) view.findViewById(R.id.card_view);
    mFeatureName = (TextView) view.findViewById(R.id.feature_name);
    mClassNameTitle = (TextView) view.findViewById(R.id.class_name_title);

    view.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            mOnListItemClickedListener.onListItemClicked(mFeature);
          }
        });
  }

  public void bind(Feature feature) {
    mFeature = feature;
    mCardView.setBackgroundResource(feature.getBackgroundResourceId());
    mFeatureName.setText(feature.getScreenName());
    mClassNameTitle.setText(feature.getClassNameTitle());
  }

  public interface OnListItemClickedListener {
    void onListItemClicked(final Feature feature);
  }

  public static CardViewHolder newInstance(
      final ViewGroup parent, final OnListItemClickedListener onListItemClickedListener) {
    return new CardViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false),
        onListItemClickedListener);
  }
}
