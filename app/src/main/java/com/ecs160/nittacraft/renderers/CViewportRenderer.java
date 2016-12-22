package com.ecs160.nittacraft.renderers;

import android.graphics.Canvas;
import android.util.Log;

import com.ecs160.nittacraft.CGameDataTypes;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPosition;
import com.ecs160.nittacraft.SRectangle;
import com.ecs160.nittacraft.views.GameView;

import java.util.ArrayList;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atBarracks;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atBlacksmith;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atFarm;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atLumberMill;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atNone;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atScoutTower;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atTownHall;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atWall;

public class CViewportRenderer {

//    protected CMapRenderer DMapRenderer;          //    #include "MapRenderer.h"
//    protected CAssetRenderer DAssetRenderer;      //    #include "AssetRenderer.h"
//    protected CFogRenderer DFogRenderer;          //    #include "FogRenderer.h"
//    protected int DViewportX;
//    protected int DViewportY;
//    protected int DLastViewportWidth;
//    protected int DLastViewportHeight;
//
//
//    public CViewportRenderer(CMapRenderer maprender, CAssetRenderer assetrender, CFogRenderer fogrender) {
//        //Class is created by being passed the three main renders created in main
//        DMapRenderer = maprender;
//        DAssetRenderer = assetrender;
//        DFogRenderer = fogrender;
//
//        //Also tracks what portion of the map is being displayed in the viewport.
//        DViewportX = 0;
//        DViewportY = 0;
//        DLastViewportWidth = maprender.DetailedMapWidth();
//        DLastViewportHeight = maprender.DetailedMapHeight();
//    }
//
//
//    public void InitViewportDimensions(int width, int height) {
//        DLastViewportWidth = width;
//        DLastViewportHeight = height;
//    }
//
//    //Two version of the ViewportX/Y functions, one which simply returns the value
//    //The second takes an int as a parameter and sets the Viewport to it after some checks
//    //Possibly called when the mini-map is clicked?
//    public int ViewportX() {
//        return DViewportX;
//    }
//
//    public int ViewportX(int x) {
//        DViewportX = x;
//        if (DViewportX + DLastViewportWidth >= DMapRenderer.DetailedMapWidth()) {
//            DViewportX = DMapRenderer.DetailedMapWidth() - DLastViewportWidth;
//        }
//        if (0 > DViewportX) {
//            DViewportX = 0;
//        }
//        return DViewportX;
//    }
//
//    public int ViewportY() {
//        return DViewportY;
//    }
//
//    public int ViewportY(int y) {
//        DViewportY = y;
//        if (DViewportY + DLastViewportHeight >= DMapRenderer.DetailedMapHeight()) {
//            DViewportY = DMapRenderer.DetailedMapHeight() - DLastViewportHeight;
//        }
//        if (0 > DViewportY) {
//            DViewportY = 0;
//        }
//        return DViewportY;
//    }
//
//    public int LastViewportWidth() {
//        return DLastViewportWidth;
//    }
//
//    public int LastViewportHeight() {
//        return DLastViewportHeight;
//    }
//
//    public CPosition DetailedPosition(CPosition pos) {
//        return new CPosition(pos.X() + DViewportX, pos.Y() + DViewportY);
//    }
//
//    public void CenterViewport(CPosition pos) {
//        ViewportX(pos.X() - DLastViewportWidth/2);
//        ViewportY(pos.Y() - DLastViewportHeight/2);
//    }
//
//    //Pan functions, checks bounds and restes to 0 if going past.
//    //Unsure what keeps the PanSouth/East functions from running past boundaries.
//    public void PanNorth(int pan) {
//        DViewportY -= pan;
//        if (0 > DViewportY) {
//            DViewportY = 0;
//        }
//    }
//
//    public void PanEast(int pan) {
//        ViewportX(DViewportX + pan);
//    }
//
//    public void PanSouth(int pan) {
//        ViewportY(DViewportY + pan);
//    }
//
//    public void PanWest(int pan) {
//        DViewportX -= pan;
//        if (0 > DViewportX) {
//            DViewportX = 0;
//        }
//    }
//
//    public void DrawViewport(Canvas canvas, ArrayList<CPlayerAsset>
//            selectionMarkerList, Rect rect, CGameDataTypes.EAssetCapabilityType curcapability) {
//
//        CGameDataTypes.EAssetType PlaceType = atNone;
//        CPlayerAsset Builder;
////        gdk_pixmap_get_size(canvas, DLastViewportWidth, DLastViewportHeight);
//
//        if (DViewportX + DLastViewportWidth >= DMapRenderer.DetailedMapWidth()) {
//            DViewportX = DMapRenderer.DetailedMapWidth() - DLastViewportWidth;
//        }
//        if (DViewportY + DLastViewportHeight >= DMapRenderer.DetailedMapHeight()) {
//            DViewportY = DMapRenderer.DetailedMapHeight() - DLastViewportHeight;
//        }
//
////        TempRectangle.DXPosition = DViewportX;
////        TempRectangle.DYPosition = DViewportY;
////        TempRectangle.DWidth = DLastViewportWidth;
////        TempRectangle.DHeight = DLastViewportHeight;
//       // Rect TempRectangle = new Rect(DViewportX, DViewportY, DLastViewportWidth, DLastViewportHeight);
//
//
//        //Switch likely for when the peasant build command is active as a transparent
//        //image of the building being places will need to be rendered
//        switch(curcapability) {
//            case actBuildFarm:
//                PlaceType = atFarm;
//                break;
//            case actBuildTownHall:
//                PlaceType = atTownHall;
//                break;
//            case actBuildBarracks:
//                PlaceType = atBarracks;
//                break;
//            case actBuildLumberMill:
//                PlaceType = atLumberMill;
//                break;
//            case actBuildBlacksmith:
//                PlaceType = atBlacksmith;
//                break;
//            case actBuildScoutTower:
//                PlaceType = atScoutTower;
//                break;
//            default:
//                break;
//        }
//
//
//        //Calls by the various renderer objects to their relevant functions responsible for Drawing
////        DMapRenderer.DrawMap(canvas, typedrawable, TempRectangle, 0);
////        DAssetRenderer.DrawSelections(canvas, TempRectangle, selectionMarkerList, selectrect, atNone != PlaceType);
//        //DAssetRenderer.DrawAssets(canvas, mainView);
//
//        //Unsure of the purpose of two passes with the 0/1 parameter, will review in the MapRenderer class
////        DMapRenderer.DrawMap(canvas, typedrawable, TempRectangle, 1);
////        DAssetRenderer.DrawOverlays(canvas, TempRectangle);
//
////        if (selectionMarkerList.size()) {
////            Builder = selectionMarkerList.front().lock();
////        }
////        DAssetRenderer.DrawPlacement(canvas, TempRectangle, CPosition(selectrect.DXPosition, selectrect.DYPosition), PlaceType, Builder);
////        DFogRenderer.DrawMap(canvas, TempRectangle);
//    }

