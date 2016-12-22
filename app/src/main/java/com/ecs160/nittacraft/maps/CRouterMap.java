package com.ecs160.nittacraft.maps;

import com.ecs160.nittacraft.CGameDataTypes;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPosition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class CRouterMap {
    protected class SSearchTarget {
        public int DX;
        public int DY;
        public int DSteps;
        public CTerrainMap.ETileType DTileType;
        public int DTargetDistanceSquared;
        public CGameDataTypes.EDirection DInDirection;
    }

    protected final static int SEARCH_STATUS_UNVISITED = -1;
    protected final static int SEARCH_STATUS_VISITED = -2;
    protected final static int SEARCH_STATUS_OCCUPIED = -3;

    protected ArrayList<ArrayList<Integer>> DMap = new ArrayList<>();
    protected ArrayList<SSearchTarget> DSearchTargets = new ArrayList<>();

    protected static CGameDataTypes.EDirection DIdealSearchDirection = CGameDataTypes.EDirection.dNorth;
    protected static int DMapWidth = 1;

    protected static Boolean SearchTargetCompare(SSearchTarget first, SSearchTarget second) {
        if (first.DTargetDistanceSquared == second.DTargetDistanceSquared) {
            if (first.DSteps == second.DSteps) {
                return (first.DX + first.DY * DMapWidth) <= (second.DX + second.DY * DMapWidth);
            }
            return first.DSteps < second.DSteps;
        }
        return first.DTargetDistanceSquared < second.DTargetDistanceSquared;
/*
    if (first.DTargetDistanceSquared == second.DTargetDistanceSquared) {
        if (first.DSteps == second.DSteps) {
            if (first.DTileType == second.DTileType) {
                int DeltaDir1 = DIdealSearchDirection - first.DInDirection;
                int DeltaDir2 = DIdealSearchDirection - second.DInDirection;
                if (-(dMax/2) >= DeltaDir1) {
                    DeltaDir1 += dMax;
                }
                if ((dMax/2) < DeltaDir1) {
                    DeltaDir1 -= dMax;
                }
                if (-(dMax/2) >= DeltaDir2) {
                    DeltaDir2 += dMax;
                }
                if ((dMax/2) < DeltaDir2) {
                    DeltaDir2 -= dMax;
                }
                if ((DeltaDir1 == DeltaDir2) || (DeltaDir1 == -DeltaDir2)) {
                    return DeltaDir1 <= DeltaDir2;
                }
                DeltaDir1 = DeltaDir1 < 0 ? -DeltaDir1 : DeltaDir1;
                DeltaDir2 = DeltaDir2 < 0 ? -DeltaDir2 : DeltaDir2;
                return DeltaDir1 <= DeltaDir2;
            }
        }
        if (first.DTileType == second.DTileType) {
            return first.DSteps < second.DSteps;
        }
        if (CTerrainMap::ttNone == second.DTileType) {
            return false;
        }
        if (CTerrainMap::ttNone == first.DTileType) {
            return true;
        }
        return first.DSteps < second.DSteps;
    }
    return first.DTargetDistanceSquared <= second.DTargetDistanceSquared;
*/
    }

    protected static Boolean MovingAway(CGameDataTypes.EDirection dir1, int dir2) {
        int Value;
        if ((0 > dir2 || (CGameDataTypes.EDirection.dMax.ordinal() <= dir2))) {
            return false;
        }
        Value = ((CGameDataTypes.EDirection.dMax.ordinal() + dir2) - dir1.ordinal()) %
                CGameDataTypes.EDirection.dMax.ordinal();
        return (1 >= Value) || (CGameDataTypes.EDirection.dMax.ordinal() - 1 <= Value);
    }

    public CGameDataTypes.EDirection FindRoute(CAssetDecoratedMap resmap, CPlayerAsset asset, CPosition target) {
        int MapWidth = resmap.Width();
        int MapHeight = resmap.Height();
        int StartX = asset.TilePositionX();
        int StartY = asset.TilePositionY();
        SSearchTarget CurrentSearch = new SSearchTarget();
        SSearchTarget BestSearch = new SSearchTarget();
        SSearchTarget TempSearch = new SSearchTarget();
        CPosition CurrentTile;
        CPosition TargetTile = new CPosition();
        CPosition TempTile = new CPosition();
        CGameDataTypes.EDirection SearchDirecitons[] = {
                CGameDataTypes.EDirection.dNorth,
                CGameDataTypes.EDirection.dEast,
                CGameDataTypes.EDirection.dSouth,
                CGameDataTypes.EDirection.dWest};
        int ResMapXOffsets[] = {0, 1, 0, -1};
        int ResMapYOffsets[] = {-1, 0, 1, 0};
        int DiagCheckXOffset[] = {0, 1, 1, 1, 0, -1, -1, -1};
        int DiagCheckYOffset[] = {-1, -1, 0, 1, 1, 1, 0, -1};
        int SearchDirectionCount = SearchDirecitons.length;
        CGameDataTypes.EDirection LastInDirection, DirectionBeforeLast;
        Queue<SSearchTarget> SearchQueue = new LinkedList<>();

        //printf("Initializing Map\n");
        TargetTile.SetToTile(target);

        for (int i = 0; i < MapHeight; i++) {
            ArrayList<Integer> temprow = new ArrayList<>();
            for (int j = 0; j < MapWidth; j++) {
                temprow.add(SEARCH_STATUS_UNVISITED);
            }
            DMap.add(temprow);
        }

        if ((DMap.size() != MapHeight + 2) || (DMap.get(0).size() != MapWidth + 2)) {
            int LastYIndex = MapHeight + 1;
            int LastXIndex = MapWidth + 1;

            ArrayList<Integer> tempRow = new ArrayList<>();
            for (int i = 0; i < MapWidth + 2; i++) {
                tempRow.add(SEARCH_STATUS_VISITED);
            }
            DMap.add(0, tempRow);   //First boundary Row
            for (int j = 1; j < MapHeight + 1; j++) {
                // add left right boundary caps to each row
                DMap.get(j).add(0, SEARCH_STATUS_VISITED);
                DMap.get(j).add(SEARCH_STATUS_VISITED);
            }

            tempRow = new ArrayList<>();
            for (int k = 0; k < MapWidth + 2; k++) {
                tempRow.add(SEARCH_STATUS_VISITED);
            }
            //add boundary on bottom
            DMap.add(tempRow);

//            for (int Index = 0; Index < DMap.size(); Index++) {
//                Log.d("ROUTE", "LastXIndex" + LastXIndex+ " Index" + Index);
//                DMap.get(Index).set(0, SEARCH_STATUS_VISITED);
//                DMap.get(Index).set(LastXIndex, SEARCH_STATUS_VISITED);
//            }
//            for (int Index = 0; Index < MapWidth; Index++) {
//                DMap.get(0).set(Index+1, SEARCH_STATUS_VISITED);
//                DMap.get(LastYIndex).set(Index+1, SEARCH_STATUS_VISITED);
//            }
            DMapWidth = MapWidth + 2;
        }
        //printf("Comparing target to current\n");
//        Log.d("RM", "Target" + String.valueOf(target.X()) + "," + String.valueOf(target.Y()));
//        Log.d("RM", "Ass Pos" + String.valueOf(asset.PositionX()) + "," + String.valueOf(asset
//                .PositionY()));
        if (asset.TilePosition().equals(TargetTile)) {
            int DeltaX = target.X() - asset.PositionX();
            int DeltaY = target.Y() - asset.PositionY();

            if (0 < DeltaX) {
                if (0 < DeltaY) {
                    return CGameDataTypes.EDirection.dNorthEast;
                } else if (0 > DeltaY) {
                    return CGameDataTypes.EDirection.dSouthEast;
                }
                return CGameDataTypes.EDirection.dEast;
            } else if (0 > DeltaX) {
                if (0 < DeltaY) {
                    return CGameDataTypes.EDirection.dNorthWest;
                } else if (0 > DeltaY) {
                    return CGameDataTypes.EDirection.dSouthWest;
                }
                return CGameDataTypes.EDirection.dWest;
            }
            if (0 < DeltaY) {
                return CGameDataTypes.EDirection.dNorth;
            } else if (0 > DeltaY) {
                return CGameDataTypes.EDirection.dSouth;
            }
            return CGameDataTypes.EDirection.dMax;
        }
        //printf("Initializing unvisited\n");
        for (int Y = 0; Y < MapHeight; Y++) {
            for (int X = 0; X < MapWidth; X++) {
                DMap.get(Y + 1).set(X + 1, SEARCH_STATUS_UNVISITED);
            }
        }
        //printf("Marking assets visited\n");
        for (CPlayerAsset Res : resmap.Assets()) {
            if (asset != Res) {
                if (CGameDataTypes.EAssetType.atNone != Res.Type()) {
                    if ((CGameDataTypes.EAssetAction.aaWalk != Res.Action()) || (asset.Color() !=
                            Res.Color())) {
                        if ((asset.Color() != Res.Color()) || ((CGameDataTypes.EAssetAction
                                .aaConveyGold != Res.Action()) && (CGameDataTypes.EAssetAction
                                .aaConveyLumber != Res.Action()) && (CGameDataTypes.EAssetAction
                                .aaMineGold != Res.Action()))) {
                            for (int YOff = 0; YOff < Res.Size(); YOff++) {
                                for (int XOff = 0; XOff < Res.Size(); XOff++) {
                                    DMap.get(Res.TilePositionY() + YOff + 1).set(Res.TilePositionX()
                                            + XOff + 1, SEARCH_STATUS_VISITED);
                                }
                            }
                        }
                    } else {
                        DMap.get(Res.TilePositionY() + 1).set(Res.TilePositionX() + 1,
                                SEARCH_STATUS_OCCUPIED - Res.Direction().ordinal());
                    }
                }
            }
        }

        //DSearchTargets.clear();
        DIdealSearchDirection = asset.Direction();//(asset.Direction()/2) * 2;
        CurrentTile = new CPosition(asset.TilePosition());
        CurrentSearch.DX = BestSearch.DX = CurrentTile.X();
        CurrentSearch.DY = BestSearch.DY = CurrentTile.Y();
        CurrentSearch.DSteps = 0;
        CurrentSearch.DTargetDistanceSquared = BestSearch.DTargetDistanceSquared = CurrentTile
                .DistanceSquared(TargetTile);
        CurrentSearch.DInDirection = BestSearch.DInDirection = CGameDataTypes.EDirection.dMax;
        DMap.get(StartY + 1).set(StartX + 1, SEARCH_STATUS_VISITED);
//        Log.d("RM", "Searching Map" + CurrentTile.X()+ "," + CurrentTile.Y()+ " to " + TargetTile.X()+","+ TargetTile.Y());
        while (true) {
            if (CurrentTile.equals(TargetTile)) {
                BestSearch = CurrentSearch;
                break;
            }
            if (CurrentSearch.DTargetDistanceSquared < BestSearch.DTargetDistanceSquared) {
                BestSearch = CurrentSearch;
            }
            for (int Index = 0; Index < SearchDirectionCount; Index++) {
//                Log.d("RM", "Searching in direction " + String.valueOf(Index));
                TempTile.X(CurrentSearch.DX + ResMapXOffsets[Index]);
                TempTile.Y(CurrentSearch.DY + ResMapYOffsets[Index]);
                //printf("Tile(%d, %d) = %d\n",TempTile.X(), TempTile.Y(), DMap[TempTile.Y() + 1][TempTile.X() + 1]);
                if ((SEARCH_STATUS_UNVISITED == DMap.get(TempTile.Y() + 1).get(TempTile.X() + 1))
                        || MovingAway(SearchDirecitons[Index], (SEARCH_STATUS_OCCUPIED -
                        DMap.get(TempTile.Y() + 1).get(TempTile.X() + 1)))) {
                    DMap.get(TempTile.Y() + 1).set(TempTile.X() + 1, Index);
                    CTerrainMap.ETileType CurTileType = resmap.TileType(TempTile.X(), TempTile.Y());
                    if ((CTerrainMap.ETileType.ttGrass == CurTileType) || (CTerrainMap.ETileType.ttDirt
                            == CurTileType) || (CTerrainMap.ETileType.ttStump == CurTileType) ||
                            (CTerrainMap.ETileType.ttRubble == CurTileType) || (CTerrainMap.ETileType
                            .ttNone == CurTileType) || (CTerrainMap.ETileType.ttSeedling == CurTileType)) {
                        TempSearch = new SSearchTarget();
                        TempSearch.DX = TempTile.X();
                        TempSearch.DY = TempTile.Y();
                        TempSearch.DSteps = CurrentSearch.DSteps + 1;
                        TempSearch.DTileType = CurTileType;
                        TempSearch.DTargetDistanceSquared = TempTile.DistanceSquared(TargetTile);
                        TempSearch.DInDirection = SearchDirecitons[Index];
                        //DSearchTargets.push_back(TempSearch);
                        SearchQueue.add(TempSearch);
//                        Log.d("RM", "Temp Search adding: " + String.valueOf(TempSearch.DX) + "," +String.valueOf(TempSearch.DY));
                    }
                }
            }
            //DSearchTargets.sort(SearchTargetCompare);
            //if (DSearchTargets.empty()) {
            //    break;
            //}
            if (SearchQueue.isEmpty()) {
                break;
            }
            //CurrentSearch = DSearchTargets.front();
            //DSearchTargets.pop_front();
            CurrentSearch = SearchQueue.remove();
            CurrentTile.X(CurrentSearch.DX);
            CurrentTile.Y(CurrentSearch.DY);
        }

        //printf("Finding backpath (%d,%d) %d\n", BestSearch.DX, BestSearch.DY, BestSearch.DInDirection);
        DirectionBeforeLast = LastInDirection = BestSearch.DInDirection;
        CurrentTile.X(BestSearch.DX);
        CurrentTile.Y(BestSearch.DY);
        while ((CurrentTile.X() != StartX) || (CurrentTile.Y() != StartY)) {
            int Index = DMap.get(CurrentTile.Y() + 1).get(CurrentTile.X() + 1);

            //printf("DMap[%d][%d] == %d\n",CurrentTile.Y()+1, CurrentTile.X()+1, Index);
            if ((0 > Index) || (SearchDirectionCount <= Index)) {
                System.exit(0);
            }
            DirectionBeforeLast = LastInDirection;
            LastInDirection = SearchDirecitons[Index];
            CurrentTile.DecrementX(ResMapXOffsets[Index]);
            CurrentTile.DecrementY(ResMapYOffsets[Index]);
        }
        //printf("Returning direction\n");
//        Log.d("RM", "Directions: " + LastInDirection + " " + DirectionBeforeLast);
        if (DirectionBeforeLast != LastInDirection) {

            CTerrainMap.ETileType CurTileType = resmap.TileType(StartX +
                    DiagCheckXOffset[DirectionBeforeLast.ordinal()], StartY +
                    DiagCheckYOffset[DirectionBeforeLast.ordinal()]);
            if ((CTerrainMap.ETileType.ttGrass == CurTileType) || (CTerrainMap.ETileType.ttDirt ==
                    CurTileType) || (CTerrainMap.ETileType.ttStump == CurTileType) ||
                    (CTerrainMap.ETileType.ttRubble == CurTileType) || (CTerrainMap.ETileType
                    .ttNone == CurTileType)) {

                int Sum = LastInDirection.ordinal() + DirectionBeforeLast.ordinal();
//                Log.d("RM", "What is sum? " + Sum);
                if ((6 == Sum) && (LastInDirection == CGameDataTypes.EDirection.dNorth ||
                        DirectionBeforeLast == CGameDataTypes.EDirection.dNorth)) { // NW wrap around
                    Sum += 8;
                }
//                Log.d("RM", "What is sum? " + Sum);
                Sum /= 2;
//                Log.d("RM", "What is sum? " + Sum);
                LastInDirection = CGameDataTypes.EDirection.values()[Sum];
            }
        }

        return LastInDirection;
    }
}