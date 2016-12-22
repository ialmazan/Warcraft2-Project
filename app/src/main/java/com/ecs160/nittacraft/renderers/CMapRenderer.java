package com.ecs160.nittacraft.renderers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;

import com.ecs160.nittacraft.CApplicationData;
import com.ecs160.nittacraft.CGraphicTileset;
import com.ecs160.nittacraft.CPosition;
import com.ecs160.nittacraft.activities.BaseActivity;
import com.ecs160.nittacraft.maps.CTerrainMap;
import com.ecs160.nittacraft.views.GameView;

import java.util.ArrayList;

public class CMapRenderer {
    protected CGraphicTileset DTileset;
    protected CTerrainMap DMap;
    protected ArrayList< Integer > DGrassIndices;
    protected ArrayList< Integer > DTreeIndices;
    protected ArrayList< Integer > DDirtIndices;
    protected ArrayList< Integer > DWaterIndices;
    protected ArrayList< Integer > DRockIndices;
    protected ArrayList< Integer > DWallIndices;
    protected ArrayList< Integer > DWallDamagedIndices;
    protected ArrayList< Integer > DPixelIndices;
    public ArrayList<Integer> DGrowthIndices;

    protected Bitmap minimapBitmap;
    protected Bitmap mapLevel0;
    protected float scale;
    protected  float width;
    protected float height;

//    protected Map< Integer, Integer > DTreeUnknown = new HashMap< Integer, Integer >;
//    protected Map< Integer, Integer > DWaterUnknown = new HashMap< Integer, Integer >;
//    protected Map< Integer, Integer > DDirtUnknown = new HashMap< Integer, Integer >;
//    protected Map< Integer, Integer > DRockUnknown = new HashMap< Integer, Integer >;

