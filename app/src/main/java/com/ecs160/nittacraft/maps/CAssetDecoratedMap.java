package com.ecs160.nittacraft.maps;

import android.content.res.AssetManager;
import android.util.Log;

import com.ecs160.nittacraft.CDataSource;
import com.ecs160.nittacraft.CGameDataTypes;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerAssetType;
import com.ecs160.nittacraft.CPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

import static java.lang.Integer.parseInt;

public class CAssetDecoratedMap extends CTerrainMap {
    public class SAssetInitialization {
        public String DType;
        public CGameDataTypes.EPlayerColor DColor;
        public CPosition DTilePosition = new CPosition();
    }

    public class SResourceInitialization {
        public CGameDataTypes.EPlayerColor DColor;
        public int DGold;
        public int DLumber;
    }

    protected ArrayList<CPlayerAsset> DAssets = new ArrayList<>();
    public ArrayList<SAssetInitialization> DAssetInitializationList = new ArrayList<>();
    protected ArrayList<SResourceInitialization> DResourceInitializationList = new ArrayList<>();
    protected ArrayList<ArrayList<Integer>> DSearchMap = new ArrayList<>();

    protected static TreeMap<String, Integer> DMapNameTranslation = new TreeMap<>();
    protected static ArrayList<CAssetDecoratedMap> DAllMaps = new ArrayList<>();
    public static ArrayList<CPlayerAsset> DStationaryAssets = new ArrayList<>();
    public static ArrayList<CPlayerAsset> DMobileAssets = new ArrayList<>();

    public CAssetDecoratedMap() {}

    public CAssetDecoratedMap(CAssetDecoratedMap map) {
        DMap = map.DMap;
        DStringMap = map.DStringMap;
        DPlayerCount = map.DPlayerCount;
        DMapName = map.DMapName;
        DAssets = map.DAssets;
        DAssetInitializationList = map.DAssetInitializationList;
        DResourceInitializationList = map.DResourceInitializationList;
    }

    public CAssetDecoratedMap(CAssetDecoratedMap map, CGameDataTypes.EPlayerColor newColors[]) {
        DAssets = map.DAssets;
        DMap = map.DMap;
        DStringMap = map.DStringMap;
        DPlayerCount = map.DPlayerCount;
        DMapName = map.DMapName;


        for (int InitVal = 0; InitVal < map.DAssetInitializationList.size(); ++InitVal) {
            SAssetInitialization NewInitVal = map.DAssetInitializationList.get(InitVal);
            NewInitVal.DColor = newColors[NewInitVal.DColor.ordinal()];
            DAssetInitializationList.add(NewInitVal);
        }
        for (int InitVal = 0; InitVal < map.DResourceInitializationList.size(); ++InitVal) {
            SResourceInitialization NewInitVal = map.DResourceInitializationList.get(InitVal);
            NewInitVal.DColor = newColors[InitVal];
            DResourceInitializationList.add(NewInitVal);
        }
    }

    public CAssetDecoratedMap SetEqual(CAssetDecoratedMap map) {
        if (this != map) {
            super.SetEqual(map);
            DAssets = map.DAssets;
            DAssetInitializationList = map.DAssetInitializationList;
            DResourceInitializationList = map.DResourceInitializationList;
        }
        return this;
    }

    public static boolean LoadMaps(AssetManager assetmanager) {
        String[] Maps;
        CDataSource TempDataSource;
        CAssetDecoratedMap TempMap;

        if (assetmanager == null) {
            Log.e("CAssetDecoratedMap", "Failed to get asset manager.");
            return false;
        }

        // Get list of maps
        try {
            Maps = assetmanager.list("");
        } catch(IOException e) {
            Log.e("CAssetDecoratedMap", "Failed to get list of maps.");
            return false;
        }

        for (String Map : Maps) {
            try {
                if (Map.matches(".*map")) {
                    TempDataSource = new CDataSource(assetmanager.open(Map));
                } else {
                    continue;
                }
                TempMap = new CAssetDecoratedMap();
                if (!TempMap.LoadMap(TempDataSource)) {
                    Log.e("CAssetDecoratedMap", "Failed to load map.");
                    continue;
                }
                DMapNameTranslation.put(TempMap.MapName(), DAllMaps.size());
                DAllMaps.add(TempMap);
            } catch (IOException e) {
                Log.e("CAssetDecoratedMap", "Failed to open map file.");
            }
        }

        Log.d("CAssetDecoratedMap", "Maps loaded.");
        return true;
    }

