package com.ecs160.nittacraft.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ecs160.nittacraft.R;
import com.ecs160.nittacraft.fragments.AssetDescriptionFragment;
import com.ecs160.nittacraft.views.GameView;
import com.ecs160.nittacraft.views.SideBarView;

public class MainActivity extends BaseActivity implements AssetDescriptionFragment.IAssetDescription {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        LinearLayout map = (LinearLayout)findViewById(R.id.gameView);
        FrameLayout sidebar = (FrameLayout)findViewById(R.id.fl_minimap);
        MainData.InitializeViews(map, sidebar, this);
        map.addView(GameView.getInstance(this));
        sidebar.addView(SideBarView.getInstance(this));
    }

    @Override
    public void setAssetDescription(Bitmap icon, String name, int currHP, int maxHP) {

    }

    @Override
    public void updateHP(int currHP) {

    }

}