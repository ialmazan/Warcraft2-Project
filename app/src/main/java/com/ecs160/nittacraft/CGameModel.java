package com.ecs160.nittacraft;

import android.util.Log;

import com.ecs160.nittacraft.capabilities.CPlayerCapability;
import com.ecs160.nittacraft.maps.CAssetDecoratedMap;
import com.ecs160.nittacraft.maps.CForest;
import com.ecs160.nittacraft.maps.CRouterMap;
import com.ecs160.nittacraft.maps.CTerrainMap;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaConstruct;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaConveyGold;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaConveyLumber;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaDeath;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaDecay;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaHarvestLumber;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actBuildGoldMine;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGoldVein;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atPeasant;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atWall;
import static com.ecs160.nittacraft.CGameDataTypes.EPlayerColor.pcMax;
import static com.ecs160.nittacraft.CGameDataTypes.EPlayerColor.pcNone;
import static com.ecs160.nittacraft.CPlayerData.RangeToDistanceSquared;
import static com.ecs160.nittacraft.views.GameView.DApplicationData;

public class CGameModel {
    public enum EEventType {
        etNone,
        etWorkComplete,
        etSelection,
        etAcknowledge,
        etReady,
        etDeath,
        etAttacked,
        etMissleFire,
        etMissleHit,
        etHarvest,
        etMeleeHit,
        etPlaceAction,
        etButtonTick,
        etMax
    }

    public static class SGameEvent {
        public EEventType DType;
        public CPlayerAsset DAsset;

        public SGameEvent() {}
    }

//    protected CRandomNumberGenerator DRandomNumberGenerator;
    public CAssetDecoratedMap DActualMap;
    protected ArrayList<ArrayList<CPlayerAsset>> DAssetOccupancyMap;//std::vector< std::vector< std::shared_ptr< CPlayerAsset > > > DAssetOccupancyMap;
    protected ArrayList<ArrayList<Boolean>> DDiagonalOccupancyMap;//std::vector< std::vector< bool > > DDiagonalOccupancyMap;
    protected CRouterMap DRouterMap;
    protected ArrayList<CPlayerData> DPlayers;//[pcMax];
    protected int[][] DLumberAvailable; //std::vector< std::vector< int > > DLumberAvailable;
    protected int DGameCycle;
    protected int DHarvestTime;
    protected int DHarvestSteps;
    protected int DMineTime;
    protected int DMineSteps;
    protected int DConveyTime;
    protected int DConveySteps;
    protected int DDeathTime;
    protected int DDeathSteps;
    protected int DDecayTime;
    protected int DDecaySteps;
    protected int DLumberPerHarvest;
    protected int DGoldPerMining;
    public ArrayList<CForest> DChoppedForest;

    public CGameModel(int mapindex, int seed, CGameDataTypes.EPlayerColor newcolors[]) {
        DPlayers = new ArrayList<>(pcMax.ordinal());
        DHarvestTime = 5;
        DHarvestSteps = CPlayerAsset.UpdateFrequency() * DHarvestTime;
        DMineTime = 5;
        DMineSteps = CPlayerAsset.UpdateFrequency() * DMineTime;
        DConveyTime = 1;
        DConveySteps = CPlayerAsset.UpdateFrequency() * DConveyTime;
        DDeathTime = 1;
        DDeathSteps = CPlayerAsset.UpdateFrequency() * DDeathTime;
        DDecayTime = 4;
        DDecaySteps = CPlayerAsset.UpdateFrequency() * DDecayTime;
        DLumberPerHarvest = 100;
        DGoldPerMining = 100;
        DRouterMap = new CRouterMap();
        DDiagonalOccupancyMap = new ArrayList<ArrayList<Boolean>>();
        DChoppedForest = new ArrayList<>(1);
//        CForest forestt = null;
//        CPosition pos = new CPosition();
//        forestt = new CForest(,0,0,0);
//        DChoppedForest.add(forestt);
//        DRandomNumberGenerator.Seed(seed);
        DActualMap = CAssetDecoratedMap.DuplicateMap(mapindex, newcolors);

        for (int PlayerIndex = 0; PlayerIndex < pcMax.ordinal(); PlayerIndex++) {
            DPlayers.add(PlayerIndex, new CPlayerData(DActualMap, CGameDataTypes.EPlayerColor.values()[PlayerIndex]));
        }
//        Log.d("cgm", "dplayers: " + DPlayers);

//        CPlayerCapabilityTrainNormal c = new CPlayerCapabilityTrainNormal();
        //This creates maps to store Occupancy and Resource Information
        //A bit unwieldy as nested array lists.  Possibly change to a real 2D array since the map
        //dimensions should be known at this time.

        DAssetOccupancyMap = new ArrayList<>();

        for (int i = 0; i < DActualMap.DMap.size(); i++) {
            ArrayList<CPlayerAsset> temprow = new ArrayList<>();
            for (int j = 0; j < DActualMap.DMap.get(0).size(); j++) {
                temprow.add(null);
            }
            DAssetOccupancyMap.add(temprow);
        }

        DDiagonalOccupancyMap = new ArrayList<>();

        for (int i = 0; i < DActualMap.DMap.size(); i++) {
            ArrayList<Boolean> temprow = new ArrayList<>();
            for (int j = 0; j < DActualMap.DMap.get(0).size(); j++) {
                temprow.add(false);
            }
            DDiagonalOccupancyMap.add(temprow);
        }

        DLumberAvailable = new int[DActualMap.Width()][DActualMap.Height()];
        for (int Row = 0; Row < DActualMap.Height(); Row++) {
            for (int Col = 0; Col < DActualMap.Width(); Col++) {
                if (CTerrainMap.ETileType.ttTree == DActualMap.TileType(Col, Row)) {
                    //FIXME Had to hardcode 500 because the colors passed into the map arne't correct and I'm not sure why
                    DLumberAvailable[Col][Row] = 500;
                } else {
                    DLumberAvailable[Col][Row] = 0;
                }
            }
        }
    }

    void AddGameEvent(SGameEvent event) {
        CPlayerData.DGameEvents.add(event);
    };

