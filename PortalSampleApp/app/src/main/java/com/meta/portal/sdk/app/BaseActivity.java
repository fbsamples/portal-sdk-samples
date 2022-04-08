package com.meta.portal.sdk.app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    
    @Override
    protected void onResume() {
        super.onResume();
        updateSystemUiVisibility();
    }
    
    private void updateSystemUiVisibility() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | 
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    
}
