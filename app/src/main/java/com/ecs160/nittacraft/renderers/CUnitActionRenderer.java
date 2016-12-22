package com.ecs160.nittacraft.renderers;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.ecs160.nittacraft.Action;
import com.ecs160.nittacraft.ActionAdapter;
import com.ecs160.nittacraft.CGameDataTypes;
import com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType;
import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.CPlayerData;
import com.ecs160.nittacraft.R;
import com.ecs160.nittacraft.Resource;
import com.ecs160.nittacraft.capabilities.CPlayerCapability;
import com.ecs160.nittacraft.fragments.AssetActionsFragment;
import com.ecs160.nittacraft.maps.CTerrainMap;

import java.util.ArrayList;

import static com.ecs160.nittacraft.CApplicationData.DIconTileset;
import static com.ecs160.nittacraft.CApplicationData.DMainActivity;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actConvey;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actLongbow;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actRepair;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actStandGround;

public class CUnitActionRenderer {
    public static String FRAG_ASSET_ACTIONS = "assetActions";

    protected CPlayerData DPlayerData;
    protected ArrayList<Integer> DCommandIndices;
    protected ArrayList<EAssetCapabilityType> DDisplayedCommands;
    protected int DDisabledIndex;
    protected CTerrainMap DMap;
    protected CMapRenderer DMapRenderer;
    protected ArrayList<EAssetCapabilityType> buttonPos;
    protected ActionAdapter actionAdapter;
    private boolean DInitialized;

