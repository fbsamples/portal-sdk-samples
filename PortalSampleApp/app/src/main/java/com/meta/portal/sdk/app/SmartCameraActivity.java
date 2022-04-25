package com.meta.portal.sdk.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

//public class SmartCameraActivity extends FeatureBaseActivity {
public class SmartCameraActivity extends FeatureBaseActivity implements CameraEditorLauncher {

    Button mLaunchSmartCameraEditorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new CameraFragment())
                    .commit();
        }
//        mLaunchSmartCameraEditorButton = (Button) findViewById(R.id.launch_smart_camera_button);
//
//        mLaunchSmartCameraEditorButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
                Intent intent =
                        new Intent(
                                "com.facebook.portal.action.LAUNCH_SMART_CAMERA_EDITOR");
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(intent);
//            }
//        });
//        mLaunchSmartCameraEditorButton.setVisibility(View.GONE);
    }

    @Override
    public void onLaunchClicked() {
//        SmartCameraEditor.launchSmartCameraEditor(getBaseContext());
        Intent intent =
                new Intent(
                        "com.facebook.portal.action.LAUNCH_SMART_CAMERA_EDITOR");
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLaunchSmartCameraEditorButton.setVisibility(View.VISIBLE);
    }
    
        protected @LayoutRes
    int getFeatureLayoutResId() {
        return R.layout.activity_smartcamera;
    }

    protected @StringRes
    int getFeatureInfoHeaderResId() {
        return R.string.smart_camera_feature_info_header;
    }

    protected @StringRes int getFeatureInfoTextResId() {
        return R.string.smart_camera_feature_info_text;
    }

}

//public class CameraActivity extends Activity implements CameraEditorLauncher {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
//        Log.i("CameraActivity", "onCreate: " + getIntent().toString());
//        if (null == savedInstanceState) {
//            getFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.container, new CameraFragment())
//                    .commit();
//        }
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        Log.w("CameraActivity", "onNewIntent: " + intent.toString());
//    }
//
//    @Override
//    public void onLaunchClicked() {
//        SmartCameraEditor.launchSmartCameraEditor(getBaseContext());
//    }
//}
