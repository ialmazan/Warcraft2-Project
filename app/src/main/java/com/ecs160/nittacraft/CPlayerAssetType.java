package com.ecs160.nittacraft;

import android.content.Context;
import android.util.Log;

import com.ecs160.nittacraft.capabilities.CPlayerCapability;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atArcher;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atBarracks;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atBlacksmith;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atCannonTower;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atCastle;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atFarm;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atFootman;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGold;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGoldMine;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGoldVein;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGuardTower;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atKeep;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atKnight;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atLumber;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atLumberMill;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atNone;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atPeasant;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atRanger;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atScoutTower;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atTownHall;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atWall;
import static com.ecs160.nittacraft.CGameDataTypes.EPlayerColor.pcNone;

public class CPlayerAssetType {
    private CPlayerAssetType DThis;
    public String DName;
    public CGameDataTypes.EAssetType DType;
    private CGameDataTypes.EPlayerColor DColor;
    private ArrayList<CGameDataTypes.EAssetCapabilityType> DCapabilities = new ArrayList<>();
    private ArrayList<CGameDataTypes.EAssetType> DAssetRequirements = new ArrayList<>();
    private ArrayList< CPlayerUpgrade > DAssetUpgrades = new ArrayList<>();
    private int DHitPoints;
    private int DArmor;
    private int DSight;
    private int DConstructionSight;
    private int DSize;
    private int DSpeed;
    private int DGoldCost;
    private int DLumberCost;
    private int DFoodConsumption;
    private int DBuildTime;
    private int DAttackSteps;
    private int DReloadSteps;
    private int DBasicDamage;
    private int DPiercingDamage;
    private int DRange;
    private static HashMap< String, CPlayerAssetType > DRegistry= new HashMap<>();
    private static ArrayList<String> DTypeStrings = new ArrayList<>();
//    private static HashMap< String, CGameDataTypes.EAssetType> DNameTypeTranslation = new HashMap<>();

    public CPlayerAssetType() {
        DHitPoints = 1;
        DArmor = 0;
        DSight = 0;
        DConstructionSight = 0;
        DSize = 1;
        DSpeed = 0;
        DGoldCost = 0;
        DLumberCost = 0;
        DFoodConsumption = 0;
        DBuildTime = 0;
        DAttackSteps = 0;
        DReloadSteps = 0;
        DBasicDamage = 0;
        DPiercingDamage = 0;
        DRange = 0;
        DTypeStrings.add("None");
        DTypeStrings.add("Peasant");
        DTypeStrings.add("Footman");
        DTypeStrings.add("Archer");
        DTypeStrings.add("Ranger");
        DTypeStrings.add("GoldMine");
        DTypeStrings.add("GoldVein");
        DTypeStrings.add("TownHall");
        DTypeStrings.add("Keep");
        DTypeStrings.add("Castle");
        DTypeStrings.add("Farm");
        DTypeStrings.add("Barracks");
        DTypeStrings.add("LumberMill");
        DTypeStrings.add("Blacksmith");
        DTypeStrings.add("ScoutTower");
        DTypeStrings.add("GuardTower");
        DTypeStrings.add("CannonTower");
        DTypeStrings.add("Gold");
        DTypeStrings.add("Lumber");
        DTypeStrings.add("Wall");
        DTypeStrings.add("Knight");
    }

    static private CGameDataTypes.EAssetType NameTypeTranslation(String typeString) {
        CGameDataTypes.EAssetType returnValue = atNone;
        switch (typeString) {
            case "None":
                returnValue = atNone;
                break;
            case "Peasant":
                returnValue = atPeasant;
                break;
            case "Footman":
                returnValue = atFootman;
                break;
            case "Archer":
                returnValue = atArcher;
                break;
            case "Ranger":
                returnValue = atRanger;
                break;
            case "GoldMine":
                returnValue = atGoldMine;
                break;
            case "TownHall":
                returnValue = atTownHall;
                break;
            case "Keep":
                returnValue = atKeep;
                break;
            case "Castle":
                returnValue = atCastle;
                break;
            case "Farm":
                returnValue = atFarm;
               break;
            case "Barracks":
                returnValue = atBarracks;
                break;
            case "LumberMill":
                returnValue = atLumberMill;
                break;
            case "Blacksmith":
                returnValue = atBlacksmith;
                break;
            case "ScoutTower":
                returnValue = atScoutTower;
                break;
            case "GuardTower":
                returnValue = atGuardTower;
                break;
            case "CannonTower":
                returnValue = atCannonTower;
                break;
            case "GoldVein":
                returnValue = atGoldVein;
                break;
            case "Gold":
                returnValue = atGold;
                break;
            case "Lumber":
                returnValue = atLumber;
                break;
            case "Wall":
                returnValue = atWall;
                break;
            case "Knight":
                returnValue = atKnight;
                break;
        }
        return returnValue;
    }

