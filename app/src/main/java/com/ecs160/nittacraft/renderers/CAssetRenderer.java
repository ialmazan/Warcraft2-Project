package com.ecs160.nittacraft.renderers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ecs160.nittacraft.CGameDataTypes;
import com.ecs160.nittacraft.CGameDataTypes.EAssetType;
import com.ecs160.nittacraft.CGameDataTypes.EPlayerColor;
import com.ecs160.nittacraft.CGraphicMulticolorTileset;
import com.ecs160.nittacraft.CGraphicTileset;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerAssetType;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.CPosition;
import com.ecs160.nittacraft.SAssetCommand;
import com.ecs160.nittacraft.SRectangle;
import com.ecs160.nittacraft.maps.CAssetDecoratedMap;
import com.ecs160.nittacraft.maps.CTerrainMap;
import com.ecs160.nittacraft.views.GameView;

import java.util.ArrayList;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaAttack;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaConstruct;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaDeath;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actPatrol;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actStandGround;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGold;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGoldMine;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atLumber;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atNone;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dMax;
import static com.ecs160.nittacraft.CGameDataTypes.EPlayerColor.pcMax;
import static com.ecs160.nittacraft.CGameDataTypes.EPlayerColor.pcNone;

public class CAssetRenderer {

    static protected final int TARGET_FREQUENCY = 10;

    protected CPlayerData DPlayerData;
    protected CAssetDecoratedMap DPlayerMap;
    protected ArrayList<CGraphicMulticolorTileset> DTilesets = new ArrayList<>();
    protected CGraphicTileset DMarkerTileset;
    protected ArrayList<CGraphicTileset> DFireTilesets = new ArrayList<>();
    protected CGraphicTileset DBuildingDeathTileset;
    protected CGraphicTileset DCorpseTileset;
    protected CGraphicTileset DArrowTileset;
    protected ArrayList<Integer> DMarkerIndices = new ArrayList<>();
    protected ArrayList<Integer> DCorpseIndices = new ArrayList<>();
    protected ArrayList<Integer> DArrowIndices = new ArrayList<>();
    protected int DPlaceGoodIndex;
    protected int DPlaceBadIndex;
    protected ArrayList<ArrayList<Integer>> DNoneIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DConstructIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DBuildIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DWalkIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DAttackIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DCarryGoldIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DCarryLumberIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DDeathIndices = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DPlaceIndices = new ArrayList<>();

    protected ArrayList< Integer > DPixelIndices = new ArrayList<>();
    protected static int DAnimationDownSample = 1;

