package com.ecs160.nittacraft.maps;

import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPosition;

import java.util.ArrayList;
import java.util.List;

import static com.ecs160.nittacraft.maps.CVisibilityMap.ETileVisibility.tvNone;
import static com.ecs160.nittacraft.maps.CVisibilityMap.ETileVisibility.tvVisible;

public class CVisibilityMap {
    public enum ETileVisibility {
        tvNone,
        tvPartialPartial,
        tvPartial,
        tvVisible,
        tvSeenPartial,
        tvSeen
    }

    protected ArrayList<ArrayList<ETileVisibility>> DMap;
    private int DMaxVisibility;
    private int DTotalMapTiles;
    private int DUnseenTiles;

    public ETileVisibility TileType(int xindex, int yindex) {
        if ((-DMaxVisibility > xindex) || (-DMaxVisibility > yindex)) {
            return tvNone;
        }
        if (DMap.size() <= yindex+DMaxVisibility) {
            return tvNone;
        }
        if (DMap.get(yindex+DMaxVisibility).size() <= xindex + DMaxVisibility) {
            return tvNone;
        }
        return DMap.get(yindex+DMaxVisibility).get(xindex + DMaxVisibility);
    }


    public CVisibilityMap (int width, int height, int maxvisibility) {
        DMaxVisibility = maxvisibility;
        DMap = new ArrayList<>(height + 2 * DMaxVisibility);
        for (int rows = 0; rows < height + 2 * DMaxVisibility; rows ++) {
            ArrayList Row = new ArrayList<>();
            for (int cols = 0; cols < width + 2 * DMaxVisibility; cols ++) {
                Row.add(tvNone);
            }
            DMap.add(Row);
        }
        DTotalMapTiles = width * height;
        DUnseenTiles = DTotalMapTiles;
    }

    // TODO: Arraylists may be shallow copies
    CVisibilityMap(CVisibilityMap map) {
        DMaxVisibility = map.DMaxVisibility;
        DMap = map.DMap;
        DTotalMapTiles = map.DTotalMapTiles;
        DUnseenTiles = map.DUnseenTiles;
    }


    public int Width() {
        if (0 < DMap.size()) {
            return DMap.get(0).size() - 2 * DMaxVisibility;
        }
        return 0;
    }

    public int Height() {
        return DMap.size() - 2 * DMaxVisibility;
    }

    public int SeenPercent(int max) {
        return (max * (DTotalMapTiles - DUnseenTiles)) / DTotalMapTiles;
    }

