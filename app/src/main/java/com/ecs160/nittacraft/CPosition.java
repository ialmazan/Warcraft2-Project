package com.ecs160.nittacraft;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dEast;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dMax;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dNorth;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dNorthEast;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dNorthWest;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dSouth;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dSouthEast;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dSouthWest;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dWest;

public class CPosition {
    private int DX;
    private int DY;
    protected static int DTileWidth = 1;
    protected static int DTileHeight = 1;
    protected static int DHalfTileWidth = 0;
    protected static int DHalfTileHeight = 0;
    static protected ArrayList<ArrayList<CGameDataTypes.EDirection>> DOctant;
    static protected ArrayList<ArrayList<CGameDataTypes.EDirection>> DTileDirections = new
            ArrayList<>();
    static {
        /* Static block used to initialize static members */
        // asList() used even for one value -- leaves it mutable
//        DOctant.add(new ArrayList<>(Arrays.asList(dMax)));
        DTileDirections.add(new ArrayList<>(Arrays.asList(dNorthWest, dNorth, dNorthEast)));
        DTileDirections.add(new ArrayList<>(Arrays.asList(dWest, dMax, dEast)));
        DTileDirections.add(new ArrayList<>(Arrays.asList(dSouthWest, dSouth, dSouthEast)));
    }

    public CPosition() {}

    public CPosition(int x, int y) {
        DX = x;
        DY = y;
    }