    public CAssetRenderer(ArrayList<CGraphicMulticolorTileset> tileSets, CGraphicTileset markertileset,
                          CGraphicTileset corpsetileset, ArrayList <CGraphicTileset> firetileset,
                          CGraphicTileset buildingdeath, CGraphicTileset arrowtileset, CPlayerData player,
                          CAssetDecoratedMap map) {

        int TypeIndex = 0, MarkerIndex = 0;
        DTilesets = tileSets;
        DPlayerMap = map;
        DFireTilesets = firetileset;
        DMarkerTileset = markertileset;
        DBuildingDeathTileset = buildingdeath;
        DCorpseTileset = corpsetileset;
        DArrowTileset = arrowtileset;
        DPlayerData = player;

//        Log.d("CAssetRenderer", "get " + DTilesets.get(EAssetType.atNone));
//        Log.d("CAssetRenderer", "findpixel: " + DTilesets.get(EAssetType.atNone).FindPixel("none"));
        DPixelIndices.add(pcNone.ordinal(), DTilesets.get(atNone.ordinal()).FindPixel("none"));
        DPixelIndices.add(EPlayerColor.pcBlue.ordinal(), DTilesets.get(atNone.ordinal()).FindPixel("blue"));
        DPixelIndices.add(EPlayerColor.pcRed.ordinal(), DTilesets.get(atNone.ordinal()).FindPixel("red"));
        DPixelIndices.add(EPlayerColor.pcGreen.ordinal(), DTilesets.get(atNone.ordinal()).FindPixel("green"));
        DPixelIndices.add(EPlayerColor.pcPurple.ordinal(), DTilesets.get(atNone.ordinal()).FindPixel("purple"));
        DPixelIndices.add(EPlayerColor.pcOrange.ordinal(), DTilesets.get(atNone.ordinal()).FindPixel("orange"));
        DPixelIndices.add(EPlayerColor.pcYellow.ordinal(), DTilesets.get(atNone.ordinal()).FindPixel("yellow"));
        DPixelIndices.add(EPlayerColor.pcBlack.ordinal(), DTilesets.get(atNone.ordinal()).FindPixel("black"));
        DPixelIndices.add(EPlayerColor.pcWhite.ordinal(), DTilesets.get(atNone.ordinal()).FindPixel("white"));
        DPixelIndices.add(pcMax.ordinal(), DTilesets.get(atNone.ordinal()).FindPixel("self"));
        DPixelIndices.add(pcMax.ordinal() + 1, DTilesets.get(atNone.ordinal()).FindPixel("enemy"));
        DPixelIndices.add(pcMax.ordinal() + 2, DTilesets.get(atNone.ordinal()).FindPixel("building"));

        while (true) {
            int Index = DMarkerTileset.FindTile("marker-" + Integer.toString(MarkerIndex));
            if (0 > Index) {
                break;
            }
            DMarkerIndices.add(Index);
            MarkerIndex++;
        }
        DPlaceGoodIndex = DMarkerTileset.FindTile("place-good");
        DPlaceBadIndex = DMarkerTileset.FindTile("place-bad");

        {
            String LastDirectionName = "decay-nw";
            String decayDirections[] = {"decay-n","decay-ne","decay-e","decay-se","decay-s","decay-sw","decay-w","decay-nw"};
            for (String DirectionName : decayDirections) {
                int StepIndex = 0, TileIndex;
                while (true) {
                    TileIndex = DCorpseTileset.FindTile(DirectionName.toString() + String.valueOf(StepIndex));
                    if (0 <= TileIndex) {
                        DCorpseIndices.add(TileIndex);
                    }
                    else {
                        TileIndex = DCorpseTileset.FindTile(LastDirectionName + String.valueOf(StepIndex));
                        if (0 <= TileIndex) {
                            DCorpseIndices.add(TileIndex);
                        }
                        else {
                            break;
                        }
                    }
                    StepIndex++;
                }
                LastDirectionName = DirectionName;
            }
        }
        String attDirections[] = {"attack-n","attack-ne","attack-e","attack-se","attack-s","attack-sw","attack-w","attack-nw"};
        for (String DirectionName : attDirections) {
            int StepIndex = 0, TileIndex;
            while (true) {
                TileIndex = DArrowTileset.FindTile(DirectionName + String.valueOf(StepIndex));
                if (0 <= TileIndex) {
                    DArrowIndices.add(TileIndex);
                }
                else {
                    break;
                }
                StepIndex++;
            }
        }

        for (CGraphicMulticolorTileset Tileset : DTilesets) {
            ArrayList<Integer> tempRow= new ArrayList<>();
            DConstructIndices.add(tempRow);
            tempRow = new ArrayList<>();
            DBuildIndices.add(tempRow);
            tempRow = new ArrayList<>();
            DWalkIndices.add(tempRow);
            tempRow = new ArrayList<>();
            DNoneIndices.add(tempRow);
            tempRow = new ArrayList<>();
            DCarryGoldIndices.add(tempRow);
            tempRow = new ArrayList<>();
            DCarryLumberIndices.add(tempRow);
            tempRow = new ArrayList<>();
            DAttackIndices.add(tempRow);
            tempRow = new ArrayList<>();
            DDeathIndices.add(tempRow);
            tempRow = new ArrayList<>();
            DPlaceIndices.add(tempRow);
            if (Tileset != null) {
//                PrintDebug(DEBUG_LOW,"Checking Walk on %d\n",TypeIndex);
                String walkDirections[] = {"walk-n","walk-ne","walk-e","walk-se","walk-s","walk-sw","walk-w","walk-nw"};
                for (String DirectionName : walkDirections) {
                    int StepIndex = 0, TileIndex;
                    while (true) {
                        TileIndex = Tileset.FindTile(DirectionName + Integer.toString(StepIndex));
                        if (0 <= TileIndex) {
                            DWalkIndices.get(TypeIndex).add(TileIndex);
                        }
                        else {
                            break;
                        }
                        StepIndex++;
                    }
                }
//                PrintDebug(DEBUG_LOW,"Checking Construct on %d\n",TypeIndex);
                {
                    int StepIndex = 0, TileIndex;
                    while (true) {
                        TileIndex = Tileset.FindTile("construct-" + Integer.toString(StepIndex));
                        if (0 <= TileIndex) {
                            DConstructIndices.get(TypeIndex).add(TileIndex);
                        }
                        else {
                            if (StepIndex == 0) {
                                DConstructIndices.get(TypeIndex).add(-1);
                            }
                            break;
                        }
                        StepIndex++;
                    }
                }
//                PrintDebug(DEBUG_LOW,"Checking Gold on %d\n",TypeIndex);
                String goldDirections[] = {"gold-n","gold-ne","gold-e","gold-se","gold-s","gold-sw","gold-w","gold-nw"};
                for (String DirectionName : goldDirections) {
                    int StepIndex = 0, TileIndex;
                    while (true) {
                        TileIndex = Tileset.FindTile(DirectionName + Integer.toString(StepIndex));
                        if (0 <= TileIndex) {
                            DCarryGoldIndices.get(TypeIndex).add(TileIndex);
                        }
                        else {
                            break;
                        }
                        StepIndex++;
                    }
                }
//                PrintDebug(DEBUG_LOW,"Checking Lumber on %d\n",TypeIndex);
                String lumberDirections[] = {"lumber-n","lumber-ne","lumber-e","lumber-se","lumber-s","lumber-sw","lumber-w","lumber-nw"};
                for (String DirectionName : lumberDirections) {
                    int StepIndex = 0, TileIndex;
                    while (true) {
                        TileIndex = Tileset.FindTile(DirectionName + Integer.toString(StepIndex));
                        if (0 <= TileIndex) {
                            DCarryLumberIndices.get(TypeIndex).add(TileIndex);
                        }
                        else {
                            break;
                        }
                        StepIndex++;
                    }
                }
//                PrintDebug(DEBUG_LOW,"Checking Attack on %d\n",TypeIndex);
                String attackDirs[] =  {"attack-n","attack-ne","attack-e","attack-se","attack-s","attack-sw","attack-w","attack-nw"};
                for (String DirectionName : attackDirs) {
                    int StepIndex = 0, TileIndex;
                    while (true) {
                        TileIndex = Tileset.FindTile(DirectionName + Integer.toString(StepIndex));
                        if (0 <= TileIndex) {
                            DAttackIndices.get(TypeIndex).add(TileIndex);
                        }
                        else {
                            break;
                        }
                        StepIndex++;
                    }
                }
                if (0 == DAttackIndices.get(TypeIndex).size()) {
                    int TileIndex;
                    for (int Index = 0; Index < dMax.ordinal(); Index++) {
                        if (0 <= (TileIndex = Tileset.FindTile("active"))) {
                            DAttackIndices.get(TypeIndex).add(TileIndex);
                        }
                        else if (0 <= (TileIndex = Tileset.FindTile("inactive"))) {
                            DAttackIndices.get(TypeIndex).add(TileIndex);
                        }
                    }
                }
//                PrintDebug(DEBUG_LOW,"Checking Death on %d\n",TypeIndex);
                String LastDirectionName = "death-nw";
                String deathDirections[] = {"death-n","death-ne","death-e","death-se","death-s","death-sw","death-w","death-nw"};
                for (String DirectionName : deathDirections) {
                    int StepIndex = 0, TileIndex;
                    while (true) {
                        TileIndex = Tileset.FindTile(DirectionName + Integer.toString(StepIndex));
                        if (0 <= TileIndex) {
                            DDeathIndices.get(TypeIndex).add(TileIndex);
                        }
                        else {
                            TileIndex = Tileset.FindTile(LastDirectionName + Integer.toString(StepIndex));
                            if (0 <= TileIndex) {
                                DDeathIndices.get(TypeIndex).add(TileIndex);
                            }
                            else {
                                break;
                            }
                        }
                        StepIndex++;
                    }
                    LastDirectionName = DirectionName;
                }
                if (DDeathIndices.get(TypeIndex).size() != 0) {

                }
//                PrintDebug(DEBUG_LOW,"Checking None on %d\n",TypeIndex);
                String noDirections[] = {"none-n","none-ne","none-e","none-se","none-s","none-sw","none-w","none-nw"};
                for (String DirectionName : noDirections) {
                    int TileIndex = Tileset.FindTile(DirectionName);
                    if (0 <= TileIndex) {
                        DNoneIndices.get(TypeIndex).add(TileIndex);
                    } else if ((DWalkIndices.get(TypeIndex)).size() != 0) {
                        DNoneIndices.get(TypeIndex).add(DWalkIndices.get(TypeIndex).get
                                (DNoneIndices.get(TypeIndex).size() * (DWalkIndices.get
                                        (TypeIndex).size() / dMax.ordinal())));
                    } else if (0 <= (TileIndex = Tileset.FindTile("inactive"))) {
                        DNoneIndices.get(TypeIndex).add(TileIndex);
                    }
                }
//                PrintDebug(DEBUG_LOW,"Checking Build on %d\n",TypeIndex);
                String buildDirections[] = {"build-n","build-ne","build-e","build-se","build-s","build-sw","build-w","build-nw"};
                for (String DirectionName : buildDirections) {
                    int StepIndex = 0, TileIndex;
                    while (true) {
                        TileIndex = Tileset.FindTile(DirectionName + Integer.toString(StepIndex));
                        if (0 <= TileIndex) {
                            DBuildIndices.get(TypeIndex).add(TileIndex);
                        } else {
                            if (StepIndex == 0) {
                                if (0 <= (TileIndex = Tileset.FindTile("active"))) {
                                    DBuildIndices.get(TypeIndex).add(TileIndex);
                                } else if (0 <= (TileIndex = Tileset.FindTile("inactive"))) {
                                    DBuildIndices.get(TypeIndex).add(TileIndex);
                                }
                            }
                            break;
                        }
                        StepIndex++;
                    }
                }
//                PrintDebug(DEBUG_LOW,"Checking Place on %d\n",TypeIndex);
                {
                    DPlaceIndices.get(TypeIndex).add( Tileset.FindTile("place") );
                }

//                PrintDebug(DEBUG_LOW,"Done checking type %d\n",TypeIndex);
            }
            TypeIndex++;
        }
    }