    public int GameCycle() {
        return DGameCycle;
    };

    public boolean ValidAsset(CPlayerAsset asset) {
        for (CPlayerAsset Asset : DActualMap.Assets()) {
            if (asset == Asset) {
                return true;
            }
        }
        return false;
    }

    public CAssetDecoratedMap Map() {
        return DActualMap;
    }

    public CPlayerData Player(CGameDataTypes.EPlayerColor color) {

        if ((0 > color.ordinal()) || (pcMax.ordinal() <= color.ordinal())) {
            return null;
        }
        return DPlayers.get(color.ordinal());
    }

    public void TimeStep() {
//        Log.d("TimeStep", "Asset Count: " + DActualMap.Assets().size());
//        Log.d("TimeStep", "Player Asset Count: " + DPlayers.get(DApplicationData.DPlayerColor.ordinal()).Assets().size());
//        Log.d("TimeStep", "Player Map Asset Count: " + DPlayers.get(DApplicationData.DPlayerColor.ordinal()).DActualMap().Assets().size());

        ArrayList< SGameEvent > CurrentEvents = new ArrayList<>();
        SGameEvent TempEvent = new SGameEvent();

        for (ArrayList<CPlayerAsset> Row : DAssetOccupancyMap) {
            for (int Index = 0; Index < Row.size(); Index++) {
                Row.set(Index, null);
            }
        }

        for (ArrayList<Boolean> Row : DDiagonalOccupancyMap) {
            for (int Index = 0; Index < Row.size(); Index++) {
                Row.set(Index, false);
            }
        }

        for (CPlayerAsset Asset : DActualMap.Assets()) {
            if ((aaConveyGold != Asset.Action()) && (aaConveyLumber != Asset.Action()) &&
                    (CGameDataTypes.EAssetAction.aaMineGold != Asset.Action())) {
                DAssetOccupancyMap.get(Asset.TilePositionY()).set(Asset.TilePositionX(), Asset);
            }
        }
        for (int PlayerIndex = 1; PlayerIndex < CGameDataTypes.EPlayerColor.pcMax.ordinal(); PlayerIndex++) {
            if (DPlayers.get(PlayerIndex).IsAlive()) {
                DPlayers.get(PlayerIndex).UpdateVisibility();
            }
        }

        TimestepAssets(DActualMap.DMobileAssets, CurrentEvents, TempEvent);
        TimestepAssets(DActualMap.DStationaryAssets, CurrentEvents, TempEvent);

        DGameCycle++;
        for (int PlayerIndex = 0; PlayerIndex < pcMax.ordinal(); PlayerIndex++) {
//            int HealingPeriod = DPlayers.get(PlayerIndex).GetPeriod();

            DPlayers.get(PlayerIndex).IncrementCycle();
            DPlayers.get(PlayerIndex).AppendGameEvents(CurrentEvents);

            // healing stuff
//            if (DPlayers.get(PlayerIndex).GetPeriod() && (DHealingTimeStep >= HealingPeriod))
//            {
//                DPlayers.get(PlayerIndex).HealIdleUnits();
//                DHealingTimeStep = 0;
//            }
        }
//        DHealingTimeStep++;
    }

