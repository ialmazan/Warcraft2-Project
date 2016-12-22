package com.ecs160.nittacraft;

public class CGameDataTypes {

    public enum EPlayerColor {
        pcNone,
        pcBlue,
        pcRed,
        pcGreen,
        pcPurple,
        pcOrange,
        pcYellow,
        pcBlack,
        pcWhite,
        pcMax
    }

    public enum EAssetAction {
        aaNone,
        aaConstruct,
        aaBuild,
        aaRepair,
        aaWalk,
        aaStandGround,
        aaAttack,
        aaHarvestLumber,
        aaMineGold,
        aaConveyLumber,
        aaConveyGold,
        aaDeath,
        aaDecay,
        aaCapability
    }

    public enum EAssetCapabilityType {
        actNone,
        actBuildPeasant,
        actBuildFootman,
        actBuildArcher,
        actBuildRanger,
        actBuildFarm,
        actBuildTownHall,
        actBuildBarracks,
        actBuildLumberMill,
        actBuildBlacksmith,
        actBuildGoldMine,
        actBuildKeep,
        actBuildCastle,
        actBuildScoutTower,
        actBuildGuardTower,
        actBuildCannonTower,
        actMove,
        actRepair,
        actMine,
        actBuildSimple,
        actBuildAdvanced,
        actConvey,
        actCancel,
        actBuildWall,
        actAttack,
        actStandGround,
        actPatrol,
        actWeaponUpgrade1,
        actWeaponUpgrade2,
        actWeaponUpgrade3,
        actArrowUpgrade1,
        actArrowUpgrade2,
        actArrowUpgrade3,
        actArmorUpgrade1,
        actArmorUpgrade2,
        actArmorUpgrade3,
        actLongbow,
        actRangerScouting,
        actMarksmanship,
        actMax
    }

    public enum EAssetType {
        atNone,
        atPeasant,
        atFootman,
        atArcher,
        atRanger,
        atGoldMine,
        atGoldVein,
        atTownHall,
        atKeep,
        atCastle,
        atFarm,
        atBarracks,
        atLumberMill,
        atBlacksmith,
        atScoutTower,
        atGuardTower,
        atCannonTower,
        atGold,
        atLumber,
        atWall,
        atKnight,
        atMax
    }

    public enum EDirection {
        dNorth,
        dNorthEast,
        dEast,
        dSouthEast,
        dSouth,
        dSouthWest,
        dWest,
        dNorthWest,
        dMax
    }
}