    public static int UpdateFrequency(int freq) {
        if (TARGET_FREQUENCY >= freq) {
            DAnimationDownSample = 1;
            return TARGET_FREQUENCY;
        }
        DAnimationDownSample = freq / TARGET_FREQUENCY;
        return freq;
    }

    public class SAssetRenderData{
        EAssetType DType;
        int DX;
        int DY;
        int DBottomY;
        int DTileIndex;
        int DColorIndex;
        int DPixelColor;
    }// SAssetRenderData, *SAssetRenderDataRef;

//    boolean CompareRenderData(SAssetRenderData first, SAssetRenderData second) {
//        if (first.DBottomY < second.DBottomY) {
//            return true;
//        }
//        if (first.DBottomY > second.DBottomY) {
//            return false;
//        }
//
//        return first.DX <= second.DX;
//    }
//

    boolean CompareRenderData(SAssetRenderData first, SAssetRenderData second) {
        if (first.DBottomY < second.DBottomY) {
            return true;
        }
        if (first.DBottomY > second.DBottomY) {
            return false;
        }

        return first.DX <= second.DX;
    }

    public void DrawSelections(Canvas canvas, GameView gameView, ArrayList<CPlayerAsset>
            selectionlist, SRectangle selectrect, boolean highlightbuilding) {
        int ScreenLeftX = (int)Math.floor(gameView.movX/CPosition.TileWidth())*CPosition.TileWidth();
        int ScreenTopY = (int)Math.floor(gameView.movY/CPosition.TileHeight())*CPosition.TileHeight();
//        GdkColor RectangleColor(DTilesets.get(0).PixelColor(DPixelIndices.get(pcMax)));
        int ScreenRightX = (int)Math.floor(ScreenLeftX) + gameView.getWidth()-1;
        int ScreenBottomY = (int)Math.floor(ScreenTopY) + gameView.getHeight()-1;
        if (selectionlist != null && !selectionlist.isEmpty()) {
            for (int i = 0; i < selectionlist.size(); i++) {
                int SelectionX = selectionlist.get(i).PositionX() - CPosition.HalfTileWidth();
                int SelectionY = selectionlist.get(i).PositionY() - CPosition.HalfTileHeight();
                int SelBotX = SelectionX + (selectionlist.get(i).AssetType().Size()) * CPosition
                        .TileWidth();
                int SelBotY = SelectionY + (selectionlist.get(i).AssetType().Size()) * CPosition
                        .TileHeight();

                boolean OnScreen = true;
                if ((SelectionX < ScreenLeftX) || (SelectionX > ScreenRightX)) {
                    OnScreen = false;
                } else if ((SelectionY < ScreenTopY) || (SelectionY > ScreenBottomY)) {
                    OnScreen = false;
                }
                if (OnScreen) {
                    SelectionX -= (int) Math.floor(ScreenLeftX);// + DTilesets.get(selectionlist.get(0)
//                    .AssetType().Type().ordinal()).TileHalfWidth() * 2;
                    SelectionY -= (int) Math.floor(ScreenTopY);// + DTilesets.get(selectionlist.get(0)
//                    .AssetType().Type().ordinal()).TileHalfHeight() * 2;
                    SelBotX -= (int) Math.floor(ScreenLeftX);// + DTilesets.get(selectionlist.get(0)
//                    .AssetType().Type().ordinal()).TileHalfWidth() * 2;
                    SelBotY -= (int) Math.floor(ScreenTopY);// + DTilesets.get(selectionlist.get(0)
//                    .AssetType().Type().ordinal()).TileHalfHeight() * 2;

                    Paint myPaint = new Paint();
                    myPaint.setColor(Color.rgb(0, 150, 0));
                    myPaint.setStyle(Paint.Style.STROKE);
                    myPaint.setStrokeWidth(3);
                    canvas.drawRect(SelectionX, SelectionY, SelBotX, SelBotY, myPaint);
                }
            }
        }
//        if (highlightbuilding) {
//            RectangleColor = DTilesets.get(0).PixelColor(DPixelIndices.get(pcMax + 2));
//            gdk_gc_set_rgb_fg_color(TempGC, RectangleColor);
//
//            for (auto &AssetIterator : DPlayerMap.Assets()) {
//                SAssetRenderData TempRenderData;
//                TempRenderData.DType = AssetIterator.Type();
//                if (atNone == TempRenderData.DType) {
//                    continue;
//                }
//                if ((0 <= TempRenderData.DType) && (TempRenderData.DType < DTilesets.size())) {
//                    if (0 == AssetIterator.Speed()) {
//                        int RightX;
//                        int Offset = atGoldMine == TempRenderData.DType ? 1 : 0;
//
//                        TempRenderData.DX = AssetIterator.PositionX() + (AssetIterator.Size() - 1) * CPosition.HalfTileWidth() - DTilesets.get(TempRenderData.DType).TileHalfWidth();
//                        TempRenderData.DY = AssetIterator.PositionY() + (AssetIterator.Size() - 1) * CPosition.HalfTileHeight() - DTilesets.get(TempRenderData.DType).TileHalfHeight();
//                        TempRenderData.DX -= Offset * CPosition.TileWidth();
//                        TempRenderData.DY -= Offset * CPosition.TileHeight();
//                        //printf("%d @ (%d, %d)\n",TempRenderData.DType, TempRenderData.DX , TempRenderData.DY);
//                        RightX = TempRenderData.DX + DTilesets.get(TempRenderData.DType).TileWidth() + (2 * Offset * CPosition.TileWidth()) - 1;
//                        TempRenderData.DBottomY = TempRenderData.DY + DTilesets.get(TempRenderData.DType).TileHeight() + (2 * Offset * CPosition.TileHeight()) - 1;
//                        boolean OnScreen = true;
//                        if ((RightX < rect.DXPosition) || (TempRenderData.DX > ScreenRightX)) {
//                            OnScreen = false;
//                        }
//                        else if ((TempRenderData.DBottomY < rect.DYPosition) || (TempRenderData.DY > ScreenBottomY)) {
//                            OnScreen = false;
//                        }
//                        TempRenderData.DX -= rect.DXPosition;
//                        TempRenderData.DY -= rect.DYPosition;
//                        if (OnScreen) {
//                            gdk_draw_rectangle(drawable, TempGC, FALSE, TempRenderData.DX, TempRenderData.DY, DTilesets.get(TempRenderData.DType).TileWidth() + (2 * Offset * CPosition.TileWidth()), DTilesets.get(TempRenderData.DType).TileHeight() + (2 * Offset * CPosition.TileHeight()));
//                        }
//                    }
//                }
//            }
//
//
//            RectangleColor = DTilesets.get(0).PixelColor(DPixelIndices.get(pcMax));
//        }

//        if (selectrect.DWidth && selectrect.DHeight) {
//            SelectionX = selectrect.DXPosition - rect.DXPosition;
//            SelectionY = selectrect.DYPosition - rect.DYPosition;
//
//            gdk_draw_rectangle(drawable, TempGC, FALSE, SelectionX, SelectionY, selectrect.DWidth, selectrect.DHeight);
//        }
//
//        if (selectionlist.size() != 0) {
//            if (auto Asset = selectionlist.front().lock()) {
//                if (pcNone == Asset.Color()) {
//                    RectangleColor = DTilesets.get(0).PixelColor(DPixelIndices.get(pcNone));
//                }
//                else if (DPlayerData.Color() != Asset.Color()) {
//                    RectangleColor = DTilesets.get(0).PixelColor(DPixelIndices.get(pcMax+1));
//                }
//                gdk_gc_set_rgb_fg_color(TempGC, RectangleColor);
//            }
//        }
//
//        for (auto &AssetIterator : selectionlist) {
//            if (auto LockedAsset = AssetIterator.lock()) {
//                SAssetRenderData TempRenderData;
//                TempRenderData.DType = LockedAsset.Type();
//                if (atNone == TempRenderData.DType) {
//                    if (aaDecay == LockedAsset.Action()) {
//                        int RightX;
//                        boolean OnScreen = true;
//
//                        TempRenderData.DX = LockedAsset.PositionX() - DCorpseTileset.TileWidth()/2;
//                        TempRenderData.DY = LockedAsset.PositionY() - DCorpseTileset.TileHeight()/2;
//                        RightX = TempRenderData.DX + DCorpseTileset.TileWidth();
//                        TempRenderData.DBottomY = TempRenderData.DY + DCorpseTileset.TileHeight();
//
//                        if ((RightX < rect.DXPosition) || (TempRenderData.DX > ScreenRightX)) {
//                            OnScreen = false;
//                        }
//                        else if ((TempRenderData.DBottomY < rect.DYPosition) || (TempRenderData.DY > ScreenBottomY)) {
//                            OnScreen = false;
//                        }
//                        TempRenderData.DX -= rect.DXPosition;
//                        TempRenderData.DY -= rect.DYPosition;
//                        if (OnScreen) {
//                            int  ActionSteps = DCorpseIndices.size();
//                            ActionSteps /= dMax;
//                            if (ActionSteps != 0) {
//                                int CurrentStep = LockedAsset.Step() / (DAnimationDownSample * TARGET_FREQUENCY);
//                                if (CurrentStep >= ActionSteps) {
//                                    CurrentStep = ActionSteps - 1;
//                                }
//                                TempRenderData.DTileIndex = DCorpseIndices.get(LockedAsset.Direction() * ActionSteps + CurrentStep);
//                            }
//
//                            DCorpseTileset.DrawTile(drawable, TempGC, TempRenderData.DX, TempRenderData.DY, TempRenderData.DTileIndex);
//                        }
//                    }
//                    else if (aaAttack != LockedAsset.Action()) {
//                        int RightX;
//                        boolean OnScreen = true;
//
//                        TempRenderData.DX = LockedAsset.PositionX() - DMarkerTileset.TileWidth()/2;
//                        TempRenderData.DY = LockedAsset.PositionY() - DMarkerTileset.TileHeight()/2;
//                        RightX = TempRenderData.DX + DMarkerTileset.TileWidth();
//                        TempRenderData.DBottomY = TempRenderData.DY + DMarkerTileset.TileHeight();
//
//                        if ((RightX < rect.DXPosition) || (TempRenderData.DX > ScreenRightX)) {
//                            OnScreen = false;
//                        }
//                        else if ((TempRenderData.DBottomY < rect.DYPosition) || (TempRenderData.DY > ScreenBottomY)) {
//                            OnScreen = false;
//                        }
//                        TempRenderData.DX -= rect.DXPosition;
//                        TempRenderData.DY -= rect.DYPosition;
//                        if (OnScreen) {
//                            int MarkerIndex = LockedAsset->Step() / DAnimationDownSample;
//                            if (MarkerIndex < DMarkerIndices.size()) {
//                                DMarkerTileset->DrawTile(drawable, TempGC, TempRenderData.DX, TempRenderData.DY, DMarkerIndices.get(MarkerIndex));
//                            }
//                        }
//                    }
//                }
//                else if ((0 <= TempRenderData.DType) && (TempRenderData.DType < DTilesets.size())) {
//                    int RightX, RectWidth, RectHeight;
//                    boolean OnScreen = true;
//
//                    TempRenderData.DX = LockedAsset.PositionX() - CPosition.HalfTileWidth();
//                    TempRenderData.DY = LockedAsset.PositionY() - CPosition.HalfTileHeight();
//                    RectWidth = CPosition.TileWidth() * LockedAsset.Size();
//                    RectHeight = CPosition.TileHeight() * LockedAsset.Size();
//                    RightX = TempRenderData.DX + RectWidth;
//                    TempRenderData.DBottomY = TempRenderData.DY + RectHeight;
//                    if ((RightX < rect.DXPosition) || (TempRenderData.DX > ScreenRightX)) {
//                        OnScreen = false;
//                    }
//                    else if ((TempRenderData.DBottomY < rect.DYPosition) || (TempRenderData.DY > ScreenBottomY)) {
//                        OnScreen = false;
//                    }
//                    else if ((aaMineGold == LockedAsset.Action()) || (aaConveyLumber == LockedAsset.Action()) || (aaConveyGold == LockedAsset.Action())) {
//                        OnScreen = false;
//                    }
//                    TempRenderData.DX -= rect.DXPosition;
//                    TempRenderData.DY -= rect.DYPosition;
//                    if (OnScreen) {
//                        gdk_draw_rectangle(drawable, TempGC, FALSE, TempRenderData.DX, TempRenderData.DY, RectWidth, RectHeight);
//                    }
//                }
//            }
//        }
    }




