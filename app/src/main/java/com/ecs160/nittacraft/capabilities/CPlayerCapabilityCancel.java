package com.ecs160.nittacraft.capabilities;

import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.SAssetCommand;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaCapability;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaConstruct;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaNone;

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
                            AssetCommand.DAssetTarget.CurrentCommand().DActivatedCapability.Cancel();
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
