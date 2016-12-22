package com.ecs160.nittacraft.capabilities;

import com.ecs160.nittacraft.CGameDataTypes;
import com.ecs160.nittacraft.CGameModel;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.SAssetCommand;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actPatrol;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dMax;

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
        if (!actor.TilePosition().equals(target.TilePosition())) {
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

                    SAssetCommand PatrolCommand = new SAssetCommand();
                    SAssetCommand WalkCommand = new SAssetCommand();
                    CGameModel.SGameEvent TempEvent = new CGameModel.SGameEvent();

                    TempEvent.DType = CGameModel.EEventType.etAcknowledge;
                    TempEvent.DAsset = DActor;
                    DPlayerData.AddGameEvent(TempEvent);

                    PatrolCommand.DAction = CGameDataTypes.EAssetAction.aaCapability;
                    PatrolCommand.DCapability = CGameDataTypes.EAssetCapabilityType.actPatrol;
                    PatrolCommand.DAssetTarget = DPlayerData.CreateMarker(DActor.Position(), false);
                    PatrolCommand.DActivatedCapability = this;// new CActivatedPlayerCapability(DActor, DPlayerData, PatrolCommand.DAssetTarget)
//                    {
//                        @Override
//                        public int PercentComplete(int max) {
//                            return 0;
//                        }
//
//                        @Override
//                        public boolean IncrementStep() {
//
//                            return true;
//                        }
//
//                        @Override
//                        public void Cancel() {
//
//                        }
//                    };

                    CPlayerAsset tempTarget = DTarget;
                    if (DActor.CurrentCommand().DAssetTarget != null && DActor.CurrentCommand()
                            .DCapability == actPatrol) {
                        tempTarget = DPlayerData.CreateMarker(DActor.CurrentCommand()
                                .DAssetTarget.Position(), false);
                    }

                    DActor.ClearCommand();
                    DActor.PushCommand(PatrolCommand);

                    WalkCommand.DAction = CGameDataTypes.EAssetAction.aaWalk;
                    WalkCommand.DAssetTarget = tempTarget;
                    if (!DActor.TileAligned()) {
                        DActor.Direction( CGameDataTypes.EDirection.values()[((DActor.Position()
                                .TileOctant().ordinal() + dMax.ordinal() / 2) % dMax.ordinal())]);
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