    public void DrawAssets(Canvas canvas, GameView gameView) {
        int ScreenLeftX = (int)Math.floor(gameView.movX/CPosition.TileWidth())*CPosition.TileWidth();
        int ScreenTopY = (int)Math.floor(gameView.movY/CPosition.TileHeight())*CPosition.TileHeight();

        int ScreenRightX = (int)Math.floor(ScreenLeftX) + gameView.getWidth()-1;
        int ScreenBottomY = (int)Math.floor(ScreenTopY) + gameView.getHeight()-1;
        ArrayList<SAssetRenderData> FinalRenderList = new ArrayList<>();

        for (CPlayerAsset AssetIterator : DPlayerMap.Assets()) {
//            Log.d("AR", "rendering" + AssetIterator.Type());
            SAssetRenderData TempRenderData = new SAssetRenderData();
            TempRenderData.DType = AssetIterator.Type();
//            Log.d("assetr", " " + TempRenderData.DType );
            if (atNone == TempRenderData.DType) {
                continue;
            }
            if ((0 <= TempRenderData.DType.ordinal()) && (TempRenderData.DType.ordinal() <
                    DTilesets.size())) {
//                CPixelType PixelType(AssetIterator);
                TempRenderData.DX = AssetIterator.PositionX() + (AssetIterator.Size() - 1) *
                        CPosition.HalfTileWidth() - DTilesets.get(TempRenderData.DType.ordinal()).TileHalfWidth();
                TempRenderData.DY = AssetIterator.PositionY() + (AssetIterator.Size() - 1) *
                        CPosition.HalfTileHeight() - DTilesets.get(TempRenderData.DType.ordinal()).TileHalfHeight();
//                TempRenderData.DPixelColor = PixelType.ToPixelColor();
//                printf("%d @ (%d, %d)\n",TempRenderData.DType, TempRenderData.DX , TempRenderData.DY);
                boolean OnScreen = true;
                if ((AssetIterator.PositionX() < ScreenLeftX) || (AssetIterator.PositionX() > ScreenRightX)) {
                    OnScreen = false;
                } else if ((AssetIterator.PositionY() < ScreenTopY) || (AssetIterator.PositionY() > ScreenBottomY)) {
                    OnScreen = false;
                }
                TempRenderData.DX -= ScreenLeftX;
                TempRenderData.DY -= ScreenTopY;

                TempRenderData.DColorIndex = AssetIterator.Color().ordinal();
                TempRenderData.DTileIndex = -1;
                if (OnScreen) {
                    if (atGold == TempRenderData.DType) {
                    String name;
                    if (100 >= AssetIterator.Gold()) {
                        name = "gold-small";
                    }  else if (200 >= AssetIterator.Gold()) {
                        name = "gold-medium";
                    } else {
                        name = "gold-large";
                    }
                    TempRenderData.DTileIndex = DTilesets.get(atGold.ordinal()).FindTile(name);
                    FinalRenderList.add(TempRenderData);
                    continue;
                } else if (atLumber == TempRenderData.DType) {
                    String name;
                    if (33 > AssetIterator.Lumber()) {
                        name = "lumber-small";
                    } else if (67 > AssetIterator.Lumber()) {
                        name = "lumber-medium";
                    } else {
                        name = "lumber-large";
                    }
                    TempRenderData.DTileIndex = DTilesets.get(atLumber.ordinal()).FindTile(name);
                    FinalRenderList.add(TempRenderData);
                    continue;
                }
                    int ActionSteps, CurrentStep, TileIndex;
                    switch (AssetIterator.Action()) {
                        case aaBuild:
                            ActionSteps = DBuildIndices.get(TempRenderData.DType.ordinal()).size();
                            ActionSteps /= dMax.ordinal();
                            if (ActionSteps != 0) {
                                TileIndex = AssetIterator.Direction().ordinal() * ActionSteps + (
                                        (AssetIterator.Step() / DAnimationDownSample) %
                                                ActionSteps);
                                TempRenderData.DTileIndex = (DBuildIndices.get(TempRenderData
                                        .DType.ordinal())).get(TileIndex);
                            }
                            break;
                        case aaConstruct:
                            ActionSteps = DConstructIndices.get(TempRenderData.DType.ordinal())
                                    .size();
                            if (ActionSteps != 0) {
                                int TotalSteps = AssetIterator.BuildTime() * CPlayerAsset
                                        .UpdateFrequency();
                                CurrentStep = AssetIterator.Step() * ActionSteps / TotalSteps;
                                if (CurrentStep == DConstructIndices.get(TempRenderData.DType
                                        .ordinal()).size()) {
                                    CurrentStep--;
                                }
                                TempRenderData.DTileIndex = DConstructIndices.get(TempRenderData
                                        .DType.ordinal()).get(CurrentStep);
                            }
                            break;
                        case aaWalk:
                            if (AssetIterator.Lumber() > 0) {
                            ActionSteps = DCarryLumberIndices.get(TempRenderData.DType.ordinal()).size();
                            ActionSteps /= dMax.ordinal();
                            TileIndex = AssetIterator.Direction().ordinal() * ActionSteps + (
                                    (AssetIterator.Step() / DAnimationDownSample) % ActionSteps);
                            TempRenderData.DTileIndex = (DCarryLumberIndices.get(TempRenderData
                                    .DType.ordinal())).get(TileIndex);
                        } else if (AssetIterator.Gold() > 0) {
                            ActionSteps = DCarryGoldIndices.get(TempRenderData.DType.ordinal())
                                    .size();
                            ActionSteps /= dMax.ordinal();
                            TileIndex = AssetIterator.Direction().ordinal() * ActionSteps + (
                                    (AssetIterator.Step() / DAnimationDownSample) % ActionSteps);
                            TempRenderData.DTileIndex = DCarryGoldIndices.get(TempRenderData
                                    .DType.ordinal()).get(TileIndex);
                        } else {
                            ActionSteps = DWalkIndices.get(TempRenderData.DType.ordinal()).size();
                            ActionSteps /= dMax.ordinal();
                            TileIndex = AssetIterator.Direction().ordinal() * ActionSteps + (
                                    (AssetIterator.Step() / DAnimationDownSample) % ActionSteps);
                            TempRenderData.DTileIndex = (DWalkIndices.get(TempRenderData.DType.ordinal())).get(TileIndex);
                        }
                            break;
                        case aaAttack:
                            CurrentStep = AssetIterator.Step() % (AssetIterator.AttackSteps() +
                                    AssetIterator.ReloadSteps());
                            if (CurrentStep < AssetIterator.AttackSteps()) {
                                ActionSteps = DAttackIndices.get(TempRenderData.DType.ordinal()).size();
                                ActionSteps /= dMax.ordinal();
                                TileIndex = (AssetIterator.Direction().ordinal() % dMax.ordinal()
                                ) * ActionSteps + (CurrentStep * ActionSteps  / AssetIterator
                                        .AttackSteps());
                                TempRenderData.DTileIndex = (DAttackIndices.get(TempRenderData.DType.ordinal())).get(TileIndex);
                            } else {
                                TempRenderData.DTileIndex = (DNoneIndices.get(TempRenderData
                                        .DType.ordinal())).get(AssetIterator.Direction().ordinal
                                        () % dMax.ordinal());
                            }
                            break;
                        case aaRepair:
                        case aaHarvestLumber:
                            ActionSteps = DAttackIndices.get(TempRenderData.DType.ordinal()).size();
                            ActionSteps /= dMax.ordinal();
                            TileIndex = AssetIterator.Direction().ordinal() * ActionSteps + (
                                    (AssetIterator.Step() / DAnimationDownSample) % ActionSteps);
                            TempRenderData.DTileIndex = (DAttackIndices.get(TempRenderData.DType.ordinal())).get(TileIndex);
                            break;
                        case aaMineGold:
                            break;
                        case aaStandGround:
                        case aaNone:
                            TempRenderData.DTileIndex = (DNoneIndices.get(TempRenderData.DType
                                    .ordinal())).get(AssetIterator.Direction().ordinal());
                            if (AssetIterator.Speed() > 0) {
                                if (AssetIterator.Lumber() > 0) {
                                    ActionSteps = DCarryLumberIndices.get(TempRenderData.DType
                                            .ordinal()).size();
                                    ActionSteps /= dMax.ordinal();
                                    TempRenderData.DTileIndex = (DCarryLumberIndices.get
                                            (TempRenderData.DType.ordinal())).get(AssetIterator
                                            .Direction().ordinal() * ActionSteps);
                                } else if (AssetIterator.Gold() > 0) {
                                    ActionSteps = DCarryGoldIndices.get(TempRenderData.DType
                                            .ordinal()).size();
                                    ActionSteps /= dMax.ordinal();
                                    TempRenderData.DTileIndex = (DCarryGoldIndices.get
                                            (TempRenderData.DType.ordinal())).get(AssetIterator
                                            .Direction().ordinal() * ActionSteps);
                                }
                            }
                            break;
                        case aaCapability:
                            if (AssetIterator.Speed() > 0) {
                                if ((actPatrol == AssetIterator.CurrentCommand().DCapability) ||
                                        (actStandGround == AssetIterator.CurrentCommand()
                                                .DCapability)) {
                                    TempRenderData.DTileIndex = (DNoneIndices.get(TempRenderData.DType.ordinal())).get(AssetIterator.Direction()
                                            .ordinal());
                                }
                            } else {
                                // Buildings
                                TempRenderData.DTileIndex = (DNoneIndices.get(TempRenderData.DType
                                        .ordinal())).get(AssetIterator.Direction().ordinal());
                            }
                            break;
                        case aaDeath:
                            ActionSteps = DDeathIndices.get(TempRenderData.DType.ordinal()).size();
                            if (AssetIterator.Speed() > 0) {
                                ActionSteps /= dMax.ordinal();
                                if (ActionSteps != 0) {
                                    CurrentStep = AssetIterator.Step() / DAnimationDownSample;
                                    if (CurrentStep >= ActionSteps) {
                                        CurrentStep = ActionSteps - 1;
                                    }
                                    TempRenderData.DTileIndex = (DDeathIndices.get(TempRenderData
                                            .DType.ordinal())).get(AssetIterator.Direction()
                                            .ordinal() * ActionSteps + CurrentStep);
                                }
                            } else if (AssetIterator.Step() < DBuildingDeathTileset.DTileCount) {
                                    TempRenderData.DTileIndex = DTilesets.get(TempRenderData.DType
                                            .ordinal()).DTileCount + AssetIterator.Step();
                                    TempRenderData.DX = AssetIterator.PositionX() + (AssetIterator
                                            .Size() - 1) * CPosition.HalfTileWidth() - DTilesets
                                            .get(TempRenderData.DType.ordinal()).TileHalfWidth();
                                    TempRenderData.DY = AssetIterator.PositionY() + (AssetIterator
                                            .Size() - 1) * CPosition.HalfTileHeight() - DTilesets
                                            .get(TempRenderData.DType.ordinal()).TileHalfHeight();
                            }
                        default:
                            break;
                    }
//                    Log.d("assetr", "after switch");
                    if (0 <= TempRenderData.DTileIndex) {
                        FinalRenderList.add(TempRenderData);
                    }
                }
            }
        }
//        Log.d("assetr", "after for loop");
//        FinalRenderList.sort(CompareRenderData);
        for (SAssetRenderData RenderIterator : FinalRenderList) { //renderIterator type may be wrong. for auto &
            if (RenderIterator.DTileIndex < DTilesets.get(RenderIterator.DType.ordinal()).DTileCount) {
                DTilesets.get(RenderIterator.DType.ordinal()).DrawTile(canvas, RenderIterator.DX, RenderIterator.DY, RenderIterator.DTileIndex, RenderIterator.DColorIndex);
//                DTilesets.get(RenderIterator.DType.ordinal()).DrawClipped(typedrawable, TempTypeGC, RenderIterator.DX, RenderIterator.DY, RenderIterator.DTileIndex, RenderIterator.DPixelColor);
            }
            else {
                DBuildingDeathTileset.DrawTile(canvas, RenderIterator.DX, RenderIterator.DY, RenderIterator.DTileIndex);
            }
        }
    }

