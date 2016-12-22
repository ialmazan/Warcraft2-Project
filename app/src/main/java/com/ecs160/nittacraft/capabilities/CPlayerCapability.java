package com.ecs160.nittacraft.capabilities;

import com.ecs160.nittacraft.CGameDataTypes;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actNone;

public abstract class CPlayerCapability {

    public enum ETargetType {
        ttNone,
        ttAsset,
        ttTerrain,
        ttTerrainOrAsset,
        ttPlayer
    }

    public String DName;
    protected CGameDataTypes.EAssetCapabilityType DAssetCapabilityType;
    protected ETargetType DTargetType;

    private final static HashMap<String, CPlayerCapability> NameRegistry = new HashMap<>();
    private final static HashMap<Integer, CPlayerCapability> TypeRegistry = new HashMap<>();

    protected CPlayerCapability(String name, ETargetType targettype) {
        DName = name;
        DAssetCapabilityType = NameToType(name);
        DTargetType = targettype;
//        NameRegistry = new HashMap<String, CPlayerCapability>();
//        TypeRegistry = new HashMap<Integer, CPlayerCapability>();
    }

    // To deal with the C++ static local variables, move them outside the function

    protected static HashMap< String, CPlayerCapability > NameRegistry() {
//        static HashMap< String, CPlayerCapability > TheRegistry;
//        NameRegistry = new HashMap< String, CPlayerCapability >();

        return NameRegistry;
    }
    protected static HashMap< Integer, CPlayerCapability > TypeRegistry() {
//        static HashMap< Integer, CPlayerCapability > TheRegistry;
//        TypeRegistry = new HashMap<>();

        return TypeRegistry;
    }
    public static boolean Register(CPlayerCapability capability) {
        if (null != FindCapability(capability.Name())) {
            return false;
        }
//        Log.d("nugget", "capability: " + capability.DName);
        NameRegistry().put(capability.Name(), capability);
        TypeRegistry().put(NameToType(capability.Name()).ordinal(), capability);
//        Log.d("nugget", "registering: " + TypeRegistry());
//        Log.d("nugget", "nameregistry: " + NameRegistry());
        return true;
    }

    public String Name()  {
        return DName;
    }

    public CGameDataTypes.EAssetCapabilityType AssetCapabilityType()  {
        return DAssetCapabilityType;
    }

    public ETargetType TargetType()  {
        return DTargetType;
    }

    public static CPlayerCapability FindCapability(CGameDataTypes.EAssetCapabilityType type) {
        CPlayerCapability Iterator = TypeRegistry().get(type.ordinal());
//        Log.d("nugget", "typeregistry: " + TypeRegistry());
//        Log.d("nugget", "findCapability " + Iterator);
        if (Iterator != null) {
            return Iterator;
        }
        return null;
    }

    public static CPlayerCapability FindCapability(String name) {
        CPlayerCapability Iterator = NameRegistry().get(name);

        if (Iterator != null) {
            return Iterator;
        }
        return null; // maybe return null instead?
    }

    private static HashMap< String, CGameDataTypes.EAssetCapabilityType> NameTypeTranslation;

