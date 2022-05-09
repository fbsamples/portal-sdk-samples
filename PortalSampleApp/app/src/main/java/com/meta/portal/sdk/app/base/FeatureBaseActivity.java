package com.meta.portal.sdk.app.base;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.meta.portal.sdk.app.ui.FeatureInfoAnimationController;
import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.ui.TopAppBarAnimationController;

public abstract class FeatureBaseActivity extends BaseActivity {

    private static final int TOP_APP_BAR_FADE_OUT_DELAY = 3000;
    
    FrameLayout mFeatureInfoContainerBackground;
    RelativeLayout mFeatureInfoContainer;
    TextView mFeatureInfoHeader;
    TextView mFeatureInfoText;
    Button mFeatureInfoCloseButton;
    Toolbar mTopAppBar;
    FrameLayout mTopAppBarBackground;

    private FeatureInfoAnimationController mFeatureInfoAnimationController;
    
    private TopAppBarAnimationController mTopAppBarAnimationController;

    private final Handler mHandler = new Handler();

    private final Runnable mTopAppBarFadeOutRunnable = new Runnable() {
        @Override
        public void run() {
            mTopAppBarAnimationController.startTopAppBarViewOutAnimation();
        }
    };

    private boolean mFeatureInfoShowing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature);
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
                mFeatureInfoShowing = false;
                startTopAppBarFadeOutAnimation();
            }
        });

        mFeatureInfoCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFeatureInfoAnimationController.startFeatureInfoViewOutAnimation();
                mFeatureInfoShowing = false;
                startTopAppBarFadeOutAnimation();
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

        mTopAppBar = (Toolbar) findViewById(R.id.top_app_bar);
        setSupportActionBar(mTopAppBar);

        mTopAppBarBackground = (FrameLayout) findViewById(R.id.top_app_bar_container);

        mTopAppBarBackground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mTopAppBarAnimationController.startTopAppBarViewInAnimation(
                        new TopAppBarAnimationController.AnimationFinishedCallback() {
                            @Override
                            public void animationFinished() {
                                FeatureBaseActivity.this.startTopAppBarFadeOutAnimation();
                            }
                        }
                );
                return false;
            }
        });

        mTopAppBarAnimationController = new TopAppBarAnimationController(mTopAppBar);

        ImageButton infoButton = (ImageButton) findViewById(R.id.info_button);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFeatureInfoAnimationController.startFeatureInfoViewInAnimation();
            }
        });

        TextView featureName = (TextView) findViewById(R.id.feature_name);
        featureName.setText(getFeatureInfoHeaderResId());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, getFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mFeatureInfoShowing) {
            startTopAppBarFadeOutAnimation();
        }
    }

    private void startTopAppBarFadeOutAnimation() {
        mHandler.removeCallbacks(mTopAppBarFadeOutRunnable);
        mHandler.postDelayed(mTopAppBarFadeOutRunnable, TOP_APP_BAR_FADE_OUT_DELAY);
    }

    @Override
    public void updateSystemUiVisibility() {
        return;
    }
    
    protected abstract Fragment getFragment();
    
    protected abstract @StringRes int getFeatureInfoHeaderResId();

    protected abstract @StringRes int getFeatureInfoTextResId();

}
