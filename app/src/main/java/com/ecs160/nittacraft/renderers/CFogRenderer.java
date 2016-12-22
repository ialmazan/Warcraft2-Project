package com.ecs160.nittacraft.renderers;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.ecs160.nittacraft.CGraphicTileset;
import com.ecs160.nittacraft.maps.CVisibilityMap;
import com.ecs160.nittacraft.SRectangle;

import java.util.ArrayList;

public class CFogRenderer {

    private CGraphicTileset DTileset;
    protected CVisibilityMap DMap;
    private int DNoneIndex;
    private int DSeenIndex;
    private int DPartialIndex;
    private ArrayList<Integer> DFogIndices= new ArrayList<>();
    private ArrayList<Integer> DBlackIndices = new ArrayList<>();
    private static ArrayList<Boolean> UnknownFog = new ArrayList<>();
    private static ArrayList<Boolean> UnknownBlack = new ArrayList<>();

    static int HammingDistance(int v1, int v2) {
        int Delta = v1 ^ v2;
        int Distance = 0;

        while (0 > Delta) {
            if (0 > (Delta & 0x01)) {
                Distance++;
            }
            Delta >>= 1;
        }

        return Distance;
    }

    public CFogRenderer(CGraphicTileset tileset, CVisibilityMap map) {

        int Index, VisibleIndex, NextIndex;
        ArrayList<Integer> OriginalValues = new ArrayList<>();
        OriginalValues.add(0x0B);
        OriginalValues.add(0x16);
        OriginalValues.add(0xD0);
        OriginalValues.add(0x68);
        OriginalValues.add(0x07);
        OriginalValues.add(0x94);
        OriginalValues.add(0xE0);
        OriginalValues.add(0x29);
        OriginalValues.add(0x03);
        OriginalValues.add(0x06);
        OriginalValues.add(0x14);
        OriginalValues.add(0x90);
        OriginalValues.add(0x60);
        OriginalValues.add(0xC0);
        OriginalValues.add(0x09);
        OriginalValues.add(0x28);
        OriginalValues.add(0x01);
        OriginalValues.add(0x02);
        OriginalValues.add(0x04);
        OriginalValues.add(0x10);
        OriginalValues.add(0x80);
        OriginalValues.add(0x40);
        OriginalValues.add(0x20);
        OriginalValues.add(0x08);

        DTileset = tileset;
        DMap = map;
        VisibleIndex = DTileset.FindTile("visible");
        DNoneIndex = DTileset.FindTile("none");
        DSeenIndex = DTileset.FindTile("seen");
        DPartialIndex = DTileset.FindTile("partial");

        for (Index = 0; Index < 0x100; Index++) {
            DFogIndices.add(DTileset.FindTile("pf-" + Integer.toString(Index)));
        }

        DFogIndices.set(0x00, DSeenIndex);
        DFogIndices.set(0x03, DFogIndices.get(0x07));
        DFogIndices.set(0x06, DFogIndices.get(0x07));
        DFogIndices.set(0x14, DFogIndices.get(0x94));
        DFogIndices.set(0x90, DFogIndices.get(0x94));
        DFogIndices.set(0x60, DFogIndices.get(0xE0));
        DFogIndices.set(0xC0, DFogIndices.get(0xE0));
        DFogIndices.set(0x09, DFogIndices.get(0x29));
        DFogIndices.set(0x28, DFogIndices.get(0x29));

    /* Commented @ Linux Code
    DFogIndices[0x00] = DSeenIndex;
    DFogIndices[0x03] = DFogIndices[0x07];
    DFogIndices[0x06] = DFogIndices[0x07];

    DFogIndices[0x0F] = DFogIndices[0x0B];
    DFogIndices[0x2B] = DFogIndices[0x0B];
    DFogIndices[0x2F] = DFogIndices[0x0B];

    DFogIndices[0x17] = DFogIndices[0x16];
    DFogIndices[0x96] = DFogIndices[0x16];
    DFogIndices[0x97] = DFogIndices[0x16];

    DFogIndices[0x09] = DFogIndices[0x29];
    DFogIndices[0x28] = DFogIndices[0x29];

    DFogIndices[0x69] = DFogIndices[0x68];
    DFogIndices[0xE8] = DFogIndices[0x68];
    DFogIndices[0xE9] = DFogIndices[0x68];

    DFogIndices[0x14] = DFogIndices[0x94];
    DFogIndices[0x90] = DFogIndices[0x94];

    DFogIndices[0xD4] = DFogIndices[0xD0];
    DFogIndices[0xF0] = DFogIndices[0xD0];
    DFogIndices[0xF4] = DFogIndices[0xD0];

    DFogIndices[0x60] = DFogIndices[0xE0];
    DFogIndices[0xC0] = DFogIndices[0xE0];
    */

        for (Index = 0; Index < 0x100; Index++) {
            DBlackIndices.add(DTileset.FindTile("pb-" + Integer.toString(Index)));
        }
        DBlackIndices.set(0x00, DNoneIndex);
        DBlackIndices.set(0x03, DBlackIndices.get(0x07));
        DBlackIndices.set(0x06, DBlackIndices.get(0x07));
        DBlackIndices.set(0x14, DBlackIndices.get(0x94));
        DBlackIndices.set(0x90, DBlackIndices.get(0x94));
        DBlackIndices.set(0x60, DBlackIndices.get(0xE0));
        DBlackIndices.set(0xC0, DBlackIndices.get(0xE0));
        DBlackIndices.set(0x09, DBlackIndices.get(0x29));
        DBlackIndices.set(0x28, DBlackIndices.get(0x29));
    /* Commented from Linux Code
    DBlackIndices[0xA7] = VisibleIndex;
    DBlackIndices[0xAD] = VisibleIndex;
    DBlackIndices[0xB5] = VisibleIndex;
    DBlackIndices[0xE5] = VisibleIndex;


    DBlackIndices[0x00] = DNoneIndex;
    DBlackIndices[0x03] = DBlackIndices[0x07];
    DBlackIndices[0x06] = DBlackIndices[0x07];

    DBlackIndices[0x0F] = DBlackIndices[0x0B];
    DBlackIndices[0x2B] = DBlackIndices[0x0B];
    DBlackIndices[0x2F] = DBlackIndices[0x0B];

    DBlackIndices[0x17] = DBlackIndices[0x16];
    DBlackIndices[0x96] = DBlackIndices[0x16];
    DBlackIndices[0x97] = DBlackIndices[0x16];

    DBlackIndices[0x09] = DBlackIndices[0x29];
    DBlackIndices[0x28] = DBlackIndices[0x29];

    DBlackIndices[0x69] = DBlackIndices[0x68];
    DBlackIndices[0xE8] = DBlackIndices[0x68];
    DBlackIndices[0xE9] = DBlackIndices[0x68];

    DBlackIndices[0x14] = DBlackIndices[0x94];
    DBlackIndices[0x90] = DBlackIndices[0x94];

    DBlackIndices[0xD4] = DBlackIndices[0xD0];
    DBlackIndices[0xF0] = DBlackIndices[0xD0];
    DBlackIndices[0xF4] = DBlackIndices[0xD0];

    DBlackIndices[0x60] = DBlackIndices[0xE0];
    DBlackIndices[0xC0] = DBlackIndices[0xE0];
    */
//This code block is probably responsible for making the edge tiles select correctly.
//Currently gets stuck in some sort of infinite loop.  Duplicate tile calls also don't work
//since we don't have a way of memcopying the tile segments into new locations?


//        DTileset.TileCount(DTileset.TileCount() + (0x100 - OriginalValues.size()) * 2);
//        for (int AllowedHamming = 1; AllowedHamming < 8; AllowedHamming++) {
//            for (int Value = 0; Value < 0x100; Value++) {
//                Log.d("CFR-Fog", "In a largish nested set of for loops");
//                if (-1 == DFogIndices.get(Value)) {
//                    int BestMatch = -1;
//                    int BestHamming = 8;
//
//                    for (Integer Orig : OriginalValues) {
//                        int CurHamming = HammingDistance(Orig, Value);
//
//                        if (CurHamming == HammingDistance(0, ~Orig & Value)) {
//                            if (CurHamming < BestHamming) {
//                                BestHamming = CurHamming;
//                                BestMatch = Orig;
//                            }
//                        }
//                    }
//
//                    if (BestHamming <= AllowedHamming) {
//                        int CurVal;
//                        DTileset.DuplicateTile(NextIndex, "pf-" + Integer.toString(Value), DFogIndices.get(BestMatch));
//                        DFogIndices.set(Value, NextIndex);
//                        DTileset.DuplicateTile(NextIndex+1, "pb-" + Integer.toString(Value), DBlackIndices.get(BestMatch));
//                        DBlackIndices.set(Value, NextIndex+1);
//                        CurVal = Value & ~BestMatch;
//                        BestMatch = -1;
//                        BestHamming = 8;
//                        while (0 < CurVal) {
//                            for (Integer Orig : OriginalValues) {
//                                int CurHamming = HammingDistance(Orig, CurVal);
//
//                                if (CurHamming == HammingDistance(0, ~Orig & CurVal)) {
//                                    if (CurHamming < BestHamming) {
//                                        BestHamming = CurHamming;
//                                        BestMatch = Orig;
//                                    }
//                                }
//                            }
//                            DTileset.OrAlphaTile(NextIndex, DFogIndices.get(BestMatch));
//                            DTileset.OrAlphaTile(NextIndex+1, DBlackIndices.get(BestMatch));
//                            CurVal &= ~BestMatch;
//                        }
//                        NextIndex += 2;
//                    }
//                }
//            }
//        }
    }


