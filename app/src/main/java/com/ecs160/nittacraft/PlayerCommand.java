package com.ecs160.nittacraft;

import java.util.ArrayList;

public class PlayerCommand {

    static class SPlayerCommandRequest {

        CGameDataTypes.EAssetCapabilityType DAction;
        ArrayList<CPlayerAsset> DActors;
        CGameDataTypes.EPlayerColor DTargetColor;
        CGameDataTypes.EAssetType DTargetType;
        CPosition DTargetLocation;
    }
}