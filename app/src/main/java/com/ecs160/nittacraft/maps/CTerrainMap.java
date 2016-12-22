package com.ecs160.nittacraft.maps;

import android.util.Log;

import com.ecs160.nittacraft.CDataSource;
import com.ecs160.nittacraft.CLineDataSource;
import com.ecs160.nittacraft.CPosition;

import java.util.ArrayList;

public class CTerrainMap {
    public enum ETileType {
        ttNone,
        ttGrass,
        ttDirt,
        ttRock,
        ttTree,
        ttStump,
        ttSeedling,
        ttAdolescent,
        ttWater,
        ttWall,
        ttWallDamaged,
        ttRubble,
        ttMax
    }

    public ArrayList<ArrayList<ETileType>> DMap = new ArrayList<>();
    public ArrayList<String> DStringMap = new ArrayList<>();
    public int DPlayerCount;
    public String DMapName;
    public CLineDataSource LineSource;

    public CTerrainMap() {}

    public CTerrainMap(CTerrainMap map) {
        DMap = map.DMap;
        DStringMap = map.DStringMap;
        DPlayerCount = map.DPlayerCount;
        DMapName = map.DMapName;
    }

    public CTerrainMap SetEqual(CTerrainMap map) {
        if (this != map) {
            DMap = map.DMap;
            DStringMap = map.DStringMap;
            DPlayerCount = map.DPlayerCount;
            DMapName = map.DMapName;
        }
        return this;
    }

    public String MapName() {
        return DMapName;
    }

    public int Width() {
        if (DMap.size() > 0) {
            return DMap.get(0).size() - 2;
        }
        return 0;
    }

    public int Height() {
        return DMap.size() - 2;
    }

    public CPosition FindNearestTileType(CPosition pos, ETileType type) {
        int MaxDistance = Width() > Height() ? Width() : Height();
        int XOffset = pos.X() + 1;
        int YOffset = pos.Y() + 1;

        for (int SearchDistance = 0; SearchDistance < MaxDistance; SearchDistance++) {
            int PositiveX = XOffset + SearchDistance;
            int NegativeX = XOffset - SearchDistance;
            int PositiveY = YOffset + SearchDistance;
            int NegativeY = YOffset - SearchDistance;
            Boolean SearchPX = true;
            Boolean SearchNX = true;
            Boolean SearchPY = true;
            Boolean SearchNY = true;

            if (0 >= NegativeX) {
                NegativeX = 1;
                SearchNX = false;
            }
            if (PositiveX + 1 >= DMap.get(0).size()) {
                PositiveX = DMap.get(0).size() - 2;
                SearchPX = false;
            }
            if (0 >= NegativeY) {
                NegativeY = 1;
                SearchNY = false;
            }
            if (PositiveY + 1 >= DMap.size()) {
                PositiveY = DMap.size() - 2;
                SearchPY = false;
            }
            if (!SearchNX && !SearchNY && !SearchPX && !SearchPY) {
                break;
            }
            if (SearchNY) {
                for (int XOff = NegativeX; XOff <= PositiveX; XOff++) {
                    if (type == DMap.get(NegativeY).get(XOff)) {
                        return new CPosition(XOff-1, NegativeY-1);
                    }
                }
            }
            if (SearchPX) {
                for (int YOff = NegativeY; YOff <= PositiveY; YOff++) {
                    if (type == DMap.get(YOff).get(PositiveX)) {
                        return new CPosition(PositiveX-1, YOff-1);
                    }
                }
            }
            if (SearchPY) {
                for (int XOff = PositiveX; XOff >= NegativeX; XOff--) {
                    if (type == DMap.get(PositiveY).get(XOff)) {
                        return new CPosition(XOff-1, PositiveY-1);
                    }
                }
            }
            if (SearchNX) {
                for (int YOff = PositiveY; YOff >= NegativeY; YOff--) {
                    if (type == DMap.get(YOff).get(NegativeX)) {
                        return new CPosition(NegativeX-1, YOff-1);
                    }
                }
            }
        }
        return new CPosition(-1, -1);
    }

    public ETileType TileType(int xindex, int yindex) {
        if ((-1 > xindex) || (-1 > yindex)) {
            return ETileType.ttNone;
        }
        if (DMap.size() <= yindex+1) {
            return ETileType.ttNone;
        }
        if (DMap.get(yindex+1).size() <= xindex+1) {
            return ETileType.ttNone;
        }
        return DMap.get(yindex+1).get(xindex+1);
    }

