package com.ecs160.nittacraft.capabilities;

import com.ecs160.nittacraft.CGameModel;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.SAssetCommand;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaCapability;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaConstruct;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaConveyGold;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaConveyLumber;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaWalk;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atCastle;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atKeep;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atLumberMill;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atTownHall;
import static com.ecs160.nittacraft.CGameModel.EEventType.etAcknowledge;
import static com.ecs160.nittacraft.capabilities.CPlayerCapability.ETargetType.ttAsset;

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
            if ((atTownHall == target.Type()) || (atKeep == target.Type()) || (atCastle == target
                    .Type())) {
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
                } else if (DActor.Gold() > 0) {
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