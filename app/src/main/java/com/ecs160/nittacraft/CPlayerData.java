package com.ecs160.nittacraft;

import android.util.Log;

import com.ecs160.nittacraft.CGameDataTypes.EAssetAction;
import com.ecs160.nittacraft.CGameDataTypes.EAssetType;
import com.ecs160.nittacraft.CGameDataTypes.EPlayerColor;
import com.ecs160.nittacraft.capabilities.CPlayerCapability;
import com.ecs160.nittacraft.capabilities.CPlayerCapabilityAttack;
import com.ecs160.nittacraft.capabilities.CPlayerCapabilityBuildNormal;
import com.ecs160.nittacraft.capabilities.CPlayerCapabilityBuildingUpgrade;
import com.ecs160.nittacraft.capabilities.CPlayerCapabilityConvey;
import com.ecs160.nittacraft.capabilities.CPlayerCapabilityMineHarvest;
import com.ecs160.nittacraft.capabilities.CPlayerCapabilityMove;
import com.ecs160.nittacraft.capabilities.CPlayerCapabilityPatrol;
import com.ecs160.nittacraft.capabilities.CPlayerCapabilityRepair;
import com.ecs160.nittacraft.capabilities.CPlayerCapabilityTrainNormal;
import com.ecs160.nittacraft.capabilities.CPlayerCapabilityUnitUpgrade;
import com.ecs160.nittacraft.maps.CAssetDecoratedMap;
import com.ecs160.nittacraft.maps.CVisibilityMap;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaNone;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actMax;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGoldMine;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGoldVein;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atNone;



public class CPlayerData {
    private boolean DIsAI;
    private CGameDataTypes.EPlayerColor DColor;
    private CVisibilityMap DVisibilityMap;
    public CAssetDecoratedMap DActualMap = new CAssetDecoratedMap();
    public CAssetDecoratedMap DPlayerMap = new CAssetDecoratedMap();
    private HashMap<String, CPlayerAssetType> DAssetTypes = new HashMap<>();
    //std::shared_ptr< std::unordered_map< std::string, std::shared_ptr< CPlayerAssetType > > > DAssetTypes;
    private ArrayList<CPlayerAsset> DAssets; //std::list< std::weak_ptr< CPlayerAsset > > DAssets;
    private ArrayList<Boolean> DUpgrades;

    static public ArrayList<CGameModel.SGameEvent> DGameEvents;
    private int DGold;
    private int DLumber;
    private int DGameCycle;

    private CGameDataTypes.EAssetCapabilityType DActionToRender;

    public CAssetDecoratedMap DActualMap() {
        return DActualMap;
    }

