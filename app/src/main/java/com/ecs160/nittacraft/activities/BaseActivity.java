package com.ecs160.nittacraft.activities;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ecs160.nittacraft.CApplicationData;

/**
 * Base activity for all activities. Implements the basic "hide system UI" functionality,
 * and sets volume control to "media" using the hardware controls.
 */
public class BaseActivity extends AppCompatActivity {
    private View decorView;
    public static CApplicationData MainData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        decorView = getWindow().getDecorView();
        HideSystemUI();
    }

    private void HideSystemUI() {
        // Sets the IMMERSIVE flag.
        // Also has the content appear under the bars, so no resizing is done
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * Rehides the system navigation bars
     * @param hasFocus Whether the window has focus, or not
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            HideSystemUI();
        }
    }

    public void finishActivity(View view) {
        finish();
    }

    public void gameOver(boolean win) {
        Intent intent;
        if (win) {
            intent = new Intent(this, WinSplashActivity.class);
        } else {
            intent = new Intent(this, LossSplashActivity.class);
        }
        startActivity(intent);
    }
}