//import com.ecs160.nittacraft.CPlayerAsset;
//import com.ecs160.nittacraft.CPlayerAssetType;
//import com.ecs160.nittacraft.CPlayerData;
//import com.ecs160.nittacraft.CPlayerUpgrade;
//import com.ecs160.nittacraft.CPosition;
//import com.ecs160.nittacraft.SAssetCommand;
//import com.ecs160.nittacraft.capabilities.CActivatedPlayerCapability;
//import com.ecs160.nittacraft.capabilities.CPlayerCapability;
//
//class CPlayerCapabilityBuildRanger extends CPlayerCapability {
//    protected class CRegistrant{
//        public CRegistrant() {
//            CPlayerCapabilityBuildRanger.super.Register(new CPlayerCapabilityBuildRanger("Ranger"));
//        }
//    }
//    protected static CRegistrant DRegistrant;
//
//    protected class CActivatedCapability extends CActivatedPlayerCapability {
//        protected CPlayerAssetType DUpgradingType;
//        protected String DUnitName;
//        protected int DCurrentStep;
//        protected int DTotalSteps;
//        protected int DLumber;
//        protected int DGold;
//
//        public CActivatedCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target, CPlayerAssetType upgradingtype, String unitname, int lumber, int gold, int steps) {
//            DUnitName = unitname;
//            DCurrentStep = 0;
//            DTotalSteps = steps;
//            DLumber = lumber;
//            DGold = gold;
//            DPlayerData.DecrementLumber(DLumber);
//            DPlayerData.DecrementGold(DGold);
//            if (atLumberMill == actor.Type()) {
//                DUpgradingType = upgradingtype;
//                DUpgradingType.RemoveCapability(CPlayerCapability.NameToType("Build" + DUnitName));
//            }
//            else if (atBarracks == actor.Type()) {
//                SAssetCommand AssetCommand;
//
//                AssetCommand.DAction = aaConstruct;
//                AssetCommand.DAssetTarget = DActor;
//                DTarget.PushCommand(AssetCommand);
//            }
//        }
//
//        public int PercentComplete(int max) {
//            return DCurrentStep * max / DTotalSteps;
//        }
//        public boolean IncrementStep() {
//            if (atBarracks == DActor.Type()) {
//                int AddHitPoints = (DTarget.MaxHitPoints() * (DCurrentStep + 1) / DTotalSteps) - (DTarget.MaxHitPoints() * DCurrentStep / DTotalSteps);
//
//                DTarget.IncrementHitPoints(AddHitPoints);
//                if (DTarget.HitPoints() > DTarget.MaxHitPoints()) {
//                    DTarget.HitPoints(DTarget.MaxHitPoints());
//                }
//            }
//
//            DCurrentStep++;
//            DActor.IncrementStep();
//            if (DCurrentStep >= DTotalSteps) {
//                SGameEvent TempEvent;
//
//                if (atLumberMill == DActor.Type()) {
//                    auto BarracksIterator = DPlayerData.AssetTypes().find("Barracks");
//                    auto RangerIterator = DPlayerData.AssetTypes().find("Ranger");
//                    auto LumberMillIterator = DPlayerData.AssetTypes().find("LumberMill");
//
//                    TempEvent.DType = etWorkComplete;
//                    TempEvent.DAsset = DActor;
//
//                    BarracksIterator.second.AddCapability(actBuildRanger);
//                    BarracksIterator.second.RemoveCapability(actBuildArcher);
//                    LumberMillIterator.second.AddCapability(actLongbow);
//                    LumberMillIterator.second.AddCapability(actRangerScouting);
//                    LumberMillIterator.second.AddCapability(actMarksmanship);
//                    // Upgrade all Archers
//                    for (auto WeakAsset : DPlayerData.Assets()) {
//                        if (auto Asset = WeakAsset.lock()) {
//                            if (atArcher == Asset.Type()) {
//                                int HitPointIncrement = RangerIterator.second.HitPoints() - Asset.MaxHitPoints();
//
//                                Asset.ChangeType(RangerIterator.second);
//                                Asset.IncrementHitPoints(HitPointIncrement);
//                            }
//                        }
//                    }
//                }
//                else if (atBarracks == DActor.Type()) {
//                    TempEvent.DType = etReady;
//                    TempEvent.DAsset = DTarget;
//
//                    DTarget.PopCommand();
//                    DTarget.TilePosition(DPlayerData.PlayerMap().FindAssetPlacement(DTarget, DActor, CPosition(DPlayerData.PlayerMap().Width() - 1, DPlayerData.PlayerMap().Height() - 1)));
//                }
//                DPlayerData.AddGameEvent(TempEvent);
//                DActor.PopCommand();
//                return true;
//            }
//            return false;
//        }
//        public void Cancel() {
//            DPlayerData.IncrementLumber(DLumber);
//            DPlayerData.IncrementGold(DGold);
//            if (atLumberMill == DActor.Type()) {
//                DUpgradingType.AddCapability(CPlayerCapability.NameToType("Build") + DUnitName);
//            }
//            else if (atBarracks == DActor.Type()) {
//                DPlayerData.DeleteAsset(DTarget);
//            }
//            DActor.PopCommand();
//        }
//    }
//    protected String DUnitName;
//    protected CPlayerCapabilityBuildRanger(String unitname) { //const
//        super("Build" + unitname, ttNone);
//        DUnitName = unitname;
//    }
//
//    public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
//        if (atLumberMill == actor.Type()) {
//            auto Upgrade = CPlayerUpgrade.FindUpgradeFromName("Build" + DUnitName);
//
//            if (Upgrade) {
//
//                if (UpgradeLumberCost() > playerData.Lumber()) {
//                    return false;
//                }
//                if (Upgrade.GoldCost() > playerData.Gold()) {
//                    return false;
//                }
//                if (!playerData.AssetRequirementsMet(DUnitName)) {
//                    return false;
//                }
//            }
//        }
//        else if (atBarracks == actor.Type()) {
//            auto AssetIterator = playerData.AssetTypes().find(DUnitName);
//
//            if (AssetIterator != playerData.AssetTypes().end()) {
//                auto AssetType = AssetIterator.second;
//                if (AssetType.LumberCost() > playerData.Lumber()) {
//                    return false;
//                }
//                if (AssetType.GoldCost() > playerData.Gold()) {
//                    return false;
//                }
//                if ((AssetType.FoodConsumption() + playerData.FoodConsumption()) > playerData.FoodProduction()) {
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }
//    public boolean CanApply(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target) {
//        return CanInitiate(actor, playerData);
//    }
//    public boolean ApplyCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target) {
//        if (atLumberMill == actor.Type()) {
//            auto Upgrade = CPlayerUpgrade.FindUpgradeFromName("Build" + DUnitName);
//
//            if (Upgrade) {
//                SAssetCommand NewCommand;
//
//                actor.ClearCommand();
//                NewCommand.DAction = aaCapability;
//                NewCommand.DCapability = AssetCapabilityType();
//                NewCommand.DAssetTarget = target;
//                NewCommand.DActivatedCapability = std.make_shared< CActivatedCapability >(actor, playerData, target, actor.AssetType(), DUnitName, Upgrade.LumberCost(), Upgrade.GoldCost(), CPlayerAsset.UpdateFrequency() * Upgrade.ResearchTime())
//                ;
//                actor.PushCommand(NewCommand);
//
//                return true;
//            }
//        }
//        else if (atBarracks == actor.Type()) {
//            auto AssetIterator = playerData.AssetTypes().find(DUnitName);
//
//            if (AssetIterator != playerData.AssetTypes().end()) {
//                auto AssetType = AssetIterator.second;
//                auto NewAsset = playerData.CreateAsset(DUnitName);
//                SAssetCommand NewCommand;
//                CPosition TilePosition;
//                TilePosition.SetToTile(actor.Position());
//                NewAsset.TilePosition(TilePosition);
//                NewAsset.HitPoints(1);
//
//                NewCommand.DAction = aaCapability;
//                NewCommand.DCapability = AssetCapabilityType();
//                NewCommand.DAssetTarget = NewAsset;
//                NewCommand.DActivatedCapability = std.make_shared< CActivatedCapability >(actor, playerData, NewAsset, actor.AssetType(), DUnitName, AssetType.LumberCost(), AssetType.GoldCost(), CPlayerAsset.UpdateFrequency() * AssetType.BuildTime());
//                actor.PushCommand(NewCommand);
//            }
//        }
//        return false;
//    }
//}