    public void DrawMiniAssets(Canvas canvas, float scale) {
        if (null != DPlayerData) {
            for (CPlayerAsset AssetIterator : DPlayerMap.Assets()) {
                EPlayerColor AssetColor = AssetIterator.Color();
                if (AssetColor == DPlayerData.Color()) {
                    AssetColor = pcMax;
                }
                DTilesets.get(atNone.ordinal()).DrawPixel(canvas, AssetIterator.TilePositionX() * scale, AssetIterator.TilePositionY() * scale, AssetIterator.Size() * scale, DPixelIndices.get(AssetColor.ordinal()));
            }
        }
        else {
            for (CAssetDecoratedMap.SAssetInitialization AssetIterator : DPlayerMap.AssetInitializationList()) {
                EPlayerColor AssetColor = AssetIterator.DColor;
//                DTilesets.get(0).DrawPixel(canvas, AssetIterator.DTilePosition.X(), AssetIterator.DTilePosition.Y(), CPlayerAssetType.FindDefaultFromName(AssetIterator.DType).Size(), DPixelIndices.get(AssetColor.ordinal()));
//                Log.d("CAssetRenderer", "dtilesets.color: " + AssetColor);
                DTilesets.get(atNone.ordinal()).DrawPixel(canvas, AssetIterator.DTilePosition.X() * scale, AssetIterator.DTilePosition.Y() * scale, CPlayerAssetType.FindDefaultFromName(AssetIterator.DType).Size() * scale, DPixelIndices.get(AssetColor.ordinal()));
            }
        }
    }

