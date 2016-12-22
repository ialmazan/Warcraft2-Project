package com.ecs160.nittacraft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ecs160.nittacraft.R;

public class MultiplayerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
    }

    public void displayHostOptions(View view) {
        MainData.OnlineMultiPlayerButtonCallback();
        Intent intent = new Intent(this, MultiplayerHostActivity.class);
        startActivity(intent);
    }
}