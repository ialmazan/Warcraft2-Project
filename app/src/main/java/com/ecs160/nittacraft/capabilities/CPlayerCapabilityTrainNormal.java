package com.ecs160.nittacraft.capabilities;

import android.util.Log;

import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerAssetType;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.CPosition;
import com.ecs160.nittacraft.SAssetCommand;
import com.ecs160.nittacraft.CGameDataTypes.*;
import com.ecs160.nittacraft.CGameModel.*;

// Build normal buildings capability
public class CPlayerCapabilityTrainNormal extends CPlayerCapability {
    protected class CRegistrant {
        public CRegistrant() {
            CPlayerCapability.Register(new CPlayerCapabilityTrainNormal("Peasant"));
            CPlayerCapability.Register(new CPlayerCapabilityTrainNormal("Footman"));
            CPlayerCapability.Register(new CPlayerCapabilityTrainNormal("Archer"));
            CPlayerCapability.Register(new CPlayerCapabilityTrainNormal("Ranger"));
            CPlayerCapability.Register(new CPlayerCapabilityTrainNormal("Knight"));
        }
    }
    protected static CRegistrant DRegistrant;

    public class CActivatedCapability extends CActivatedPlayerCapability{
        protected int DCurrentStep;
        protected int DTotalSteps;
        protected int DLumber;
        protected int DGold;

        public CActivatedCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
                target, int lumber, int gold, int steps) {
            super(actor, playerData, target);
            Log.d("nugget", "cactivatedcability constructor");
            SAssetCommand AssetCommand = new SAssetCommand();

            DCurrentStep = 0;
            DTotalSteps = steps;
            DLumber = lumber;
            DGold = gold;
            DPlayerData.DecrementLumber(DLumber);
            DPlayerData.DecrementGold(DGold);
            AssetCommand.DAction = EAssetAction.aaConstruct;
            AssetCommand.DAssetTarget = DActor;
            DTarget.PushCommand(AssetCommand);
        }

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
                Log.d("create", "Peasant is created");
                SGameEvent TempEvent = new SGameEvent();

                TempEvent.DType = EEventType.etReady;
                TempEvent.DAsset = DTarget;
                DPlayerData.AddGameEvent(TempEvent);

                DTarget.PopCommand();
                DActor.PopCommand();

                DTarget.TilePosition(DPlayerData.DActualMap().FindAssetPlacement(DTarget, DActor,
                        new CPosition(DPlayerData.DActualMap().Width()-1,
                        DPlayerData.PlayerMap().Height()-1)));
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
    protected String DUnitName;
    public CPlayerCapabilityTrainNormal(String unitname) {
        super("Build" + unitname, ETargetType.ttNone);
        DUnitName = unitname;
    }

    public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
        CPlayerAssetType Iterator = playerData.AssetTypes().get(DUnitName);

        if (Iterator != null) {
            CPlayerAssetType AssetType = Iterator;
            if (AssetType.LumberCost() > playerData.Lumber()) {
                return false;
            }
            if (AssetType.GoldCost() > playerData.Gold()) {
                return false;
            }
            if ((AssetType.FoodConsumption() + playerData.FoodConsumption()) > playerData
                    .FoodProduction()) {
                return false;
            }
            if (!playerData.AssetRequirementsMet(DUnitName)) {
                return false;
            }
        }

        return true;
    }

    public boolean CanApply(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target) {
        return CanInitiate(actor, playerData);
    }

    public boolean ApplyCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
            target) {

        CPlayerAssetType Iterator = playerData.AssetTypes().get(DUnitName);
        if (Iterator != null) {
            CPlayerAssetType AssetType = Iterator;
            CPlayerAsset NewAsset = playerData.CreateAsset(DUnitName);
            SAssetCommand NewCommand = new SAssetCommand();
            CPosition TilePosition = new CPosition();
            TilePosition.SetToTile(actor.Position());

            NewAsset.Position().SetFromTile(TilePosition);
            NewAsset.TilePosition(TilePosition);
            NewAsset.HitPoints(1);

            NewCommand.DAction = EAssetAction.aaCapability;
            NewCommand.DCapability = AssetCapabilityType();
            NewCommand.DAssetTarget = NewAsset;

            // cost of resources was hardcoded to 0
            NewCommand.DActivatedCapability = new CActivatedCapability(actor, playerData,
                    NewAsset, AssetType.LumberCost(), AssetType.GoldCost(), CPlayerAsset
                    .UpdateFrequency() * AssetType.BuildTime());

            actor.PushCommand(NewCommand);
            actor.ResetStep();
        }
        return false;
    }
}
