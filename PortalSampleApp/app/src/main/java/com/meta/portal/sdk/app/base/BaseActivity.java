package com.meta.portal.sdk.app.base;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

  @Override
  protected void onResume() {
    super.onResume();
    updateSystemUiVisibility();
  }

  public void updateSystemUiVisibility() {
    return;
  }
}
