package com.ecs160.nittacraft;

import android.content.Context;
import android.graphics.Canvas;

import com.ecs160.nittacraft.maps.CGraphicRecolorMap;

public class CGraphicMulticolorTileset extends CGraphicTileset {

    CGraphicMulticolorTileset(Context context) {
        DContext = context;
    }

    public boolean LoadTileset(CGraphicRecolorMap DRecolorMap, CDataSource source) {
        boolean returnStatus = super.LoadTileset(source);
        return returnStatus;
    }

    public void DrawTile(Canvas canvas, int xPos, int yPos, int tileIndex, int colorIndex) {
        super.DrawTile(canvas, xPos, yPos, tileIndex);
    }
}