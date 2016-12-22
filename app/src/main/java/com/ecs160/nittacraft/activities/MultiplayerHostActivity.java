package com.ecs160.nittacraft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ecs160.nittacraft.R;

public class MultiplayerHostActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_host);
    }

    public void JoinMultiPlayer(View view) throws InterruptedException {
        MainData.JoinMultiPlayerButtonCallback();
        Thread.sleep(1000);
        MainData.ReadyButtonCallback();
        Intent intent = new Intent(this, SelectMapActivity.class);
        startActivity(intent);
    }

    public void HostMultiPlayer(View view) {
        MainData.HostMultiPlayerButtonCallback();
        MainData.ChangeTypeButtonCallback();
        Intent intent = new Intent(this, SelectMapActivity.class);
        startActivity(intent);
    }
}