    void DrawMap(Canvas drawable, SRectangle rect) {
        Paint TempGC = new Paint();
        int TileWidth, TileHeight;
        if (UnknownBlack.size() < 0x100) {
            for (int i = 0; i < 0x100; i++) {
                UnknownBlack.add(false);
            }
        }
        if (UnknownFog.size() < 0x100) {
            for (int i = 0; i < 0x100; i++) {
                UnknownFog.add(false);
            }
        }

        TileWidth = DTileset.TileWidth();
        TileHeight = DTileset.TileHeight();

        for (int YIndex = rect.DYPosition / TileHeight, YPos = -(rect.DYPosition % TileHeight); YPos < rect.DHeight; YIndex++, YPos += TileHeight) {
            for (int XIndex = rect.DXPosition / TileWidth, XPos = -(rect.DXPosition % TileWidth); XPos < rect.DWidth; XIndex++, XPos += TileWidth) {
                CVisibilityMap.ETileVisibility TileType = DMap.TileType(XIndex, YIndex);

                if (CVisibilityMap.ETileVisibility.tvNone == TileType) {
                    DTileset.DrawTile(drawable, XPos, YPos, DNoneIndex);
                    continue;
                }
                else if (CVisibilityMap.ETileVisibility.tvVisible == TileType) {
                    continue;
                }
                if ((CVisibilityMap.ETileVisibility.tvSeen == TileType) || (CVisibilityMap.ETileVisibility.tvSeenPartial == TileType)) {
                    DTileset.DrawTile(drawable, XPos, YPos, DSeenIndex);
                }
                if ((CVisibilityMap.ETileVisibility.tvPartialPartial == TileType) || (CVisibilityMap.ETileVisibility.tvPartial == TileType)) {
                    int VisibilityIndex = 0, VisibilityMask = 0x1;

                    for (int YOff = -1; YOff < 2; YOff++) {
                        for (int XOff = -1; XOff < 2; XOff++) {
                            if ((0 < YOff)|| (0 < XOff)) {
                                CVisibilityMap.ETileVisibility VisTile = DMap.TileType(XIndex + XOff, YIndex + YOff);

                                if (CVisibilityMap.ETileVisibility.tvVisible == VisTile) {
                                    VisibilityIndex |= VisibilityMask;
                                }
                                VisibilityMask <<= 1;
                            }
                        }
                    }

                    if (-1 == DFogIndices.get(VisibilityIndex)) {
                        if (!UnknownFog.get(VisibilityIndex)) {
                            UnknownFog.set(VisibilityIndex, true);
                        }
                    }

                    DTileset.DrawTile(drawable, XPos, YPos, DPartialIndex);  //DFogIndices.get(VisibilityIndex));
                }

                if ((CVisibilityMap.ETileVisibility.tvPartialPartial == TileType) || (CVisibilityMap.ETileVisibility.tvSeenPartial == TileType)) {
                    int VisibilityIndex = 0, VisibilityMask = 0x1;

                    for (int YOff = -1; YOff < 2; YOff++) {
                        for (int XOff = -1; XOff < 2; XOff++) {
                            if ((0 < YOff) || (0 < XOff)) {
                                CVisibilityMap.ETileVisibility VisTile = DMap.TileType(XIndex + XOff, YIndex + YOff);

                                if ((CVisibilityMap.ETileVisibility.tvVisible == VisTile) || (CVisibilityMap.ETileVisibility.tvPartial ==
                                        VisTile) || (CVisibilityMap.ETileVisibility.tvSeen ==
                                        VisTile)) {
                                    VisibilityIndex |= VisibilityMask;
                                }
                                VisibilityMask <<= 1;
                            }
                        }
                    }
                    if (-1 == DBlackIndices.get(VisibilityIndex)) {
                        if (!UnknownBlack.get(VisibilityIndex)) {
//                            printf("Unknown black 0x%02X @ (%d, %d)\n",VisibilityIndex, XIndex, YIndex);
                            UnknownBlack.set(VisibilityIndex, true);
                        }
                    }
                    DTileset.DrawTile(drawable, XPos, YPos, DSeenIndex);//DBlackIndices.get(VisibilityIndex));
                }
            }
        }
    }

    void DrawMiniMap(Canvas drawable) {
        Paint TempGC = new Paint();

        for (int YIndex = 0; YIndex < DMap.Height(); YIndex++) {
            for (int XIndex = 0; XIndex < DMap.Width(); XIndex++) {
                CVisibilityMap.ETileVisibility TileType = DMap.TileType(XIndex, YIndex);

                if (CVisibilityMap.ETileVisibility.tvNone == TileType) {
                    DTileset.DrawTileCorner(drawable, XIndex, YIndex, DNoneIndex);
                    continue;
                }
                else if (CVisibilityMap.ETileVisibility.tvVisible == TileType) {
                    continue;
                }
                else if ((CVisibilityMap.ETileVisibility.tvSeen == TileType) || (CVisibilityMap.ETileVisibility.tvSeenPartial == TileType)) {
                    DTileset.DrawTileCorner(drawable, XIndex, YIndex, DSeenIndex);
                }
                else {
                    DTileset.DrawTileCorner(drawable, XIndex, YIndex, DPartialIndex);
                }
            }
        }
    }
}
