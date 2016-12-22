package com.ecs160.nittacraft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ecs160.nittacraft.CApplicationData;
import com.ecs160.nittacraft.R;

public class SplashActivity extends BaseActivity {
    protected Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set splash screen with a small timer
        // Otherwise, activity ends before the splash screen image can appear
        // resulting in ugly empty activity screen briefly appearing
        setContentView(R.layout.activity_splash);
        int SPLASH_DISPLAY_LENGTH = 500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Initialize CApplicationData
                MainData = new CApplicationData();
                MainData.Init(SplashActivity.this);

                // Switch to MenuActivity once the data is loaded
                mainIntent = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}