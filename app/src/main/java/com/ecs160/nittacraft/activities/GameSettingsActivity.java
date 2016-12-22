package com.ecs160.nittacraft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ecs160.nittacraft.R;

public class GameSettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);
    }

    public void PlayGame(View view) {
//        MainData.ReadyButtonCallback();
        MainData.PlayGameButtonCallback();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}