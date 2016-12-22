package com.ecs160.nittacraft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ecs160.nittacraft.R;

public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void SinglePlayerGame(View view) {
        MainData.SinglePlayerGameButtonCallback();
        Intent intent = new Intent(this, SelectMapActivity.class);
        startActivity(intent);
    }

    public void MultiPlayerGame(View view) {
        MainData.MultiPlayerGameButtonCallback();
        Intent intent = new Intent(this, MultiplayerActivity.class);
        startActivity(intent);
    }

    public void showOptions(View view) {
        MainData.OptionsButtonCallback();
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    public void exitGame(View view) {
        MainData.ExitGameButtonCallback();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}