    public static int FindMapIndex(String name) {
        Integer key = DMapNameTranslation.get(name);

        if (key != null) {
            return key;
        }
        return -1;
    }

    public static CAssetDecoratedMap GetMap(int index) {
        CAssetDecoratedMap NewMap = new CAssetDecoratedMap();
        if ((0 > index) || (DAllMaps.size() <= index)) {
            return NewMap;
        }
        return DAllMaps.get(index);
    }

    public static CAssetDecoratedMap DuplicateMap(int index, CGameDataTypes.EPlayerColor[]
            newColors) {
        CAssetDecoratedMap NewMap;
        if ((0 > index) || (DAllMaps.size() <= index)) {
            NewMap = new CAssetDecoratedMap();
            return NewMap;
        }
        NewMap = new CAssetDecoratedMap(DAllMaps.get(index), newColors);
        return NewMap;
    }

    /**
     * Returns the number of maps (size of DAllMaps)
     * @return int Number of maps in the ArrayList
     */
    public static int MapCount() {
        return DAllMaps.size();
    }

    public int PlayerCount() {
        return DResourceInitializationList.size() - 1;
    }

    public Boolean AddAsset(CPlayerAsset asset) {
        //Linux code
//        asset.ID(DAssetCount);
//        DAssetCount++;

        if (0 == asset.Speed()) {
            DStationaryAssets.add(asset);
        } else {
            DMobileAssets.add(asset);
        }
        DAssets.add(asset);
        return true;
    }

    public Boolean RemoveAsset(CPlayerAsset asset) {
        if (0 == asset.Speed()) {
//            CPlayerAsset begin = DStationaryAssets.get(0);
//            for (int index=0; index<DStationaryAssets.size(); index++) {
//                CPlayerAsset Asset =  DStationaryAssets.get(index);
//                if (asset == Asset) {
//                    DStationaryAssets.remove(begin+index);
//                }
//            }
            DStationaryAssets.remove(asset);
        }

        else {
//            auto begin = DMobileAssets.begin();
//            for (int index = 0; index<DMobileAssets.size(); index++) {
//                auto Asset = DMobileAssets.at(index);
//                if (asset == Asset) {
//                    DMobileAssets.erase(begin + index);
//                }
//            }
            DMobileAssets.remove(asset);
        }

        DAssets.remove(asset);
        return true;
    }

    public Boolean CanPlaceAsset(CPosition pos, int size, CPlayerAsset ignoreAsset) {
        int RightX, BottomY;
//        Log.d("CADM", "Checking Tiles");
//        Log.d("CADM", "PosXY" + pos.X() + " " + pos.Y());
        for (int YOff = 0; YOff < size; YOff++) {
            for (int XOff = 0; XOff < size; XOff++) {
                ETileType TileTerrainType = super.TileType(pos.X() + XOff, pos.Y() + YOff);
//                Log.d("CADM", "TileType" + TileTerrainType);
                if ((ETileType.ttGrass != TileTerrainType) && (ETileType.ttDirt != TileTerrainType)
                        && (ETileType.ttStump != TileTerrainType) && (ETileType.ttRubble !=
                        TileTerrainType)) {
                    return false;
                }
            }
        }
//        Log.d("CADM", "PosXY" + pos.X() + " " + pos.Y());
        RightX = pos.X() + size;
        BottomY = pos.Y() + size;
//        Log.d("CADM", "Boundaries");
        if (RightX >= Width()) {
            return false;
        }
        if (BottomY >= Height()) {
            return false;
        }
        Iterator<CPlayerAsset> Iterator = DAssets.iterator();
        while (Iterator.hasNext()) {
//            Log.d("CADM", "Searching asset Space");
            CPlayerAsset Asset = Iterator.next();
            int Offset = CGameDataTypes.EAssetType.atGoldMine == Asset.Type() ? 1 : 0;

            if (CGameDataTypes.EAssetType.atNone == Asset.Type()) {
                continue;
            }
            if (ignoreAsset == Asset) {
                continue;
            }
            if (RightX <= Asset.TilePositionX() - Offset) {
                continue;
            }
            if (pos.X() >= (Asset.TilePositionX() + Asset.Size() + Offset)) {
                continue;
            }
            if (BottomY <= Asset.TilePositionY() - Offset) {
                continue;
            }
            if (pos.Y() >= (Asset.TilePositionY() + Asset.Size() + Offset)) {
                continue;
            }
            return false;
        }
        return true;
    }