    public CMapRenderer(CGraphicTileset tileset, CTerrainMap map) {
        int Index;
        ArrayList<Integer> Hamming = new ArrayList<>();
        DTileset = tileset;
        DMap = map;

        scale = 1;

        DGrassIndices = new ArrayList<Integer>();
        DTreeIndices = new ArrayList<Integer>();
        DDirtIndices = new ArrayList<Integer>();
        DWaterIndices = new ArrayList<Integer>();
        DRockIndices = new ArrayList<Integer>();
        DWallIndices = new ArrayList<Integer>();
        DWallDamagedIndices = new ArrayList<Integer>();
        DPixelIndices = new ArrayList<Integer>();
        DGrowthIndices = new ArrayList<>();

//        Log.d("CMapRenderer", Integer.toString(DTileset.FindPixel("grass")));
        DPixelIndices.add(0,0);
        DPixelIndices.add(CTerrainMap.ETileType.ttGrass.ordinal(), DTileset.FindPixel("grass")-1);
        DPixelIndices.add(CTerrainMap.ETileType.ttDirt.ordinal(), DTileset.FindPixel("dirt")-1);
        DPixelIndices.add(CTerrainMap.ETileType.ttRock.ordinal(), DTileset.FindPixel("rock")-1);
        DPixelIndices.add(CTerrainMap.ETileType.ttTree.ordinal(), DTileset.FindPixel("tree")-1);
        DPixelIndices.add(CTerrainMap.ETileType.ttStump.ordinal(), DTileset.FindPixel("stump")-1);
        DPixelIndices.add(CTerrainMap.ETileType.ttSeedling.ordinal(), DTileset.FindPixel("seedling")-1);
        DPixelIndices.add(CTerrainMap.ETileType.ttAdolescent.ordinal(), DTileset.FindPixel("adolescent")-1);
        DPixelIndices.add(CTerrainMap.ETileType.ttWater.ordinal(), DTileset.FindPixel("water")-1);
        DPixelIndices.add(CTerrainMap.ETileType.ttWall.ordinal(), DTileset.FindPixel("wall")-1);
        DPixelIndices.add(CTerrainMap.ETileType.ttWallDamaged.ordinal(), DTileset.FindPixel
                ("walldamaged") - 1);
        DPixelIndices.add(CTerrainMap.ETileType.ttRubble.ordinal(), DTileset.FindPixel("rubble")-1);

        Index = 0;
        while (true) {
            //Log.d("CMapRenderer", "beforevalue: " + Integer.toString(Index));
            int Value = DTileset.FindTile("grass-" + Integer.toString(Index));
            //Log.d("CMapRenderer", "value: " + Value);
            if (0 > Value) {
                break;
            }
            DGrassIndices.add(Value);
            Index++;
        }

        for (Index = 9001; Index < 9004; Index++) {
             DGrowthIndices.add(DTileset.FindTile("growth-" + Integer.toString(Index)));
        }

        for (Index = 0; Index < 0x40; Index++) {
            DTreeIndices.add(DTileset.FindTile("tree-" + Integer.toString(Index)));
        }
        DTreeIndices.set(0x0A, DTreeIndices.get(0x03));
        DTreeIndices.set(0x0B, DTreeIndices.get(0x03));
        DTreeIndices.set(0x23, DTreeIndices.get(0x03));
        DTreeIndices.set(0x2B, DTreeIndices.get(0x03));
        DTreeIndices.set(0x0E, DTreeIndices.get(0x06));
        DTreeIndices.set(0x22, DTreeIndices.get(0x06));
        DTreeIndices.set(0x26, DTreeIndices.get(0x06));
        DTreeIndices.set(0x2E, DTreeIndices.get(0x06));
        DTreeIndices.set(0x0F, DTreeIndices.get(0x07));
        DTreeIndices.set(0x27, DTreeIndices.get(0x07));
        DTreeIndices.set(0x2F, DTreeIndices.get(0x07));
        DTreeIndices.set(0x18, DTreeIndices.get(0x11));
        DTreeIndices.set(0x19, DTreeIndices.get(0x11));
        DTreeIndices.set(0x39, DTreeIndices.get(0x11));
        DTreeIndices.set(0x30, DTreeIndices.get(0x14));
        DTreeIndices.set(0x34, DTreeIndices.get(0x14));
        DTreeIndices.set(0x32, DTreeIndices.get(0x16));
        DTreeIndices.set(0x3A, DTreeIndices.get(0x1A));

        DTreeIndices.set(0x11, DTreeIndices.get(0x10));
        DTreeIndices.set(0x14, DTreeIndices.get(0x10));
        DTreeIndices.set(0x15, DTreeIndices.get(0x10));

        DTreeIndices.set(0x39, DTreeIndices.get(0x38));
        DTreeIndices.set(0x3C, DTreeIndices.get(0x38));
        DTreeIndices.set(0x3D, DTreeIndices.get(0x38));
        for (Index = 0; Index < 0x100; Index++) {
            DDirtIndices.add(DTileset.FindTile("dirt-" + Integer.toString(Index)));
        }
        // Three in a row on side
        DDirtIndices.set(0x03, DDirtIndices.get(0x02));
        DDirtIndices.set(0x05, DDirtIndices.get(0x02));
        DDirtIndices.set(0x06, DDirtIndices.get(0x02));
        DDirtIndices.set(0x07, DDirtIndices.get(0x02));

        DDirtIndices.set(0x09, DDirtIndices.get(0x08));
        DDirtIndices.set(0x21, DDirtIndices.get(0x08));
        DDirtIndices.set(0x28, DDirtIndices.get(0x08));
        DDirtIndices.set(0x29, DDirtIndices.get(0x08));

        DDirtIndices.set(0x14, DDirtIndices.get(0x10));
        DDirtIndices.set(0x84, DDirtIndices.get(0x10));
        DDirtIndices.set(0x90, DDirtIndices.get(0x10));
        DDirtIndices.set(0x94, DDirtIndices.get(0x10));

        DDirtIndices.set(0x60, DDirtIndices.get(0x40));
        DDirtIndices.set(0xA0, DDirtIndices.get(0x40));
        DDirtIndices.set(0xC0, DDirtIndices.get(0x40));
        DDirtIndices.set(0xE0, DDirtIndices.get(0x40));

        // Corner three
        DDirtIndices.set(0x0A, DDirtIndices.get(0x0B));
        DDirtIndices.set(0x0F, DDirtIndices.get(0x0B));
        DDirtIndices.set(0x25, DDirtIndices.get(0x0B));
        DDirtIndices.set(0x27, DDirtIndices.get(0x0B));
        DDirtIndices.set(0x2D, DDirtIndices.get(0x0B));
        DDirtIndices.set(0x2B, DDirtIndices.get(0x0B));
        DDirtIndices.set(0x2F, DDirtIndices.get(0x0B));

        DDirtIndices.set(0x12, DDirtIndices.get(0x16));
        DDirtIndices.set(0x17, DDirtIndices.get(0x16));
        DDirtIndices.set(0x85, DDirtIndices.get(0x16));
        DDirtIndices.set(0x87, DDirtIndices.get(0x16));
        DDirtIndices.set(0x95, DDirtIndices.get(0x16));
        DDirtIndices.set(0x96, DDirtIndices.get(0x16));
        DDirtIndices.set(0x97, DDirtIndices.get(0x16));

        DDirtIndices.set(0x48, DDirtIndices.get(0x68));
        DDirtIndices.set(0x69, DDirtIndices.get(0x68));
        DDirtIndices.set(0xA1, DDirtIndices.get(0x68));
        DDirtIndices.set(0xA9, DDirtIndices.get(0x68));
        DDirtIndices.set(0xE1, DDirtIndices.get(0x68));
        DDirtIndices.set(0xE8, DDirtIndices.get(0x68));
        DDirtIndices.set(0xE9, DDirtIndices.get(0x68));

        DDirtIndices.set(0x50, DDirtIndices.get(0xD0));
        DDirtIndices.set(0xA4, DDirtIndices.get(0xD0));
        DDirtIndices.set(0xB4, DDirtIndices.get(0xD0));
        DDirtIndices.set(0xD4, DDirtIndices.get(0xD0));
        DDirtIndices.set(0xE4, DDirtIndices.get(0xD0));
        DDirtIndices.set(0xF0, DDirtIndices.get(0xD0));
        DDirtIndices.set(0xF4, DDirtIndices.get(0xD0));

        MakeHammingSet(0x5A, Hamming);
        for (int I = 0; I < Hamming.size(); I++) {
            int Value = Hamming.get(I);
            if (-1 == DDirtIndices.get(Value | 0x24)) {
                DDirtIndices.set(Value | 0x24, DDirtIndices.get(0x7E));
            }
            if (-1 == DDirtIndices.get(Value | 0x81)) {
                DDirtIndices.set(Value | 0x81, DDirtIndices.get(0xDB));
            }
        }


        DDirtIndices.set(0x1D, DDirtIndices.get(0xFF));
        DDirtIndices.set(0x3D, DDirtIndices.get(0xFF));
        DDirtIndices.set(0x9D, DDirtIndices.get(0xFF));

        DDirtIndices.set(0x63, DDirtIndices.get(0xFF));
        DDirtIndices.set(0x67, DDirtIndices.get(0xFF));
        DDirtIndices.set(0xE3, DDirtIndices.get(0xFF));

        DDirtIndices.set(0xB8, DDirtIndices.get(0xFF));
        DDirtIndices.set(0xB9, DDirtIndices.get(0xFF));
        DDirtIndices.set(0xBC, DDirtIndices.get(0xFF));

        DDirtIndices.set(0xC6, DDirtIndices.get(0xFF));
        DDirtIndices.set(0xC7, DDirtIndices.get(0xFF));
        DDirtIndices.set(0xE6, DDirtIndices.get(0xFF));

        DDirtIndices.set(0xBD, DDirtIndices.get(0xFF));
        DDirtIndices.set(0xE7, DDirtIndices.get(0xFF));
    /*
    DDirtIndices[0x0F] = DDirtIndices[0x0B];
    DDirtIndices[0x2B] = DDirtIndices[0x0B];
    DDirtIndices[0x2F] = DDirtIndices[0x0B];
    DDirtIndices[0x17] = DDirtIndices[0x16];
    DDirtIndices[0x87] = DDirtIndices[0x16];
    DDirtIndices[0x96] = DDirtIndices[0x16];
    DDirtIndices[0x97] = DDirtIndices[0x16];
    DDirtIndices[0x69] = DDirtIndices[0x68];
    DDirtIndices[0xC8] = DDirtIndices[0x68];
    DDirtIndices[0xE8] = DDirtIndices[0x68];
    DDirtIndices[0xE9] = DDirtIndices[0x68];
    DDirtIndices[0xD4] = DDirtIndices[0xD0];
    DDirtIndices[0xF0] = DDirtIndices[0xD0];
    DDirtIndices[0xF4] = DDirtIndices[0xD0];
    DDirtIndices[0x63] = DDirtIndices[0xFF];
    DDirtIndices[0xC6] = DDirtIndices[0xFF];
    DDirtIndices[0xC7] = DDirtIndices[0xFF];
    DDirtIndices[0xE7] = DDirtIndices[0xFF];
    */
        for (Index = 0; Index < 0x100; Index++) {
            DWaterIndices.add(DTileset.FindTile("water-" + Integer.toString(Index)));
        }
        DWaterIndices.set(0x00, DDirtIndices.get(0xFF));
        DWaterIndices.set(0x01, DWaterIndices.get(0x00));
        DWaterIndices.set(0x02, DWaterIndices.get(0x00));
        DWaterIndices.set(0x04, DWaterIndices.get(0x00));
        DWaterIndices.set(0x08, DWaterIndices.get(0x00));
        DWaterIndices.set(0x10, DWaterIndices.get(0x00));
        DWaterIndices.set(0x20, DWaterIndices.get(0x00));
        DWaterIndices.set(0x40, DWaterIndices.get(0x00));
        DWaterIndices.set(0x80, DWaterIndices.get(0x00));


        // Three ground in corner
        DWaterIndices.set(0x0F, DWaterIndices.get(0x0B));
        DWaterIndices.set(0x2B, DWaterIndices.get(0x0B));
        DWaterIndices.set(0x2F, DWaterIndices.get(0x0B));

        DWaterIndices.set(0x17, DWaterIndices.get(0x16));
        DWaterIndices.set(0x96, DWaterIndices.get(0x16));
        DWaterIndices.set(0x97, DWaterIndices.get(0x16));

        DWaterIndices.set(0x69, DWaterIndices.get(0x68));
        DWaterIndices.set(0xE8, DWaterIndices.get(0x68));
        DWaterIndices.set(0xE9, DWaterIndices.get(0x68));

        DWaterIndices.set(0xD4, DWaterIndices.get(0xD0));
        DWaterIndices.set(0xF0, DWaterIndices.get(0xD0));
        DWaterIndices.set(0xF4, DWaterIndices.get(0xD0));

        // Three ground straight
        DWaterIndices.set(0x3F, DWaterIndices.get(0x1F));
        DWaterIndices.set(0x9F, DWaterIndices.get(0x1F));

        DWaterIndices.set(0x6F, DWaterIndices.get(0x6B));
        DWaterIndices.set(0xEB, DWaterIndices.get(0x6B));

        DWaterIndices.set(0xD7, DWaterIndices.get(0xD6));
        DWaterIndices.set(0xF6, DWaterIndices.get(0xD6));

        DWaterIndices.set(0xF9, DWaterIndices.get(0xF8));
        DWaterIndices.set(0xFC, DWaterIndices.get(0xF8));

        Index = 0;
        for (Index = 0; Index < 0x100; Index++) {
            int Value = DTileset.FindTile("rock-" + Integer.toString(Index));
            DRockIndices.add(Value);
        }

        // Three rocks in corner
        MakeHammingSet(0x25, Hamming);
        for (int I = 0; I < Hamming.size(); I++) {
            int Value = Hamming.get(I);
            if (-1 == DRockIndices.get(0x0A | Value)) {
                DRockIndices.set(0x0A | Value, DRockIndices.get(0x0B));
            }
        }

        MakeHammingSet(0x85, Hamming);
        for (int I = 0; I < Hamming.size(); I++) {
            int Value = Hamming.get(I);
            if (-1 == DRockIndices.get(0x12 | Value)) {
                DRockIndices.set(0x12 | Value, DRockIndices.get(0x16));
            }
        }

        MakeHammingSet(0xA1, Hamming);
        for (int I = 0; I < Hamming.size(); I++) {
            int Value = Hamming.get(I);
            if (-1 == DRockIndices.get(0x48 | Value)) {
                DRockIndices.set(0x48 | Value, DRockIndices.get(0x68));
            }
        }

        MakeHammingSet(0xA4, Hamming);
        for (int I = 0; I < Hamming.size(); I++) {
            int Value = Hamming.get(I);
            if (-1 == DRockIndices.get(0x50 | Value)) {
                DRockIndices.set(0x50 | Value, DRockIndices.get(0xD0));
            }
        }
        // Three rocks straight
        DRockIndices.set(0x1B, DRockIndices.get(0x1F));
        DRockIndices.set(0x1E, DRockIndices.get(0x1F));
        DRockIndices.set(0x3F, DRockIndices.get(0x1F));
        DRockIndices.set(0x9F, DRockIndices.get(0x1F));

        DRockIndices.set(0x6F, DRockIndices.get(0x6B));
        DRockIndices.set(0xEB, DRockIndices.get(0x6B));

        DRockIndices.set(0xD7, DRockIndices.get(0xD6));
        DRockIndices.set(0xF6, DRockIndices.get(0xD6));

        DRockIndices.set(0x78, DRockIndices.get(0xF8));
        DRockIndices.set(0xD8, DRockIndices.get(0xF8));
        DRockIndices.set(0xF9, DRockIndices.get(0xF8));
        DRockIndices.set(0xFC, DRockIndices.get(0xF8));

        // Ends
        DRockIndices.set(0x09, DRockIndices.get(0x08));
        DRockIndices.set(0x28, DRockIndices.get(0x08));
        DRockIndices.set(0x29, DRockIndices.get(0x08));

        DRockIndices.set(0x14, DRockIndices.get(0x10));
        DRockIndices.set(0x90, DRockIndices.get(0x10));
        DRockIndices.set(0x94, DRockIndices.get(0x10));

        MakeHammingSet(0xA5, Hamming);
        for (int I = 0; I < Hamming.size(); I++) {
            int Value = Hamming.get(I);
            if (-1 == DRockIndices.get(Value)) {
                DRockIndices.set(Value, DRockIndices.get(0x02));
            }
        }

        Index = 0;
        for (Index = 0; Index < 0x10; Index++) {
            int Value = DTileset.FindTile("wall-" + Integer.toString(Index));
            DWallIndices.add(Value);
        }
        Index = 0;
        for (Index = 0; Index < 0x10; Index++) {
            int Value = DTileset.FindTile("wall-damaged-" + Integer.toString(Index));
            DWallDamagedIndices.add(Value);
        }
    }

