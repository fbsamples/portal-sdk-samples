package com.meta.portal.sdk.app.calling;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.meta.portal.sdk.app.base.BaseActivity;
import com.meta.portal.sdk.app.R;
import com.meta.portal.sdk.app.smartcamera.SmartCameraActivity;
import com.meta.portal.sdk.app.smartcameraEditor.SmartCameraEditorActivity;

public class CallingActivity extends BaseActivity {

    Button mOutgoingCallButton;
    Button mIncomingCallButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calling);

        mOutgoingCallButton = (Button) findViewById(R.id.outgoing_call_button);
        mIncomingCallButton = (Button) findViewById(R.id.incoming_call_button);

        mOutgoingCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CallingActivity.this, OutgoingCallActivity.class);
                startActivity(intent);
            }
        });

        mIncomingCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

}