    public CPosition FindAssetPlacement(CPlayerAsset placeAsset, CPlayerAsset fromAsset,
                                        CPosition nextTileTarget) {
        int TopY, BottomY, LeftX, RightX;
        int BestDistance = -1, CurDistance;
        CPosition BestPosition = new CPosition(-1, -1);
        TopY = fromAsset.TilePositionY() - placeAsset.Size();
        BottomY = fromAsset.TilePositionY() + fromAsset.Size();
        LeftX = fromAsset.TilePositionX() - placeAsset.Size();
        RightX = fromAsset.TilePositionX() + fromAsset.Size();
        while (true) {
            int Skipped = 0;
            if (0 <= TopY) {
                int ToX = Math.min(RightX, Width() - 1);

                for (int CurX = Math.max(LeftX, 0); CurX <= ToX; CurX++) {
                    if (CanPlaceAsset(new CPosition(CurX, TopY), placeAsset.Size(), placeAsset)) {
                        CPosition TempPosition = new CPosition(CurX, TopY);
                        CurDistance = TempPosition.DistanceSquared(nextTileTarget);
                        if ((-1 == BestDistance) || (CurDistance < BestDistance)) {
                            BestDistance = CurDistance;
                            BestPosition = TempPosition;
                        }
                    }
                }
            } else {
                Skipped++;
            }
            if (Width() > RightX) {
                int ToY = Math.min(BottomY, Height() - 1);

                for (int CurY = Math.max(TopY, 0); CurY <= ToY; CurY++) {
                    if (CanPlaceAsset(new CPosition(RightX, CurY), placeAsset.Size(), placeAsset)) {
                        CPosition TempPosition = new CPosition(RightX, CurY);
                        CurDistance = TempPosition.DistanceSquared(nextTileTarget);
                        if ((-1 == BestDistance) || (CurDistance < BestDistance)) {
                            BestDistance = CurDistance;
                            BestPosition = TempPosition;
                        }
                    }
                }
            } else {
                Skipped++;
            }
            if (Height() > BottomY) {
                int ToX = Math.max(LeftX, 0);

                for (int CurX = Math.min(RightX, Width() - 1); CurX >= ToX; CurX--) {
                    if (CanPlaceAsset(new CPosition(CurX, BottomY), placeAsset.Size(),
                            placeAsset)) {

                        CPosition TempPosition = new CPosition(CurX, BottomY);
                        CurDistance = TempPosition.DistanceSquared(nextTileTarget);
                        if ((-1 == BestDistance) || (CurDistance < BestDistance)) {
                            BestDistance = CurDistance;
                            BestPosition = TempPosition;
                        }
                    }
                }
            } else {
                Skipped++;
            }
            if (0 <= LeftX) {
                int ToY = Math.max(TopY, 0);

                for (int CurY = Math.min(BottomY, Height() - 1); CurY >= ToY; CurY--) {
                    if (CanPlaceAsset(new CPosition(LeftX, CurY), placeAsset.Size(), placeAsset)) {
                        CPosition TempPosition = new CPosition(LeftX, CurY);
                        CurDistance = TempPosition.DistanceSquared(nextTileTarget);
                        if ((-1 == BestDistance) || (CurDistance < BestDistance)) {
                            BestDistance = CurDistance;
                            BestPosition = TempPosition;
                        }
                    }
                }
            } else {
                Skipped++;
            }
            if (4 == Skipped) {
                break;
            }
            if (-1 != BestDistance) {
                break;
            }
            TopY--;
            BottomY++;
            LeftX--;
            RightX++;
        }
        return BestPosition;
    }

    public CPlayerAsset FindNearestAsset(CPosition pos, CGameDataTypes.EPlayerColor color,
                                         CGameDataTypes.EAssetType type) {
        CPlayerAsset BestAsset = new CPlayerAsset();
        int BestDistanceSquared = -1;

        for (CPlayerAsset Asset : DAssets) {
            if ((Asset.Type() == type) && (Asset.Color() == color) && (CGameDataTypes.EAssetAction
                    .aaConstruct != Asset.Action())) {
                int CurrentDistance = Asset.Position().DistanceSquared(pos);

                if ((-1 == BestDistanceSquared) || (CurrentDistance < BestDistanceSquared)) {
                    BestDistanceSquared = CurrentDistance;
                    BestAsset = Asset;
                }
            }
        }
        return BestAsset;
    }