    public CPlayerData(CAssetDecoratedMap map, CGameDataTypes.EPlayerColor color) {
        DIsAI = true;
        DColor = color;
        DActualMap = map;
        DVisibilityMap = DActualMap.CreateVisibilityMap();
        DPlayerMap = DActualMap.CreateInitializeMap();
//        Log.d("CPD-Fog", "DPlayer Map made with size " + DPlayerMap.Width() + " " + DPlayerMap.Height());


        DAssetTypes = CPlayerAssetType.DuplicateRegistry(color);
        DAssets = new ArrayList<>(); //std::list< std::weak_ptr< CPlayerAsset > > DAssets;
        DUpgrades = new ArrayList<>();

        DGameEvents = new ArrayList<>();
        DGold = 0;
        DLumber = 0;
        DGameCycle = 0;



        DUpgrades.ensureCapacity(actMax.ordinal());

        for (int Index = 0; Index < actMax.ordinal(); Index++) {
            DUpgrades.add(false);
        }

        if (DActualMap.ResourceInitializationList() == null)
            Log.d("RESOURCEINIT", "null");


        for (CAssetDecoratedMap.SResourceInitialization ResourceInit : DActualMap
                .ResourceInitializationList()) {
            if (ResourceInit.DColor == color) {
                DGold = ResourceInit.DGold;
                DLumber = ResourceInit.DLumber;
            }
        }
        for (CAssetDecoratedMap.SAssetInitialization AssetInit : DActualMap
                .AssetInitializationList()) {
            if (AssetInit.DColor == color) {
//                Log.d(DEBUG_LOW, "Init %s %d (%d, %d)\n", AssetInit.DType.c_str(), AssetInit
//                        .DColor, AssetInit.DTilePosition.X(),
//                        AssetInit.DTilePosition.Y());
                CPlayerAsset InitAsset = CreateAsset(AssetInit);
                InitAsset.TilePosition(AssetInit.DTilePosition);
//                InitAsset.Position().SetFromTile(AssetInit.DTilePosition);

                if (atGoldMine == CPlayerAssetType.NameToType(AssetInit.DType) || atGoldVein ==
                        CPlayerAssetType.NameToType((AssetInit.DType))) {
                    InitAsset.Gold(DGold);
                }
            }
        }
        CPlayerCapability.Register(new CPlayerCapabilityMove());
        CPlayerCapability.Register(new CPlayerCapabilityMineHarvest());
        CPlayerCapability.Register(new CPlayerCapabilityConvey());
        CPlayerCapability.Register(new CPlayerCapabilityAttack());
        CPlayerCapability.Register(new CPlayerCapabilityRepair());
        CPlayerCapability.Register(new CPlayerCapabilityPatrol());

        CPlayerCapability.Register(new CPlayerCapabilityTrainNormal("Peasant"));
        CPlayerCapability.Register(new CPlayerCapabilityTrainNormal("Footman"));
        CPlayerCapability.Register(new CPlayerCapabilityTrainNormal("Archer"));

        CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("TownHall"));
        CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("Farm"));
        CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("Barracks"));
        CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("LumberMill"));
        CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("Blacksmith"));
        CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("ScoutTower"));
        CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("GoldMine"));
        CPlayerCapability.Register(new CPlayerCapabilityBuildNormal("Wall"));


        CPlayerCapability.Register(new CPlayerCapabilityBuildingUpgrade("Keep"));
        CPlayerCapability.Register(new CPlayerCapabilityBuildingUpgrade("Castle"));
        CPlayerCapability.Register(new CPlayerCapabilityBuildingUpgrade("GuardTower"));
        CPlayerCapability.Register(new CPlayerCapabilityBuildingUpgrade("CannonTower"));

        CPlayerCapability.Register(new CPlayerCapabilityUnitUpgrade("WeaponUpgrade2"));
        CPlayerCapability.Register(new CPlayerCapabilityUnitUpgrade("WeaponUpgrade3"));
        CPlayerCapability.Register(new CPlayerCapabilityUnitUpgrade("ArmorUpgrade2"));
        CPlayerCapability.Register(new CPlayerCapabilityUnitUpgrade("ArmorUpgrade3"));
        CPlayerCapability.Register(new CPlayerCapabilityUnitUpgrade("ArrowUpgrade2"));
        CPlayerCapability.Register(new CPlayerCapabilityUnitUpgrade("ArrowUpgrade3"));
        CPlayerCapability.Register(new CPlayerCapabilityUnitUpgrade("BuildRanger"));
        CPlayerCapability.Register(new CPlayerCapabilityUnitUpgrade("BuildKnight"));
    }

    public CGameDataTypes.EAssetCapabilityType ActionToRender() {
        return DActionToRender;
    }

    public void ActionToRender(CGameDataTypes.EAssetCapabilityType Action) {
        DActionToRender = Action;
    }

    static int RangeToDistanceSquared(int range) {
        range *= CPosition.TileWidth();
        range *= range;
        range += CPosition.TileWidth() * CPosition.TileWidth();
        return range;
    }

    public int GameCycle() {
        return DGameCycle;
    }

    public void IncrementCycle() {
        DGameCycle++;
    }

    public CGameDataTypes.EPlayerColor Color() {
        return DColor;
    }

    public boolean IsAI() {
        return DIsAI;
    }

    public boolean IsAI(boolean isai) {
        return DIsAI = isai;
    }

    public boolean IsAlive() {
        if (DAssets.size() == 0) {
            return false;
        }
        return true;
    }

    public int Gold() {
        return DGold;
    }
    public int Lumber() {
        return DLumber;
    }
    public int IncrementGold(int gold) {
        DGold += gold;
        return DGold;
    }
    public int DecrementGold(int gold) {
        DGold -= gold;
        return DGold;
    }
    public int IncrementLumber(int lumber) {
        DLumber += lumber;
        return DLumber;
    }
    public int DecrementLumber(int lumber) {
        DLumber -= lumber;
        return DLumber;
    }

    public int FoodConsumption() {
        int TotalConsumption = 0;

        for (CPlayerAsset WeakAsset : DAssets) {
            CPlayerAsset Asset = WeakAsset; //if (auto Asset = WeakAsset.lock()) {
            int AssetConsumption = Asset.FoodConsumption();
            if (0 < AssetConsumption) {
                TotalConsumption += AssetConsumption;
            }
            // }
        }
        return TotalConsumption;
    }

    public int FoodProduction() {
        int TotalProduction = 0;

        for (CPlayerAsset WeakAsset : DAssets) {
            CPlayerAsset Asset = WeakAsset;//if (auto Asset = WeakAsset.lock()) {
            int AssetConsumption = Asset.FoodConsumption();
            if ((0 > AssetConsumption) && ((EAssetAction.aaConstruct != Asset.Action()) || (Asset
                    .CurrentCommand().DAssetTarget == null))) {
                TotalProduction += -AssetConsumption;
            }
            //}
        }
        return TotalProduction;
    }

    public CVisibilityMap VisibilityMap() {
        return DVisibilityMap;
    }

    public CAssetDecoratedMap PlayerMap() {
        return DPlayerMap;
    }
    public ArrayList<CPlayerAsset> Assets()  { //std::list< std::weak_ptr< CPlayerAsset > > Assets() const{
        return DAssets;
    }
    public HashMap<String, CPlayerAssetType> AssetTypes() { //std::shared_ptr< std::unordered_map< std::string, std::shared_ptr< CPlayerAssetType > > > &AssetTypes() {
        return DAssetTypes;
    };


    public CPlayerAsset CreateMarker (CPosition pos, boolean addtomap) {//std::shared_ptr< CPlayerAsset > CreateMarker(const CPosition &pos, bool addtomap) {
        CPlayerAsset NewMarker = DAssetTypes.get("None").Construct();
        CPosition TilePosition = new CPosition();

        TilePosition.SetToTile(pos);

        NewMarker.TilePosition(TilePosition);
        if (addtomap) {
            DPlayerMap.AddAsset(NewMarker);
//            DActualMap.AddAsset(NewMarker);
        }

        return NewMarker;
    }
    public CPlayerAsset CreateAsset(String assettypename) {
        Log.d("addedstuff", "Trying to create Asset: " + assettypename);

        CPlayerAsset CreatedAsset = new CPlayerAsset(DAssetTypes.get(assettypename).Construct());
        CreatedAsset.CreationCycle(DGameCycle);
        DAssets.add(CreatedAsset);
        //DActualMap.Assets().add(CreatedAsset);
        DActualMap.AddAsset(CreatedAsset);
        return CreatedAsset;
    }

    public CPlayerAsset CreateAsset(CAssetDecoratedMap.SAssetInitialization assettypename) {
//        Log.d("CPD", "Trying to create Asset: " + assettypename);
        CPlayerAsset CreatedAsset = DAssetTypes.get(assettypename.DType).Construct();
//        CreatedAsset.DTilePosition = assettypename.DTilePosition;
        CreatedAsset.CreationCycle(DGameCycle);
        DAssets.add(CreatedAsset);
//        Log.d("CPlayerData", "dassets: " + DAssets);
        DActualMap.AddAsset(CreatedAsset);
        return CreatedAsset;
    }

    public boolean AssetRequirementsMet( String assettypename) {
        ArrayList<Integer> AssetCount = new ArrayList<>();
        for (int resize = 0; resize <= EAssetType.atMax.ordinal(); resize++) {
            AssetCount.add(0);
        }

        for (CPlayerAsset WeakAsset : DAssets) {
            CPlayerAsset Asset = WeakAsset; //if (auto Asset = WeakAsset.lock()) {
            if (EAssetAction.aaConstruct != Asset.Action()) {
                AssetCount.set(Asset.Type().ordinal(), AssetCount.get(Asset.Type().ordinal()) + 1);
            }
        }
        //}
        for (EAssetType Requirement : DAssetTypes.get(assettypename).AssetRequirements()) {
            if (0 == AssetCount.get(Requirement.ordinal())) {
                if ((EAssetType.atKeep.ordinal() == Requirement.ordinal()) && (AssetCount.get
                        (EAssetType.atCastle.ordinal()) != 0)) {
                    continue;
                }
                if ((EAssetType.atTownHall == Requirement) && (AssetCount.get(EAssetType.atKeep
                        .ordinal()) != 0 ||AssetCount.get(EAssetType.atCastle.ordinal()) != 0)) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    public void DeleteAsset(CPlayerAsset asset) {
        for (CPlayerAsset Iterator : DAssets) {
            if (Iterator == asset) {
                DAssets.remove(Iterator);
                break;
            }
        }
        DActualMap.RemoveAsset(asset);
    }
    public void UpdateVisibility() {
        ArrayList<CPlayerAsset> RemoveList = new ArrayList<>();//std::list< std::shared_ptr< CPlayerAsset > > RemoveList;

        DVisibilityMap.Update(DAssets);
        DPlayerMap.UpdateMap(DVisibilityMap, DActualMap);
        for (CPlayerAsset Asset : DPlayerMap.Assets()) {
            if ((atNone == Asset.Type()) && (aaNone == Asset.Action())) {
                Asset.IncrementStep();
                if (CPlayerAsset.UpdateFrequency() < Asset.Step() * 2) {
                    RemoveList.add(Asset);
                }
            }
        }
        for (CPlayerAsset Asset : RemoveList) {
            DPlayerMap.RemoveAsset(Asset);
        }
    }

   public ArrayList<CPlayerAsset> SelectAssets( SRectangle selectarea, CGameDataTypes.EAssetType assettype, boolean selectidentical) { //std::list< std::weak_ptr< CPlayerAsset > > SelectAssets(const SRectangle &selectarea, EAssetType assettype, bool selectidentical = false) {
        ArrayList<CPlayerAsset> ReturnList = new ArrayList<CPlayerAsset>(); //std::list< std::weak_ptr< CPlayerAsset > > ReturnList;
//        Log.d("CPlayerData", "width: " + selectarea.DWidth + " height: " + selectarea.DHeight);
        if ((selectarea.DWidth == 0) || (selectarea.DHeight == 0)) {
//            Log.d("CPlayerData", "height and width = 0");
//            Log.d("chicken", "x " + selectarea.DXPosition + " y " + selectarea.DYPosition);
            CPlayerAsset BestAsset = SelectAsset(new CPosition(selectarea.DXPosition, selectarea.DYPosition), assettype);
            CPlayerAsset LockedAsset = BestAsset;//if (auto LockedAsset = BestAsset.lock()) {
//            Log.d("chicken", "SelectAssets Dassets: " + DAssets);
            ReturnList.add(BestAsset);
            if (selectidentical && LockedAsset.Speed() != 0) {
                for (CPlayerAsset WeakAsset : DAssets) {
                    CPlayerAsset Asset = WeakAsset;//if (auto Asset = WeakAsset.lock()) {
//                    Log.d("CPlayerData", "inside select assets: " + WeakAsset.DType);
                    if ((LockedAsset != Asset) && (Asset.Type() == assettype)) {
                        ReturnList.add(Asset);
                    }
                    //}
                }
            }
            //}
        } else {
            boolean AnyMovable = false;
//            Log.d("CPlayerData", "dassets: " + DAssets);
            for (CPlayerAsset WeakAsset : DAssets) {
//                Log.d("CPlayerData", "select asset dassets" + DAssets);
                CPlayerAsset Asset = WeakAsset;
                //if (CPlayerAsset Asset = WeakAsset.lock()) {
//                Log.d("CPlayerData", "x " + selectarea.DXPosition + " y " + selectarea.DYPosition);
                if ((selectarea.DXPosition <= Asset.PositionX()) && (Asset.PositionX() < selectarea.DXPosition + selectarea.DWidth) && (selectarea.DYPosition <= Asset.PositionY()) && (Asset.PositionY() < selectarea.DYPosition + selectarea.DHeight)) {
//                    Log.d("CPlayerData", "inside if statement");
                    if (AnyMovable) {
                        if (Asset.Speed() != 0) {
                            ReturnList.add(Asset);
                        }
                    }
                    else {
                        if (Asset.Speed() != 0) {
                            ReturnList.clear();
                            ReturnList.add(Asset);
                            AnyMovable = true;
                        }
                        else {
                            if (ReturnList.isEmpty()) {
                                ReturnList.add(Asset);
                            }
                        }
                    }
                }
                //}
            }
        }
        return ReturnList;
    }

    public CPlayerAsset SelectAsset(CPosition pos, CGameDataTypes.EAssetType assettype) {

        CPlayerAsset BestAsset = new CPlayerAsset();
        int BestDistanceSquared = -1;

        if (atNone != assettype) {
            for (CPlayerAsset WeakAsset : DAssets) {
                CPlayerAsset Asset = WeakAsset;
                //if (CPlayerAsset Asset = WeakAsset.lock()) {
                if (Asset.Type() == assettype) {
                    int CurrentDistance = Asset.Position().DistanceSquared(pos);

                    if ((-1 == BestDistanceSquared) || (CurrentDistance < BestDistanceSquared)) {
                        Log.d("chicken", "inside if statement");
                        BestDistanceSquared = CurrentDistance;
                        BestAsset = Asset;
                    }
                }
                //}
            }
        }

        return BestAsset;
    }

    public CPlayerAsset FindNearestOwnedAsset( CPosition pos, ArrayList< EAssetType > assettypes) {
        CPlayerAsset BestAsset = new CPlayerAsset();
        int BestDistanceSquared = -1;

        for (CPlayerAsset WeakAsset : DAssets) {
            for (EAssetType AssetType : assettypes) {
                if ((WeakAsset.Type() == AssetType) && ((EAssetAction.aaConstruct != WeakAsset.Action()) ||
                        (EAssetType.atKeep == AssetType) || (EAssetType.atCastle == AssetType))) {
                    int CurrentDistance = WeakAsset.Position().DistanceSquared(pos);

                    if ((-1 == BestDistanceSquared) || (CurrentDistance < BestDistanceSquared)) {
                        BestDistanceSquared = CurrentDistance;
                        BestAsset = WeakAsset;
                    }
                    break;
                }
            }
        }
        return BestAsset;
    }

    public CPlayerAsset FindNearestAsset( CPosition pos, CGameDataTypes.EAssetType assettype) {
        CPlayerAsset BestAsset = new CPlayerAsset();
        int BestDistanceSquared = -1;

        for (CPlayerAsset Asset : DPlayerMap.Assets()) {
            if (Asset.Type() == assettype) {
                int CurrentDistance = Asset.Position().DistanceSquared(pos);

                if ((-1 == BestDistanceSquared) || (CurrentDistance < BestDistanceSquared)) {
                    BestDistanceSquared = CurrentDistance;
                    BestAsset = Asset;
                }
            }
        }
        return BestAsset;
    }
    public CPlayerAsset FindNearestEnemy(CPosition pos, int range) {
        CPlayerAsset BestAsset = new CPlayerAsset();
        int BestDistanceSquared = -1;

        // Assume tile width == tile height
        if (0 < range) {
            range = RangeToDistanceSquared(range);
        }
        for (CPlayerAsset Asset : DActualMap.Assets()) {//DPlayerMap.Assets()) {
            if ((Asset.Color() != DColor) && (Asset.Color() != EPlayerColor.pcNone) && (Asset.Alive())) {
                SAssetCommand Command = Asset.CurrentCommand();
                if (EAssetAction.aaCapability == Command.DAction) {
                    if ((Command.DAssetTarget != null) && (EAssetAction.aaConstruct == Command
                            .DAssetTarget.Action())) {
                        continue;
                    }
                }
                if ((EAssetAction.aaConveyGold != Command.DAction) && (EAssetAction.aaConveyLumber
                        != Command.DAction) && (EAssetAction.aaMineGold != Command.DAction)) {
                    int CurrentDistance = Asset.ClosestPosition(pos).DistanceSquared( pos );//Asset.Position().DistanceSquared(pos);

                    if ((0 > range) || (CurrentDistance <= range)) {
                        if ((-1 == BestDistanceSquared) || (CurrentDistance < BestDistanceSquared)) {
                            BestDistanceSquared = CurrentDistance;
                            BestAsset = Asset;
                        }
                    }
                }
            }
        }
        return BestAsset;
    }
    public CPosition FindBestAssetPlacement( CPosition pos, CPlayerAsset builder, CGameDataTypes
            .EAssetType assetType, int buffer) {
        CPlayerAssetType AssetType = DAssetTypes.get(CPlayerAssetType.TypeToName(assetType));
        int PlacementSize = AssetType.Size() + 2 * buffer;
        int MaxDistance = Math.max(DPlayerMap.Width(), DPlayerMap.Height());

        for (int Distance = 1; Distance < MaxDistance; Distance++) {
            CPosition BestPosition = new CPosition();
            int BestDistance = -1;
            int LeftX = pos.X() - Distance;
            int TopY = pos.Y() - Distance;
            int RightX = pos.X() + Distance;
            int BottomY = pos.Y() + Distance;
            boolean LeftValid = true;
            boolean RightValid = true;
            boolean TopValid = true;
            boolean BottomValid = true;

            if (0 > LeftX) {
                LeftValid = false;
                LeftX = 0;
            }
            if (0 > TopY) {
                TopValid = false;
                TopY = 0;
            }
            if (DPlayerMap.Width() <= RightX) {
                RightValid = false;
                RightX = DPlayerMap.Width() - 1;
            }
            if (DPlayerMap.Height() <= BottomY) {
                BottomValid = false;
                BottomY = DPlayerMap.Height() - 1;
            }
            if (TopValid) {
                for (int Index = LeftX; Index <= RightX; Index++) {
                    CPosition TempPosition = new CPosition(Index, TopY);
                    if (DPlayerMap.CanPlaceAsset(TempPosition, PlacementSize, builder)) {
                        int CurrentDistance = builder.TilePosition().DistanceSquared(TempPosition);
                        if ((-1 == BestDistance) || (CurrentDistance < BestDistance)) {
                            BestDistance = CurrentDistance;
                            BestPosition = TempPosition;
                        }
                    }
                }
            }
            if (RightValid) {
                for (int Index = TopY; Index <= BottomY; Index++) {
                    CPosition TempPosition = new CPosition(RightX, Index);
                    if (DPlayerMap.CanPlaceAsset(TempPosition, PlacementSize, builder)) {
                        int CurrentDistance = builder.TilePosition().DistanceSquared(TempPosition);
                        if ((-1 == BestDistance) || (CurrentDistance < BestDistance)) {
                            BestDistance = CurrentDistance;
                            BestPosition = TempPosition;
                        }
                    }
                }
            }
            if (BottomValid) {
                for (int Index = LeftX; Index <= RightX; Index++) {
                    CPosition TempPosition = new CPosition(Index, BottomY);
                    if (DPlayerMap.CanPlaceAsset(TempPosition, PlacementSize, builder)) {
                        int CurrentDistance = builder.TilePosition().DistanceSquared(TempPosition);
                        if ((-1 == BestDistance) || (CurrentDistance < BestDistance)) {
                            BestDistance = CurrentDistance;
                            BestPosition = TempPosition;
                        }
                    }
                }
            }
            if (LeftValid) {
                for (int Index = TopY; Index <= BottomY; Index++) {
                    CPosition TempPosition = new CPosition(LeftX, Index);
                    if (DPlayerMap.CanPlaceAsset(TempPosition, PlacementSize, builder)) {
                        int CurrentDistance = builder.TilePosition().DistanceSquared(TempPosition);
                        if ((-1 == BestDistance) || (CurrentDistance < BestDistance)) {
                            BestDistance = CurrentDistance;
                            BestPosition = TempPosition;
                        }
                    }
                }
            }
            if (-1 != BestDistance) {
                return new CPosition(BestPosition.X() + buffer, BestPosition.Y() + buffer);
            }
        }

        return new CPosition(-1, -1);
    }
    public ArrayList<CPlayerAsset> IdleAssets() {//std::list< std::weak_ptr< CPlayerAsset > > IdleAssets() const{
        ArrayList<CPlayerAsset> AssetList = new ArrayList<>(); //std::list< std::weak_ptr< CPlayerAsset > > AssetList;

        for (CPlayerAsset Asset : DAssets) {
//            if (CPlayerAsset Asset = WeakAsset) {
            if ((aaNone == Asset.Action()) && (atNone != Asset.Type())) {
                AssetList.add(Asset);
            }
//            }
        }
        return AssetList;
    }

    public int PlayerIdleAssetCount() {
        int Count = 0;
        for (CPlayerAsset Asset : DAssets) {
            if ((aaNone == Asset.Action()) && (atNone != Asset.Type())) {
                Count++;
            }
        }
        return Count;
    }

    public int PlayerAssetCount(CGameDataTypes.EAssetType type) {
        int Count = 0;

        for (CPlayerAsset Asset : DPlayerMap.Assets()) {
            if ((Asset.Color() == DColor) && (type == Asset.Type())) {
                Count++;
            }
        }

        return Count;
    }

    public int FoundAssetCount(CGameDataTypes.EAssetType type) {
        int Count = 0;

        for (CPlayerAsset Asset : DPlayerMap.Assets()) {
            if (type == Asset.Type()) {
                Count++;
            }
        }

        return Count;
    }
    public void AddUpgrade(String upgradename) {
        CPlayerUpgrade Upgrade = CPlayerUpgrade.FindUpgradeFromName(upgradename);

        if (Upgrade != null) {
            for (CGameDataTypes.EAssetType AssetType : Upgrade.AffectedAssets()) {
                String AssetName = CPlayerAssetType.TypeToName(AssetType);
                CPlayerAssetType AssetIterator = DAssetTypes.get(AssetName);

                if (AssetIterator != DAssetTypes.get(DAssetTypes.size() - 1)) {
                    AssetIterator.AddUpgrade(Upgrade);
                }

            }
            DUpgrades.set(CPlayerCapability.NameToType(upgradename).ordinal(), true);
        }
    }

    public boolean HasUpgrade(CGameDataTypes.EAssetCapabilityType upgrade) {
        if ((0 > upgrade.ordinal()) || (DUpgrades.size() <= upgrade.ordinal())) {
            return false;
        }
        return DUpgrades.get(upgrade.ordinal());
    }

    public ArrayList<CGameModel.SGameEvent> GameEvents() {
        return DGameEvents;
    }
    public void ClearGameEvents() {
        DGameEvents.clear();
    }
    public void AddGameEvent(CGameModel.SGameEvent event) {
        DGameEvents.add(event);
    }
    public void AppendGameEvents( ArrayList<CGameModel.SGameEvent> events) {
        for (CGameModel.SGameEvent eventIt: events) {
            DGameEvents.add(eventIt);
        }
    }
}