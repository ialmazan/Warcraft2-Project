package com.ecs160.nittacraft;

import android.content.Context;
import android.util.Log;

import com.ecs160.nittacraft.capabilities.CPlayerCapability;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actNone;

public class CPlayerUpgrade {
    protected String DName;
    protected int DArmor;
    protected int DSight;
    protected int DSpeed;
    protected int DBasicDamage;
    protected int DPiercingDamage;
    protected int DRange;
    protected int DGoldCost;
    protected int DLumberCost;
    protected int DResearchTime;
    protected ArrayList< CGameDataTypes.EAssetType > DAffectedAssets = new ArrayList<>();
    protected static HashMap< String, CPlayerUpgrade > DRegistryByName = new HashMap<>();
    protected static HashMap<CGameDataTypes.EAssetCapabilityType, CPlayerUpgrade >
            DRegistryByType = new HashMap<>();

    public CPlayerUpgrade() {};

    public String Name()  {
        return DName;
    }

    public int Armor()  {
        return DArmor;
    }

    public int Sight()  {
        return DSight;
    }

    public int Speed()  {
        return DSpeed;
    }

    public int BasicDamage()  {
        return DBasicDamage;
    }

    public int PiercingDamage()  {
        return DPiercingDamage;
    }

    public int Range()  {
        return DRange;
    }

    public int GoldCost()  {
        return DGoldCost;
    }

    public int LumberCost()  {
        return DLumberCost;
    }

    public int ResearchTime()  {
        return DResearchTime;
    }

    public ArrayList<CGameDataTypes.EAssetType> AffectedAssets()  {
        return DAffectedAssets;
    }

    public static boolean LoadUpgrades(Context context) {
        CDataSource TempDataSource;
        //Part of the iterator through directory.
        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            if (field.getName().startsWith("upg")) {
                TempDataSource = new CDataSource(context.getResources().openRawResource(context
                        .getResources().getIdentifier(field.getName(), "raw", context
                                .getPackageName())));
                if (!CPlayerUpgrade.Load(TempDataSource)) {
                    Log.d("RES", "Failed to load resource");
                    continue;
                } else {
                    Log.d("RES", "Loaded resource: " + field.getName());
                }
            }
        }
        return true;
    }


    public static boolean Load(CDataSource source) {
        CLineDataSource LineSource = new CLineDataSource(source);
        StringBuffer Name = new StringBuffer();
        StringBuffer TempString = new StringBuffer();
        CPlayerUpgrade PlayerUpgrade;
        CGameDataTypes.EAssetCapabilityType UpgradeType;
        int AffectedAssetCount;
        boolean ReturnStatus = false;

        if (null == source) {
            return false;
        }
        if (!LineSource.Read(Name)) {
            Log.e("UPG", "Failed to get player upgrade name.");
            return false;
        }
        UpgradeType = CPlayerCapability.NameToType(Name.toString());
        if ((actNone == UpgradeType) && (Name.toString() != CPlayerCapability.TypeToName(actNone)
        )) {
            Log.e("UPG", "Unknown upgrade type %s.");
            return false;
        }
        CPlayerUpgrade Iterator = DRegistryByName.get(Name.toString());
        if (null != Iterator) {
            PlayerUpgrade = Iterator;
        } else {
            PlayerUpgrade = new CPlayerUpgrade();
            PlayerUpgrade.DName = Name.toString();
            DRegistryByName.put(Name.toString(), PlayerUpgrade);
            DRegistryByType.put(UpgradeType, PlayerUpgrade);
        }
        if (!LineSource.Read(TempString)) {
            Log.e("UPG", "Failed to get upgrade armor.\n");
            return ReturnStatus;
        }
        PlayerUpgrade.DArmor = Integer.parseInt(TempString.toString());
        if (!LineSource.Read(TempString)) {
            Log.e("UPG", "Failed to get upgrade sight.\n");
            return ReturnStatus;
        }
        PlayerUpgrade.DSight = Integer.parseInt(TempString.toString());
        if (!LineSource.Read(TempString)) {
            Log.e("UPG", "Failed to get upgrade speed.\n");
            return ReturnStatus;
        }
        PlayerUpgrade.DSpeed = Integer.parseInt(TempString.toString());
        if (!LineSource.Read(TempString)) {
            Log.e("UPG", "Failed to get upgrade basic damage.\n");
            return ReturnStatus;
        }
        PlayerUpgrade.DBasicDamage = Integer.parseInt(TempString.toString());
        if (!LineSource.Read(TempString)) {
            Log.e("UPG", "Failed to get upgrade piercing damage.\n");
            return ReturnStatus;
        }
        PlayerUpgrade.DPiercingDamage = Integer.parseInt(TempString.toString());
        if (!LineSource.Read(TempString)) {
            Log.e("UPG", "Failed to get upgrade range.\n");
            return ReturnStatus;
        }
        PlayerUpgrade.DRange = Integer.parseInt(TempString.toString());
        if (!LineSource.Read(TempString)) {
            Log.e("UPG", "Failed to get upgrade gold cost.\n");
            return ReturnStatus;
        }
        PlayerUpgrade.DGoldCost = Integer.parseInt(TempString.toString());
        if (!LineSource.Read(TempString)) {
            Log.e("UPG", "Failed to get upgrade lumber cost.\n");
            return ReturnStatus;
        }
        PlayerUpgrade.DLumberCost = Integer.parseInt(TempString.toString());
        if (!LineSource.Read(TempString)) {
            Log.e("UPG", "Failed to get upgrade research time.\n");
            return ReturnStatus;
        }
        PlayerUpgrade.DResearchTime = Integer.parseInt(TempString.toString());
        if (!LineSource.Read(TempString)) {
            Log.e("UPG", "Failed to get upgrade affected asset count.");
            return ReturnStatus;
        }
        AffectedAssetCount = Integer.parseInt(TempString.toString());
        for (int Index = 0; Index < AffectedAssetCount; Index++) {
            if (!LineSource.Read(TempString)) {
                Log.e("UPG", "Failed to read upgrade affected asset %d.");
                return ReturnStatus;
            }
            PlayerUpgrade.DAffectedAssets.add(CPlayerAssetType.NameToType(TempString.toString()));
        }
        return true;
    }

    public static CPlayerUpgrade FindUpgradeFromType(CGameDataTypes.EAssetCapabilityType type) {
        CPlayerUpgrade Iterator = DRegistryByType.get(type);

        if (Iterator != null) {
            return Iterator;
        }
        return new CPlayerUpgrade();
    }
    public static CPlayerUpgrade FindUpgradeFromName(String name) {
        CPlayerUpgrade Iterator = DRegistryByName.get(name);

        if (Iterator != null) {
            return Iterator;
        }
        return new CPlayerUpgrade();
    }
}