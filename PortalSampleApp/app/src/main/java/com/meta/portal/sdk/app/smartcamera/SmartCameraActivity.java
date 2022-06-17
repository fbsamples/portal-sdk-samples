package com.meta.portal.sdk.app.smartcamera;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.Utils;
import com.meta.portal.sdk.app.base.BaseActivity;
import com.meta.portal.sdk.app.base.FeatureBaseActivity;
import com.meta.portal.sdk.app.data.Feature;
import com.meta.portal.sdk.app.data.FeatureParser;
import com.meta.portal.sdk.app.smartcameraEditor.SmartCameraEditorActivity;
import com.meta.portal.sdk.app.ui.FeatureCardAdapter;
import com.meta.portal.sdk.app.ui.FeatureCardAdapterListener;
import com.meta.portal.sdk.app.ui.FeatureCardAdapterTv;

import java.util.ArrayList;
import java.util.List;

public class SmartCameraActivity extends BaseActivity {
    
    Button mWithUIButton;
    Button mWithoutUIButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_smart_camera);

        mWithUIButton = (Button) findViewById(R.id.with_ui_button);
        mWithoutUIButton = (Button) findViewById(R.id.without_ui_button);

        mWithUIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SmartCameraActivity.this, SmartCameraEditorActivity.class);
                startActivity(intent);
            }
        });

        mWithoutUIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SmartCameraActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

    }

}
