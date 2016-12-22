package com.ecs160.nittacraft.capabilities;

import com.ecs160.nittacraft.CGameDataTypes;
import com.ecs160.nittacraft.CGameModel;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.SAssetCommand;
import com.ecs160.nittacraft.maps.CTerrainMap;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.*;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.*;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.*;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dMax;
import static com.ecs160.nittacraft.CGameModel.EEventType.etAcknowledge;
import static com.ecs160.nittacraft.capabilities.CPlayerCapability.ETargetType.ttAsset;
import static com.ecs160.nittacraft.capabilities.CPlayerCapability.ETargetType.ttTerrainOrAsset;

public class CBasicCapabilities {
    public class CPlayerCapabilityMove extends CPlayerCapability {
        protected class CRegistrant {
            public CRegistrant() {
                CPlayerCapability.Register(new CPlayerCapabilityMove());
            }
        }

        protected CRegistrant DRegistrant;

        protected CPlayerCapabilityMove() {
            super("Move", ttTerrainOrAsset);
        }

        public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
            return actor.Speed() > 0;
        }
        public boolean CanApply(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target) {
            return actor.Speed() > 0;
        }
        public boolean ApplyCapability(CPlayerAsset actor, CPlayerData playerData,CPlayerAsset
                target) {
            if (actor.TilePosition() != target.TilePosition()) {
                SAssetCommand NewCommand = new SAssetCommand();

                NewCommand.DAction = aaCapability;
                NewCommand.DCapability = AssetCapabilityType();
                NewCommand.DAssetTarget = target;
                NewCommand.DActivatedCapability = new CActivatedPlayerCapability(actor,
                        playerData, target) {
                    @Override
                    public int PercentComplete(int max) {
                        return 0;
                    }

                    @Override
                    public boolean IncrementStep() {
                        SAssetCommand AssetCommand = new SAssetCommand();
                        CGameModel.SGameEvent TempEvent = new CGameModel.SGameEvent();

                        TempEvent.DType = etAcknowledge;
                        TempEvent.DAsset = DActor;
                        DPlayerData.AddGameEvent(TempEvent);

                        AssetCommand.DAction = aaWalk;
                        AssetCommand.DAssetTarget = DTarget;
                        if (!DActor.TileAligned()) {
                            DActor.Direction(CGameDataTypes.EDirection.values()[DActor.Position()
                                    .TileOctant().ordinal() + dMax.ordinal() / 2 % dMax.ordinal()]);
                        }
                        DActor.ClearCommand();
                        DActor.PushCommand(AssetCommand);
                        return true;
                    }

                    @Override
                    public void Cancel() {
                        DActor.PopCommand();
                    }
                };
                actor.ClearCommand();
                actor.PushCommand(NewCommand);
                return true;
            }

            return false;
        }
    }

    public class CPlayerCapabilityMineHarvest extends CPlayerCapability {
        protected class CRegistrant {
            public CRegistrant() {
                CPlayerCapability.Register(new CPlayerCapabilityMineHarvest());
            }
        }

        protected CRegistrant DRegistrant;

        protected CPlayerCapabilityMineHarvest() {
            super("Mine", ETargetType.ttTerrainOrAsset);
        }

        public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
            return actor.HasCapability(actMine);
        }

        public boolean CanApply(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target) {

            return actor.HasCapability(actMine) && !(0 > actor.Lumber() || 0 > actor.Gold()) &&
                    (atGoldMine == target.Type() || atNone == target.Type() && CTerrainMap
                            .ETileType.ttTree == playerData.PlayerMap().TileType(target
                            .TilePosition()));
        }

        public boolean ApplyCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
                target) {
            SAssetCommand NewCommand= new SAssetCommand();

            NewCommand.DAction = aaCapability;
            NewCommand.DCapability = AssetCapabilityType();
            NewCommand.DAssetTarget = target;
            NewCommand.DActivatedCapability = new CActivatedPlayerCapability(actor, playerData,
                    target) {
                @Override
                public int PercentComplete(int max) {
                    return 0;
                }

                @Override
                public boolean IncrementStep() {
                    SAssetCommand AssetCommand = new SAssetCommand();
                    CGameModel.SGameEvent TempEvent= new CGameModel.SGameEvent();

                    TempEvent.DType = etAcknowledge;
                    TempEvent.DAsset = DActor;
                    DPlayerData.AddGameEvent(TempEvent);

                    AssetCommand.DAssetTarget = DTarget;
                    if (atGoldMine == DTarget.Type()) {
                        AssetCommand.DAction = aaMineGold;
                    }
                    else {
                        AssetCommand.DAction = aaHarvestLumber;
                    }
                    DActor.ClearCommand();
                    DActor.PushCommand(AssetCommand);
                    AssetCommand.DAction = aaWalk;
                    if (!DActor.TileAligned()) {
                        DActor.Direction(CGameDataTypes.EDirection.values()[DActor.Position()
                                .TileOctant().ordinal() + dMax.ordinal()/2 % dMax.ordinal()]);
                    }
                    DActor.PushCommand(AssetCommand);
                    return true;
                }

                @Override
                public void Cancel() {
                    DActor.PopCommand();
                }
            };
            actor.ClearCommand();
            actor.PushCommand(NewCommand);

            return true;
        }
    }

    public class CPlayerCapabilityStandGround extends CPlayerCapability{
        protected class CRegistrant{
            public CRegistrant() {
                CPlayerCapability.Register(new CPlayerCapabilityStandGround());
            }
        }
        protected CRegistrant DRegistrant;


        public CPlayerCapabilityStandGround() {
            super("StandGround", ETargetType.ttNone);
        }

        public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
            return true;
        }


        public boolean CanApply(CPlayerAsset actor,  CPlayerData playerData, CPlayerAsset target) {
            return true;
        }

        public boolean ApplyCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
                target) {
            SAssetCommand NewCommand = new SAssetCommand();

            NewCommand.DAction = CGameDataTypes.EAssetAction.aaCapability;
            NewCommand.DCapability = AssetCapabilityType();
            NewCommand.DAssetTarget = target;
            NewCommand.DActivatedCapability = new CActivatedPlayerCapability(actor, playerData,
                    target) {
                @Override
                public int PercentComplete(int max) {
                    return 0;
                }

                @Override
                public boolean IncrementStep() {
                    SAssetCommand AssetCommand = new SAssetCommand();
                    CGameModel.SGameEvent TempEvent = new CGameModel.SGameEvent();

                    TempEvent.DType = etAcknowledge;
                    TempEvent.DAsset = DActor;
                    DPlayerData.AddGameEvent(TempEvent);

                    AssetCommand.DAssetTarget = DPlayerData.CreateMarker(DActor.Position(), false);
                    AssetCommand.DAction = aaStandGround;

                    DActor.ClearCommand();
                    DActor.PushCommand(AssetCommand);

                    if (!DActor.TileAligned()) {
                        AssetCommand.DAction = aaWalk;
                        DActor.Direction(CGameDataTypes.EDirection.values()[DActor.Position()
                                .TileOctant().ordinal() + dMax.ordinal() / 2 % dMax.ordinal()]);
                        DActor.PushCommand(AssetCommand);
                    }

                    return true;
                }

                @Override
                public void Cancel() {
                    DActor.PopCommand();
                }
            };
            actor.ClearCommand();
            actor.PushCommand(NewCommand);

            return true;
        }
    }

    public class CPlayerCapabilityCancel extends CPlayerCapability {
        protected class CRegistrant{
            public  CRegistrant() {
                CPlayerCapability.Register(new CPlayerCapabilityCancel());
            }
        }
        protected CRegistrant DRegistrant;


        public CPlayerCapabilityCancel() {
            super("Cancel", ETargetType.ttNone);
        }

        public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
            return true;
        }

        public boolean CanApply(CPlayerAsset actor,  CPlayerData playerData,  CPlayerAsset target) {
            return true;
        }

        public boolean ApplyCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
                target) {
            SAssetCommand NewCommand = new SAssetCommand();

            NewCommand.DAction = aaCapability;
            NewCommand.DCapability = AssetCapabilityType();
            NewCommand.DAssetTarget = target;
            NewCommand.DActivatedCapability = new CActivatedPlayerCapability(actor, playerData,
                    target) {
                @Override
                public int PercentComplete(int max) {
                    return 0;
                }

                @Override
                public boolean IncrementStep() {
                    DActor.PopCommand();

                    if (aaNone != DActor.Action()) {
                        SAssetCommand AssetCommand = new SAssetCommand();

                        AssetCommand = DActor.CurrentCommand();
                        if (aaConstruct == AssetCommand.DAction) {
                            if (AssetCommand.DAssetTarget != null) {
                                AssetCommand.DAssetTarget.CurrentCommand().DActivatedCapability
                                        .Cancel();
                            } else if (AssetCommand.DActivatedCapability != null) {
                                AssetCommand.DActivatedCapability.Cancel();
                            }
                        } else if (AssetCommand.DActivatedCapability != null) {
                            AssetCommand.DActivatedCapability.Cancel();
                        }
                    }

                    return true;
                }

                @Override
                public void Cancel() {
                    DActor.PopCommand();
                }
            };
            actor.PushCommand(NewCommand);

            return true;
        }
    }

    public class CPlayerCapabilityConvey extends CPlayerCapability {
        protected class CRegistrant{
            public CRegistrant() {
                CPlayerCapability.Register(new CPlayerCapabilityConvey());
            }
        }
        protected CRegistrant DRegistrant;

        public CPlayerCapabilityConvey() {
            super("Convey", ttAsset);
        }

        public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
            return actor.Speed() > 0 && (actor.Lumber() > 0 || actor.Gold() > 0);
        }

        public boolean CanApply(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target) {
            if (actor.Speed() > 0 && (actor.Lumber() > 0 || actor.Gold() > 0)) {
                if (aaConstruct == target.Action()) {
                    return false;
                }
                if ((atTownHall == target.Type()) || (atKeep == target.Type()) || (atCastle ==
                        target.Type())) {
                    return true;
                }
                if (actor.Lumber() > 0 && (atLumberMill == target.Type())) {
                    return true;
                }
            }
            return false;
        }

        public boolean ApplyCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
                target) {
            SAssetCommand NewCommand = new SAssetCommand();

            NewCommand.DAction = aaCapability;
            NewCommand.DCapability = AssetCapabilityType();
            NewCommand.DAssetTarget = target;
            NewCommand.DActivatedCapability = new CActivatedPlayerCapability(actor, playerData,
                    target) {
                @Override
                public int PercentComplete(int max) {
                    return 0;
                }

                @Override
                public boolean IncrementStep() {
                    CPlayerAsset NearestRepository = new CPlayerAsset(); // TODO: Figure out purpose
                    SAssetCommand AssetCommand = new SAssetCommand();
                    CGameModel.SGameEvent TempEvent = new CGameModel.SGameEvent();

                    TempEvent.DType = etAcknowledge;
                    TempEvent.DAsset = DActor;
                    DPlayerData.AddGameEvent(TempEvent);

                    DActor.PopCommand();
                    if (DActor.Lumber() > 0) {
                        AssetCommand.DAction = aaConveyLumber;
                        AssetCommand.DAssetTarget = DTarget;
                        DActor.PushCommand(AssetCommand);
                        AssetCommand.DAction = aaWalk;
                        DActor.PushCommand(AssetCommand);
                        DActor.ResetStep();
                    }
                    else if (DActor.Gold() > 0) {
                        AssetCommand.DAction = aaConveyGold;
                        AssetCommand.DAssetTarget = DTarget;
                        DActor.PushCommand(AssetCommand);
                        AssetCommand.DAction = aaWalk;
                        DActor.PushCommand(AssetCommand);
                        DActor.ResetStep();
                    }

                    return true;
                }

                @Override
                public void Cancel() {
                    DActor.PopCommand();
                }
            };
            actor.ClearCommand();
            actor.PushCommand(NewCommand);
            return true;
        }

    }

    public class CPlayerCapabilityPatrol extends CPlayerCapability {
        protected class CRegistrant {
            public CRegistrant() {
                CPlayerCapability.Register(new CPlayerCapabilityPatrol());
            }
        }

        protected CRegistrant DRegistrant;


        public CPlayerCapabilityPatrol() {
            super("Patrol", ETargetType.ttTerrain);
        }

        public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
            return actor.Speed() > 0;
        }

        public boolean CanApply(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target) {
            return actor.Speed() > 0;
        }

        public boolean ApplyCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
                target) {
            if (actor.TilePosition() != target.TilePosition()) {
                SAssetCommand NewCommand = new SAssetCommand();

                NewCommand.DAction = CGameDataTypes.EAssetAction.aaCapability;
                NewCommand.DCapability = AssetCapabilityType();
                NewCommand.DAssetTarget = target;
                NewCommand.DActivatedCapability = new CActivatedPlayerCapability(actor,
                        playerData, target) {
                    @Override
                    public int PercentComplete(int max) {
                        return 0;
                    }

                    @Override
                    public boolean IncrementStep() {
                        SAssetCommand PatrolCommand = new SAssetCommand();
                        SAssetCommand WalkCommand = new SAssetCommand();
                        CGameModel.SGameEvent TempEvent = new CGameModel.SGameEvent();

                        TempEvent.DType = CGameModel.EEventType.etAcknowledge;
                        TempEvent.DAsset = DActor;
                        DPlayerData.AddGameEvent(TempEvent);

                        PatrolCommand.DAction = CGameDataTypes.EAssetAction.aaCapability;
                        PatrolCommand.DCapability = CGameDataTypes.EAssetCapabilityType.actPatrol;
                        PatrolCommand.DAssetTarget = DPlayerData.CreateMarker(DActor.Position(),
                                false);
                        PatrolCommand.DActivatedCapability = new CActivatedPlayerCapability
                                (DActor, DPlayerData, PatrolCommand.DAssetTarget) {
                            @Override
                            public int PercentComplete(int max) {
                                return 0;
                            }

                            @Override
                            public boolean IncrementStep() {
                                return false;
                            }

                            @Override
                            public void Cancel() {

                            }
                        };

                        DActor.ClearCommand();
                        DActor.PushCommand(PatrolCommand);

                        WalkCommand.DAction = CGameDataTypes.EAssetAction.aaWalk;
                        WalkCommand.DAssetTarget = DTarget;
                        if (!DActor.TileAligned()) {
                            DActor.Direction( CGameDataTypes.EDirection.values()[((DActor
                                    .Position().TileOctant().ordinal() + dMax.ordinal() / 2) %
                                    dMax.ordinal())]);
                        }
                        DActor.PushCommand(WalkCommand);
                        return true;
                    }

                    @Override
                    public void Cancel() {
                        DActor.PopCommand();
                    }
                };

                actor.ClearCommand();
                actor.PushCommand(NewCommand);
                return true;
            }

            return false;
        }
    }

    public class CPlayerCapabilityAttack extends CPlayerCapability {
        protected class CRegistrant {
            public CRegistrant() {
                CPlayerCapability.Register(new CPlayerCapabilityAttack());
            }
        }
        protected CRegistrant DRegistrant;

        public CPlayerCapabilityAttack() {
            super("Attack", CPlayerCapability.ETargetType.ttAsset);
        }

        public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
            return actor.Speed() > 0;
        }

        public boolean CanApply(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target) {
            if ((actor.Color() == target.Color()) || (CGameDataTypes.EPlayerColor.pcNone == target
                    .Color())) {
                return false;
            }
            return actor.Speed() > 0;
        }

        public boolean ApplyCapability(CPlayerAsset actor,CPlayerData playerData, CPlayerAsset
                target) {
            if (actor.TilePosition() != target.TilePosition()) {
                SAssetCommand NewCommand = new SAssetCommand();

                NewCommand.DAction = aaCapability;
                NewCommand.DCapability = AssetCapabilityType();
                NewCommand.DAssetTarget = target;
                NewCommand.DActivatedCapability = new CActivatedPlayerCapability(actor,
                        playerData, target) {
                    @Override
                    public int PercentComplete(int max) {
                        return 0;
                    }

                    @Override
                    public boolean IncrementStep() {
                        SAssetCommand AssetCommand = new SAssetCommand();
                        CGameModel.SGameEvent TempEvent = new CGameModel.SGameEvent();

                        TempEvent.DType = CGameModel.EEventType.etAcknowledge;
                        TempEvent.DAsset = DActor;
                        DPlayerData.AddGameEvent(TempEvent);

                        AssetCommand.DAction = aaAttack;
                        AssetCommand.DAssetTarget = DTarget;
                        DActor.ClearCommand();
                        DActor.PushCommand(AssetCommand);

                        AssetCommand.DAction = aaWalk;
                        if (!DActor.TileAligned()) {
                            DActor.Direction(CGameDataTypes.EDirection.values()[((DActor.Position
                                    ().TileOctant().ordinal() + dMax.ordinal() / 2) % dMax.ordinal
                                    ())]);
                        }
                        DActor.PushCommand(AssetCommand);
                        return true;
                    }

                    @Override
                    public void Cancel() {
                        DActor.PopCommand();
                    }
                };
                actor.ClearCommand();
                actor.PushCommand(NewCommand);
            }

            return true;
        }
    }

    public class CPlayerCapabilityRepair extends CPlayerCapability {

        protected class CRegistrant {
            public CRegistrant() {
                CPlayerCapability.Register(new CPlayerCapabilityRepair());
            }
        }

        protected CRegistrant DRegistrant;

        public CPlayerCapabilityRepair() {
            super(("Repair"), ttAsset);
        }

        public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
            return (actor.Speed() > 0) && playerData.Gold() > 0 && playerData.Lumber() > 0;
        }

        public boolean CanApply(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target) {
            if ((actor.Color() != target.Color()) || (target.Speed() > 0)) {
                return false;
            }
            if (target.HitPoints() >= target.MaxHitPoints()) {
                return false;
            }
            return CanInitiate(actor, playerData);
        }

        public boolean ApplyCapability(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
                target) {
            if (actor.TilePosition() != target.TilePosition()) {
                SAssetCommand NewCommand = new SAssetCommand();

                NewCommand.DAction = aaCapability;
                NewCommand.DCapability = AssetCapabilityType();
                NewCommand.DAssetTarget = target;
                NewCommand.DActivatedCapability = new CActivatedPlayerCapability(actor,
                        playerData, target) {
                    @Override
                    public int PercentComplete(int max) {
                        return 0;
                    }

                    @Override
                    public boolean IncrementStep() {
                        SAssetCommand AssetCommand = new SAssetCommand();
                        CGameModel.SGameEvent TempEvent = new CGameModel.SGameEvent();

                        TempEvent.DType = etAcknowledge;
                        TempEvent.DAsset = DActor;
                        DPlayerData.AddGameEvent(TempEvent);

                        AssetCommand.DAction = aaRepair;
                        AssetCommand.DAssetTarget = DTarget;
                        DActor.ClearCommand();
                        DActor.PushCommand(AssetCommand);

                        AssetCommand.DAction = aaWalk;
                        if (!DActor.TileAligned()) {
                            DActor.Direction(CGameDataTypes.EDirection.values()[((DActor.Position
                                    ().TileOctant().ordinal() + dMax.ordinal() / 2) % dMax
                                    .ordinal())]);
                        }
                        DActor.PushCommand(AssetCommand);
                        return true;
                    }

                    @Override
                    public void Cancel() {
                        DActor.PopCommand();
                    }
                };
                actor.ClearCommand();
                actor.PushCommand(NewCommand);
                return true;
            }

            return false;
        }
    }
}