    public CPlayerAssetType(CPlayerAssetType asset) {
        if (null != asset) { //if (nullptr != asset) {
            DName = asset.DName;
            DType = asset.DType;
            DColor = asset.DColor;
            DCapabilities = asset.DCapabilities;
            DAssetRequirements = asset.DAssetRequirements;
            DHitPoints = asset.DHitPoints;
            DArmor = asset.DArmor;
            DSight = asset.DSight;
            DConstructionSight = asset.DConstructionSight;
            DSize = asset.DSize;
            DSpeed = asset.DSpeed;
            DGoldCost = asset.DGoldCost;
            DLumberCost = asset.DLumberCost;
            DFoodConsumption = asset.DFoodConsumption;
            DBuildTime = asset.DBuildTime;
            DAttackSteps = asset.DAttackSteps;
            DReloadSteps = asset.DReloadSteps;
            DBasicDamage = asset.DBasicDamage;
            DPiercingDamage = asset.DPiercingDamage;
            DRange = asset.DRange;
            DTypeStrings.add("None");
            DTypeStrings.add("Peasant");
            DTypeStrings.add("Footman");
            DTypeStrings.add("Archer");
            DTypeStrings.add("Ranger");
            DTypeStrings.add("GoldMine");
            DTypeStrings.add("GoldVein");
            DTypeStrings.add("TownHall");
            DTypeStrings.add("Keep");
            DTypeStrings.add("Castle");
            DTypeStrings.add("Farm");
            DTypeStrings.add("Barracks");
            DTypeStrings.add("LumberMill");
            DTypeStrings.add("Blacksmith");
            DTypeStrings.add("ScoutTower");
            DTypeStrings.add("GuardTower");
            DTypeStrings.add("CannonTower");
            DTypeStrings.add("Wall");
        }
    }

    public String Name() {
        return DName;
    }

    public CGameDataTypes.EAssetType Type() {
        return DType;
    }

    public CGameDataTypes.EPlayerColor Color() {
        return DColor;
    }

    public int HitPoints() {
        return DHitPoints;
    }

    public int Armor() {
        return DArmor;
    }

    public int Sight() {
        return DSight;
    }

    public int ConstructionSight() {
        return DConstructionSight;
    }

    public int Size() {
        return DSize;
    }

    public int Speed() {
        return DSpeed;
    }

    public int GoldCost() {
        return DGoldCost;
    }

    public int LumberCost() {
        return DLumberCost;
    }

    public int FoodConsumption() {
        return DFoodConsumption;
    }

    public int BuildTime() {
        return DBuildTime;
    }

    public int AttackSteps() {
        return DAttackSteps;
    }

    public int ReloadSteps() {
        return DReloadSteps;
    }

    public int BasicDamage() {
        return DBasicDamage;
    }

    public int PiercingDamage() {
        return DPiercingDamage;
    }

    public int Range() {
        return DRange;
    }

    public int ArmorUpgrade() {
        int ReturnValue = 0;
        for (CPlayerUpgrade Upgrade : DAssetUpgrades) {
            ReturnValue += Upgrade.Armor();
        }
        return ReturnValue;
    }
    public int SightUpgrade() {
        int ReturnValue = 0;
        for (CPlayerUpgrade Upgrade : DAssetUpgrades) {
            ReturnValue += Upgrade.Sight();
        }
        return ReturnValue;
    }
    public int SpeedUpgrade() {
        int ReturnValue = 0;
        for (CPlayerUpgrade Upgrade : DAssetUpgrades) {
            ReturnValue += Upgrade.Speed();
        }
        return ReturnValue;
    }
    public int BasicDamageUpgrade() {
        int ReturnValue = 0;
        for (CPlayerUpgrade Upgrade : DAssetUpgrades) {
            ReturnValue += Upgrade.BasicDamage();
        }
        return ReturnValue;
    }
    public int PiercingDamageUpgrade() {
        int ReturnValue = 0;
        for (CPlayerUpgrade Upgrade : DAssetUpgrades) {
            ReturnValue += Upgrade.PiercingDamage();
        }
        return ReturnValue;
    }
    public int RangeUpgrade() {
        int ReturnValue = 0;
        for (CPlayerUpgrade Upgrade : DAssetUpgrades) {
            ReturnValue += Upgrade.Range();
        }
        return ReturnValue;
    }

    public boolean HasCapability(CGameDataTypes.EAssetCapabilityType capability) {
        return DCapabilities.contains(capability);
    }