    @Override
    public Boolean LoadMap(CDataSource source) {
        StringBuffer TempString = new StringBuffer("");
        String [] Tokens;
        SResourceInitialization TempResourceInit = new SResourceInitialization();
        SAssetInitialization TempAssetInit;
        int ResourceCount, AssetCount;
        Boolean ReturnStatus = false;
        if (!super.LoadMap(source)) {
            return false;
        }
        if (!LineSource.Read(TempString)) {
                Log.e("CAssetDecoratedMap", "Failed to read map resource count.\n");
                return ReturnStatus;
        }

        DResourceInitializationList.clear();
        ResourceCount = parseInt(TempString.toString());
        for (int Index = 0; Index <= ResourceCount; Index++) {
            TempResourceInit = new SResourceInitialization();
            if (!LineSource.Read(TempString)) {
                Log.e("CAssetDecoratedMap", "Failed to read map resource " + String.valueOf(Index));
                return ReturnStatus;
            }
            Tokens = TempString.toString().split(" ");
            if (3 > Tokens.length) {
                Log.e("CAssetDecoratedMap", "Too few tokens for resource " + String.valueOf(Index));
                return ReturnStatus;
            }
            TempResourceInit.DColor = CGameDataTypes.EPlayerColor.values()[parseInt(Tokens[0])];
            if ((0 == Index) && (CGameDataTypes.EPlayerColor.pcNone != TempResourceInit.DColor)) {
                Log.e("CAssetDecoratedMap", "Expected first resource to be for color None.");
                return ReturnStatus;
            }
            TempResourceInit.DGold = parseInt(Tokens[1]);
            TempResourceInit.DLumber = parseInt(Tokens[2]);

            DResourceInitializationList.add(TempResourceInit);
        }

        if (!LineSource.Read(TempString)) {
            Log.e("CAssetDecoratedMap", "Failed to read map asset count.");
            return ReturnStatus;
        }

        DAssetInitializationList.clear();
        AssetCount = parseInt(TempString.toString());
        for (int Index = 0; Index < AssetCount; Index++) {
            TempAssetInit = new SAssetInitialization();
            if (!LineSource.Read(TempString)) {
                Log.e("CAssetDecoratedMap", "Failed to read map asset " + String.valueOf(Index));
                return ReturnStatus;
            }
            Tokens = TempString.toString().split(" ");
            if (4 > Tokens.length) {
                Log.e("CAssetDecoratedMap", "Too few tokens for asset " + String.valueOf(Index));
                return ReturnStatus;
            }
            TempAssetInit.DType = Tokens[0];
            TempAssetInit.DColor = CGameDataTypes.EPlayerColor.values()[parseInt(Tokens[1])];
            TempAssetInit.DTilePosition.X(parseInt(Tokens[2]));
            TempAssetInit.DTilePosition.Y(Integer.parseInt(Tokens[3]));

//            Log.d("val", "TempAsset tile position: " + TempAssetInit.DType + "-" + TempAssetInit.DTilePosition.X() + " " + TempAssetInit.DTilePosition.Y());

            if ((0 > TempAssetInit.DTilePosition.X()) || (0 > TempAssetInit.DTilePosition.Y())) {
                Log.d("Test", "Invalid resource position %d (%d, %d).\n" + Index + TempAssetInit.DTilePosition.X() + TempAssetInit
                        .DTilePosition.Y());
                return ReturnStatus;
            }
            if ((Width() <= TempAssetInit.DTilePosition.X()) || (Height() <= TempAssetInit.DTilePosition.Y())) {
                Log.d("Test", "Invalid resource position %d (%d, %d).\n" + Index + TempAssetInit.DTilePosition.X() + TempAssetInit
                        .DTilePosition.Y());
                return ReturnStatus;
            }
            DAssetInitializationList.add(TempAssetInit);
        }

        ReturnStatus = true;
        return ReturnStatus;
    }

    public ArrayList<CPlayerAsset> Assets() {
        return DAssets;
    }

    public ArrayList<SAssetInitialization> AssetInitializationList() {
        return DAssetInitializationList;
    }

    public ArrayList<SResourceInitialization> ResourceInitializationList() {
        return DResourceInitializationList;
    }

