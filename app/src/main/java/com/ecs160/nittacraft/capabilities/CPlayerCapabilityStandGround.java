package com.ecs160.nittacraft.capabilities;

import com.ecs160.nittacraft.CGameDataTypes;
import com.ecs160.nittacraft.CGameModel;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.SAssetCommand;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaStandGround;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaWalk;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dMax;
import static com.ecs160.nittacraft.CGameModel.EEventType.etAcknowledge;

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
                    SAssetCommand AssetCommand2 = new SAssetCommand();
                    AssetCommand2.DAction = aaWalk;
                    AssetCommand2.DAssetTarget = DPlayerData.CreateMarker(DActor.Position(), false);
                    DActor.Direction(CGameDataTypes.EDirection.values()[DActor.Position()
                            .TileOctant().ordinal() + dMax.ordinal() / 2 % dMax.ordinal()]);
                    DActor.PushCommand(AssetCommand2);
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
