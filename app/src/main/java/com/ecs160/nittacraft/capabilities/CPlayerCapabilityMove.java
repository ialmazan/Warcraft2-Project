package com.ecs160.nittacraft.capabilities;

import com.ecs160.nittacraft.CGameDataTypes;
import com.ecs160.nittacraft.CGameModel;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.SAssetCommand;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaCapability;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaWalk;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dMax;
import static com.ecs160.nittacraft.CGameModel.EEventType.etAcknowledge;
import static com.ecs160.nittacraft.capabilities.CPlayerCapability.ETargetType.ttTerrainOrAsset;

public class CPlayerCapabilityMove extends CPlayerCapability {
    protected class CRegistrant {
        public CRegistrant() {
            CPlayerCapability.Register(new CPlayerCapabilityMove());
        }
    }

    protected CRegistrant DRegistrant;

    public CPlayerCapabilityMove() {
        super("Move", ttTerrainOrAsset);
    }

    public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
        return actor.Speed() > 0;
    }

    public boolean CanApply(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target) {
        return actor.Speed() > 0;
    }

    public boolean ApplyCapability(CPlayerAsset actor, CPlayerData playerData,CPlayerAsset target) {
        if (actor.TilePosition() != target.TilePosition()) {
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
                    SAssetCommand AssetCommand = new SAssetCommand();
                    CGameModel.SGameEvent TempEvent = new CGameModel.SGameEvent();

                    TempEvent.DType = etAcknowledge;
                    TempEvent.DAsset = DActor;
                    DPlayerData.AddGameEvent(TempEvent);

                    AssetCommand.DAction = aaWalk;
                    AssetCommand.DAssetTarget = DTarget;
                    if (!DActor.TileAligned()) {
                        DActor.Direction(CGameDataTypes.EDirection.values()[(DActor.Position()
                                .TileOctant().ordinal() + dMax.ordinal() / 2) % dMax.ordinal()]);
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