    public void TimestepAssets(ArrayList<CPlayerAsset> AssetList, ArrayList<SGameEvent> CurrentEvents, SGameEvent TempEvent) {

     /*
      *  This is the loop iterates through all the games assets and increments their actions
      *
      */
        ArrayList<CPlayerAsset> AllAssets = AssetList; //DActualMap.Assets();
        for (int Index = 0; Index  < AllAssets.size(); Index++) {
            CPlayerAsset Asset = AllAssets.get(Index);
            // Forest regrowth
            CPosition AssetLocation = Asset.TilePosition(); //walking unit
            for (CForest Forest : DChoppedForest) {
//            for (CForest Forest = DChoppedForest.get(0); DChoppedForest.get(DChoppedForest.size() - 1) != Forest; ++Forest) {
                CPosition TreeLocation = Forest.GetCurrentPos();
                int CurrentType = Forest.GetCurrentType();
                if (TreeLocation == AssetLocation && (0 == CurrentType|| 1 == CurrentType)) {
                    //TODO: should we consider rangers stepping on young trees?
                    Forest.SetCurrentTime(0); //reset tree growth timesteps
                    Forest.SetCurrentType(0); //reset tree growth to stump
                    DActualMap.ChangeTileType(TreeLocation, CTerrainMap.ETileType.ttStump); //change to stump
                } //hecks if the walking unit's position matches a position in the chopped forest vector
            } //for all chopped down trees
            Asset.IncrementAge();

            if (CGameDataTypes.EAssetAction.aaNone == Asset.Action()) {
                Asset.ChangeDirection();
                Asset.PopCommand();
            }
//            Log.d("letmegohome", "aa cap and asset. act " + CGameDataTypes.EAssetAction.aaCapability + " & " + Asset.Action());
            if (CGameDataTypes.EAssetAction.aaCapability == Asset.Action()) {
                SAssetCommand Command = Asset.CurrentCommand();
//                Log.d("letmegohome", "comand actiated cap: " + Command.DActivatedCapability);
                if (Command.DActivatedCapability != null) {
                    if (Command.DActivatedCapability.IncrementStep()) {
                        // All Done
//                        Log.d("letmegohome", "act " + Command.DCapability);
//                        Log.d("letmegohome", "at " + Command.DAssetTarget.Type());
                        if (actBuildGoldMine == Command.DCapability && atGoldVein == Command.DAssetTarget.Type()) {
//                            Log.d("letmegohome", "actbuildgoldMine first if ");
                            CPlayerAsset NewAsset = DPlayers.get(CGameDataTypes.EPlayerColor.pcNone.ordinal()).CreateAsset("GoldMine");

                            NewAsset.Gold(Command.DAssetTarget.Gold());

                            CPosition TilePosition = new CPosition();
                            TilePosition.SetToTile(Command.DAssetTarget.Position());
                            NewAsset.TilePosition(TilePosition);
                            NewAsset.HitPoints(NewAsset.AssetType().HitPoints());
                            DPlayers.get(CGameDataTypes.EPlayerColor.pcNone.ordinal()).DeleteAsset(Command.DAssetTarget);
                            Asset.ClearCommand();
//                            Log.d("letmegohome", "End of buildgold");
                        }
                    }
                } else {
                    CPlayerCapability PlayerCapability = CPlayerCapability.FindCapability(Command
                            .DCapability);
                    Asset.PopCommand();

                    if (PlayerCapability.CanApply(Asset, DPlayers.get(Asset.Color().ordinal()),
                            Command.DAssetTarget)) {
                        PlayerCapability.ApplyCapability(Asset, DPlayers.get(Asset.Color()
                                .ordinal()), Command.DAssetTarget);
                    } else {
                        // Can't apply notify problem
                    }
                }
            } else if (aaHarvestLumber == Asset.Action()) {
                SAssetCommand Command = Asset.CurrentCommand();
                CPosition TilePosition = Command.DAssetTarget.TilePosition();
                CGameDataTypes.EDirection HarvestDirection = Asset.TilePosition()
                        .AdjacentTileDirection(TilePosition, 1); //1 is default object size
                if (CTerrainMap.ETileType.ttTree != DActualMap.TileType(TilePosition)) {
                    HarvestDirection = CGameDataTypes.EDirection.dMax;
                    TilePosition = Asset.TilePosition();
                }
                if (CGameDataTypes.EDirection.dMax == HarvestDirection) {
                    if (TilePosition == Asset.TilePosition()) {
                        CPosition NewPosition = DPlayers.get(Asset.Color().ordinal()).DActualMap()
                                .FindNearestReachableTileType(Asset.TilePosition(), CTerrainMap
                                        .ETileType.ttTree);
                        // Find new lumber
                        Asset.PopCommand();

                        if (0 <= NewPosition.X()) {
                            NewPosition.SetFromTile(NewPosition);
                            SAssetCommand Command1 = new SAssetCommand();
                            Command1.DAssetTarget = DPlayers.get(Asset.Color().ordinal())
                                    .CreateMarker(NewPosition, false);
                            Command1.DAction = Command.DAction;
                            Asset.PushCommand(Command1);
                            SAssetCommand Command2 = new SAssetCommand();
                            Command2.DAction = CGameDataTypes.EAssetAction.aaWalk;
                            Command2.DAssetTarget = DPlayers.get(Asset.Color().ordinal())
                                    .CreateMarker(NewPosition, false);
                            Asset.PushCommand(Command2);
                            Asset.ResetStep();
                        }
                    } else {
                        SAssetCommand NewCommand = Command;
                        NewCommand.DAction = CGameDataTypes.EAssetAction.aaWalk;
                        Asset.PushCommand(NewCommand);
                        Asset.ResetStep();
                    }
                } else {
                    TempEvent.DType = CGameModel.EEventType.etHarvest;
                    TempEvent.DAsset = Asset;
                    CurrentEvents.add(TempEvent);
                    Asset.Direction(HarvestDirection);
                    Asset.IncrementStep();
                    if (DHarvestSteps <= Asset.Step()) {
                        CPlayerAsset NearestRepository = DPlayers.get(Asset.Color().ordinal())
                                .FindNearestOwnedAsset(Asset.Position(), new ArrayList<>(Arrays
                                        .asList(CGameDataTypes.EAssetType.atTownHall,
                                                CGameDataTypes.EAssetType.atKeep, CGameDataTypes
                                                        .EAssetType.atCastle,
                                                CGameDataTypes.EAssetType.atLumberMill)));

                        DLumberAvailable[TilePosition.X()][TilePosition.Y()] =- DLumberPerHarvest;
                        if (0 >= DLumberAvailable[TilePosition.X()][TilePosition.Y()]) {
                            DActualMap.ChangeTileType(TilePosition, CTerrainMap.ETileType.ttStump);
                            DActualMap.ChangeTileType(TilePosition, CTerrainMap.ETileType.ttStump);
                            int GrowthPeriod = ForestGrowthPeriod(TilePosition); //get growth period of chopped down tree
                            CForest ChoppedTree = new CForest(TilePosition, 0, GrowthPeriod, 0); //create new CForest object for chopped down tree
                            DChoppedForest.add(ChoppedTree); //add to chopped down tree vector
                        }

                        if (NearestRepository != null) {
                            SAssetCommand CommandConvey = new SAssetCommand();
                            CommandConvey.DAction = aaConveyLumber;
                            CommandConvey.DAssetTarget = NearestRepository;//.lock();
                            Asset.PushCommand(CommandConvey);

                            SAssetCommand Command2 = new SAssetCommand();
                            Command2.DAction = CGameDataTypes.EAssetAction.aaWalk;
                            Command2.DAssetTarget = CommandConvey.DAssetTarget;
                            Asset.PushCommand(Command2);

                            Asset.Lumber(DLumberPerHarvest);
                            Asset.ResetStep();
                        } else {
                            Asset.PopCommand();
                            Asset.Lumber(DLumberPerHarvest);
                            Asset.ResetStep();
                        }
                    }
                }
            } else if (CGameDataTypes.EAssetAction.aaMineGold == Asset.Action()) {
                SAssetCommand Command = Asset.CurrentCommand();
                CPosition ClosestPosition = Command.DAssetTarget.Position();
                CPosition TilePosition = new CPosition();
//                CPosition TilePosition = Asset.TilePosition();
                CGameDataTypes.EDirection MineDirection;

                TilePosition.SetToTile(ClosestPosition);
                MineDirection = Asset.TilePosition().AdjacentTileDirection(TilePosition, 1);

                if ((CGameDataTypes.EDirection.dMax == MineDirection) && (!TilePosition.equals(Asset
                        .TilePosition()))) {
                    SAssetCommand NewCommand = new SAssetCommand();//Command;
                    NewCommand.DAction = CGameDataTypes.EAssetAction.aaWalk;
                    NewCommand.DAssetTarget = Command.DAssetTarget;
                    Asset.PushCommand(NewCommand);
                    Asset.ResetStep();
                } else {
                    if (0 == Asset.Step()) {
                        if ((Command.DAssetTarget.CommandCount() + 1) * DGoldPerMining <=
                                Command.DAssetTarget.Gold()) {
                            SAssetCommand NewCommand = new SAssetCommand();
                            NewCommand.DAction = CGameDataTypes.EAssetAction.aaBuild;
                            NewCommand.DAssetTarget = Asset;

                            Command.DAssetTarget.EnqueueCommand(NewCommand);
                            Asset.IncrementStep();
                            Asset.TilePosition(Command.DAssetTarget.TilePosition());
                        } else {
                            // Look for new mine or give up?
                            Asset.PopCommand();
                        }
                    } else {
                        Asset.IncrementStep();
                        if (DMineSteps <= Asset.Step()) {
                            CPlayerAsset OldTarget = Command.DAssetTarget;
                            CPlayerAsset NearestRepository = DPlayers.get(Asset.Color().ordinal()
                            ).FindNearestOwnedAsset(Asset.Position(), new ArrayList<>(Arrays
                                    .asList(CGameDataTypes.EAssetType.atTownHall,
                                    CGameDataTypes.EAssetType.atKeep, CGameDataTypes.EAssetType
                                                    .atCastle)));

                            CPosition NextTarget = new CPosition(DPlayers.get(Asset.Color()
                                    .ordinal()).DActualMap().Width()-1,DPlayers.get(Asset.Color() // PlayerMap()
                                    .ordinal()).DActualMap().Height()-1);

                            Command.DAssetTarget.DecrementGold(DGoldPerMining);
                            Command.DAssetTarget.PopCommand();

                            if (0 >= Command.DAssetTarget.Gold()) {
                                SAssetCommand NewCommand = new SAssetCommand();

                                NewCommand.DAction = aaDeath;
                                Command.DAssetTarget.ClearCommand();
                                Command.DAssetTarget.PushCommand(NewCommand);
                                Command.DAssetTarget.ResetStep();
                            }
                            Asset.Gold(DGoldPerMining);
                            if (NearestRepository != null) {
                                SAssetCommand NewCommand = new SAssetCommand();
                                NewCommand.DAction = aaConveyGold;
                                NewCommand.DAssetTarget = NearestRepository;//.lock();
                                Asset.PushCommand(NewCommand);
                                SAssetCommand Command2 = new SAssetCommand();
                                Command2.DAssetTarget = NewCommand.DAssetTarget;
                                Command2.DAction = CGameDataTypes.EAssetAction.aaWalk;
                                Asset.PushCommand(Command2);
                                Asset.ResetStep();
                                NextTarget = Command2.DAssetTarget.TilePosition();
                            } else {
                                Asset.PopCommand();
                            }
//                            Asset.TilePosition(DPlayers.get(Asset.Color().ordinal()).PlayerMap()
                              Asset.TilePosition(DPlayers.get(Asset.Color().ordinal()).DActualMap()
                                      .FindAssetPlacement(Asset, OldTarget, NextTarget));
                        }
                    }
                }
            } else if (CGameDataTypes.EAssetAction.aaStandGround == Asset.Action()) {
                SAssetCommand Command = Asset.CurrentCommand();
                CPlayerAsset NewTarget = DPlayers.get(Asset.Color().ordinal()).FindNearestEnemy
                        (Asset.Position(), Asset.EffectiveRange());

                if (NewTarget == null) {
                    Command.DAction = CGameDataTypes.EAssetAction.aaNone;
                } else {
                    Command.DAction = CGameDataTypes.EAssetAction.aaAttack;
                    Command.DAssetTarget = NewTarget;
                }
                Asset.PushCommand(Command);
                Asset.ResetStep();
            } else if (CGameDataTypes.EAssetAction.aaRepair == Asset.Action()) {
                SAssetCommand CurrentCommand = Asset.CurrentCommand();
                if (CurrentCommand.DAssetTarget.Alive()) {
                    CGameDataTypes.EDirection RepairDirection = Asset.TilePosition()
                            .AdjacentTileDirection(CurrentCommand.DAssetTarget.TilePosition(),1);
                                    //CurrentCommand.DAssetTarget.Size());

                    if (CGameDataTypes.EDirection.dMax == RepairDirection) {
                        SAssetCommand NextCommand = Asset.NextCommand();

                        CurrentCommand.DAction = CGameDataTypes.EAssetAction.aaWalk;
                        Asset.PushCommand(CurrentCommand);
                        Asset.ResetStep();
                    } else {
                        Asset.Direction(RepairDirection);
                        Asset.IncrementStep();
                        // Assume same movement as attack
                        if (Asset.Step() == Asset.AttackSteps()) {
                            if (DPlayers.get(Asset.Color().ordinal()).Gold() > 0 && DPlayers.get
                                    (Asset.Color().ordinal()).Lumber() > 0) {

                                int RepairPoints = (CurrentCommand.DAssetTarget.MaxHitPoints() *
                                        (Asset.AttackSteps() + Asset.ReloadSteps())) /
                                        (CPlayerAsset.UpdateFrequency() * CurrentCommand
                                                .DAssetTarget.BuildTime());

                                if (0 == RepairPoints) {
                                    RepairPoints = 1;
                                }

                                DPlayers.get(Asset.Color().ordinal()).DecrementGold(1);
                                DPlayers.get(Asset.Color().ordinal()).DecrementLumber(1);
                                CurrentCommand.DAssetTarget.IncrementHitPoints(RepairPoints);

                                if (CurrentCommand.DAssetTarget.HitPoints() == CurrentCommand
                                        .DAssetTarget.MaxHitPoints()) {

                                    TempEvent.DType = EEventType.etWorkComplete;
                                    TempEvent.DAsset = Asset;
                                    DPlayers.get(Asset.Color().ordinal()).AddGameEvent(TempEvent);
                                    Asset.PopCommand();
                                }
                            } else {
                                // Stop repair
                                Asset.PopCommand();
                            }
                        }
                        if (Asset.Step() >= (Asset.AttackSteps() + Asset.ReloadSteps())) {
                            Asset.ResetStep();
                        }
                    }
                } else {
                    Asset.PopCommand();
                }
            } else if (CGameDataTypes.EAssetAction.aaAttack == Asset.Action()) {
                SAssetCommand CurrentCommand = Asset.CurrentCommand();
                if (CGameDataTypes.EAssetType.atNone == Asset.Type()) {
                    CPosition ClosestTargetPosition = CurrentCommand.DAssetTarget.ClosestPosition
                            (Asset.Position());

                    CPosition DeltaPosition = new CPosition(ClosestTargetPosition.X() - Asset
                            .PositionX(), ClosestTargetPosition.Y() - Asset.PositionY());
                    int Movement = CPosition.TileWidth() * 5 / CPlayerAsset.UpdateFrequency();
                    int TargetDistance = Asset.Position().Distance(ClosestTargetPosition);
                    int Divisor = (TargetDistance + Movement - 1)/ Movement;

                    if (Divisor > 0) {
                        DeltaPosition.X( DeltaPosition.X() / Divisor);
                        DeltaPosition.Y( DeltaPosition.Y() / Divisor);
                    }
                    Asset.PositionX(Asset.PositionX() + DeltaPosition.X());
                    Asset.PositionY(Asset.PositionY() + DeltaPosition.Y());
                    Asset.Direction(Asset.Position().DirectionTo(ClosestTargetPosition));

                    if (CPosition.HalfTileWidth() * CPosition.HalfTileHeight() > Asset.Position()
                            .DistanceSquared(ClosestTargetPosition)) {
                        TempEvent.DType = EEventType.etMissleHit;
                        TempEvent.DAsset = Asset;
                        CurrentEvents.add(TempEvent);

                        if (CurrentCommand.DAssetTarget.Alive()) {
                            SAssetCommand TargetCommand = CurrentCommand.DAssetTarget
                                    .CurrentCommand();
                            TempEvent.DType = EEventType.etAttacked;
                            TempEvent.DAsset = CurrentCommand.DAssetTarget;
                            DPlayers.get(CurrentCommand.DAssetTarget.Color().ordinal())
                                    .AddGameEvent(TempEvent);

                            if (CGameDataTypes.EAssetAction.aaMineGold != TargetCommand.DAction) {

                                if ((aaConveyGold == TargetCommand
                                        .DAction) || (aaConveyLumber
                                        ==  TargetCommand.DAction)) {

                                    // Damage the target
                                    CurrentCommand.DAssetTarget = TargetCommand.DAssetTarget;
                                } else if ((CGameDataTypes.EAssetAction.aaCapability.ordinal() ==
                                        TargetCommand.DAction.ordinal()) && TargetCommand
                                        .DAssetTarget != null) {

                                    if (CurrentCommand.DAssetTarget.Speed() != 0 &&
                                            (aaConstruct ==
                                                    TargetCommand.DAssetTarget.Action())) {
                                        CurrentCommand.DAssetTarget = TargetCommand.DAssetTarget;
                                    }
                                }
                                if (!((CurrentCommand.DAssetTarget.Type() == atWall) && (Asset.Type() == atPeasant))) {
                                    CurrentCommand.DAssetTarget.DecrementHitPoints(Asset.HitPoints());
                                }
                                if (!CurrentCommand.DAssetTarget.Alive()) {
                                    SAssetCommand Command = CurrentCommand.DAssetTarget
                                            .CurrentCommand();

                                    TempEvent.DType = EEventType.etDeath;
                                    TempEvent.DAsset = CurrentCommand.DAssetTarget;
                                    CurrentEvents.add(TempEvent);
                                    // Remove constructing
                                    if ((CGameDataTypes.EAssetAction.aaCapability == Command
                                            .DAction) && (Command.DAssetTarget != null)) {
                                        if (aaConstruct == Command
                                                .DAssetTarget.Action()) {
                                            DPlayers.get(Command.DAssetTarget.Color().ordinal())
                                                    .DeleteAsset(Command.DAssetTarget);
                                            Index--;
                                        }
                                    } else if (aaConstruct == Command
                                            .DAction) {
                                        if (Command.DAssetTarget != null) {
                                            Command.DAssetTarget.ClearCommand();
                                        }
                                    }

                                    CurrentCommand.DAssetTarget.Direction(CGameDataTypes
                                            .EDirection.values()[((Asset.Direction().ordinal() +
                                            CGameDataTypes.EDirection.dMax.ordinal()/2) %
                                            CGameDataTypes.EDirection.dMax.ordinal())]);

                                    Command.DAction = aaDeath;
                                    CurrentCommand.DAssetTarget.ClearCommand();
                                    CurrentCommand.DAssetTarget.PushCommand(Command);
                                    CurrentCommand.DAssetTarget.ResetStep();
                                }
                            }
                        }
                        DPlayers.get(Asset.Color().ordinal()).DeleteAsset(Asset);
                        Index--;
                    }
                } else if (CurrentCommand.DAssetTarget.Alive()) {
                    if (1 == Asset.EffectiveRange()) {
                        CGameDataTypes.EDirection AttackDirection = Asset.TilePosition()
                                .AdjacentTileDirection(CurrentCommand.DAssetTarget.TilePosition()
                                        , 1);
//                                        , CurrentCommand.DAssetTarget.Size());

                        if (CGameDataTypes.EDirection.dMax == AttackDirection) {
                            SAssetCommand NextCommand = Asset.NextCommand();
                            if (CGameDataTypes.EAssetAction.aaStandGround != NextCommand.DAction) {
                                CurrentCommand.DAction = CGameDataTypes.EAssetAction.aaWalk;
                                Asset.PushCommand(CurrentCommand);
                                Asset.ResetStep();
                            } else {
                                Asset.PopCommand();
                            }
                        } else {
                            Asset.Direction(AttackDirection);
                            Asset.IncrementStep();
                            if (Asset.Step() == Asset.AttackSteps()) {
                                int Damage = Asset.EffectiveBasicDamage() - CurrentCommand
                                        .DAssetTarget.EffectiveArmor();
                                Damage = 0 > Damage ? 0 : Damage;
                                Damage += Asset.EffectivePiercingDamage();
//                                if (DRandomNumberGenerator.Random() & 0x1) { // 50% chance half damage
//                                    Damage /= 2;
//                                }
                                if (!((CurrentCommand.DAssetTarget.Type() == atWall) && (Asset.Type() == atPeasant))) {
                                    CurrentCommand.DAssetTarget.DecrementHitPoints(Damage);
                                }
                                TempEvent.DType = EEventType.etMeleeHit;
                                TempEvent.DAsset = Asset;
                                CurrentEvents.add(TempEvent);
                                TempEvent.DType = EEventType.etAttacked;
                                TempEvent.DAsset = CurrentCommand.DAssetTarget;
                                DPlayers.get(CurrentCommand.DAssetTarget.Color().ordinal())
                                        .AddGameEvent(TempEvent);

                                if (!CurrentCommand.DAssetTarget.Alive()) {
                                    SAssetCommand Command = CurrentCommand.DAssetTarget
                                            .CurrentCommand();

                                    TempEvent.DType = EEventType.etDeath;
                                    TempEvent.DAsset = CurrentCommand.DAssetTarget;
                                    CurrentEvents.add(TempEvent);
                                    // Remove constructing
                                    if ((CGameDataTypes.EAssetAction.aaCapability == Command
                                            .DAction) && (Command.DAssetTarget != null)) {
                                        if (aaConstruct == Command
                                                .DAssetTarget.Action()) {
                                            DPlayers.get(Command.DAssetTarget.Color().ordinal())
                                                    .DeleteAsset(Command.DAssetTarget);
                                            Index--;
                                        }
                                    } else if (aaConstruct == Command
                                            .DAction) {
                                        if (Command.DAssetTarget != null) {
                                            Command.DAssetTarget.ClearCommand();
                                        }
                                    }
                                    Command.DCapability = CGameDataTypes.EAssetCapabilityType
                                            .actNone;
                                    Command.DAssetTarget = null;
                                    Command.DActivatedCapability = null;
                                    CurrentCommand.DAssetTarget.Direction(CGameDataTypes
                                            .EDirection.values()[((AttackDirection.ordinal() +
                                            CGameDataTypes.EDirection.dMax.ordinal()/2) %
                                            CGameDataTypes.EDirection.dMax.ordinal())]);

                                    Command.DAction = aaDeath;
                                    CurrentCommand.DAssetTarget.ClearCommand();
                                    CurrentCommand.DAssetTarget.PushCommand(Command);
                                    CurrentCommand.DAssetTarget.ResetStep();
                                }
                            }
                            if (Asset.Step() >= (Asset.AttackSteps() + Asset.ReloadSteps())) {
                                Asset.ResetStep();
                            }
                        }
                    } else { // EffectiveRanged
                        CPosition ClosestTargetPosition = CurrentCommand.DAssetTarget
                                .ClosestPosition(Asset.Position());
                        if (ClosestTargetPosition.DistanceSquared( Asset.Position() ) >
                                RangeToDistanceSquared(Asset.EffectiveRange())) {
                            SAssetCommand NextCommand = Asset.NextCommand();

                            if (CGameDataTypes.EAssetAction.aaStandGround != NextCommand.DAction) {
                                SAssetCommand Command = new SAssetCommand();
                                Command.DAction = CGameDataTypes.EAssetAction.aaWalk;
                                Command.DAssetTarget = CurrentCommand.DAssetTarget;
                                Asset.PushCommand(Command);
                                Asset.ResetStep();
                            } else {
                                Asset.PopCommand();
                            }
                        } else {
                    /*
                    CPosition DeltaPosition(ClosestTargetPosition.X() - Asset.PositionX(), ClosestTargetPosition.Y() - Asset.PositionY());
                    int DivX = DeltaPosition.X() / CPosition::HalfTileWidth();
                    int DivY = DeltaPosition.Y() / CPosition::HalfTileHeight();
                    int Div;
                    EDirection AttackDirection;
                    DivX = 0 > DivX ? -DivX : DivX;
                    DivY = 0 > DivY ? -DivY : DivY;
                    Div = DivX > DivY ? DivX : DivY;

                    if (Div) {
                        DeltaPosition.X(DeltaPosition.X() / Div);
                        DeltaPosition.Y(DeltaPosition.Y() / Div);
                    }
                    DeltaPosition.IncrementX(CPosition::HalfTileWidth());
                    DeltaPosition.IncrementY(CPosition::HalfTileHeight());
                    if (0 > DeltaPosition.X()) {
                        DeltaPosition.X(0);
                    }
                    if (0 > DeltaPosition.Y()) {
                        DeltaPosition.Y(0);
                    }
                    if (CPosition::TileWidth() <= DeltaPosition.X()) {
                        DeltaPosition.X(CPosition::TileWidth() - 1);
                    }
                    if (CPosition::TileHeight() <= DeltaPosition.Y()) {
                        DeltaPosition.Y(CPosition::TileHeight() - 1);
                    }
                    AttackDirection = DeltaPosition.TileOctant();
                    */
                            CGameDataTypes.EDirection AttackDirection = Asset.Position()
                                    .DirectionTo(ClosestTargetPosition);
                            Asset.Direction(AttackDirection);
                            Asset.IncrementStep();
                            if (Asset.Step() == Asset.AttackSteps()) {
                                SAssetCommand AttackCommand = new SAssetCommand(); // Create missle
                                CPlayerAsset ArrowAsset = DPlayers.get(pcNone.ordinal()).CreateAsset("None");
                                int Damage = Asset.EffectiveBasicDamage() - CurrentCommand
                                        .DAssetTarget.EffectiveArmor();
                                Damage = 0 > Damage ? 0 : Damage;
                                Damage += Asset.EffectivePiercingDamage();
//                                if (DRandomNumberGenerator.Random() & 0x1) { // 50% chance half damage
//                                    Damage /= 2;
//                                }
                                TempEvent.DType = EEventType.etMissleFire;
                                TempEvent.DAsset = Asset;
                                CurrentEvents.add(TempEvent);

                                ArrowAsset.HitPoints(Damage);

                                CPosition AssetPosition = new CPosition(Asset.Position());
                                ArrowAsset.Position(AssetPosition);

                                if (ArrowAsset.PositionX() < ClosestTargetPosition.X()) {
                                    ArrowAsset.PositionX(ArrowAsset.PositionX() + CPosition
                                            .HalfTileWidth());
                                } else if (ArrowAsset.PositionX() > ClosestTargetPosition.X()) {
                                    ArrowAsset.PositionX(ArrowAsset.PositionX() - CPosition
                                            .HalfTileWidth());
                                }

                                if (ArrowAsset.PositionY() < ClosestTargetPosition.Y()) {
                                    ArrowAsset.PositionY(ArrowAsset.PositionY() + CPosition
                                            .HalfTileHeight());
                                } else if (ArrowAsset.PositionY() > ClosestTargetPosition.Y()) {
                                    ArrowAsset.PositionY(ArrowAsset.PositionY() - CPosition
                                            .HalfTileHeight());
                                }

                                ArrowAsset.Direction(AttackDirection);
                                AttackCommand.DAction = aaConstruct;
                                AttackCommand.DAssetTarget = Asset;
                                ArrowAsset.PushCommand(AttackCommand);

                                SAssetCommand AttackCommand2 = new SAssetCommand();
                                AttackCommand2.DAction = CGameDataTypes.EAssetAction.aaAttack;
                                AttackCommand2.DAssetTarget = CurrentCommand.DAssetTarget;
                                ArrowAsset.PushCommand(AttackCommand2);
                            }
                            if (Asset.Step() >= (Asset.AttackSteps() + Asset.ReloadSteps())) {
                                Asset.ResetStep();
                            }
                        }
                    }
                } else {
                    SAssetCommand NextCommand = Asset.NextCommand();
                    Asset.PopCommand();
                    if (CGameDataTypes.EAssetAction.aaStandGround != NextCommand.DAction) {
                        CPlayerAsset NewTarget = DPlayers.get(Asset.Color().ordinal())
                                .FindNearestEnemy(Asset.Position(), Asset.EffectiveSight());

                        if (NewTarget != null) {
                            CurrentCommand.DAssetTarget = NewTarget;
                            Asset.PushCommand(CurrentCommand);
                            Asset.ResetStep();
                        }
                    }
                }
            } else if ((aaConveyLumber == Asset.Action()) || (aaConveyGold == Asset.Action())) {
//                Log.d("cgm", "getting lumber");
                Asset.IncrementStep();
                if (DConveySteps <= Asset.Step()) {
                    SAssetCommand Command = Asset.CurrentCommand();
                    CGameDataTypes.EAssetAction tempAction = Asset.Action();
                    CPosition NextTarget = new CPosition(DPlayers.get(Asset.Color().ordinal())
                            .PlayerMap().Width()-1,DPlayers.get(Asset.Color().ordinal())
                            .PlayerMap().Height()-1);
                    DPlayers.get(Asset.Color().ordinal()).IncrementGold(Asset.Gold());
                    DPlayers.get(Asset.Color().ordinal()).IncrementLumber(Asset.Lumber());
                    Asset.Gold(0);
                    Asset.Lumber(0);
                    Asset.PopCommand();
                    Asset.ResetStep();

                    if (CGameDataTypes.EAssetAction.aaNone != Asset.Action()) {
                        NextTarget = Asset.CurrentCommand().DAssetTarget.TilePosition();
                    }

                    // puts the peasant in front of town hall instead of where it enters
                    // do we want that?
                    Asset.TilePosition(DPlayers.get(Asset.Color().ordinal()).DActualMap() //PlayerMap()
                            .FindAssetPlacement(Asset, Command.DAssetTarget, NextTarget));
                }
            } else if (aaConstruct == Asset.Action()) {
                SAssetCommand Command = Asset.CurrentCommand();
                if (Command.DActivatedCapability != null) {
                    if (Command.DActivatedCapability.IncrementStep()) {
                        // All Done
                    }
                }
            } else if (aaDeath == Asset.Action()) {
                Asset.IncrementStep();
                if (Asset.Step() > DDeathSteps) {
                    if (Asset.Speed() != 0) {
                        SAssetCommand DecayCommand = new SAssetCommand();
                        // Create corpse

                        CPlayerAsset CorpseAsset = DPlayers.get(pcNone.ordinal()).CreateAsset("None");

                        DecayCommand.DAction = aaDecay;
                        CorpseAsset.Position(Asset.Position());
                        CorpseAsset.Direction(Asset.Direction());
                        CorpseAsset.PushCommand(DecayCommand);

                        CPlayerAsset DropAsset = null;
                        if (Asset.Gold() != 0) {
                            DropAsset = DPlayers.get(pcNone.ordinal()).CreateAsset("Gold");
                            DropAsset.Gold(Asset.Gold());
                        } else if (Asset.Lumber() != 0) {
                            DropAsset = DPlayers.get(pcNone.ordinal()).CreateAsset("Lumber");
                            DropAsset.Lumber(Asset.Lumber());
                        }
                        if (DropAsset != null) {
                            DropAsset.Position(Asset.Position());
                        }

                    }
                    DPlayers.get(Asset.Color().ordinal()).DeleteAsset(Asset);
                    Index--;
                }
            } else if (aaDecay == Asset.Action()) {
                Asset.IncrementStep();
                if (Asset.Step() > DDecaySteps) {
                    DPlayers.get(Asset.Color().ordinal()).DeleteAsset(Asset);
                    Index--;
                }
            }
            if (CGameDataTypes.EAssetAction.aaWalk == Asset.Action()) {
                if (Asset.TileAligned()) {
                    SAssetCommand Command = Asset.CurrentCommand();
                    SAssetCommand NextCommand = Asset.NextCommand();
                    CGameDataTypes.EDirection TravelDirection;

                    CPosition MapTarget = Command.DAssetTarget.Position();//Command.DAssetTarget.ClosestPosition(Asset.Position());

                    if (CGameDataTypes.EAssetAction.aaAttack == NextCommand.DAction) {

//                     Check to see if can attack now
                        if (NextCommand.DAssetTarget.ClosestPosition(Asset.Position())
                                .DistanceSquared(Asset.Position()) <= RangeToDistanceSquared
                                (Asset.EffectiveRange())) {
                            Asset.PopCommand();
                            Asset.ResetStep();
                            continue;
                        }
                    }
                    TravelDirection = DRouterMap.FindRoute(DActualMap, Asset, MapTarget);

                    if (CGameDataTypes.EDirection.dMax != TravelDirection) {
                        Asset.Direction(TravelDirection);
                    } else {
                        CPosition TilePosition = new CPosition();
                        TilePosition.SetToTile(MapTarget);
                        if ((TilePosition == Asset.TilePosition()) || (CGameDataTypes.EDirection
                                .dMax != Asset.TilePosition().AdjacentTileDirection(TilePosition,
                                1))) {
                            Asset.PopCommand();
                            Asset.ResetStep();
                            continue;
                        } else if (aaHarvestLumber == NextCommand.DAction) {
                            CPosition NewPosition = DPlayers.get(Asset.Color().ordinal())
                                    .DActualMap.FindNearestReachableTileType(Asset.TilePosition
                                            (), CTerrainMap.ETileType.ttTree);
                            // Find new lumber
                            Asset.PopCommand();
                            Asset.PopCommand();

                            if (0 <= NewPosition.X()) {
                                NewPosition.SetFromTile(NewPosition);
                                Command.DAction = aaHarvestLumber;
                                Command.DAssetTarget = DPlayers.get(Asset.Color().ordinal())
                                        .CreateMarker(NewPosition, false);
                                Asset.PushCommand(Command);

                                SAssetCommand Command2 = new SAssetCommand();
                                Command2.DAction = CGameDataTypes.EAssetAction.aaWalk;
                                Command2.DAssetTarget = Command.DAssetTarget;
                                Asset.PushCommand(Command2);
                                Asset.ResetStep();
                                continue;
                            }
                        } else {
                            Command.DAction = CGameDataTypes.EAssetAction.aaNone;
                            Asset.PushCommand(Command);
                            Asset.ResetStep();
                            continue;
                        }
                    }
                }

                if (!Asset.MoveStep(DAssetOccupancyMap, DDiagonalOccupancyMap)) {
                    Asset.Direction(CGameDataTypes.EDirection.values()[((Asset.Position()
                            .TileOctant().ordinal() + CGameDataTypes.EDirection.dMax.ordinal() /
                            2) % CGameDataTypes.EDirection.dMax.ordinal())]);
                }
            }
        }

        // Forest regrowth
        for (int i = 0; i < DChoppedForest.size();) {
            CForest Forest = DChoppedForest.get(i);
            int NewGrowthPeriod = ForestGrowthPeriod(Forest.GetCurrentPos());
            Forest.UpdateGrowthPeriod(NewGrowthPeriod); //update growth period dynamically on each timestep
            //if tree's timesteps is bigger than the growth period
//            while (true) {
                if (Forest.GetGrowthPeriod() <= Forest.GetForestTime()) {
                    Forest.IncrementType(); //upgrade to next tree growth stage
                    int CurrentType = Forest.GetCurrentType();
                    CTerrainMap.ETileType TreeType = CTerrainMap.ETileType.ttNone;
                    if (CurrentType == 0) {
                        TreeType = CTerrainMap.ETileType.ttStump;
                    }
                    if (CurrentType == 1) {
                        TreeType = CTerrainMap.ETileType.ttSeedling;
                    }
                    if (CurrentType == 2) {
                        TreeType = CTerrainMap.ETileType.ttAdolescent;
                    }
                    if (CurrentType == 3) {
                        TreeType = CTerrainMap.ETileType.ttTree;
                    }
                    DActualMap.ChangeTileType(Forest.GetCurrentPos(), TreeType); //change tile on map
                    Forest.SetCurrentTime(0); //reset the tree timestep to 0
                }
                Forest.IncrementTime(); //update forest timestep
                if (3 == Forest.GetCurrentType()) {
                    Forest = DChoppedForest.remove(DChoppedForest.indexOf(Forest));
                    //DChoppedForest.erase(std::remove(DChoppedForest.begin(), DChoppedForest.end(), DForest));
                } //remove fully grown trees from vector of chopped down trees
                else {
//                    break;
                    ++i;
                }
//            }
        }
    }

