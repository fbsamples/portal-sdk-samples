package com.meta.portal.sdk.app.fbns.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;
import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.fbns.FbnsData;

public class ListViewHolderFbns extends RecyclerView.ViewHolder {
  private final TextView mFeatureNumber;
  private final TextView mFeatureTitle;
  private final ImageButton mInfoButton;

  private FbnsData mFeature;

  private final FbnsUiListener mFbnsUiListener;

  public ListViewHolderFbns(final View view, final FbnsUiListener fbnsUiListener) {
    super(view);
    mFbnsUiListener = fbnsUiListener;
    mFeatureNumber = (TextView) view.findViewById(R.id.feature_number);
    mFeatureTitle = (TextView) view.findViewById(R.id.feature_title);
    mInfoButton = (ImageButton) view.findViewById(R.id.info_button);

    mInfoButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            System.out.println("ReferenceApp ListViewHolderFbns onInfoButtonClicked");
            mFbnsUiListener.onInfoButtonClicked(mFeature);
          }
        });
  }

  public void bind(FbnsData fbnsData) {
    mFeature = fbnsData;
    mFeatureNumber.setText(String.valueOf(fbnsData.getStepIndex()));
    mFeatureTitle.setText(fbnsData.getCardTitle());
  }

  private @DrawableRes int getBackgroundResource(boolean finished) {
    if (finished) {
      return R.drawable.ic_checked;
    } else {
      return R.drawable.ic_unchecked;
    }
  }

  public static ListViewHolderFbns newInstance(
      final ViewGroup parent, final FbnsUiListener fbnsUiListener) {
    return new ListViewHolderFbns(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_fbns, parent, false),
        fbnsUiListener);
  }
}
