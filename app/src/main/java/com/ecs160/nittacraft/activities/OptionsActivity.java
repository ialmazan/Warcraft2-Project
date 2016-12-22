package com.ecs160.nittacraft.activities;

import android.os.Bundle;
import android.view.View;

import com.ecs160.nittacraft.CPosition;
import com.ecs160.nittacraft.R;

public class OptionsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }

    public void ChangeZoom(View view) {
        MainData.DTileScaleFactor++;
        if (MainData.DTileScaleFactor > 6) {MainData.DTileScaleFactor = 1;}
        ((android.widget.Button)findViewById(R.id.zoomButton)).setText("Zoom: " + MainData.DTileScaleFactor);
        CPosition.SetTileDimensions(MainData.DTerrainTileset.TileWidth(), MainData.DTerrainTileset.TileHeight());
    }
}