    public void DrawOverlays(Canvas drawable, SRectangle rect) {
        Paint TempGC = new Paint();
        int ScreenRightX = rect.DXPosition + rect.DWidth - 1;
        int ScreenBottomY = rect.DYPosition + rect.DHeight - 1;

        for (CPlayerAsset AssetIterator : DPlayerMap.Assets()) {
            SAssetRenderData TempRenderData = new SAssetRenderData();
            TempRenderData.DType = AssetIterator.Type();
            if (atNone == TempRenderData.DType) {
                if (aaAttack == AssetIterator.Action()) {
                    int RightX;
                    boolean OnScreen = true;

                    TempRenderData.DX = AssetIterator.PositionX() - DArrowTileset.TileWidth()/2;
                    TempRenderData.DY = AssetIterator.PositionY() - DArrowTileset.TileHeight()/2;
                    RightX = TempRenderData.DX + DArrowTileset.TileWidth();
                    TempRenderData.DBottomY = TempRenderData.DY + DArrowTileset.TileHeight();

                    if ((RightX < rect.DXPosition) || (TempRenderData.DX > ScreenRightX)) {
                        OnScreen = false;
                    }
                    else if ((TempRenderData.DBottomY < rect.DYPosition) || (TempRenderData.DY > ScreenBottomY)) {
                        OnScreen = false;
                    }
                    TempRenderData.DX -= rect.DXPosition;
                    TempRenderData.DY -= rect.DYPosition;
                    if (OnScreen) {
                        int ActionSteps = DArrowIndices.size();
                        ActionSteps /= dMax.ordinal();

                        DArrowTileset.DrawTile(drawable, TempRenderData.DX, TempRenderData.DY, DArrowIndices.get(AssetIterator.Direction().ordinal() * ActionSteps + ((DPlayerData.GameCycle() - AssetIterator.CreationCycle()) % ActionSteps )));
                    }
                }
            }

            else if (0 == AssetIterator.Speed()) {
                CGameDataTypes.EAssetAction CurrentAction = AssetIterator.Action();

                if (aaDeath != CurrentAction) {
                    int HitRange = AssetIterator.HitPoints() * DFireTilesets.size() * 2 / AssetIterator.MaxHitPoints();

                    if (aaConstruct == CurrentAction) {
                        SAssetCommand Command = AssetIterator.CurrentCommand();

                        if (Command.DAssetTarget != null) {
                            Command = Command.DAssetTarget.CurrentCommand();
                            if (Command.DActivatedCapability != null) {
                                int Divisor = Command.DActivatedCapability.PercentComplete(AssetIterator.MaxHitPoints());
                                Divisor = Divisor > 0 ? Divisor : 1;
                                HitRange = AssetIterator.HitPoints() * DFireTilesets.size() * 2 / Divisor;
                            }
                        }
                        else if (Command.DActivatedCapability != null) {
                            int Divisor = Command.DActivatedCapability.PercentComplete(AssetIterator.MaxHitPoints());
                            Divisor = Divisor > 0 ? Divisor : 1;
                            HitRange = AssetIterator.HitPoints() * DFireTilesets.size() * 2 / Divisor;
                        }
                    }

                    if (HitRange < DFireTilesets.size()) {
                        int TilesetIndex = DFireTilesets.size() - 1 - HitRange;
                        int RightX;

                        TempRenderData.DTileIndex = (DPlayerData.GameCycle() - AssetIterator
                                .CreationCycle()) % DFireTilesets.get(TilesetIndex).TileCount();
                        TempRenderData.DX = AssetIterator.PositionX() + (AssetIterator.Size() -
                                1) * CPosition.HalfTileWidth() - DFireTilesets.get(TilesetIndex)
                                .TileHalfWidth();
                        TempRenderData.DY = AssetIterator.PositionY() + (AssetIterator.Size() -
                                1) * CPosition.HalfTileHeight() - DFireTilesets.get(TilesetIndex)
                                .TileHeight();

                        RightX = TempRenderData.DX + DFireTilesets.get(TilesetIndex).TileWidth() - 1;
                        TempRenderData.DBottomY = TempRenderData.DY + DFireTilesets.get(TilesetIndex).TileHeight() - 1;
                        boolean OnScreen = true;
                        if ((RightX < rect.DXPosition) || (TempRenderData.DX > ScreenRightX)) {
                            OnScreen = false;
                        }
                        else if ((TempRenderData.DBottomY < rect.DYPosition) || (TempRenderData.DY > ScreenBottomY)) {
                            OnScreen = false;
                        }
                        TempRenderData.DX -= rect.DXPosition;
                        TempRenderData.DY -= rect.DYPosition;
                        if (OnScreen) {
                            DFireTilesets.get(TilesetIndex).DrawTile(drawable, TempRenderData.DX, TempRenderData.DY, TempRenderData.DTileIndex);
                        }
                    }
                }
            }
        }
    }