    public ArrayList<CGameDataTypes.EAssetCapabilityType> Capabilities() {
//        ArrayList<CGameDataTypes.EAssetCapabilityType> ReturnVector = new ArrayList<>();
//
//        for (int Index = actNone.ordinal(); Index < actMax.ordinal(); Index++) {
//            if (DCapabilities.contains(CGameDataTypes.EAssetCapabilityType.values()[Index])) {
//                ReturnVector.add(CGameDataTypes.EAssetCapabilityType.values()[Index]);
//            }
//        }
        return DCapabilities;
    }

    public void AddCapability(CGameDataTypes.EAssetCapabilityType capability) {
        DCapabilities.add(capability);
    }

    public void RemoveCapability(CGameDataTypes.EAssetCapabilityType capability) {
        DCapabilities.remove(capability);
    }

    public void AddUpgrade(CPlayerUpgrade upgrade) {
        DAssetUpgrades.add(upgrade);
    }

    public ArrayList<CGameDataTypes.EAssetType> AssetRequirements()  {
        return DAssetRequirements;
    }

    public static CGameDataTypes.EAssetType NameToType(String name) {
//        Log.d("RES", name);
        if (NameTypeTranslation(name) != null) {
            return NameTypeTranslation(name);
        }
        return atNone;
    }

    public static String TypeToName(CGameDataTypes.EAssetType type) {
        if ((0 > type.ordinal()) || (type.ordinal() >= DTypeStrings.size())) {
            return "";
        }
        return DTypeStrings.get(type.ordinal());
    }
    public static int MaxSight() {
        int MaxSightFound = 0;

//        for (CPlayerAssetType ResType : DRegistry) {
//            MaxSightFound = MaxSightFound > ResType.second.DSight ? MaxSightFound : ResType.second.DSight;
        for (CPlayerAssetType ResType : DRegistry.values()) {
            MaxSightFound = MaxSightFound > ResType.DSight ? MaxSightFound : ResType.DSight;
        }

        return MaxSightFound;
    }
    public static boolean LoadTypes(Context context) {
        CDataSource TempDataSource;
        //Part of the iterator through directory.
        //Using getFields and string comparison to find res files
        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            if (field.getName().startsWith("res")) {
                TempDataSource = new CDataSource(context.getResources().openRawResource(context
                        .getResources().getIdentifier(field.getName(), "raw", context
                                .getPackageName())));
                if (!CPlayerAssetType.Load(TempDataSource)) {
                    Log.d("RES", "Failed to load resource");
                    continue;
                } else {
                    Log.d("RES", "Loaded resource: " + field.getName());
                }
            }
        }
        CPlayerAssetType PlayerAssetType = new CPlayerAssetType();
        PlayerAssetType.DThis = PlayerAssetType;
        PlayerAssetType.DName = "None";
        PlayerAssetType.DType = atNone;
        PlayerAssetType.DColor = pcNone;
        PlayerAssetType.DHitPoints = 256;
        DRegistry.put("None", PlayerAssetType);