    int ForestGrowthPeriod(CPosition Forest)
    {
        int NumAdjForest = GetAdjForest(Forest);
        int Period = 2700 / (1 + NumAdjForest); //Nitta's tree growth period formula
        return Period;
    }

    /**
     *  getAdjForest() goes to every adjacent tile around the specified tile and returns a count of the # of the forest tiles.
     *
     **/
    int GetAdjForest(CPosition forest)
    {

        int NumAdjForest = 0;
        for (int XOffset = -1; XOffset <= 1; XOffset++) {
            for (int YOffset = 1; YOffset >= -1; YOffset--) {
                int MyX = forest.X();
                int MyY = forest.Y();
                if (CTerrainMap.ETileType.ttTree == DActualMap.TileType(MyX + XOffset, MyY + YOffset)) {
                    NumAdjForest++;
                }

            }
        } //loop through all 9 tiles around the specified tile
        return NumAdjForest;
    }

    /**
     * Calls each individual player's CPlayer.ClearGameEvents.
     * CPlayer.ClearGameEvents is found in CGameModel, and clears a vector of DGameEvents.
     */
    public void ClearGameEvents() {

        for (int PlayerIndex = 0; PlayerIndex < pcMax.ordinal(); PlayerIndex++) {
            DPlayers.get(PlayerIndex).ClearGameEvents();
        }
    }
}