    public CUnitActionRenderer(CTerrainMap map, CMapRenderer mapRenderer) {
        DInitialized = false;
        buttonPos = new ArrayList<>(10);
        DMap = map;
        DMapRenderer = mapRenderer;

        DCommandIndices = new ArrayList<>();
        DDisplayedCommands = new ArrayList<>();
        DCommandIndices.ensureCapacity(EAssetCapabilityType.actMax.ordinal());
        DDisplayedCommands.ensureCapacity(9);

        // Need
        for (EAssetCapabilityType Commands : DDisplayedCommands) {
            Commands = EAssetCapabilityType.actNone;
        }

        // Links all the action build buttons with the relevant index from the IconTileset
        DCommandIndices.ensureCapacity(EAssetCapabilityType.actMax.ordinal());
        DCommandIndices.add(EAssetCapabilityType.actNone.ordinal(), -1);
        DCommandIndices.add(EAssetCapabilityType.actBuildPeasant.ordinal(), DIconTileset.FindTile("peasant"));
        DCommandIndices.add(EAssetCapabilityType.actBuildFootman.ordinal(), DIconTileset.FindTile("footman"));
        DCommandIndices.add(EAssetCapabilityType.actBuildArcher.ordinal(), DIconTileset.FindTile("archer"));
        DCommandIndices.add(EAssetCapabilityType.actBuildRanger.ordinal(), DIconTileset.FindTile("ranger"));
        DCommandIndices.add(EAssetCapabilityType.actBuildFarm.ordinal(), DIconTileset.FindTile("chicken-farm"));
        DCommandIndices.add(EAssetCapabilityType.actBuildTownHall.ordinal(), DIconTileset.FindTile("town-hall"));
        DCommandIndices.add(EAssetCapabilityType.actBuildBarracks.ordinal(), DIconTileset.FindTile("human-barracks"));
        DCommandIndices.add(EAssetCapabilityType.actBuildLumberMill.ordinal(), DIconTileset.FindTile("human-lumber-mill"));
        DCommandIndices.add(EAssetCapabilityType.actBuildBlacksmith.ordinal(), DIconTileset.FindTile("human-blacksmith"));
        DCommandIndices.add(EAssetCapabilityType.actBuildGoldMine.ordinal(), DIconTileset.FindTile("gold-mine"));
        DCommandIndices.add(EAssetCapabilityType.actBuildKeep.ordinal(), DIconTileset.FindTile("keep"));
        DCommandIndices.add(EAssetCapabilityType.actBuildCastle.ordinal(), DIconTileset.FindTile("castle"));
        DCommandIndices.add(EAssetCapabilityType.actBuildScoutTower.ordinal(), DIconTileset.FindTile("scout-tower"));
        DCommandIndices.add(EAssetCapabilityType.actBuildGuardTower.ordinal(), DIconTileset.FindTile("human-guard-tower"));
        DCommandIndices.add(EAssetCapabilityType.actBuildCannonTower.ordinal(), DIconTileset.FindTile("human-cannon-tower"));
        DCommandIndices.add(EAssetCapabilityType.actMove.ordinal(), DIconTileset.FindTile("human-move"));
        DCommandIndices.add(actRepair.ordinal(), DIconTileset.FindTile("repair"));
        DCommandIndices.add(EAssetCapabilityType.actMine.ordinal(), DIconTileset.FindTile("mine"));
        DCommandIndices.add(EAssetCapabilityType.actBuildSimple.ordinal(), DIconTileset.FindTile("build-simple"));
        DCommandIndices.add(EAssetCapabilityType.actBuildAdvanced.ordinal(), DIconTileset.FindTile("build-advanced"));
        DCommandIndices.add(actConvey.ordinal(), DIconTileset.FindTile("human-convey"));
        DCommandIndices.add(EAssetCapabilityType.actCancel.ordinal(), DIconTileset.FindTile("cancel"));
        DCommandIndices.add(EAssetCapabilityType.actBuildWall.ordinal(), DIconTileset.FindTile("human-wall"));
        DCommandIndices.add(EAssetCapabilityType.actAttack.ordinal(), DIconTileset.FindTile("human-weapon-1"));
        DCommandIndices.add(actStandGround.ordinal(), DIconTileset.FindTile("human-armor-1"));
        DCommandIndices.add(EAssetCapabilityType.actPatrol.ordinal(), DIconTileset.FindTile("human-patrol"));
        DCommandIndices.add(EAssetCapabilityType.actWeaponUpgrade1.ordinal(), DIconTileset.FindTile("human-weapon-1"));
        DCommandIndices.add(EAssetCapabilityType.actWeaponUpgrade2.ordinal(), DIconTileset.FindTile("human-weapon-2"));
        DCommandIndices.add(EAssetCapabilityType.actWeaponUpgrade3.ordinal(), DIconTileset.FindTile("human-weapon-3"));
        DCommandIndices.add(EAssetCapabilityType.actArrowUpgrade1.ordinal(), DIconTileset.FindTile("human-arrow-1"));
        DCommandIndices.add(EAssetCapabilityType.actArrowUpgrade2.ordinal(), DIconTileset.FindTile("human-arrow-2"));
        DCommandIndices.add(EAssetCapabilityType.actArrowUpgrade3.ordinal(), DIconTileset.FindTile("human-arrow-3"));
        DCommandIndices.add(EAssetCapabilityType.actArmorUpgrade1.ordinal(), DIconTileset.FindTile("human-armor-1"));
        DCommandIndices.add(EAssetCapabilityType.actArmorUpgrade2.ordinal(), DIconTileset.FindTile("human-armor-2"));
        DCommandIndices.add(EAssetCapabilityType.actArmorUpgrade3.ordinal(), DIconTileset.FindTile("human-armor-3"));
        DCommandIndices.add(actLongbow.ordinal(), DIconTileset.FindTile("longbow"));
        DCommandIndices.add(EAssetCapabilityType.actRangerScouting.ordinal(), DIconTileset.FindTile("ranger-scouting"));
        DCommandIndices.add(EAssetCapabilityType.actMarksmanship.ordinal(), DIconTileset.FindTile("marksmanship"));

        DDisabledIndex = DIconTileset.FindTile("disabled");
    }


    private void InitializeViews() {
        actionAdapter = new ActionAdapter(DMainActivity.getApplicationContext());
        DInitialized = true;
    }

    public EAssetCapabilityType Selection(int index) {
        if (buttonPos.get(index) != null) {
            return buttonPos.get(index);
        }

        return EAssetCapabilityType.actNone;
    }