    public void Update(List<CPlayerAsset> assets) {
        for (ArrayList<CVisibilityMap.ETileVisibility> Row : DMap) {
            for (int Index = 0; Index < Row.size(); Index++) {
                if ((tvVisible == Row.get(Index)) || (CVisibilityMap.ETileVisibility.tvPartial ==
                        Row.get(Index))) {
                    Row.set(Index, CVisibilityMap.ETileVisibility.tvSeen);
                } else if (CVisibilityMap.ETileVisibility.tvPartialPartial == Row.get(Index)) {
                    Row.set(Index, CVisibilityMap.ETileVisibility.tvSeenPartial);
                }
            }
        }
        for (CPlayerAsset WeakAsset : assets) {

            CPosition Anchor = new CPosition(WeakAsset.TilePosition());
            int Sight = WeakAsset.EffectiveSight() + WeakAsset.Size()/2;
            int SightSquared = Sight * Sight;
            Anchor.X(Anchor.X() + WeakAsset.Size()/2);
            Anchor.Y(Anchor.Y() + WeakAsset.Size()/2);
            for (int X = 0; X <= Sight; X++) {
                int XSquared = X * X;
                int XSquared1 = 0 < X ? (X - 1) * (X - 1) : 0;

                for (int Y = 0; Y <= Sight; Y++) {
                    int YSquared = Y * Y;
                    int YSquared1 = 0 < Y ? (Y - 1) * (Y - 1) : 0;

                    if ((XSquared + YSquared) < SightSquared) {
                        // Visible
                        DMap.get(Anchor.Y() - Y + DMaxVisibility).set(Anchor.X() - X +
                                DMaxVisibility, tvVisible);
                        DMap.get(Anchor.Y() - Y + DMaxVisibility).set(Anchor.X() + X +
                                DMaxVisibility, tvVisible);
                        DMap.get(Anchor.Y() + Y + DMaxVisibility).set(Anchor.X() - X +
                                DMaxVisibility, tvVisible);
                        DMap.get(Anchor.Y() + Y + DMaxVisibility).set(Anchor.X() + X +
                                DMaxVisibility, tvVisible);
                    } else if ((XSquared1 + YSquared1) < SightSquared) {
                        // Partial
                        CVisibilityMap.ETileVisibility CurVis = DMap.get(Anchor.Y() - Y +
                                DMaxVisibility).get(Anchor.X() - X + DMaxVisibility);
                        if (CVisibilityMap.ETileVisibility.tvSeen == CurVis) {
                            DMap.get(Anchor.Y() - Y + DMaxVisibility).set(Anchor.X() - X +
                                    DMaxVisibility, CVisibilityMap.ETileVisibility.tvPartial);
                        } else if ((tvNone == CurVis) || (CVisibilityMap.ETileVisibility.tvSeenPartial
                                == CurVis)) {
                            DMap.get(Anchor.Y() - Y + DMaxVisibility).set(Anchor.X() - X +
                                    DMaxVisibility, CVisibilityMap.ETileVisibility
                                    .tvPartialPartial);
                        }
                        CurVis = DMap.get(Anchor.Y() - Y + DMaxVisibility).get(Anchor.X() + X +
                                DMaxVisibility);
                        if (CVisibilityMap.ETileVisibility.tvSeen == CurVis) {
                            DMap.get(Anchor.Y() - Y + DMaxVisibility).set(Anchor.X() + X +
                                    DMaxVisibility, CVisibilityMap.ETileVisibility.tvPartial);
                        } else if ((tvNone == CurVis) || (CVisibilityMap.ETileVisibility
                                .tvSeenPartial == CurVis)) {
                            DMap.get(Anchor.Y() - Y + DMaxVisibility).set(Anchor.X() + X +
                                    DMaxVisibility, CVisibilityMap.ETileVisibility
                                    .tvPartialPartial);
                        }
                        CurVis = DMap.get(Anchor.Y() + Y + DMaxVisibility).get(Anchor.X() - X +
                                DMaxVisibility);
                        if (CVisibilityMap.ETileVisibility.tvSeen == CurVis) {
                            DMap.get(Anchor.Y() + Y + DMaxVisibility).set(Anchor.X() - X +
                                    DMaxVisibility, CVisibilityMap.ETileVisibility.tvPartial);
                        } else if ((tvNone == CurVis) || (CVisibilityMap.ETileVisibility
                                .tvSeenPartial == CurVis)) {
                            DMap.get(Anchor.Y() + Y + DMaxVisibility).set(Anchor.X() - X +
                                    DMaxVisibility, CVisibilityMap.ETileVisibility.tvPartialPartial);
                        }
                        CurVis = DMap.get(Anchor.Y() + Y + DMaxVisibility).get(Anchor.X() + X + DMaxVisibility);
                        if (CVisibilityMap.ETileVisibility.tvSeen == CurVis) {
                            DMap.get(Anchor.Y() + Y + DMaxVisibility).set(Anchor.X() + X +
                                    DMaxVisibility, CVisibilityMap.ETileVisibility.tvPartial);
                        } else if ((tvNone == CurVis) || (CVisibilityMap.ETileVisibility
                                .tvSeenPartial == CurVis)) {
                            DMap.get(Anchor.Y() + Y + DMaxVisibility).set(Anchor.X() + X +
                                    DMaxVisibility, CVisibilityMap.ETileVisibility.tvPartialPartial);
                        }
                    }
                }
            }
        }

        int MinX, MinY, MaxX, MaxY;
        MinY = DMaxVisibility;
        MaxY = DMap.size() - DMaxVisibility;
        MinX = DMaxVisibility;
        MaxX = DMap.get(0).size() - DMaxVisibility;
        DUnseenTiles = 0;
        for (int Y = MinY; Y < MaxY; Y++) {
            for (int X = MinX; X < MaxX; X++) {
                if (tvNone == DMap.get(Y).get(X)) {
                    DUnseenTiles++;
                }
            }
        }
    }
}