    public static CGameDataTypes.EAssetCapabilityType NameToType(String name) {
        NameTypeTranslation = new HashMap< String, CGameDataTypes.EAssetCapabilityType>() {{
            put("None", actNone);
            put("BuildPeasant", CGameDataTypes.EAssetCapabilityType.actBuildPeasant);
            put("BuildFootman", CGameDataTypes.EAssetCapabilityType.actBuildFootman);
            put("BuildArcher", CGameDataTypes.EAssetCapabilityType.actBuildArcher);
            put("BuildRanger", CGameDataTypes.EAssetCapabilityType.actBuildRanger);
            put("BuildFarm", CGameDataTypes.EAssetCapabilityType.actBuildFarm);
            put("BuildTownHall", CGameDataTypes.EAssetCapabilityType.actBuildTownHall);
            put("BuildBarracks", CGameDataTypes.EAssetCapabilityType.actBuildBarracks);
            put("BuildLumberMill", CGameDataTypes.EAssetCapabilityType.actBuildLumberMill);
            put("BuildBlacksmith", CGameDataTypes.EAssetCapabilityType.actBuildBlacksmith);
            put("BuildKeep", CGameDataTypes.EAssetCapabilityType.actBuildKeep);
            put("BuildCastle", CGameDataTypes.EAssetCapabilityType.actBuildCastle);
            put("BuildScoutTower", CGameDataTypes.EAssetCapabilityType.actBuildScoutTower);
            put("BuildGuardTower", CGameDataTypes.EAssetCapabilityType.actBuildGuardTower);
            put("BuildCannonTower", CGameDataTypes.EAssetCapabilityType.actBuildCannonTower);
            put("BuildGoldMine", CGameDataTypes.EAssetCapabilityType.actBuildGoldMine);
            put("Move", CGameDataTypes.EAssetCapabilityType.actMove);
            put("Repair", CGameDataTypes.EAssetCapabilityType.actRepair);
            put("Mine", CGameDataTypes.EAssetCapabilityType.actMine);
            put("BuildSimple", CGameDataTypes.EAssetCapabilityType.actBuildSimple);
            put("BuildAdvanced", CGameDataTypes.EAssetCapabilityType.actBuildAdvanced);
            put("Convey", CGameDataTypes.EAssetCapabilityType.actConvey);
            put("Cancel", CGameDataTypes.EAssetCapabilityType.actCancel);
            put("BuildWall", CGameDataTypes.EAssetCapabilityType.actBuildWall);
            put("Attack", CGameDataTypes.EAssetCapabilityType.actAttack);
            put("StandGround", CGameDataTypes.EAssetCapabilityType.actStandGround);
            put("Patrol", CGameDataTypes.EAssetCapabilityType.actPatrol);
            put("WeaponUpgrade1", CGameDataTypes.EAssetCapabilityType.actWeaponUpgrade1);
            put("WeaponUpgrade2", CGameDataTypes.EAssetCapabilityType.actWeaponUpgrade2);
            put("WeaponUpgrade3", CGameDataTypes.EAssetCapabilityType.actWeaponUpgrade3);
            put("ArrowUpgrade1", CGameDataTypes.EAssetCapabilityType.actArrowUpgrade1);
            put("ArrowUpgrade2", CGameDataTypes.EAssetCapabilityType.actArrowUpgrade2);
            put("ArrowUpgrade3", CGameDataTypes.EAssetCapabilityType.actArrowUpgrade3);
            put("ArmorUpgrade1", CGameDataTypes.EAssetCapabilityType.actArmorUpgrade1);
            put("ArmorUpgrade2", CGameDataTypes.EAssetCapabilityType.actArmorUpgrade2);
            put("ArmorUpgrade3", CGameDataTypes.EAssetCapabilityType.actArmorUpgrade3);
            put("Longbow", CGameDataTypes.EAssetCapabilityType.actLongbow);
            put("RangerScouting", CGameDataTypes.EAssetCapabilityType.actRangerScouting);
            put("Marksmanship", CGameDataTypes.EAssetCapabilityType.actMarksmanship);
        }};

        CGameDataTypes.EAssetCapabilityType Iterator = NameTypeTranslation.get(name);

        if (Iterator != null) {
            return Iterator;
        }

        return actNone;
    }

    private static ArrayList<String> TypeStrings;

    public static String TypeToName(CGameDataTypes.EAssetCapabilityType type) {
        TypeStrings = new ArrayList< String >(Arrays.asList(
                "None",
                "BuildPeasant",
                "BuildFootman",
                "BuildArcher",
                "BuildRanger",
                "BuildFarm",
                "BuildTownHall",
                "BuildBarracks",
                "BuildLumberMill",
                "BuildBlacksmith",
                "BuildKeep",
                "BuildCastle",
                "BuildScoutTower",
                "BuildGuardTower",
                "BuildCannonTower",
                "BuildGoldMine",
                "Move",
                "Repair",
                "Mine",
                "BuildSimple",
                "BuildAdvanced",
                "Convey",
                "Cancel",
                "BuildWall",
                "Attack",
                "StandGround",
                "Patrol",
                "WeaponUpgrade1",
                "WeaponUpgrade2",
                "WeaponUpgrade3",
                "ArrowUpgrade1",
                "ArrowUpgrade2",
                "ArrowUpgrade3",
                "ArmorUpgrade1",
                "ArmorUpgrade2",
                "ArmorUpgrade3",
                "Longbow",
                "RangerScouting",
                "Marksmanship"));
        if ((0 > type.ordinal()) || (type.ordinal() >= TypeStrings.size())) {
            return "";
        }
        return TypeStrings.get(type.ordinal());
    }

    abstract public boolean CanInitiate(CPlayerAsset actor, CPlayerData playerData);
    abstract public boolean CanApply(CPlayerAsset actor, CPlayerData playerData, CPlayerAsset
            target);
    abstract public boolean ApplyCapability(CPlayerAsset actor, CPlayerData playerData,
                                            CPlayerAsset target);
}