    protected CMapRenderer DMapRenderer;
    protected CAssetRenderer DAssetRenderer;
    protected CFogRenderer DFogRenderer;
    protected int DViewportX;
    protected int DViewportY;
    protected int DLastViewportWidth;
    protected int DLastViewportHeight;

    public void InitViewportDimensions(int width, int height) {
        DLastViewportWidth = width;
        DLastViewportHeight = height;
    }

    public int ViewportX() {
        return DViewportX;
    }

    public int ViewportY() {
        return DViewportY;
    }

    public int LastViewportWidth() {
        return DLastViewportWidth;
    }

    public int LastViewportHeight() {
        return DLastViewportHeight;
    }

    public CPosition DetailedPosition(CPosition pos) {
        return new CPosition(pos.X() + DViewportX, pos.Y() + DViewportY);
    }

    public CViewportRenderer(CMapRenderer maprender, CAssetRenderer assetrender, CFogRenderer fogrender) {
        DMapRenderer = maprender;
        DAssetRenderer = assetrender;
        DFogRenderer = fogrender;
        DViewportX = 0;
        DViewportY = 0;
        DLastViewportWidth = maprender.DetailedMapWidth();
        DLastViewportHeight = maprender.DetailedMapHeight();
    }

    public int ViewportX(int x) {
        DViewportX = x;
        if (DViewportX + DLastViewportWidth >= DMapRenderer.DetailedMapWidth()) {
            DViewportX = DMapRenderer.DetailedMapWidth() - DLastViewportWidth;
        }
        if (0 > DViewportX) {
            DViewportX = 0;
        }
        return DViewportX;
    }

    public int ViewportY(int y) {
        DViewportY = y;
        if (DViewportY + DLastViewportHeight >= DMapRenderer.DetailedMapHeight()) {
            DViewportY = DMapRenderer.DetailedMapHeight() - DLastViewportHeight;
        }
        if (0 > DViewportY) {
            DViewportY = 0;
        }
        return DViewportY;
    }