        return true;
    }
    public static boolean Load(CDataSource source) {
        CLineDataSource LineSource = new CLineDataSource(source);
        StringBuffer Name = new StringBuffer("");
        StringBuffer TempString = new StringBuffer("");
        CPlayerAssetType PlayerAssetType;
        CGameDataTypes.EAssetType AssetType;
        int CapabilityCount, AssetRequirementCount;
        boolean ReturnStatus = false;

        if (null == source) {
            return false;
        }
        if (!LineSource.Read(Name)) {
            Log.d("CPAssetErr", "Failed to get resource type name.");
            return false;
        }
        AssetType = NameToType(Name.toString());
        if ((atNone == AssetType) && (Name.toString() != DTypeStrings.get(atNone.ordinal()))) {
            Log.d("CPAssetErr", "Unknown resource type %s.");
            return false;
        }
        if (null != DRegistry.get(Name)) {
            PlayerAssetType = DRegistry.get(Name);
        } else {
            PlayerAssetType = new CPlayerAssetType();
            PlayerAssetType.DThis = PlayerAssetType;
            PlayerAssetType.DName = Name.toString();
            DRegistry.put(Name.toString(), PlayerAssetType);
        }
        PlayerAssetType.DType = AssetType;
        PlayerAssetType.DColor = pcNone;
//        try {  //There is no try...
            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type hit points.");
                return ReturnStatus;
            }
            PlayerAssetType.DHitPoints = Integer.parseInt(TempString.toString().toString());
            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type armor.");
                return ReturnStatus;
            }
            PlayerAssetType.DArmor = Integer.parseInt(TempString.toString().toString());

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type sight.");
                return ReturnStatus;
            }
            PlayerAssetType.DSight = Integer.parseInt(TempString.toString());

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type construction sight.");
                return ReturnStatus;
            }
            PlayerAssetType.DConstructionSight = Integer.parseInt(TempString.toString());

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type size.");
                return ReturnStatus;
            }
            PlayerAssetType.DSize = Integer.parseInt(TempString.toString());

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type speed.");
                return ReturnStatus;
            }
            PlayerAssetType.DSpeed = Integer.parseInt(TempString.toString());
            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type gold cost.");
                return ReturnStatus;
            }
            PlayerAssetType.DGoldCost = Integer.parseInt(TempString.toString());

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type lumber cost.");
                return ReturnStatus;
            }
            PlayerAssetType.DLumberCost = Integer.parseInt(TempString.toString());

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type food consumption.");
                return ReturnStatus;
            }
            PlayerAssetType.DFoodConsumption = Integer.parseInt(TempString.toString());

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type build time.");
                return ReturnStatus;
            }
            PlayerAssetType.DBuildTime = Integer.parseInt(TempString.toString());

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type attack steps.");
                return ReturnStatus;
            }
            PlayerAssetType.DAttackSteps = Integer.parseInt(TempString.toString());

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type reload steps.");
                return ReturnStatus;
            }
            PlayerAssetType.DReloadSteps = Integer.parseInt(TempString.toString());

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type basic damage.");
                return ReturnStatus;
            }
            PlayerAssetType.DBasicDamage = Integer.parseInt(TempString.toString());

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type piercing damage.");
                return ReturnStatus;
            }
            PlayerAssetType.DPiercingDamage = Integer.parseInt(TempString.toString());

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get resource type range.");
                return ReturnStatus;
            }
            PlayerAssetType.DRange = Integer.parseInt(TempString.toString());

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get capability count.");
                return ReturnStatus;
            }
            CapabilityCount = Integer.parseInt(TempString.toString());
//            PlayerAssetType.DCapabilities.ensureCapacity(actMax.ordinal());
//            for (int Index = 0; Index < PlayerAssetType.DCapabilities.size(); Index++) {
//                PlayerAssetType.DCapabilities.set(Index, false);
//            }
            for (int Index = 0; Index < CapabilityCount; Index++) {
                if (!LineSource.Read(TempString)) {
                    Log.d("CPAssetErr", "Failed to read capability %d.");
                    return ReturnStatus;
                }
                PlayerAssetType.AddCapability(CPlayerCapability.NameToType(TempString.toString()));
            }

            if (!LineSource.Read(TempString)) {
                Log.d("CPAssetErr", "Failed to get asset requirement count.");
                return ReturnStatus;
            }
            AssetRequirementCount = Integer.parseInt(TempString.toString());
            for (int Index = 0; Index < AssetRequirementCount; Index++) {
                if (!LineSource.Read(TempString)) {
                    Log.d("CPAssetErr", "Failed to read asset requirement %d.");
                    return ReturnStatus;
                }
                PlayerAssetType.DAssetRequirements.add(NameToType(TempString.toString()));
            }
            ReturnStatus = true;
//        }
//        catch(Exception E) {
//            Log.d("CPAssetErr", "%s\n",E.what());
//        }
//        LoadExit:
        return ReturnStatus;
    }

    public static CPlayerAssetType FindDefaultFromName(String name) {
        CPlayerAssetType Iterator = DRegistry.get(name);

        if (Iterator != null) {
            return Iterator;
        }
        return new CPlayerAssetType();

    }
    public static CPlayerAssetType FindDefaultFromType(CGameDataTypes.EAssetType type) {
        return FindDefaultFromName( TypeToName(type) );
    }
    public static HashMap<String, CPlayerAssetType> DuplicateRegistry(CGameDataTypes.EPlayerColor
                                                                              color) {
        HashMap<String, CPlayerAssetType> ReturnRegistry = new HashMap<>();
        for (Map.Entry< String, CPlayerAssetType > It : DRegistry.entrySet()) {
            CPlayerAssetType NewAssetType = new CPlayerAssetType(It.getValue());
            NewAssetType.DThis = NewAssetType;
            NewAssetType.DColor = color;
            ReturnRegistry.put(It.getKey(), NewAssetType);
        }

        return ReturnRegistry;
    }

    public CPlayerAsset Construct() {
//        if (auto ThisShared = DThis.lock()) {
//            return std::shared_ptr< CPlayerAsset >(new CPlayerAsset(ThisShared));
//        }
//        return nullptr;
        if (DThis != null) {
            return new CPlayerAsset(DThis);
        }
        return null;
    }
}