    public void DrawUnitAction(ArrayList<CPlayerAsset> selectionList, EAssetCapabilityType
            currentaction) {
        if (!DInitialized) {
            InitializeViews();
        }

        buttonPos.clear();
        if (selectionList.isEmpty()) {
            actionAdapter.clearActions();
            buttonPos.clear();
            return;
        }

        // TODO: check resources available before displaying action buttons
        Bitmap tmpAction = null;
        final ArrayList<Action> actions = new ArrayList<>();

        if (selectionList != null && !selectionList.isEmpty() && selectionList.get(0) != null) {
            CPlayerAsset Asset = selectionList.get(0);
                int count = 0;
                for (CGameDataTypes.EAssetCapabilityType type : Asset.Capabilities()) {
                    count++;
                    switch (type) {
                        case actBuildPeasant:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("peasant"));
                            break;
                        case actBuildFootman:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("footman"));
                            break;
                        case actBuildArcher:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("archer"));
                            break;
                        case actBuildRanger:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("ranger"));
                            break;
                        case actBuildFarm:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("chicken-farm"));
                            break;
                        case actBuildTownHall:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("town-hall"));
                            break;
                        case actBuildBarracks:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-barracks"));
                            break;
                        case actBuildLumberMill:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-lumber-mill"));
                            break;
                        case actBuildBlacksmith:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-blacksmith"));
                            break;
                        case actBuildKeep:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("keep"));
                            break;
                        case actBuildCastle:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("castle"));
                            break;
                        case actBuildScoutTower:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("scout-tower"));
                            break;
                        case actBuildGuardTower:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-guard-tower"));
                            break;
                        case actBuildCannonTower:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-cannon-tower"));
                            break;
                        case actMove:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-move"));
                            break;
                        case actRepair:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("repair"));
                            break;
                        case actMine:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("mine"));
                            break;
                        case actBuildSimple:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("build-simple"));
                            break;
                        case actBuildAdvanced:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("build-advanced"));
                            break;
                        case actConvey:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-convey"));
                            break;
                        case actCancel:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("cancel"));
                            break;
                        case actBuildWall:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-wall"));
                            break;
                        case actAttack:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-weapon-1"));
                            break;
                        case actStandGround:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-armor-1"));
                            break;
                        case actPatrol:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-patrol"));
                            break;
                        case actWeaponUpgrade1:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-weapon-1"));
                            break;
                        case actWeaponUpgrade2:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-weapon-2"));
                            break;
                        case actWeaponUpgrade3:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-weapon-3"));
                            break;
                        case actArrowUpgrade1:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-arrow-1"));
                            break;
                        case actArrowUpgrade2:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-arrow-2"));
                            break;
                        case actArrowUpgrade3:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-arrow-3"));
                            break;
                        case actArmorUpgrade1:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-armor-1"));
                            break;
                        case actArmorUpgrade2:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-armor-2"));
                            break;
                        case actArmorUpgrade3:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("human-armor-3"));
                            break;
                        case actLongbow:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("longbow"));
                            break;
                        case actRangerScouting:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("ranger-scouting"));
                            break;
                        case actMarksmanship:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("marksmanship"));
                            break;
                        case actBuildGoldMine:
                            tmpAction = DIconTileset.mTile(DIconTileset.FindTile("gold-mine"));
                            break;
                        
                    }

                    if (tmpAction != null) {
                        CPlayerCapability PlayerCapability = CPlayerCapability.FindCapability(type);
                        if (PlayerCapability != null) {
                            Log.d("MDA", PlayerCapability.DName);
                            // TODO: Add cost of action
                            ArrayList<Resource> resources = new ArrayList<>();
                            actions.add(new Action(tmpAction, PlayerCapability.DName,resources));
                            buttonPos.add(type);
                        }
                    }
                }

            actionAdapter.setActions(actions);
            DMainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction ft = DMainActivity.getSupportFragmentManager()
                            .beginTransaction();
                    ft.replace(R.id.fl_action_completion, AssetActionsFragment.newInstance
                            (actions), FRAG_ASSET_ACTIONS);
                    ft.commit();
                }
            });
        }
    }
}