    public CAssetDecoratedMap CreateInitializeMap() {
        CAssetDecoratedMap ReturnMap = new CAssetDecoratedMap();

        if (ReturnMap.DMap.size() != DMap.size()) {
            // Initialize to empty grass
            for (int Row = 0; Row < DMap.size(); Row++) {
                ReturnMap.DMap.add(new ArrayList<ETileType>());
                for (int Col = 0; Col < DMap.get(Row).size(); Col++) {
                    ReturnMap.DMap.get(Row).add(Col, ETileType.ttNone);
                }
            }
        }
        return ReturnMap;
    }

    public CVisibilityMap CreateVisibilityMap() {
        return new CVisibilityMap(Width(), Height(), CPlayerAssetType.MaxSight());
    }

    public Boolean UpdateMap(CVisibilityMap vismap, CAssetDecoratedMap resmap) {
        Iterator<CPlayerAsset> Itr = DAssets.iterator();

        if (DMap.size() != resmap.DMap.size()) {
            for (int Row = 0; Row < DMap.size(); Row++) {
                for (int Col = 0; Col < DMap.get(Row).size(); Col++) {
                    DMap.get(Row).set(Col, ETileType.ttNone);
                }
            }
        }
        while (Itr.hasNext()) {
            CPlayerAsset Iterator = Itr.next();
            CPosition CurPosition = Iterator.TilePosition();
            int AssetSize = Iterator.Size();
            Boolean RemoveAsset = false;
            if (Iterator.Speed() > 0||(CGameDataTypes.EAssetAction.aaDecay == Iterator.Action())
                    || (CGameDataTypes.EAssetAction.aaAttack == Iterator.Action())) {
                Itr.remove();
                continue;
            }
            for (int YOff = 0; YOff < AssetSize; YOff++) {
                int YPos = CurPosition.Y() + YOff;
                for (int XOff = 0; XOff < AssetSize; XOff++) {
                    int XPos = CurPosition.X() + XOff;

                    CVisibilityMap.ETileVisibility VisType = vismap.TileType(XPos, YPos);
                    if ((CVisibilityMap.ETileVisibility.tvPartial == VisType) || (CVisibilityMap
                            .ETileVisibility.tvPartialPartial == VisType) || (CVisibilityMap
                            .ETileVisibility.tvVisible == VisType)) {
                        RemoveAsset = CGameDataTypes.EAssetType.atNone != Iterator.Type();
                        break;
                    }
                }
                if (RemoveAsset) {
                    break;
                }
            }
            if (RemoveAsset) {
                Itr.remove();
            }
        }
        for (int YPos = 0; YPos < DMap.size(); YPos++) {
            for (int XPos = 0; XPos < DMap.get(YPos).size(); XPos++) {
                CVisibilityMap.ETileVisibility VisType = vismap.TileType(XPos-1, YPos-1);
                if ((CVisibilityMap.ETileVisibility.tvPartial == VisType) || (CVisibilityMap
                        .ETileVisibility.tvPartialPartial == VisType) ||
                        (CVisibilityMap.ETileVisibility.tvVisible == VisType)) {
                    DMap.get(YPos).set(XPos, resmap.DMap.get(YPos).get(XPos));
                }
            }
        }
        Iterator<CPlayerAsset> Iterator = resmap.DAssets.iterator();
        CPlayerAsset Asset;
        while (Iterator.hasNext()) {
            Asset = Iterator.next();
            CPosition CurPosition = Asset.TilePosition();
            int AssetSize = Asset.Size();
            Boolean AddAsset = false;

            for (int YOff = 0; YOff < AssetSize; YOff++) {
                int YPos = CurPosition.Y() + YOff;
                for (int XOff = 0; XOff < AssetSize; XOff++) {
                    int XPos = CurPosition.X() + XOff;

                    CVisibilityMap.ETileVisibility VisType = vismap.TileType(XPos, YPos);
                    if ((CVisibilityMap.ETileVisibility.tvPartial == VisType) || (CVisibilityMap
                            .ETileVisibility.tvPartialPartial == VisType) || (CVisibilityMap
                            .ETileVisibility.tvVisible == VisType)) {
                        AddAsset = true;
                        break;
                    }
                }
                if (AddAsset) {
                    DAssets.add(Asset);
//                    break;
                }
            }
        }

        return true;
    }

    private final static int SEARCH_STATUS_UNVISITED = 0;
    private final static int SEARCH_STATUS_QUEUED = 1;
    private final static int SEARCH_STATUS_VISITED = 2;

