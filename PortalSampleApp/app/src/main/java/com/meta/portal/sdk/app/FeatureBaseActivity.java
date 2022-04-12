package com.meta.portal.sdk.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

public abstract class FeatureBaseActivity extends BaseActivity {
    
    FrameLayout mFeatureContainer;
    FrameLayout mFeatureInfoContainerBackground;
    RelativeLayout mFeatureInfoContainer;
    TextView mFeatureInfoHeader;
    TextView mFeatureInfoText;
    Button mFeatureInfoCloseButton;

    private FeatureInfoAnimationController mFeatureInfoAnimationController;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature);
        mFeatureContainer = (FrameLayout) findViewById(R.id.feature_container);
        View activityView = LayoutInflater.from(this).inflate(getFeatureLayoutResId(), null, false);
        mFeatureContainer.addView(activityView);
        mFeatureInfoContainerBackground = (FrameLayout) findViewById(R.id.feature_container_background);
        mFeatureInfoCloseButton = (Button) findViewById(R.id.feature_info_close_button);
        mFeatureInfoContainer = (RelativeLayout) findViewById(R.id.feature_info_container);
        mFeatureInfoHeader = (TextView) findViewById(R.id.feature_info_header);
        mFeatureInfoText = (TextView) findViewById(R.id.feature_info_text);

        mFeatureInfoHeader.setText(getFeatureInfoHeaderResId());
        mFeatureInfoText.setText(getFeatureInfoTextResId());

        mFeatureInfoContainerBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFeatureInfoAnimationController.startFeatureInfoViewOutAnimation();
            }
        });

        mFeatureInfoCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFeatureInfoAnimationController.startFeatureInfoViewOutAnimation();
            }
        });

        mFeatureInfoAnimationController = new FeatureInfoAnimationController(mFeatureInfoContainer, 
                mFeatureInfoContainerBackground);

        mFeatureInfoContainer.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mFeatureInfoContainer.getViewTreeObserver().
                                removeOnGlobalLayoutListener(this);
                        mFeatureInfoAnimationController.startFeatureInfoViewInAnimation();
                    }
                });

    }
    
    protected abstract @LayoutRes int getFeatureLayoutResId();
    
    protected abstract @StringRes int getFeatureInfoHeaderResId();

    protected abstract @StringRes int getFeatureInfoTextResId();

}
