package com.ecs160.nittacraft.capabilities;

import com.ecs160.nittacraft.CGameDataTypes;
import com.ecs160.nittacraft.CGameModel;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.SAssetCommand;
import com.ecs160.nittacraft.maps.CTerrainMap;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaCapability;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaHarvestLumber;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaMineGold;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaWalk;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actMine;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGoldMine;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atNone;
import static com.ecs160.nittacraft.CGameDataTypes.EDirection.dMax;
import static com.ecs160.nittacraft.CGameModel.EEventType.etAcknowledge;

public class CPlayerCapabilityMineHarvest extends CPlayerCapability {
    protected class CRegistrant {
        public CRegistrant() {
            CPlayerCapability.Register(new CPlayerCapabilityMineHarvest());
        }
    }

    protected CRegistrant DRegistrant;

    public CPlayerCapabilityMineHarvest() {
        super("Mine", ETargetType.ttTerrainOrAsset);

    }

    public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData) {
        return actor.HasCapability(actMine);
    }

    public boolean CanApply(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset target) {

        if (!actor.HasCapability(actMine)) {
            return false;
        }
        if (0 > actor.Lumber() || 0 > actor.Gold()) {
            return false;
        }
        if (atGoldMine == target.Type()) {
            return true;
        }
        if (atNone != target.Type()) {
            return false;
        }
//        return CTerrainMap.ETileType.ttTree == playerData.PlayerMap().TileType(target.TilePosition());
        return CTerrainMap.ETileType.ttTree == playerData.DActualMap().TileType(target.TilePosition());
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
                } else {
                    AssetCommand.DAction = aaHarvestLumber;
                }
                DActor.ClearCommand();
                DActor.PushCommand(AssetCommand);
                SAssetCommand AssetCommand2 = new SAssetCommand();
                AssetCommand2.DAssetTarget = DTarget;
                AssetCommand2.DAction = aaWalk;
                if (!DActor.TileAligned()) {
                    DActor.Direction(CGameDataTypes.EDirection.values()[(DActor.Position()
                            .TileOctant().ordinal() + dMax.ordinal() / 2) % dMax.ordinal()]);
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

        return true;
    }
}