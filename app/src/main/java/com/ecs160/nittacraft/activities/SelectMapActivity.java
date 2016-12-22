package com.ecs160.nittacraft.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ecs160.nittacraft.R;
import com.ecs160.nittacraft.maps.CAssetDecoratedMap;
import com.ecs160.nittacraft.renderers.CMiniMapRenderer;

import java.util.ArrayList;

public class SelectMapActivity extends BaseActivity {
    private ListView DListView;
    private View DPreviousSelection;
    private boolean DSurfaceReady;
    private SurfaceView DSurface;

    private CMiniMapRenderer DMiniMapRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_map);

        // MiniMap Renderer setup
        DMiniMapRenderer = MainData.DMiniMapRenderer();
        DSurface = (SurfaceView)findViewById(R.id.map_preview);

        // ListView initialization
        DListView = (ListView)findViewById(R.id.map_list);
        PrepareMiniMap();
        InitializeMapList();
        InitializeListListener();
    }

    /**
     * Initializes the ListView with a list of all maps.
     */
    public void InitializeMapList() {
        ArrayList<String> Maps = new ArrayList<>();
        for (int Index = 0; Index < CAssetDecoratedMap.MapCount(); ++Index) {
            Maps.add(CAssetDecoratedMap.GetMap(Index).DMapName);
        }

        ArrayAdapter<String> DArrayAdapter = new ArrayAdapter<>(this, R.layout.map_list_item, R.id.map, Maps);
        DListView.setAdapter(DArrayAdapter);
    }

    /**
     * Initializes the listener for when a new map is selected in the list.
     */
    public void InitializeListListener() {
        DListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Set the previous option's background to transparent
                // (so it looks like it was unselected)
                if (DPreviousSelection != null) {
                    DPreviousSelection.setBackgroundColor(0);
                }
                DPreviousSelection = view;
                view.setBackgroundColor(Color.parseColor("#CCFFA500"));

                // Change the selected map
                String Map = (String)parent.getItemAtPosition(position);
                MainData.ProcessInputMapSelectMode(Map);

                DrawMiniMap();
                DrawMapInformation();
            }
        });
    }

    /**
     * Updates a boolean if the SurfaceView's Canvas can be grabbed yet, based on
     * when it finishes loading.
     */
    public void PrepareMiniMap() {
        DSurface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                DSurfaceReady = true;
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                DSurfaceReady = false;
            }
        });
    }


    public void DrawMiniMap() {
        DMiniMapRenderer = MainData.DMiniMapRenderer();
        if (DSurfaceReady) {
            DSurface.invalidate();
            Canvas DCanvas = DSurface.getHolder().lockCanvas();

            DCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            DMiniMapRenderer.DrawMiniMap(DCanvas);
            DSurface.getHolder().unlockCanvasAndPost(DCanvas);
        }
    }

    /**
     * Edits the layout text views with the player count and map size
     */
    public void DrawMapInformation() {
        // Update player count
        TextView PlayerCount = (TextView)findViewById(R.id.map_player_count);
        PlayerCount.setText(Integer.toString(MainData.DSelectedMap().PlayerCount()) + " Players");

        // Update map size
        TextView MapSize = (TextView)findViewById(R.id.map_size);
        MapSize.setText(Integer.toString(MainData.DSelectedMap().Width()) + "x" + Integer
                .toString(MainData.DSelectedMap().Height()));
    }

    public void SelectMap(View view) throws InterruptedException {
        MainData.SelectMapButtonCallback();
        Intent intent = new Intent(this, GameSettingsActivity.class);
        startActivity(intent);
    }
}