    void DrawPlacement(Canvas drawable, SRectangle rect, CPosition pos, EAssetType type, CPlayerAsset builder) {
        Paint TempGC = new Paint();
        int ScreenRightX = rect.DXPosition + rect.DWidth - 1;
        int ScreenBottomY = rect.DYPosition + rect.DHeight - 1;

        if (atNone != type) {
            CPosition TempPosition = new CPosition();
            CPosition TempTilePosition = new CPosition();
            int PlacementRightX, PlacementBottomY;
            boolean OnScreen = true;
            CPlayerAssetType AssetType = CPlayerAssetType.FindDefaultFromType(type);
            ArrayList< ArrayList< Integer > > PlacementTiles = new ArrayList<ArrayList<Integer>>(AssetType.Size());
            int XOff, YOff;

            TempTilePosition.SetToTile(pos);
            TempPosition.SetFromTile(TempTilePosition);

            TempPosition.IncrementX( (AssetType.Size() - 1) * CPosition.HalfTileWidth() - DTilesets.get(type.ordinal()).TileHalfWidth() );
            TempPosition.IncrementY( (AssetType.Size() - 1) * CPosition.HalfTileHeight() - DTilesets.get(type.ordinal()).TileHalfHeight() );
            PlacementRightX = TempPosition.X() + DTilesets.get(type.ordinal()).TileWidth();
            PlacementBottomY = TempPosition.Y() + DTilesets.get(type.ordinal()).TileHeight();

            TempTilePosition.SetToTile(TempPosition);
            XOff = 0;
            YOff = 0;

            for (ArrayList<Integer> Row : PlacementTiles) {
                Row = new ArrayList<Integer>(AssetType.Size());
                for (Integer Cell : Row) {
                    CTerrainMap.ETileType TileType = DPlayerMap.TileType(TempTilePosition.X() + XOff, TempTilePosition.Y() + YOff);
                    if ((CTerrainMap.ETileType.ttGrass == TileType) || (CTerrainMap.ETileType.ttDirt == TileType) || (CTerrainMap.ETileType.ttStump == TileType) || (CTerrainMap.ETileType.ttRubble == TileType)) {
                        Cell = 1;
                    }
                    else {
                        Cell = 0;
                    }
                    XOff++;
                }
                XOff = 0;
                YOff++;
            }
            XOff = TempTilePosition.X() + AssetType.Size();
            YOff = TempTilePosition.Y() + AssetType.Size();
            for (CPlayerAsset PlayerAsset : DPlayerMap.Assets()) {
                int MinX, MaxX, MinY, MaxY;
                int Offset = atGoldMine == PlayerAsset.Type() ? 1 : 0;

                if (builder == PlayerAsset) {
                    continue;
                }
                if (XOff <= PlayerAsset.TilePositionX() - Offset) {
                    continue;
                }
                if (TempTilePosition.X() >= (PlayerAsset.TilePositionX() + PlayerAsset.Size() + Offset)) {
                    continue;
                }
                if (YOff <= PlayerAsset.TilePositionY() - Offset) {
                    continue;
                }
                if (TempTilePosition.Y() >= (PlayerAsset.TilePositionY() + PlayerAsset.Size() + Offset)) {
                    continue;
                }
                MinX = Math.max(TempTilePosition.X(), PlayerAsset.TilePositionX() - Offset);
                MaxX = Math.min(XOff, PlayerAsset.TilePositionX() + PlayerAsset.Size() + Offset);
                MinY = Math.max(TempTilePosition.Y(), PlayerAsset.TilePositionY() - Offset);
                MaxY = Math.min(YOff, PlayerAsset.TilePositionY() + PlayerAsset.Size() + Offset);
                for (int Y = MinY; Y < MaxY; Y++) {
                    for (int X = MinX; X < MaxX; X++) {
                        PlacementTiles.get(Y - TempTilePosition.Y()).set(X - TempTilePosition.X(), 0);
                    }
                }
            }

            if (PlacementRightX <= rect.DXPosition) {
                OnScreen = false;
            }
            else if (PlacementBottomY <= rect.DYPosition) {
                OnScreen = false;
            }
            else if (TempPosition.X() >= ScreenRightX) {
                OnScreen = false;
            }
            else if (TempPosition.Y() >= ScreenBottomY) {
                OnScreen = false;
            }
            if (OnScreen) {
                int XPos, YPos;
                TempPosition.X(TempPosition.X() - rect.DXPosition);
                TempPosition.Y(TempPosition.Y() - rect.DYPosition);
                DTilesets.get(type.ordinal()).DrawTile(drawable, TempPosition.X(), TempPosition.Y
                        (), DPlaceIndices.get(type.ordinal()).get(0), DPlayerData.Color().ordinal() - 1);
                XPos = TempPosition.X();
                YPos = TempPosition.Y();
                for (ArrayList<Integer> Row : PlacementTiles) {
                    for (Integer Cell : Row) {
                        DMarkerTileset.DrawTile(drawable, XPos, YPos, Cell > 0 ? DPlaceGoodIndex : DPlaceBadIndex);
                        XPos += DMarkerTileset.TileWidth();
                    }
                    YPos += DMarkerTileset.TileHeight();
                    XPos = TempPosition.X();
                }
            }
        }
    }

}