    public class SSearchTile {
        public int DX;
        public int DY;
    }

    public CPosition FindNearestReachableTileType(CPosition pos, ETileType type) {
        Queue<SSearchTile> SearchQueue = new LinkedList<>();
        SSearchTile CurrentSearch1 = new SSearchTile();
        int MapWidth = Width();
        int MapHeight = Height();
        int SearchXOffsets[] = {0, 1, 0, -1};
        int SearchYOffsets[] = {-1, 0, 1, 0};

        Log.d("SearchMap:", String.valueOf(MapWidth)+ " " +String.valueOf(MapHeight));

        if (DSearchMap.size() != DMap.size()) {
            for (int Row = 0; Row < DMap.size(); Row++) {
                if (Row >= DSearchMap.size()) {
                    ArrayList<Integer> tempRow = new ArrayList<>(DMap.get(Row).size());
                    DSearchMap.add(Row, tempRow);
                }
                for (int Col = 0; Col < DMap.get(Row).size(); Col++) {
                    DSearchMap.get(Row).add(Col, 0);
                }
            }

            Log.d("SearchMap Resize:", String.valueOf(MapWidth) + " " + String.valueOf(MapHeight));

            int LastYIndex = DMap.size() - 1;
            int LastXIndex = DMap.get(0).size() - 1;
            for (int Index = 0; Index < DMap.size(); Index++) {
                DSearchMap.get(Index).set(0, SEARCH_STATUS_VISITED);
                DSearchMap.get(Index).set(LastXIndex, SEARCH_STATUS_VISITED);
            }
            for (int Index = 1; Index < LastXIndex; Index++) {
                DSearchMap.get(0).set(Index, SEARCH_STATUS_VISITED);
                DSearchMap.get(LastYIndex).set(Index, SEARCH_STATUS_VISITED);
            }
        }

        for (int Y = 0; Y < MapHeight; Y++)
            for (int X = 0; X < MapWidth; X++)
                DSearchMap.get(Y + 1).set(X + 1, SEARCH_STATUS_UNVISITED);


        Iterator<CPlayerAsset> Iterator = DAssets.iterator();
        CPlayerAsset Asset;
        while (Iterator.hasNext()) {
            Asset = Iterator.next();
            if (Asset.TilePosition() != pos) {
                for (int Y = 0; Y < Asset.Size(); Y++) {
                    for (int X = 0; X < Asset.Size(); X++) {
                        DSearchMap.get(Asset.TilePositionY() + Y + 1).set(Asset.TilePositionX() +
                                X + 1, SEARCH_STATUS_VISITED);
                    }
                }
            }
        }

        CurrentSearch1.DX = pos.X() + 1;
        CurrentSearch1.DY = pos.Y() + 1;
        SearchQueue.add(CurrentSearch1);
        while (!SearchQueue.isEmpty()) {
            SSearchTile CurrentSearch = SearchQueue.remove();
            DSearchMap.get(CurrentSearch.DY).set(CurrentSearch.DX, SEARCH_STATUS_VISITED);
            for (int Index = 0; Index < SearchXOffsets.length; Index++) {
                SSearchTile TempSearch = new SSearchTile();
                TempSearch.DX = CurrentSearch.DX + SearchXOffsets[Index];
                TempSearch.DY = CurrentSearch.DY + SearchYOffsets[Index];
                if (SEARCH_STATUS_UNVISITED == DSearchMap.get(TempSearch.DY).get(TempSearch.DX)) {
                    ETileType CurTileType = DMap.get(TempSearch.DY).get(TempSearch.DX);
                    DSearchMap.get(TempSearch.DY).set(TempSearch.DX, SEARCH_STATUS_QUEUED);
                    if (type == CurTileType) {
                        return new CPosition(TempSearch.DX - 1, TempSearch.DY - 1);
                    }
                    if ((CTerrainMap.ETileType.ttGrass == CurTileType) || (CTerrainMap.ETileType
                            .ttDirt == CurTileType) || (CTerrainMap.ETileType.ttStump ==
                            CurTileType) || (CTerrainMap.ETileType.ttRubble ==
                            CurTileType) || (CTerrainMap.ETileType.ttNone == CurTileType)) {
                        SearchQueue.add(TempSearch);
                    }
                }
            }
        }
        return new CPosition(-1, -1);
    }
}