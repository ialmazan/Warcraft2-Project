package com.ecs160.nittacraft.capabilities;

import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerAssetType;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.CPlayerUpgrade;
import com.ecs160.nittacraft.SAssetCommand;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaCapability;
import static com.ecs160.nittacraft.capabilities.CPlayerCapability.ETargetType.ttNone;

public class CPlayerCapabilityUnitUpgrade extends CPlayerCapability {
    protected class CRegistrant{
        public CRegistrant() {
            CPlayerCapabilityUnitUpgrade.super.Register(new CPlayerCapabilityUnitUpgrade("WeaponUpgrade2"));
            CPlayerCapabilityUnitUpgrade.super.Register(new CPlayerCapabilityUnitUpgrade("WeaponUpgrade3"));
            CPlayerCapabilityUnitUpgrade.super.Register(new CPlayerCapabilityUnitUpgrade("ArmorUpgrade2"));
            CPlayerCapabilityUnitUpgrade.super.Register(new CPlayerCapabilityUnitUpgrade("ArmorUpgrade3"));
            CPlayerCapabilityUnitUpgrade.super.Register(new CPlayerCapabilityUnitUpgrade("ArrowUpgrade2"));
            CPlayerCapabilityUnitUpgrade.super.Register(new CPlayerCapabilityUnitUpgrade("ArrowUpgrade3"));
            CPlayerCapabilityUnitUpgrade.super.Register(new CPlayerCapabilityUnitUpgrade("Longbow"));
            CPlayerCapabilityUnitUpgrade.super.Register(new CPlayerCapabilityUnitUpgrade("RangerScouting"));
            CPlayerCapabilityUnitUpgrade.super.Register(new CPlayerCapabilityUnitUpgrade("Marksmanship"));
            CPlayerCapabilityUnitUpgrade.super.Register(new CPlayerCapabilityUnitUpgrade("BuildRanger"));
            CPlayerCapabilityUnitUpgrade.super.Register(new CPlayerCapabilityUnitUpgrade("BuildKnight"));
        }
    }
    protected static CRegistrant DRegistrant;

    protected class CActivatedCapability extends CActivatedPlayerCapability {
        protected CPlayerAssetType DUpgradingType;
        protected String DUpgradeName;
        protected int DCurrentStep;
        protected int DTotalSteps;
        protected int DLumber;
        protected int DGold;

        public CActivatedCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
                target, CPlayerAssetType upgradingtype, String upgradename, int lumber, int gold,
                                    int steps) {
            super(actor, playerData, target);

            SAssetCommand AssetCommand;

            DUpgradingType = upgradingtype;
            DUpgradeName = upgradename;
            DCurrentStep = 0;
            DTotalSteps = steps;
            DLumber = lumber;
            DGold = gold;
            DPlayerData.DecrementLumber(DLumber);
            DPlayerData.DecrementGold(DGold);
            DUpgradingType.RemoveCapability(CPlayerCapability.NameToType(DUpgradeName));
        }

        public int PercentComplete(int max) {
            return DCurrentStep * max / DTotalSteps;
        }
        public boolean IncrementStep() {
            DCurrentStep++;
            DActor.IncrementStep();
            if (DCurrentStep >= DTotalSteps) {
                DPlayerData.AddUpgrade(DUpgradeName);
                DActor.PopCommand();
                if (DUpgradeName.indexOf("2") == (DUpgradeName.length()-1)) {
                    DUpgradingType.AddCapability(CPlayerCapability.NameToType(DUpgradeName
                            .substring(0, DUpgradeName.length()-1) + "3"));
                }

                return true;
            }
            return false;
        }
        public void Cancel() {
            DPlayerData.IncrementLumber(DLumber);
            DPlayerData.IncrementGold(DGold);
            DUpgradingType.AddCapability(CPlayerCapability.NameToType(DUpgradeName));
            DActor.PopCommand();
        }
    }

    private String DUpgradeName;
    public CPlayerCapabilityUnitUpgrade(String upgradename) {
        super(upgradename, ttNone);
        DUpgradeName = upgradename;
    }

    public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
        CPlayerUpgrade Upgrade = CPlayerUpgrade.FindUpgradeFromName(DUpgradeName);

        if (Upgrade != null) {

            if (Upgrade.LumberCost() > playerData.Lumber()) {
                return false;
            }
            if (Upgrade.GoldCost() > playerData.Gold()) {
                return false;
            }
            /*
            if (!playerData->AssetRequirementsMet(DUpgradeName)) {
                return false;
            }
            */
        }

        return true;
    }
    public boolean CanApply(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target) {
        return CanInitiate(actor, playerData);
    }
    public boolean ApplyCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
            target) {
        CPlayerUpgrade Upgrade = CPlayerUpgrade.FindUpgradeFromName(DUpgradeName);

        if (Upgrade != null) {
            SAssetCommand NewCommand = new SAssetCommand();
            actor.ClearCommand();
            NewCommand.DAction = aaCapability;
            NewCommand.DCapability = AssetCapabilityType();
            NewCommand.DAssetTarget = target;
            NewCommand.DActivatedCapability = new CActivatedCapability(actor, playerData, target,
                    actor.AssetType(), DUpgradeName, Upgrade.LumberCost(), Upgrade.GoldCost(),
                    CPlayerAsset.UpdateFrequency() * Upgrade.ResearchTime());
            actor.PushCommand(NewCommand);
            return true;
        }
        return false;
    }
}