    public void CenterViewport(CPosition pos) {
        ViewportX(pos.X() - DLastViewportWidth/2);
        ViewportY(pos.Y() - DLastViewportHeight/2);
    }

    public void PanNorth(int pan) {
        DViewportY -= pan;
        if (0 > DViewportY) {
            DViewportY = 0;
        }
    }

    public void PanEast(int pan) {
        ViewportX(DViewportX + pan);
    }

    public void PanSouth(int pan) {
        ViewportY(DViewportY + pan);
    }

    public void PanWest(int pan) {
        DViewportX -= pan;
        if (0 > DViewportX) {
            DViewportX = 0;
        }
    }

    public void DrawViewport(Canvas drawable, ArrayList<CPlayerAsset> selectionmarkerlist,
                             SRectangle selectrect, CGameDataTypes.EAssetCapabilityType
                                     curcapability) {
        SRectangle TempRectangle = new SRectangle();
        CGameDataTypes.EAssetType PlaceType = atNone;
        CPlayerAsset Builder = new CPlayerAsset();

        DLastViewportHeight = drawable.getHeight();
        DLastViewportWidth = drawable.getWidth();

        if (DViewportX + DLastViewportWidth >= DMapRenderer.DetailedMapWidth()) {
            DViewportX = DMapRenderer.DetailedMapWidth() - DLastViewportWidth;
        }

        if (DViewportY + DLastViewportHeight >= DMapRenderer.DetailedMapHeight()) {
            DViewportY = DMapRenderer.DetailedMapHeight() - DLastViewportHeight;
        }

        TempRectangle.DXPosition = DViewportX;
        TempRectangle.DYPosition = DViewportY;
        TempRectangle.DWidth = DLastViewportWidth;
        TempRectangle.DHeight = DLastViewportHeight;

        if (curcapability != null) {
            switch (curcapability) {
                case actBuildFarm:
                    PlaceType = atFarm;
                    break;
                case actBuildTownHall:
                    PlaceType = atTownHall;
                    break;
                case actBuildBarracks:
                    PlaceType = atBarracks;
                    break;
                case actBuildLumberMill:
                    PlaceType = atLumberMill;
                    break;
                case actBuildBlacksmith:
                    PlaceType = atBlacksmith;
                    break;
                case actBuildScoutTower:
                    PlaceType = atScoutTower;
                    break;
                case actBuildWall:
                    PlaceType = atWall;
                    break;
                default:
                    break;
            }
        }
//        GameView gView = null;
        try {

            TempRectangle.DXPosition =  (int)Math.floor(GameView.getInstance().movX/64)*64;
            TempRectangle.DYPosition = (int)Math.floor(GameView.getInstance().movY/64)*64;
            TempRectangle.DWidth = GameView.getInstance().getWidth();
            TempRectangle.DHeight = GameView.getInstance().getHeight();
            DMapRenderer.DrawMap(drawable, 0, GameView.getInstance());
//            Log.d("map", "success");
            DAssetRenderer.DrawSelections(drawable, GameView.getInstance(), selectionmarkerlist,
                    selectrect, atNone != PlaceType);
//            Log.d("asset", "success");
            DAssetRenderer.DrawAssets(drawable, GameView.getInstance());
//            Log.d("asset2", "success");
            DMapRenderer.DrawMap(drawable, 1, GameView.getInstance());
//            Log.d("map2", "success");
            DFogRenderer.DrawMap(drawable, TempRectangle);

        } catch (Exception e) {
            Log.e("ViewportRenderer", e.getMessage());
        }

            //FIXME Should be reading info from ViewPort instead of gameview eventually

//            DMapRenderer.DrawMap(drawable, 0, gView);
//            DAssetRenderer.DrawSelections(drawable, gView, selectionmarkerlist, selectrect, atNone != PlaceType);
//            DAssetRenderer.DrawAssets(drawable, gView);
//            DMapRenderer.DrawMap(drawable, 1, gView);


//        DAssetRenderer.DrawOverlays(drawable, TempRectangle);

//        if (selectionmarkerlist.size() > 0) {
//            Builder = selectionmarkerlist.get(0);
//        }
//        DAssetRenderer.DrawPlacement(drawable, TempRectangle, new CPosition(selectrect.DXPosition, selectrect.DYPosition), PlaceType, Builder);
    }
}
