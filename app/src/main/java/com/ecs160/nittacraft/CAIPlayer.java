package com.ecs160.nittacraft;

import com.ecs160.nittacraft.capabilities.CPlayerCapability;

import java.util.ArrayList;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaAttack;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaConstruct;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaHarvestLumber;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaMineGold;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaNone;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaStandGround;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actAttack;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actBuildArcher;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actBuildBarracks;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actBuildBlacksmith;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actBuildFarm;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actBuildFootman;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actBuildLumberMill;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actBuildPeasant;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actBuildRanger;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actBuildTownHall;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actConvey;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actMine;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actMove;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actNone;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actStandGround;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atArcher;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atBarracks;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atCastle;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atFarm;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atFootman;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGoldMine;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atKeep;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atLumberMill;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atNone;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atPeasant;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atRanger;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atTownHall;
import static com.ecs160.nittacraft.CGameDataTypes.EPlayerColor.pcNone;
import static com.ecs160.nittacraft.maps.CTerrainMap.ETileType.ttNone;
import static com.ecs160.nittacraft.maps.CTerrainMap.ETileType.ttTree;

public class CAIPlayer {
    protected CPlayerData DPlayerData;
    private int DCycle;
    private int DDownSample;


    CAIPlayer(CPlayerData playerdata, int downsample) {
        DPlayerData = playerdata;
        DCycle = 0;
        DDownSample = downsample;

    }

    private boolean SearchMap(SPlayerCommandRequest command) {

        ArrayList<CPlayerAsset> IdleAssets = DPlayerData.IdleAssets();
        CPlayerAsset MovableAsset = null;

        for (CPlayerAsset Asset : IdleAssets) {
            if (Asset.Speed() > 0) {
                MovableAsset = Asset;
                break;
            }
        }

        if (MovableAsset != null) {
            CPosition UnknownPosition = DPlayerData.PlayerMap().FindNearestReachableTileType
                    (MovableAsset.TilePosition(), ttNone);
            if (0 <= UnknownPosition.X()) {
                command.DAction = actMove;
                command.DActors.add(MovableAsset);
                command.DTargetLocation.SetFromTile(UnknownPosition);
                return true;
            }
        }
        return false;
    }

    private boolean FindEnemies(SPlayerCommandRequest command) {
        CPlayerAsset TownHallAsset = null;

        for (CPlayerAsset Asset : DPlayerData.Assets()) {
            if (Asset.HasCapability(actBuildPeasant)) {
                TownHallAsset = Asset;
                break;
            }
        }

        return DPlayerData.FindNearestEnemy(TownHallAsset.Position(), -1) != null && SearchMap
                (command);
    }

    private boolean AttackEnemies(SPlayerCommandRequest command) {
        CPosition AverageLocation = new CPosition(0,0);

        for (CPlayerAsset WeakAsset : DPlayerData.Assets()) {
            if ((atFootman == WeakAsset.Type()) || (atArcher == WeakAsset.Type()) || (atRanger ==
                    WeakAsset.Type())) {
                if (!WeakAsset.HasAction(aaAttack)) {
                    command.DActors.add(WeakAsset);
                    AverageLocation.IncrementX(WeakAsset.PositionX());
                    AverageLocation.IncrementY(WeakAsset.PositionY());
                }
            }
        }
        if (command.DActors.size() > 0) {
            AverageLocation.X(AverageLocation.X() / command.DActors.size());
            AverageLocation.Y(AverageLocation.Y() / command.DActors.size());

            CPlayerAsset TargetEnemy = DPlayerData.FindNearestEnemy(AverageLocation, -1);
            if (TargetEnemy != null) {
                command.DActors.clear();
                return SearchMap(command);
            }
            command.DAction = actAttack;
            command.DTargetLocation = TargetEnemy.Position();
            command.DTargetColor = TargetEnemy.Color();
            command.DTargetType = TargetEnemy.Type();
            return true;
        }
        return false;
    }

