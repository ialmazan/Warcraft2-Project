package com.ecs160.nittacraft.capabilities;

import android.util.Log;

import com.ecs160.nittacraft.CGameModel;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerAssetType;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.SAssetCommand;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaCapability;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaConstruct;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaStandGround;
import static com.ecs160.nittacraft.CGameModel.EEventType.etWorkComplete;
import static com.ecs160.nittacraft.capabilities.CPlayerCapability.ETargetType.ttNone;

// Build normal buildings capability
public class CPlayerCapabilityBuildingUpgrade extends CPlayerCapability {
    protected class CRegistrant{
        public CRegistrant() {
            CPlayerCapability.Register(new CPlayerCapabilityBuildingUpgrade("Keep"));
            CPlayerCapability.Register(new CPlayerCapabilityBuildingUpgrade("Castle"));
            CPlayerCapability.Register(new CPlayerCapabilityBuildingUpgrade("GuardTower"));
            CPlayerCapability.Register(new CPlayerCapabilityBuildingUpgrade("CannonTower"));

        }
    }

    protected static CRegistrant DRegistrant;

    protected class CActivatedCapability extends CActivatedPlayerCapability {
        protected CPlayerAssetType DOriginalType;
        protected CPlayerAssetType DUpgradeType;
        protected int DCurrentStep;
        protected int DTotalSteps;
        protected int DLumber;
        protected int DGold;

        public CActivatedCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
                target, CPlayerAssetType origtype, CPlayerAssetType upgradetype, int lumber, int
                gold, int steps) {
            super(actor, playerData, target);
            SAssetCommand AssetCommand;

            DOriginalType = origtype;
            DUpgradeType = upgradetype;
            DCurrentStep = 0;
            DTotalSteps = steps;
            DLumber = lumber;
            DGold = gold;
            DPlayerData.DecrementLumber(DLumber);
            DPlayerData.DecrementGold(DGold);
        }

        public int PercentComplete(int max) {
            return DCurrentStep * max / DTotalSteps;
        }
        public boolean IncrementStep() {
            int AddHitPoints = ((DUpgradeType.HitPoints() - DOriginalType.HitPoints()) *
                    (DCurrentStep + 1) / DTotalSteps) - ((DUpgradeType.HitPoints() -
                    DOriginalType.HitPoints()) * DCurrentStep / DTotalSteps);

            if (0 == DCurrentStep) {
                SAssetCommand AssetCommand = DActor.CurrentCommand();
                AssetCommand.DAction = aaConstruct;
                DActor.PopCommand();
                DActor.PushCommand(AssetCommand);
                DActor.ChangeType(DUpgradeType);
                DActor.ResetStep();
            }

            DActor.IncrementHitPoints(AddHitPoints);
            if (DActor.HitPoints() > DActor.MaxHitPoints()) {
                DActor.HitPoints(DActor.MaxHitPoints());
            }
            DCurrentStep++;
            DActor.IncrementStep();
            if (DCurrentStep >= DTotalSteps) {
                CGameModel.SGameEvent TempEvent = new CGameModel.SGameEvent();

                TempEvent.DType = etWorkComplete;
                TempEvent.DAsset = DActor;
                DPlayerData.AddGameEvent(TempEvent);

                DActor.PopCommand();
                if (DActor.Range() != 0) {
                    Log.d("tower", "DActor range " + DActor.Range());
                    SAssetCommand Command = new SAssetCommand();
                    Command.DAction = aaStandGround;
                    DActor.PushCommand(Command);
                }
                return true;
            }
            return false;
        }
        public void Cancel() {
            DPlayerData.IncrementLumber(DLumber);
            DPlayerData.IncrementGold(DGold);
            DActor.ChangeType(DOriginalType);
            DActor.PopCommand();
        }
    }
    protected String DBuildingName;
    public CPlayerCapabilityBuildingUpgrade(String buildingname) {
        super("Build" + buildingname, ttNone);
        DBuildingName = buildingname;
    }

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
            if (!playerData.AssetRequirementsMet(DBuildingName)) {
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
        CPlayerAssetType Iterator = playerData.AssetTypes().get(DBuildingName);

        if (Iterator != null) {
            SAssetCommand NewCommand = new SAssetCommand();
            CPlayerAssetType AssetType = Iterator;

            actor.ClearCommand();
            NewCommand.DAction = aaCapability;
            NewCommand.DCapability = AssetCapabilityType();
            NewCommand.DAssetTarget = target;
            NewCommand.DActivatedCapability = new CActivatedCapability(actor, playerData, target,
                    actor.AssetType(), AssetType, AssetType.LumberCost(), AssetType.GoldCost(),
                    CPlayerAsset.UpdateFrequency() * AssetType.BuildTime());
            actor.PushCommand(NewCommand);
            return true;
        }
        return false;
    }
}