    protected void MakeHammingSet(int value, ArrayList< Integer > hammingset) {
        int BitCount, Anchor, LastEnd;
        hammingset.clear();
        for (int Index = 0; Index < 8; Index++) {
            int Value = 1<<Index;
            if ((value & Value) > 0) {
                hammingset.add(Value);
            }
        }
        LastEnd = BitCount = hammingset.size();
        Anchor = 0;
        for (int TotalBits = 1; TotalBits < BitCount; TotalBits++) {
            for (int LastIndex = Anchor; LastIndex < LastEnd; LastIndex++) {
                for (int BitIndex = 0; BitIndex < BitCount; BitIndex++) {
                    int NewValue = hammingset.get(LastIndex) | hammingset.get(BitIndex);
                    if (NewValue != hammingset.get(LastIndex)) {
                        Boolean Found = false;
                        for (int Index = LastEnd; Index < hammingset.size(); Index++) {
                            if (NewValue == hammingset.get(Index)) {
                                Found = true;
                                break;
                            }
                        }
                        if (!Found) {
                            hammingset.add(NewValue);
                        }
                    }
                }
            }
            Anchor = LastEnd + 1;
            LastEnd = hammingset.size();
        }
    }

//    protected int FindUnknown(CTerrainMap.ETileType type, int known, int unknown) {
//        int ReturnValue = -1;
//        if (CTerrainMap.ETileType.ttTree == type) {
//            Integer ValueFromKey = DTreeUnknown.get((known<<8) | unknown);
//            if (ValueFromKey != null) {
//                return ValueFromKey;
//            }
//            ArrayList< Integer > HammingSet = new ArrayList<>();
//            MakeHammingSet(unknown, HammingSet);
//            for (Integer Value : HammingSet) {
//                if (-1 != DTreeIndices.get(known | Value)) {
//                    DTreeUnknown.put(((known<<8) | unknown), DTreeIndices.get(known | Value));
//                    return DTreeIndices.get(known | Value);
//                }
//            }
//        }
//        else if (CTerrainMap.ETileType.ttWater == type) {
//            Integer ValueFromKey = DWaterUnknown.get((known<<8) | unknown);
//            if (ValueFromKey != null) {
//                return ValueFromKey;
//            }
//            ArrayList< Integer > HammingSet = new ArrayList<>();
//            MakeHammingSet(unknown, HammingSet);
//            for (Integer Value : HammingSet) {
//                if (-1 != DWaterIndices.get(known | Value)) {
//                    DWaterUnknown.put((known<<8) | unknown, DWaterIndices.get(known | Value));
//                    return DWaterIndices.get(known | Value);
//                }
//            }
//        }
//        else if (CTerrainMap.ETileType.ttDirt == type) {
//            Integer ValueFromKey = DDirtUnknown.get((known<<8) | unknown);
//            if (ValueFromKey != null) {
//                return ValueFromKey;
//            }
//            ArrayList< Integer > HammingSet = new ArrayList<>();
//            MakeHammingSet(unknown, HammingSet);
//            for (Integer Value: HammingSet) {
//                if (-1 != DDirtIndices.get(known | Value)) {
//                    DDirtUnknown.put((known<<8) | unknown, DDirtIndices.get(known | Value));
//                    return DDirtIndices.get(known | Value);
//                }
//            }
//        }
//        else if (CTerrainMap.ETileType.ttRock == type) {
//            Integer ValueFromKey = DRockUnknown.get((known<<8) | unknown);
//            if (ValueFromKey != null) {
//                return ValueFromKey;
//            }
//            ArrayList< Integer > HammingSet = new ArrayList<>();
//            MakeHammingSet(unknown, HammingSet);
//            for (Integer Value : HammingSet) {
//                if (-1 != DRockIndices.get(known | Value)) {
//                    DRockUnknown.put((known<<8) | unknown, DRockIndices.get(known | Value));
//                    return DRockIndices.get(known | Value);
//                }
//            }
//        }
//        return ReturnValue;
//    }
//
    public void DMap(CTerrainMap map) {
        DMap = map;
    }