    private boolean BuildTownHall(SPlayerCommandRequest command) {
        // Build Town Hall
        ArrayList<CPlayerAsset> IdleAssets = DPlayerData.IdleAssets();
        CPlayerAsset BuilderAsset = null;

        for (CPlayerAsset WeakAsset : IdleAssets) {
            if (WeakAsset.HasCapability(actBuildTownHall)) {
                BuilderAsset = WeakAsset;
                break;
            }
        }
        if (BuilderAsset != null) {
            CPlayerAsset GoldMineAsset = DPlayerData.FindNearestAsset(BuilderAsset.Position(),
                    atGoldMine);
            CPosition Placement = DPlayerData.FindBestAssetPlacement(GoldMineAsset.TilePosition()
                    , BuilderAsset, atTownHall, 1);
            if (0 <= Placement.X()) {
                command.DAction = actBuildTownHall;
                command.DActors.add(BuilderAsset);
                command.DTargetLocation.SetFromTile(Placement);
                return true;
            } else {
                return SearchMap(command);
            }
        }
        return false;
    }
    private boolean BuildBuilding(SPlayerCommandRequest command, CGameDataTypes.EAssetType
            buildingType, CGameDataTypes.EAssetType nearType) {
        CPlayerAsset BuilderAsset = null;
        CPlayerAsset TownHallAsset = null;
        CPlayerAsset NearAsset = null;
        CGameDataTypes.EAssetCapabilityType BuildAction;
        boolean AssetIsIdle = false;

        switch(buildingType) {
            case atBarracks:
                BuildAction = actBuildBarracks;
                break;
            case atLumberMill:
                BuildAction = actBuildLumberMill;
                break;
            case atBlacksmith:
                BuildAction = actBuildBlacksmith;
                break;
            default:
                BuildAction = actBuildFarm;
                break;
        }

        for (CPlayerAsset WeakAsset : DPlayerData.Assets()) {
            if (WeakAsset.HasCapability(BuildAction) && WeakAsset.Interruptible()) {
                if (BuilderAsset == null || (!AssetIsIdle && (aaNone == WeakAsset.Action()))) {
                    BuilderAsset = WeakAsset;
                    AssetIsIdle = aaNone == WeakAsset.Action();
                }
            }
            if (WeakAsset.HasCapability(actBuildPeasant)) {
                TownHallAsset = WeakAsset;
            }
            if (WeakAsset.HasActiveCapability(BuildAction)) {
                return false;
            }
            if ((nearType == WeakAsset.Type()) && (aaConstruct != WeakAsset.Action())) {
                NearAsset = WeakAsset;
            }
            if (buildingType == WeakAsset.Type()) {
                if (aaConstruct == WeakAsset.Action()) {
                    return false;
                }
            }
        }
        if ((buildingType != nearType) && NearAsset == null) {
            return false;
        }
        if (BuilderAsset != null) {
            CPlayerCapability PlayerCapability = CPlayerCapability.FindCapability(BuildAction);
            CPosition SourcePosition = new CPosition(TownHallAsset.TilePosition());
            CPosition MapCenter = new CPosition(DPlayerData.PlayerMap().Width() / 2, DPlayerData
                    .PlayerMap().Height() / 2);

            if (NearAsset != null) {
                SourcePosition = NearAsset.TilePosition();
            }
            if (MapCenter.X() < SourcePosition.X()) {
                SourcePosition.DecrementX(TownHallAsset.Size()/2);
            } else if (MapCenter.X() > SourcePosition.X()) {
                SourcePosition.IncrementX(TownHallAsset.Size()/2);
            }
            if (MapCenter.Y() < SourcePosition.Y()) {
                SourcePosition.DecrementY(TownHallAsset.Size()/2);
            } else if (MapCenter.Y() > SourcePosition.Y()) {
                SourcePosition.IncrementY(TownHallAsset.Size()/2);
            }

            CPosition Placement = DPlayerData.FindBestAssetPlacement(SourcePosition,
                    BuilderAsset, buildingType, 1);
            if (0 > Placement.X()) {
                return SearchMap(command);
            }
            if (PlayerCapability != null) {
                if (PlayerCapability.CanInitiate(BuilderAsset, DPlayerData)) {
                    if (0 <= Placement.X()) {
                        command.DAction = BuildAction;
                        command.DActors.add(BuilderAsset);
                        command.DTargetLocation.SetFromTile(Placement);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean ActivatePeasants(SPlayerCommandRequest command, boolean trainmore) {
        CPlayerAsset MiningAsset = null;
        CPlayerAsset InterruptibleAsset = null;
        CPlayerAsset TownHallAsset = null;
        int GoldMiners = 0;
        int LumberHarvesters = 0;
        boolean SwitchToGold = false;
        boolean SwitchToLumber = false;

        for (CPlayerAsset WeakAsset : DPlayerData.Assets()) {
            CPlayerAsset Asset = WeakAsset;
            //if (CPlayerAsset Asset = WeakAsset) {
            if (Asset.HasCapability(actMine)) {
                if (MiningAsset == null && (aaNone == Asset.Action())) {
                    MiningAsset = Asset;
                }

                if (Asset.HasAction(aaMineGold)) {
                    GoldMiners++;
                    if (Asset.Interruptible() && (aaNone != Asset.Action())) {
                        InterruptibleAsset = Asset;
                    }
                } else if (Asset.HasAction(aaHarvestLumber)) {
                    LumberHarvesters++;
                    if (Asset.Interruptible() && (aaNone != Asset.Action())) {
                        InterruptibleAsset = Asset;
                    }
                }
            }
            if (Asset.HasCapability(actBuildPeasant) && (aaNone == Asset.Action())) {
                TownHallAsset = Asset;
            }
            //}
        }
        if ((2 <= GoldMiners) && (0 == LumberHarvesters)) {
            SwitchToLumber = true;
        } else if ((2 <= LumberHarvesters) && (0 == GoldMiners)) {
            SwitchToGold = true;
        }
        if (MiningAsset != null || (InterruptibleAsset != null && (SwitchToLumber ||
                SwitchToGold))) {
            if (MiningAsset != null && (MiningAsset.Lumber() > 0 || MiningAsset.Gold() > 0)) {
                command.DAction = actConvey;
                command.DTargetColor = TownHallAsset.Color();
                command.DActors.add(MiningAsset);
                command.DTargetType = TownHallAsset.Type();
                command.DTargetLocation = new CPosition(TownHallAsset.Position());
            } else {
                if (MiningAsset == null) {
                    MiningAsset = InterruptibleAsset;
                }
                CPlayerAsset GoldMineAsset = DPlayerData.FindNearestAsset(MiningAsset.Position(),
                        atGoldMine);
                if (GoldMiners > 0 && ((DPlayerData.Gold() > DPlayerData.Lumber() * 3) ||
                        SwitchToLumber)) {
                    CPosition LumberLocation = DPlayerData.PlayerMap()
                            .FindNearestReachableTileType(MiningAsset.TilePosition(), ttTree);
                    if (0 <= LumberLocation.X()) {
                        command.DAction = actMine;
                        command.DActors.add(MiningAsset);
                        command.DTargetLocation.SetFromTile(LumberLocation);
                    }
                    else {
                        return SearchMap(command);
                    }
                }
                else {
                    command.DAction = actMine;
                    command.DActors.add(MiningAsset);
                    command.DTargetType = atGoldMine;
                    command.DTargetLocation = new CPosition(GoldMineAsset.Position());
                }
            }
            return true;
        } else if (TownHallAsset != null && trainmore) {
            CPlayerCapability PlayerCapability = CPlayerCapability.FindCapability(actBuildPeasant);

            if (PlayerCapability != null) {
                if (PlayerCapability.CanApply(TownHallAsset, DPlayerData, TownHallAsset)) {
                    command.DAction = actBuildPeasant;
                    command.DActors.add(TownHallAsset);
                    command.DTargetLocation = new CPosition(TownHallAsset.Position());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean ActivateFighters(SPlayerCommandRequest command) {
        ArrayList<CPlayerAsset> IdleAssets = DPlayerData.IdleAssets();

        for (CPlayerAsset WeakAsset : IdleAssets) {
            CPlayerAsset Asset = WeakAsset;
            if (Asset.Speed() > 0 && (atPeasant != Asset.Type())) {
                if (!Asset.HasAction(aaStandGround) && !Asset.HasActiveCapability(actStandGround)) {
                    command.DActors.add(Asset);
                }
            }
        }
        if (command.DActors.size() > 0) {
            command.DAction = actStandGround;
            return true;
        }
        return false;
    }

    private boolean TrainFootman(SPlayerCommandRequest command) {
        ArrayList<CPlayerAsset> IdleAssets = DPlayerData.IdleAssets();
        CPlayerAsset TrainingAsset = null;

        for (CPlayerAsset WeakAsset : IdleAssets) {
            if (WeakAsset.HasCapability(actBuildFootman)) {
                TrainingAsset = WeakAsset;
                break;
            }
        }
        if (TrainingAsset != null) {
            CPlayerCapability PlayerCapability = CPlayerCapability.FindCapability(actBuildFootman);

            if (PlayerCapability != null) {
                if (PlayerCapability.CanApply(TrainingAsset, DPlayerData, TrainingAsset)) {
                    command.DAction = actBuildFootman;
                    command.DActors.add(TrainingAsset);
                    command.DTargetLocation = new CPosition(TrainingAsset.Position());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean TrainArcher(SPlayerCommandRequest command) {
        ArrayList<CPlayerAsset> IdleAssets = DPlayerData.IdleAssets();
        CPlayerAsset TrainingAsset = null;
        CGameDataTypes.EAssetCapabilityType BuildType = actBuildArcher;
        for (CPlayerAsset WeakAsset : IdleAssets) {
            CPlayerAsset Asset = WeakAsset;
            //if (CPlayerAsset Asset = WeakAsset) {
            if (Asset.HasCapability(actBuildArcher)) {
                TrainingAsset = Asset;
                BuildType = actBuildArcher;
                break;
            }
            if (Asset.HasCapability(actBuildRanger)) {
                TrainingAsset = Asset;
                BuildType = actBuildRanger;
                break;
            }

            //}
        }
        if (TrainingAsset != null) {
            CPlayerCapability PlayerCapability = CPlayerCapability.FindCapability(BuildType);
            if (PlayerCapability != null) {
                if (PlayerCapability.CanApply(TrainingAsset, DPlayerData, TrainingAsset)) {
                    command.DAction = BuildType;
                    command.DActors.add(TrainingAsset);
                    command.DTargetLocation = new CPosition(TrainingAsset.Position());
                    return true;
                }
            }
        }
        return false;
    }

    void CalculateCommand(SPlayerCommandRequest command) {

        command.DAction = actNone;
        command.DActors.clear();
        command.DTargetColor = pcNone;
        command.DTargetType = atNone;
        if ((DCycle % DDownSample) == 0) {
            if (0 == DPlayerData.FoundAssetCount(atGoldMine)) {
                // Search for gold mine
                SearchMap(command);
            } else if ((0 == DPlayerData.PlayerAssetCount(atTownHall)) && (0 == DPlayerData
                    .PlayerAssetCount(atKeep)) && (0 == DPlayerData.PlayerAssetCount(atCastle))) {
                BuildTownHall(command);
            } else if (5 > DPlayerData.PlayerAssetCount(atPeasant)) {
                ActivatePeasants(command, true);
            } else if (12 > DPlayerData.VisibilityMap().SeenPercent(100)) {
                SearchMap(command);
            } else {
                boolean CompletedAction = false;
                int FootmanCount = DPlayerData.PlayerAssetCount(atFootman);
                int ArcherCount = DPlayerData.PlayerAssetCount(atArcher) + DPlayerData
                        .PlayerAssetCount(atRanger);

                if ((DPlayerData.FoodConsumption() >= DPlayerData
                        .FoodProduction())) {
                    CompletedAction = BuildBuilding(command, atFarm, atFarm);
                }
                if (!CompletedAction) {
                    CompletedAction = ActivatePeasants(command, false);
                }
                if (!CompletedAction && (0 == DPlayerData.PlayerAssetCount(atBarracks))) {
                    CompletedAction = BuildBuilding(command, atBarracks, atFarm);
                }
                if (!CompletedAction && (5 > FootmanCount)) {
                    CompletedAction = TrainFootman(command);
                }
                if (!CompletedAction && (0 == DPlayerData.PlayerAssetCount(atLumberMill))) {
                    CompletedAction = BuildBuilding(command, atLumberMill, atBarracks);
                }
                if (!CompletedAction &&  (5 > ArcherCount)) {
                    CompletedAction = TrainArcher(command);
                }
                if (!CompletedAction && DPlayerData.PlayerAssetCount(atFootman) > 0) {
                    CompletedAction = FindEnemies(command);
                }
                if (!CompletedAction) {
                    CompletedAction = ActivateFighters(command);
                }
                if (!CompletedAction && ((5 <= FootmanCount) && (5 <= ArcherCount))) {
                    CompletedAction = AttackEnemies(command);
                }
            }
        }
        DCycle++;
    }
}