    public ETileType TileType(CPosition pos) {
        return TileType(pos.X(), pos.Y());
    }

    public void ChangeTileType(int xindex, int yindex, ETileType type) {
        if ((-1 > xindex) || (-1 > yindex)) {
            return;
        }
        if (DMap.size() <= yindex+1) {
            return;
        }
        if (DMap.get(yindex+1).size() <= xindex+1) {
            return;
        }
        DMap.get(yindex+1).set(xindex+1, type);
    }

    public void ChangeTileType(CPosition pos, ETileType type) {
        ChangeTileType(pos.X(), pos.Y(), type);
    }

    public Boolean LoadMap(CDataSource source) {
        LineSource = new CLineDataSource(source);
        StringBuffer TempString = new StringBuffer("");
        ArrayList<String> Tokens = new ArrayList<String>();
        int MapWidth, MapHeight;
        boolean ReturnStatus = false;
        //int PlayerColorsFound = 0;

        //DMap.clear();

        if (!LineSource.Read(TempString)) {
            Log.e("CTerrainMap", "Failed to read map line.");
            return ReturnStatus;
        }
        //Using a string buffer to return the string value and setting manually afterwards
        DMapName = TempString.toString();

        if (!LineSource.Read(TempString)) {
            Log.e("CTerrainMap", "Failed to read map line.");
            return ReturnStatus;
        }
//        CTokenizer.Tokenize(Tokens, TempString);
        //Manual tokenizing to parse into integers
        String[] strArray = TempString.toString().split(" ");

//        if (2 != Tokens.size()) {
////            CDebug.PrintError("Invalid map dimensions %s (%zd).\n",TempString, Tokens.size());
//            for (int I = 0; I < Tokens.size(); I++) {
////                CDebug.PrintError("Tokens[%d] = %s\n",I,Tokens.get(I));
//            }
//            return ReturnStatus;
//        }
//        try {
            //replaced Tokens.get()

        MapWidth = Integer.parseInt(strArray[0]);
        MapHeight = Integer.parseInt(strArray[1]);

//            CDebug.PrintDebug(DEBUG_LOW, "Map %s is %d x %d\n", DMapName, MapWidth, MapHeight);
        if ((8 > MapWidth) || (8 > MapHeight)) {
            Log.e("CTerrainMap", "Invalid map dimensions.");
            return ReturnStatus;
        }

        while (DStringMap.size() < MapHeight + 2) {
            if (!LineSource.Read(TempString)) {
                Log.e("CTerrainMap", "Failed to read map line.");
                return ReturnStatus;
            }
            DStringMap.add(TempString.toString());
            if (MapWidth + 2 > DStringMap.get(DStringMap.size()-1).length()) {
                Log.e("CTerrainMap", "Map line " + Integer.toString(DStringMap.size()) + " too " +
                        "short!");
                return ReturnStatus;
            }
        }
        if (MapHeight + 2 > DStringMap.size()) {
            Log.e("CTerrainMap", "Map has too few lines!");
            return ReturnStatus;
        }

        // DMap.resize(MapHeight+2); Resizing not needed
        for (int Index = 0; Index < DStringMap.size(); Index++) {
            ArrayList<ETileType> newLine = new ArrayList<>();
            DMap.add(newLine);  //Resizing maybe needed?!
            for (int Inner = 0; Inner < MapWidth+2; Inner++) {

                switch(DStringMap.get(Index).charAt(Inner)) {
                    case 'G':
                        DMap.get(Index).add(ETileType.ttGrass);
                        break;
                    case 'F':
                        DMap.get(Index).add(ETileType.ttTree);
                        break;
                    case 'D':
                        DMap.get(Index).add(ETileType.ttDirt);
                        break;
                    case 'W':
                        DMap.get(Index).add(ETileType.ttWall);
                        break;
                    case 'w':
                        DMap.get(Index).add(ETileType.ttWallDamaged);
                        break;
                    case 'R':
                        DMap.get(Index).add(ETileType.ttRock);
                        break;
                    case ' ':
                        DMap.get(Index).add(ETileType.ttWater);
                        break;
                    default:
                        Log.e("CTerrainMap", "Unknown tile type " + DStringMap.get(Index).charAt
                                (Index) + " on line " + Integer.toString(Index + 2));
                        return false;
                }
            }
        }
        return true;
    }
}