    public int MapWidth() {
        return DMap.Width();
    }
//
    public int MapHeight() {
        return DMap.Height();
    }
//
    public int DetailedMapWidth() {
        return DMap.Width() * DTileset.TileWidth();
    }

    public int DetailedMapHeight() {
        return DMap.Height() * DTileset.TileHeight();
    }

    public void DrawMap(Canvas canvas, int level, GameView gameView) {
//        GdkDrawable drawable, GdkDrawable typedrawable,
//        GdkGC *TempGC = gdk_gc_new(drawable);
//        GdkGC *TempTypeGC = gdk_gc_new(typedrawable);
        int TileWidth, TileHeight;
//        static ArrayList< Boolean > UnknownTree = new ArrayList<>();
//        static ArrayList< Boolean > UnknownWater = new ArrayList<>();
//        static ArrayList< Boolean > UnknownDirt = new ArrayList<>();
//        static ArrayList< Boolean > UnknownRock = new ArrayList<>();
//        static HashMap< Integer, Boolean > UnknownUnknownTree, UnknownUnknownWater, UnknownUnknownDirt, UnknownUnknownRock;


//        if (UnknownTree.empty()) {
//            UnknownTree.resize(0x100);
//            UnknownWater.resize(0x100);
//            UnknownDirt.resize(0x100);
//            UnknownRock.resize(0x100);
//        }


        TileWidth = DTileset.TileWidth();
        TileHeight = DTileset.TileHeight();
        int xTiles = gameView.getWidth() / TileWidth;
        int yTiles = gameView.getHeight() / TileHeight;
        width = canvas.getWidth();
        height = canvas.getHeight();

        if (0 == level) {
//            GdkColor NoneColor;
//
//            NoneColor.pixel = 0;
//            NoneColor.red = 0;
//            NoneColor.green = 0;
//            NoneColor.blue = 0;
//            gdk_gc_set_rgb_fg_color(TempTypeGC, &NoneColor);
//            gdk_gc_set_rgb_bg_color(TempTypeGC, &NoneColor);
//            gdk_draw_rectangle(typedrawable, TempTypeGC, TRUE, 0, 0, rect.DWidth, rect.DHeight);
//            for (int YIndex = rect.DYPosition / TileHeight, YPos = -(rect.DYPosition % TileHeight); YPos < rect.DHeight; YIndex++, YPos += TileHeight) {
//                for (int XIndex = rect.DXPosition / TileWidth, XPos = -(rect.DXPosition % TileWidth); XPos < rect.DWidth; XIndex++, XPos += TileWidth) {
            int x1 = 0, y1 = 0;

            //Draws the portion of the bitmap specified by the calculate Y-value and ending at the end
            //of the frame size

            for (int ypos = 0; ypos <= yTiles; ypos++) {
                for (int xpos = 0; xpos <= xTiles; xpos++) {
//                    int XIndex = xpos;
//                    int YIndex = ypos;
                    int XIndex = (int)Math.floor(gameView.movX/CPosition.TileWidth()) + xpos;
                    int YIndex = (int)Math.floor(gameView.movY/CPosition.TileHeight()) + ypos;

//                    CPixelType PixelType(DMap.TileType(XIndex, YIndex));
                    CTerrainMap.ETileType ThisTileType = DMap.TileType(XIndex, YIndex);
                    if (CTerrainMap.ETileType.ttTree.ordinal() == ThisTileType.ordinal()) {//||(CTerrainMap::ttTree == DMap->TileType(XIndex, YIndex+1))) {
                        int TreeIndex = 0, TreeMask = 0x1, UnknownMask = 0, DisplayIndex = -1;
                        for (int YOff = 0; YOff < 2; YOff++) {
                            for (int XOff = -1; XOff < 2; XOff++) {
                                CTerrainMap.ETileType Tile = DMap.TileType(XIndex + XOff, YIndex + YOff);
                                if (CTerrainMap.ETileType.ttTree.ordinal() == Tile.ordinal()) {
                                    TreeIndex |= TreeMask;
                                }
                                else if (CTerrainMap.ETileType.ttNone.ordinal() == Tile.ordinal()) {
                                    UnknownMask |= TreeMask;
                                }
                                TreeMask <<= 1;
                            }
                        }
                        if (-1 == DTreeIndices.get(TreeIndex)) {
//                            if (!UnknownTree[TreeIndex] && !UnknownMask) {
////                                PrintError("Unknown tree 0x%02X @ (%d, %d)\n",TreeIndex, XIndex, YIndex);
//                                UnknownTree[TreeIndex] = true;
//                            }
//                            DisplayIndex = FindUnknown(CTerrainMap.ttTree, TreeIndex, UnknownMask);
                            if (-1 == DisplayIndex) {
//                                if (UnknownUnknownTree.end() == UnknownUnknownTree.find((TreeIndex<<8) | UnknownMask)) {
//                                    UnknownUnknownTree[(TreeIndex<<8) | UnknownMask] = true;
//                                    PrintError("Unknown tree 0x%02X/%02X @ (%d, %d)\n",TreeIndex, UnknownMask, XIndex, YIndex);
//                                }
                            }
                        } else {
                            DisplayIndex = DTreeIndices.get(TreeIndex);
                        }
                        if (-1 != DisplayIndex) {
                            DTileset.DrawTile(canvas, x1, y1, DisplayIndex);
//                            DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DisplayIndex, PixelType.ToPixelColor());
                        }
                    }
//                    else if (CTerrainMap.ttWater == ThisTileType) {
//                        int WaterIndex = 0, WaterMask = 0x1, UnknownMask = 0, DisplayIndex = -1;
//
//                        for (int YOff = -1; YOff < 2; YOff++) {
//                            for (int XOff = -1; XOff < 2; XOff++) {
//                                if (YOff || XOff) {
//                                    CTerrainMap.ETileType Tile = DMap.TileType(XIndex + XOff, YIndex + YOff);
//                                    if (CTerrainMap.ttWater == Tile) {
//                                        WaterIndex |= WaterMask;
//                                    }
//                                    else if (CTerrainMap.ttNone == Tile) {
//                                        UnknownMask |= WaterMask;
//                                    }
//                                    WaterMask <<= 1;
//                                }
//                            }
//                        }
//                        if (-1 == DWaterIndices[WaterIndex]) {
//                            if (!UnknownWater[WaterIndex] && !UnknownMask) {
//                                PrintError("Unknown water 0x%02X @ (%d, %d)\n",WaterIndex, XIndex, YIndex);
//                                UnknownWater[WaterIndex] = true;
//                            }
//                            DisplayIndex = FindUnknown(CTerrainMap.ttWater, WaterIndex, UnknownMask);
//                            if (-1 == DisplayIndex) {
//                                if (UnknownUnknownWater.end() == UnknownUnknownWater.find((WaterIndex<<8) | UnknownMask)) {
//                                    UnknownUnknownWater[(WaterIndex<<8) | UnknownMask] = true;
//                                    PrintError("Unknown water 0x%02X/%02X @ (%d, %d)\n",WaterIndex, UnknownMask, XIndex, YIndex);
//                                }
//                            }
//                        }
//                        else {
//                            DisplayIndex = DWaterIndices[WaterIndex];
//                        }
//                        if (-1 != DisplayIndex) {
//                            DTileset.DrawTile(drawable, TempGC, XPos, YPos, DisplayIndex);
//                            DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DisplayIndex, PixelType.ToPixelColor());
//                        }
//                    }
                    else if (CTerrainMap.ETileType.ttGrass.ordinal() == ThisTileType.ordinal()) {
//                        Log.d("CMapRenderer", "Grass loop " );
                        int OtherIndex = 0, OtherMask = 0x1, UnknownMask = 0, DisplayIndex = -1;
                        for (int YOff = -1; YOff < 2; YOff++) {
                            for (int XOff = -1; XOff < 2; XOff++) {
                                if (YOff != 0 || XOff != 0) {
                                    CTerrainMap.ETileType Tile = DMap.TileType(XIndex + XOff, YIndex + YOff);
                                    if ((CTerrainMap.ETileType.ttWater.ordinal() == Tile.ordinal
                                            ()) || (CTerrainMap.ETileType.ttDirt.ordinal() ==
                                            Tile.ordinal()) || (CTerrainMap.ETileType.ttRock.ordinal() == Tile.ordinal())) {
                                        OtherIndex |= OtherMask;
                                    }
                                    else if (CTerrainMap.ETileType.ttNone.ordinal() == Tile.ordinal()) {
                                        UnknownMask |= OtherMask;
                                    }
                                    OtherMask <<= 1;
                                }
                            }
                        }
                        if (OtherIndex != 0) {
                            if (-1 == DDirtIndices.get(OtherIndex)) {
//                                if (!UnknownDirt[OtherIndex] && !UnknownMask) {
//                                    PrintError("Unknown dirt 0x%02X @ (%d, %d)\n",OtherIndex, XIndex, YIndex);
//                                    UnknownDirt[OtherIndex] = true;
//                                }
//                                DisplayIndex = FindUnknown(CTerrainMap::ttDirt, OtherIndex, UnknownMask);
                                if (-1 == DisplayIndex) {
//                                    if (UnknownUnknownDirt.end() == UnknownUnknownDirt.find((OtherIndex<<8) | UnknownMask)) {
//                                        UnknownUnknownDirt[(OtherIndex<<8) | UnknownMask] = true;
//                                        PrintError("Unknown dirt 0x%02X/%02X @ (%d, %d)\n",OtherIndex, UnknownMask, XIndex, YIndex);
//                                    }
                                }
                            } else {
                                DisplayIndex = DDirtIndices.get(OtherIndex);
                            }

//                            DTileset.DrawTile(drawable, TempGC, XPos, YPos, DisplayIndex);

                            DTileset.DrawTile(canvas, x1, y1, DisplayIndex);
//                            DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DisplayIndex, PixelType.ToPixelColor());
                        } else {
//                            DTileset.DrawTile(drawable, TempGC, XPos, YPos, DGrassIndices[0x00]);
                            DTileset.DrawTile(canvas, x1, y1, DGrassIndices.get(0x00));
//                            DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DGrassIndices[0x00], PixelType.ToPixelColor());
                        }
                    }
                    else if (CTerrainMap.ETileType.ttRock.ordinal() == ThisTileType.ordinal()) {
                        int RockIndex = 0, RockMask = 0x1, UnknownMask = 0, DisplayIndex = -1;

                        for (int YOff = -1; YOff < 2; YOff++) {
                            for (int XOff = -1; XOff < 2; XOff++) {
                                if (YOff != 0 || XOff != 0) {
                                    CTerrainMap.ETileType Tile = DMap.TileType(XIndex + XOff, YIndex + YOff);
                                    if (CTerrainMap.ETileType.ttRock.ordinal() == Tile.ordinal()) {
                                        RockIndex |= RockMask;
                                    } else if (CTerrainMap.ETileType.ttNone.ordinal() == Tile.ordinal()) {
                                        UnknownMask |= RockMask;
                                    }
                                    RockMask <<= 1;
                                }
                            }
                        }
                        if (-1 == DRockIndices.get(RockIndex)) {
//                            if (!UnknownRock[RockIndex] && !UnknownMask) {
//                                PrintError("Unknown rock 0x%02X @ (%d, %d)\n",RockIndex, XIndex, YIndex);
//                                UnknownRock[RockIndex] = true;
//                            }
//                            DisplayIndex = FindUnknown(CTerrainMap.ttRock, RockIndex, UnknownMask);
                            if (-1 == DisplayIndex) {
//                                if (UnknownUnknownRock.end() == UnknownUnknownRock.find((RockIndex<<8) | UnknownMask)) {
//                                    UnknownUnknownRock[(RockIndex<<8) | UnknownMask] = true;
//                                    PrintError("Unknown rock 0x%02X/%02X @ (%d, %d)\n",RockIndex, UnknownMask, XIndex, YIndex);
//                                }
                            }
                        } else {
                            DisplayIndex = DRockIndices.get(RockIndex);
                        }
                        if (-1 != DisplayIndex) {
//                            DTileset.DrawTile(drawable, TempGC, XPos, YPos, DisplayIndex);
                            DTileset.DrawTile(canvas, x1, y1, DisplayIndex);
//                            DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DisplayIndex, PixelType.ToPixelColor());
                        }
                    }
                    else if ((CTerrainMap.ETileType.ttWall == ThisTileType) || (CTerrainMap.ETileType.ttWallDamaged == ThisTileType)) {
                        int WallIndex = 0, WallMask = 0x1, DisplayIndex = -1;
                        int XOffsets[] = {0, 1, 0, -1};
                        int YOffsets[] = {-1, 0, 1, 0};
                        for (int Index = 0; Index < XOffsets.length; Index++) {
                            CTerrainMap.ETileType Tile = DMap.TileType(XIndex + XOffsets[Index], YIndex + YOffsets[Index]);
                            if ((CTerrainMap.ETileType.ttWall == Tile) || (CTerrainMap.ETileType.ttWallDamaged == Tile) || (CTerrainMap.ETileType.ttRubble == Tile)) {
                                WallIndex |= WallMask;
                            }
                            WallMask <<= 1;
                        }
                        DisplayIndex = (CTerrainMap.ETileType.ttWall == ThisTileType) ? DWallIndices.get(WallIndex) : DWallDamagedIndices.get(WallIndex);
                        if (-1 != DisplayIndex) {
                            //DTileset.DrawTile(drawable, TempGC, XPos, YPos, DisplayIndex);
                            DTileset.DrawTile(canvas, x1, y1, DisplayIndex);
                           // DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DisplayIndex, PixelType.ToPixelColor());
                        }
                    }
                    else {
//                        Log.d("CMapRenderer", "Before switch : " + DMap.TileType(XIndex, YIndex));
                        switch(DMap.TileType(XIndex, YIndex)) {
                            case ttGrass:
                                DTileset.DrawTile(canvas,x1,y1,DGrassIndices.get(0x00));//DTileset.DrawTile(drawable, TempGC, XPos, YPos, DGrassIndices[0x00]);
//                                DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DGrassIndices[0x00], PixelType.ToPixelColor());
                                break;
                            case ttDirt:           DTileset.DrawTile(canvas,x1,y1,DDirtIndices.get(0xFF));//DTileset.DrawTile(drawable, TempGC, XPos, YPos, DDirtIndices[0xFF]);
//                                DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DDirtIndices[0xFF], PixelType.ToPixelColor());
                                break;
                            case ttRock:
                                DTileset.DrawTile(canvas,x1,y1,DRockIndices.get(0x00));
                                //DTileset.DrawTile(drawable, TempGC, XPos, YPos, DRockIndices[0x00]);
//                                DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DRockIndices[0x00], PixelType.ToPixelColor());
                                break;
                            case ttTree:
                                DTileset.DrawTile(canvas,x1,y1,DTreeIndices.get(0x00));
//                                DTileset.DrawTile(drawable, TempGC, XPos, YPos, DTreeIndices[0x00]);
//                                DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DTreeIndices[0x00], PixelType.ToPixelColor());
                                break;
                            case ttStump:
                                DTileset.DrawTile(canvas,x1,y1,DTreeIndices.get(0x00));
//                                DTileset.DrawTile(drawable, TempGC, XPos, YPos, DTreeIndices[0x00]);
//                                DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DTreeIndices[0x00], PixelType.ToPixelColor());
                                break;
                            case ttWater:
                                DTileset.DrawTile(canvas,x1,y1,DWaterIndices.get(0x00));
//                                DTileset.DrawTile(drawable, TempGC, XPos, YPos, DWaterIndices[0x00]);
//                                DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DWaterIndices[0x00], PixelType.ToPixelColor());
                                break;
                            case ttWall:
                                DTileset.DrawTile(canvas,x1,y1,DWallIndices.get(0x00));
//                                DTileset.DrawTile(drawable, TempGC, XPos, YPos, DWallIndices[0x00]);
//                                DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DWallIndices[0x00], PixelType.ToPixelColor());
                                break;
                            case ttWallDamaged:
                                DTileset.DrawTile(canvas,x1,y1,DWallIndices.get(0x00));
//                                DTileset.DrawTile(drawable, TempGC, XPos, YPos, DWallIndices[0x00]);
//                                DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DWallIndices[0x00], PixelType.ToPixelColor());
                                break;
                            case ttRubble:
                                DTileset.DrawTile(canvas,x1,y1,DWallIndices.get(0x00));
//                                DTileset.DrawTile(drawable, TempGC, XPos, YPos, DWallIndices[0x00]);
//                                DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DWallIndices[0x00], PixelType.ToPixelColor());
                                break;
                            case ttSeedling:
                                DTileset.DrawTile(canvas,x1,y1,DGrowthIndices.get(0x01));
                                break;
                            case ttAdolescent:
                                DTileset.DrawTile(canvas,x1,y1,DGrowthIndices.get(0x02));
                                break;
                            default:                            break;
                        }
                    }
                    x1 = x1 + TileWidth;
                }
                x1 = 0;
                y1 = y1 + TileHeight;
            }
        } // end level 0
        else {
//            for (int YIndex = rect.DYPosition / TileHeight, YPos = -(rect.DYPosition % TileHeight); YPos < rect.DHeight; YIndex++, YPos += TileHeight) {
//                for (int XIndex = rect.DXPosition / TileWidth, XPos = -(rect.DXPosition % TileWidth); XPos < rect.DWidth; XIndex++, XPos += TileWidth) {
            int x1 = 0;
            int y1 = 0;
            for (int ypos = 0; ypos <= yTiles; ypos++) {
                for (int xpos = 0; xpos <= xTiles; xpos++) {
//                    int XIndex = xpos;
//                    int YIndex = ypos;
                    int XIndex = (int)Math.floor(gameView.movX/CPosition.TileWidth()) + xpos;
                    int YIndex = (int)Math.floor(gameView.movY/CPosition.TileHeight()) + ypos;

                    if ((CTerrainMap.ETileType.ttTree.ordinal() == DMap.TileType(XIndex, YIndex+1).ordinal()) && (CTerrainMap.ETileType.ttTree
                            .ordinal() != DMap.TileType(XIndex, YIndex).ordinal())) {
//                        CPixelType PixelType(CTerrainMap.ttTree);
                        int TreeIndex = 0, TreeMask = 0x1;

                        for (int YOff = 0; YOff < 2; YOff++) {
                            for (int XOff = -1; XOff < 2; XOff++) {
                                if (CTerrainMap.ETileType.ttTree.ordinal() == DMap.TileType(XIndex + XOff, YIndex + YOff).ordinal()) {
                                    TreeIndex |= TreeMask;
                                }
                                TreeMask <<= 1;
                            }
                        }
                        DTileset.DrawTile(canvas, x1, y1, DTreeIndices.get(TreeIndex));
                       // DTileset.DrawClipped(typedrawable, TempTypeGC, XPos, YPos, DTreeIndices[TreeIndex], PixelType.ToPixelColor());
                    }
                    x1 = x1 + TileWidth;
                }
                x1 = 0;
                y1 = y1 + TileHeight;
            }
        }
//        g_object_unref(TempTypeGC);
//        g_object_unref(TempGC);
    }
//
    public void DrawMiniMap(Canvas canvas) {
        float x1 = 0f, y1 = 0f;

        if (minimapBitmap == null ||
                BaseActivity.MainData.DGameMode() != CApplicationData.EGameMode.gmBattle || CApplicationData.DMMRefresh >= 100) {
            if (minimapBitmap != null) {
                minimapBitmap.recycle();
            }
            CApplicationData.DMMRefresh = 0;
            minimapBitmap = Bitmap.createBitmap(canvas.getWidth(),canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas tmpCanvas = new Canvas(minimapBitmap);
            tmpCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            float size = (float)canvas.getWidth() / DMap.Width();
            scale = size;

            for (int YPos = 0; YPos < DMap.Height(); YPos++) {
                for (int XPos = 0; XPos < DMap.Width(); XPos++) {
                    CTerrainMap.ETileType TileType = DMap.TileType(XPos, YPos);
                    if (CTerrainMap.ETileType.ttNone.ordinal() != TileType.ordinal()) {
                        DTileset.DrawPixel(tmpCanvas, x1, y1, size, DPixelIndices.get(TileType.ordinal()));
                    }
                    x1 += size;
                }
                x1 = 0;
                y1 += size;
            }
        }
        CApplicationData.DMMRefresh++;
        canvas.drawBitmap(minimapBitmap,0,0,null);
    }

    public void DrawVisionRect(Canvas canvas) {
        float left;
        float top;
        float right;
        float bottom;
        float xScale = canvas.getWidth() / (96 * 64 - width);
        float yScale = canvas.getHeight() / (64 * 64 - height);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setARGB(255, 255, 255, 255);
        int xTiles = (int) width / DTileset.TileWidth()/2;
        int yTiles = (int) height / DTileset.TileHeight()/2;
        float xOffset = (96 * 64 - width)/xTiles;
        float yOffset = (64 * 64 - height)/yTiles;
        try {
            left = (GameView.getInstance().movX -(96 * GameView.getInstance().movX/xOffset)) * xScale;
            top = (GameView.getInstance().movY - (96 * GameView.getInstance().movY/yOffset)) * yScale;
            right = left + (width - canvas.getWidth()) * xScale;
            bottom = top + (height - canvas.getHeight()) * yScale;
            canvas.drawRect(left, top, right, bottom, paint);
        }
        catch (Exception e) {
            Log.e("VisionRect", e.getMessage());
        }
        //    paddle bound it;
//    distance * pixel scaling;
//    double rect check;
    }

    public float getScale() { return this.scale; }
}