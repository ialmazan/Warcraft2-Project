package com.ecs160.nittacraft.capabilities;

import com.ecs160.nittacraft.CGameDataTypes;
import com.ecs160.nittacraft.CGameModel.SGameEvent;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerAssetType;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.CPosition;
import com.ecs160.nittacraft.SAssetCommand;

import java.util.Objects;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaCapability;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaConstruct;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaWalk;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGoldVein;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atNone;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dMax;
import static com.ecs160.nittacraft.CGameModel.EEventType.etWorkComplete;
import static com.ecs160.nittacraft.capabilities.CPlayerCapability.ETargetType.ttTerrainOrAsset;

public class CPlayerCapabilityBuildNormal extends CPlayerCapability{
    protected class CRegistrant {
        public CRegistrant() {
            CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("TownHall"));
            CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("Farm"));
            CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("Barracks"));
            CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("LumberMill"));
            CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("Blacksmith"));
            CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("ScoutTower"));
            CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("GoldMine"));
            CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("Wall"));
        }
    }
    protected static CRegistrant DRegistrant;

    public class CActivatedCapability extends CActivatedPlayerCapability {
        protected int DCurrentStep;
        protected int DTotalSteps;
        protected int DLumber;
        protected int DGold;

        public CActivatedCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
                target, int lumber, int gold, int steps) {
            super(actor, playerData, target);
            SAssetCommand AssetCommand = new SAssetCommand();

            DCurrentStep = 0;
            DTotalSteps = steps;
            DLumber = lumber;
            DGold = gold;
            DPlayerData.DecrementLumber(DLumber);
            DPlayerData.DecrementGold(DGold);
            AssetCommand.DAction = aaConstruct;
            AssetCommand.DAssetTarget = DActor;
            DTarget.PushCommand(AssetCommand);
        }
//        virtual ~CActivatedCapability() {};

        public int PercentComplete(int max) {
            return DCurrentStep * max / DTotalSteps;
        }

        public boolean IncrementStep() {
            int AddHitPoints = (DTarget.MaxHitPoints() * (DCurrentStep + 1) / DTotalSteps) -
                    (DTarget.MaxHitPoints() * DCurrentStep / DTotalSteps);

            DTarget.IncrementHitPoints(AddHitPoints);
            if (DTarget.HitPoints() > DTarget.MaxHitPoints()) {
                DTarget.HitPoints(DTarget.MaxHitPoints());
            }
            DCurrentStep++;
            DActor.IncrementStep();
            DTarget.IncrementStep();

            if (DCurrentStep >= DTotalSteps) {
                SGameEvent TempEvent = new SGameEvent();

                TempEvent.DType = etWorkComplete;
                TempEvent.DAsset = DActor;
                DPlayerData.AddGameEvent(TempEvent);

                DTarget.PopCommand();
                DActor.PopCommand();
                DActor.TilePosition(DPlayerData.DActualMap().FindAssetPlacement(DActor, DTarget,
                        new CPosition(DPlayerData.PlayerMap().Width()-1, DPlayerData.PlayerMap()
                                .Height() - 1)));
                DActor.ResetStep();
                DTarget.ResetStep();

                return true;
            }
            return false;
        }

        public void Cancel() {
            DPlayerData.IncrementLumber(DLumber);
            DPlayerData.IncrementGold(DGold);
            DPlayerData.DeleteAsset(DTarget);
            DActor.PopCommand();
        }
    }
    protected String DBuildingName;
    public CPlayerCapabilityBuildNormal(String buildingname) {
        super("Build" + buildingname, ttTerrainOrAsset);
        DBuildingName = buildingname;
    }

//    virtual ~CPlayerCapabilityBuildNormal() {};

    public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
        CPlayerAssetType Iterator = playerData.AssetTypes().get(DBuildingName);

        if (Iterator != null) {
            CPlayerAssetType AssetType = Iterator;
            if (AssetType.LumberCost() > playerData.Lumber()) {
                return false;
            }
            if (AssetType.GoldCost() > playerData.Gold()) {
                return false;
            }
        }

        return true;
    }

    public boolean CanApply(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target) {
        CPlayerAssetType Iterator = playerData.AssetTypes().get(DBuildingName);

        if (Objects.equals("GoldMine", DBuildingName) && (target.LumberCost() <= playerData
                .Lumber())) {
            return target.Type() == atGoldVein;
        }
        if ((actor != target) && (atNone != target.Type())) {
            return false;
        }
        if (Iterator != null) {
            CPlayerAssetType AssetType = Iterator;

            if (AssetType.LumberCost() > playerData.Lumber()) {
                return false;
            }
            if (AssetType.GoldCost() > playerData.Gold()) {
                return false;
            }
//FIXME Figure out why asset placement validation doesn't work
//            Log.d("CPCBN", "Target:" + target.TilePosition().X() +" " + target.TilePosition().Y() + " " +target.PositionX());
//            if (!playerData.DActualMap().CanPlaceAsset(target.TilePosition(), AssetType.Size(), actor)) {
//                return false;
//            }
        }

        return true;
    }

    public boolean ApplyCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
            target) {
        CPlayerAssetType Iterator = playerData.AssetTypes().get(DBuildingName);
        if (Iterator != null) {
            SAssetCommand NewCommand = new SAssetCommand();

            CPosition ClosestPosition2 = target.ClosestPosition(actor.Position());
            CPosition TilePosition2 = new CPosition();
            //TilePosition2.SetToTile(ClosestPosition2);
            TilePosition2.SetToTile((target.Position()));
            CGameDataTypes.EDirection VeinDirection = actor.TilePosition().AdjacentTileDirection
                    (TilePosition2, 1);

            actor.ClearCommand();

            if (Objects.equals("GoldMine", DBuildingName) && dMax != VeinDirection) {
                CPlayerAssetType AssetType = Iterator;

                NewCommand.DAction = aaCapability;
                NewCommand.DCapability = AssetCapabilityType();
                NewCommand.DAssetTarget = target;
                NewCommand.DActivatedCapability = new CActivatedCapability (actor, playerData,
                        target, AssetType.LumberCost(), AssetType.GoldCost(), CPlayerAsset
                        .UpdateFrequency() * AssetType.BuildTime());
                actor.PushCommand(NewCommand);
            }
            if (actor.TilePosition().equals(target.TilePosition())) {
                CPlayerAssetType AssetType = Iterator;
                CPlayerAsset NewAsset = playerData.CreateAsset(DBuildingName);
                CPosition TilePosition = new CPosition();
                TilePosition.SetToTile(target.Position());
                NewAsset.TilePosition(TilePosition);
                NewAsset.HitPoints(1);

                NewCommand.DAction = aaCapability;
                NewCommand.DCapability = AssetCapabilityType();
                NewCommand.DAssetTarget = NewAsset;
                NewCommand.DActivatedCapability = new CActivatedCapability(actor, playerData,
                        NewAsset, AssetType.LumberCost(), AssetType.GoldCost(), CPlayerAsset
                        .UpdateFrequency() * AssetType.BuildTime());
                actor.PushCommand(NewCommand);
            } else {

                NewCommand.DAction = aaCapability;
                NewCommand.DCapability = AssetCapabilityType();
                NewCommand.DAssetTarget = target;
                actor.PushCommand(NewCommand);

                SAssetCommand NewCommand2 = new SAssetCommand();
                NewCommand2.DAction = aaWalk;
                NewCommand2.DCapability = AssetCapabilityType();
                NewCommand2.DAssetTarget = target;
                actor.PushCommand(NewCommand2);
            }
            return true;
        }
        return false;
    }
}