    public CPosition(CPosition pos) {
        DX = pos.DX;
        DY = pos.DY;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof CPosition)) {
            return false;
        }

        CPosition pos = (CPosition)o;
        return (DX == pos.DX) && (DY == pos.DY);
    }

    public static void SetTileDimensions(int width, int height) {
        if ((0 < width) && (0 < height)) {
            DTileWidth = width;
            DTileHeight = height;
            DHalfTileWidth = width / 2;
            DHalfTileHeight = height / 2;
//            Log.d("TileDim", "WxH" + DTileWidth+ " "+  DTileHeight);

            DOctant = new ArrayList<>();
            for (int i = 0; i < DTileHeight; i++) {
                ArrayList<CGameDataTypes.EDirection> tempRow = new ArrayList<>();
                for (int j = 0; j < DTileWidth; j++) {
                    tempRow.add(dMax);
                }
                DOctant.add(tempRow);
            }

            for (int Y = 0; Y < DTileHeight; Y++) {
                for (int X = 0; X < DTileWidth; X++) {
                    int XDistance = X - DHalfTileWidth;
                    int YDistance = Y - DHalfTileHeight;
                    Boolean NegativeX = XDistance < 0;
                    Boolean NegativeY = YDistance > 0; // Top of screen is 0
                    double SinSquared;

                    XDistance *= XDistance;
                    YDistance *= YDistance;

                    if (0 == (XDistance + YDistance)) {
                        DOctant.get(Y).set(X, dMax);
                        continue;
                    }
                    SinSquared = (double)YDistance / (double)(XDistance + YDistance);
//                    Log.d("Math", "Sin" + String.valueOf(SinSquared));
                    if (0.1464466094 > SinSquared) {
                        // East or West
                        if (NegativeX) {
                            DOctant.get(Y).set(X, dWest); // West
                        } else {
                            DOctant.get(Y).set(X, dEast); // East
                        }
                    } else if (0.85355339059 > SinSquared) {
                        // NE, SE, SW, NW
                        if (NegativeY) {
                            if (NegativeX) {
                                DOctant.get(Y).set(X, dSouthWest); // SW
                            } else {
                                DOctant.get(Y).set(X, dSouthEast); // SE
                            }
                        } else if (NegativeX) {
                            DOctant.get(Y).set(X, dNorthWest); // NW
                        } else {
                            DOctant.get(Y).set(X, dNorthEast); // NE
                        }

                    } else {
                        // North or South
                        if (NegativeY) {
                            DOctant.get(Y).set(X, dSouth); // South
                        } else {
                            DOctant.get(Y).set(X, dNorth); // North
                        }
                    }
                }
            }
//            Log.d("TileDim", "DOctant Row x Col" + DOctant.size()+ " "+  DOctant.get(0).size());

        /*
        for (auto &Row : DOctant) {
            for (auto &Cell : Row) {
                printf("%d ", Cell);
            }
            printf("\n");

        }
        */
        }
    }

    public static int TileWidth() {
        return DTileWidth;
    }

    public static int TileHeight() {
        return DTileHeight;
    }

    /// Returns half the width of a tile.
    public static int HalfTileWidth() {
        return DHalfTileWidth;
    };

    /// Returns half the height of a tile.
    public static int HalfTileHeight() {
        return DHalfTileHeight;
    };

    /// Returns the x-coord of this Position.
    public int X() {
        return DX;
    };

    /// Sets the x-coord of this Position.
    public int X(int x) {
        return DX = x;
    };

    public int IncrementX(int x) {
        DX += x;
        return DX;
    }

    public int DecrementX(int x) {
        DX -= x;
        return DX;
    }

    public int Y() {
        return DY;
    }

    public int Y(int y) {
        return DY = y;
    }

    public int IncrementY(int y) {
        DY += y;
        return DY;
    }

    public int DecrementY(int y) {
        DY -= y;
        return DY;
    }

    public void SetFromTile(CPosition pos) {
        DX = pos.DX * DTileWidth + DHalfTileWidth;
        DY = pos.DY * DTileHeight + DHalfTileHeight;
    }

    public void SetXFromTile(int x) {
        DX = x * DTileWidth + DHalfTileWidth;
    }

    public void SetYFromTile(int y) {
        DY = y * DTileHeight + DHalfTileHeight;
    }

    public void SetToTile(CPosition pos) {
        DX = pos.DX / DTileWidth;
        DY = pos.DY / DTileHeight;
    }

    public void SetXToTile(int x) {
        DX = x / DTileWidth;
    }

    public void SetYToTile(int y) {
        DY = y / DTileHeight;
    }

    public boolean TileAligned() {
        return ((DX % DTileWidth) == DHalfTileWidth) && ((DY % DTileHeight) == DHalfTileHeight);
    }

    public CGameDataTypes.EDirection TileOctant() {
        return DOctant.get(DY % DTileHeight).get(DX % DTileWidth);
    }

    public CGameDataTypes.EDirection AdjacentTileDirection(CPosition pos, int objSize) {
        if (1 == objSize) {
            int DeltaX = pos.DX - DX;
            int DeltaY = pos.DY - DY;

            if ((1 < (DeltaX * DeltaX)) || (1 < (DeltaY * DeltaY))) {
                return dMax;
            }

            return DTileDirections.get(DeltaY + 1).get(DeltaX + 1);
        } else {
            CPosition ThisPosition = new CPosition();
            CPosition TargetPosition = new CPosition();

            ThisPosition.SetFromTile(this);
            TargetPosition.SetFromTile(pos);

            TargetPosition.SetToTile( ThisPosition.ClosestPosition(TargetPosition, objSize));
            return AdjacentTileDirection(TargetPosition, 1);
        }
    }

    public CPosition ClosestPosition(CPosition objpos, int objsize) {
        CPosition CurPosition = new CPosition(objpos);
        CPosition BestPosition = new CPosition();
        int BestDistance = -1;
//        Log.d("CPOS", "Closest position to object of size: " + objsize);
        for (int YIndex = 0; YIndex < objsize; YIndex++) {
            for (int XIndex = 0; XIndex < objsize; XIndex++) {
                int CurDistance = CurPosition.DistanceSquared(this);
                if ((-1 == BestDistance) || (CurDistance < BestDistance)) {
                    BestDistance = CurDistance;
                    BestPosition = CurPosition;
                }
                CurPosition.IncrementX(TileWidth());
            }
            CurPosition.X(objpos.X());
            CurPosition.IncrementY(TileHeight());
        }
        return BestPosition;
    }

    public CGameDataTypes.EDirection DirectionTo(CPosition pos) {
        CPosition DeltaPosition = new CPosition(pos.DX - DX, pos.DY - DY);
        int DivX = DeltaPosition.DX / HalfTileWidth();
        int DivY = DeltaPosition.DY / HalfTileHeight();
        int Div;
        DivX = 0 > DivX ? -DivX : DivX;
        DivY = 0 > DivY ? -DivY : DivY;
        Div = DivX > DivY ? DivX : DivY;

        if (Div > 0) {
            DeltaPosition.DX /= Div;
            DeltaPosition.DY /= Div;
        }
        DeltaPosition.DX += HalfTileWidth();
        DeltaPosition.DY += HalfTileHeight();
        if (0 > DeltaPosition.DX) {
            DeltaPosition.DX = 0;
        }
        if (0 > DeltaPosition.DY) {
            DeltaPosition.DY = 0;
        }
        if (TileWidth() <= DeltaPosition.DX) {
            DeltaPosition.DX = TileWidth() - 1;
        }
        if (TileHeight() <= DeltaPosition.DY) {
            DeltaPosition.DY = TileHeight() - 1;
        }
        return DeltaPosition.TileOctant();
    }

    public int DistanceSquared(CPosition pos) {
        int DeltaX = pos.DX - DX;
        int DeltaY = pos.DY - DY;

        return DeltaX * DeltaX + DeltaY * DeltaY;
    }

    public int Distance(CPosition pos) {
        return (int) Math.sqrt(DistanceSquared(pos));
        /* TODO: built in Java function better?
        // Code from http://www.codecodex.com/wiki/Calculate_an_integer_square_root
        unsigned int Op, Result, One;
        Op = DistanceSquared(pos);
        Result = 0;
        One = 1 << (sizeof(unsigned int) * 8 - 2);
        while (One > Op) {
            One >>= 2;
        }
        while (0 != One) {
            if (Op >= Result + One) {
                Op -= Result + One;
                Result += One << 1;  // <-- faster than 2 * one
            }
            Result >>= 1;
            One >>= 2;
        }
        return Result;
        */
    }
}