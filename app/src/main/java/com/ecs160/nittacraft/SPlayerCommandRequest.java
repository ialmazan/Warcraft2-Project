package com.ecs160.nittacraft;

import java.util.ArrayList;

public class SPlayerCommandRequest {
    CGameDataTypes.EAssetCapabilityType DAction;
    ArrayList< CPlayerAsset > DActors = new ArrayList<>();
    CGameDataTypes.EPlayerColor DTargetColor;
    CGameDataTypes.EAssetType DTargetType;
    CPosition DTargetLocation = new CPosition();
}
