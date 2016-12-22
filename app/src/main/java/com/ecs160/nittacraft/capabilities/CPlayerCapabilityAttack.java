package com.ecs160.nittacraft.capabilities;

import com.ecs160.nittacraft.CGameDataTypes;
import com.ecs160.nittacraft.CGameModel;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.SAssetCommand;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaAttack;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaCapability;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaWalk;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dMax;

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
        return (actor.Color() == target.Color()) || (CGameDataTypes.EPlayerColor.pcNone == target
                .Color()) || actor.Speed() > 0;
    }

    public boolean ApplyCapability(CPlayerAsset actor,CPlayerData playerData, CPlayerAsset target) {
        if (!actor.TilePosition().equals(target.TilePosition())) {
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

                    TempEvent.DType = CGameModel.EEventType.etAcknowledge;
                    TempEvent.DAsset = DActor;
                    DPlayerData.AddGameEvent(TempEvent);

                    AssetCommand.DAction = aaAttack;
                    AssetCommand.DAssetTarget = DTarget;
                    DActor.ClearCommand();
                    DActor.PushCommand(AssetCommand);

                    SAssetCommand AssetCommand2 = new SAssetCommand();
                    AssetCommand2.DAssetTarget = DTarget;
                    AssetCommand2.DAction = aaWalk;
                    if (!DActor.TileAligned()) {
                        DActor.Direction(CGameDataTypes.EDirection.values()[((DActor.Position()
                                .TileOctant().ordinal() + dMax.ordinal() / 2) % dMax.ordinal())]);
                    }
                    DActor.PushCommand(AssetCommand2);
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