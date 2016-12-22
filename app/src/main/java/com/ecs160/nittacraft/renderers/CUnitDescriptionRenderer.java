package com.ecs160.nittacraft.renderers;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.ecs160.nittacraft.CPlayerAsset;
import com.ecs160.nittacraft.R;
import com.ecs160.nittacraft.SAssetCommand;
import com.ecs160.nittacraft.fragments.AssetBattleDescriptionFragment;
import com.ecs160.nittacraft.fragments.AssetCompletionFragment;
import com.ecs160.nittacraft.fragments.AssetDescriptionFragment;
import com.ecs160.nittacraft.maps.CTerrainMap;

import java.util.ArrayList;

import static com.ecs160.nittacraft.CApplicationData.DIconTileset;
import static com.ecs160.nittacraft.CApplicationData.DMainActivity;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaCapability;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetAction.aaConstruct;
import static com.ecs160.nittacraft.views.GameView.DApplicationData;

public class CUnitDescriptionRenderer {
    private static String FRAG_BASIC_DESCRIPTION = "assetBasicDescription";
    private static String FRAG_BATTLE_DESCRIPTION = "assetBattleDescription";
    private boolean hasProgress;
    protected boolean DInitialized;

    protected CTerrainMap DMap;
    protected boolean isEmpty;

    public CUnitDescriptionRenderer(CTerrainMap map) {
        DMap = map;
        hasProgress = true;
        DInitialized = false;
        isEmpty = true;
    }

    private void InitializeViews() {
        DInitialized = true;
    }

    public void DrawDescriptions(final ArrayList<CPlayerAsset> assetList) {
        if (!DInitialized) {
            InitializeViews();
        }

        int PercentComplete;
        String text = "";

        if (assetList != null && !assetList.isEmpty() && assetList.get(0) != null) {
            Bitmap tmpIcon = null;
            CPlayerAsset Asset = assetList.get(0);
            switch (Asset.AssetType().Name()) {
                case "Peasant":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("peasant"));
                    break;
                case "Footman":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("footman"));
                    break;
                case "Archer":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("archer"));
                    break;
                case "Ranger":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("ranger"));
                    break;
                case "GoldMine":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("gold-mine"));
                    break;
                case "GoldVein":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("gold-mine"));
                    break;
                case "TownHall":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("town-hall"));
                    break;
                case "Keep":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("keep"));
                    break;
                case "Castle":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("castle"));
                    break;
                case "Farm":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("chicken-farm"));
                    break;
                case "Barracks":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-barracks"));
                    break;
                case "LumberMill":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-lumber-mill"));
                    break;
                case "Blacksmith":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-blacksmith"));
                    break;
                case "ScoutTower":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("scout-tower"));
                    break;
                case "GuardTower":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-guard-tower"));
                    break;
                case "CannonTower":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-cannon-tower"));
                    break;
                case "Move":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-move"));
                    break;
                case "Repair":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("repair"));
                    break;
                case "Mine":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("mine"));
                    break;
                case "BuildSimple":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("build-simple"));
                    break;
                case "BuildAdvanced":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("build-advanced"));
                    break;
                case "Convey":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-convey"));
                    break;
                case "Cancel":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("cancel"));
                    break;
                case "Wall":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-wall"));
                    break;
                case "Attack":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-weapon-1"));
                    break;
                case "StandGround":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-armor-1"));
                    break;
                case "Patrol":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-patrol"));
                    break;
                case "WeaponUpgrade1":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-weapon-1"));
                    break;
                case "WeaponUpgrade2":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-weapon-2"));
                    break;
                case "WeaponUpgrade3":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-weapon-3"));
                    break;
                case "ArrowUpgrade1":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-arrow-1"));
                    break;
                case "ArrowUpgrade2":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-arrow-2"));
                    break;
                case "ArrowUpgrade3":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-arrow-3"));
                    break;
                case "ArmorUpgrade1":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-armor-1"));
                    break;
                case "ArmorUpgrade2":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-armor-2"));
                    break;
                case "ArmorUpgrade3":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("human-armor-3"));
                    break;
                case "Longbow":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("longbow"));
                    break;
                case "RangerScouting":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("ranger-scouting"));
                    break;
                case "Marksmanship":
                    tmpIcon = DIconTileset.mTile(DIconTileset.FindTile("marksmanship"));
                    break;
            }

            Bitmap progress = null;
            hasProgress = true;
            if (aaConstruct == Asset.Action()) {
                SAssetCommand Command = Asset.CurrentCommand();

                PercentComplete = 0;
                if (Command.DAssetTarget != null) { // why does it have this check if all it does is end up updating the progress bar?
                    SAssetCommand Command2 = Command.DAssetTarget.CurrentCommand();
                    if (Command2.DActivatedCapability != null) {
                        text = "Building: ";
                        PercentComplete = Command2.DActivatedCapability.PercentComplete(100);
                    }
                }

                if (Command.DActivatedCapability != null) {
                    text = "Upgrading: ";
                    PercentComplete = Command.DActivatedCapability.PercentComplete(100);
                }
            } else if (aaCapability == Asset.Action()) {
                SAssetCommand Command = Asset.CurrentCommand();

                PercentComplete = 1;
                if (Command.DActivatedCapability != null) {
                    PercentComplete = Command.DActivatedCapability.PercentComplete(100);
                }
                if (Command.DAssetTarget.DType() != null) {
                    Rect bounds = new Rect();

                    if (Command.DAssetTarget.Speed() == 0) {
                        PercentComplete = 0;
                    } else {
                        text = "Training: ";
                    }

                    int index = DIconTileset.FindTile(Command.DAssetTarget.Type().name().substring(2).toLowerCase());
                    if (index >= 0) {
                        progress = DIconTileset.mTile(index);
                    } else {
                        Log.d("MD", Command.DAssetTarget.Type().name().substring(2).toLowerCase());
                    }
                } else {
                    text = "Researching: ";

                    int index = DIconTileset.FindTile(Command.DCapability.name().toLowerCase());
                    if (index >= 0) {
                        progress = DIconTileset.mTile(index);
                    } else {
                        Log.d("MD", "Index = " + index +  " :: " + Command.DCapability.name().toLowerCase());
                    }

                }
            } else {
                hasProgress = false;
                PercentComplete = 0;
            }

            final Bitmap icon = tmpIcon;
            final Bitmap progressIcon = progress;
            final String progressText = text;
            final int pc = PercentComplete;

            // Draw data to sidebar
            DMainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("MESD", "URGENT");
                    CPlayerAsset asset = assetList.get(0);
//                    Show fragments based on asset.AssetType().DType
                    FragmentTransaction ft = DMainActivity.getSupportFragmentManager().beginTransaction();

                    AssetDescriptionFragment basicInfo = AssetDescriptionFragment.newInstance
                            (icon, asset.AssetType().Name(), asset.HitPoints(), asset.MaxHitPoints());
                    AssetBattleDescriptionFragment battleInfo = AssetBattleDescriptionFragment.newInstance(asset.PiercingDamage()/2, asset.PiercingDamage() + asset.BasicDamage(), asset.Armor(), asset.Speed(), asset.Sight());

                    ft.replace(R.id.fl_basic_info, basicInfo, FRAG_BASIC_DESCRIPTION);
                    ft.replace(R.id.fl_battle_info, battleInfo, FRAG_BATTLE_DESCRIPTION);

                    if (hasProgress) {
                        AssetCompletionFragment fac = AssetCompletionFragment.newInstance(progressIcon, progressText, 100);
                        ft.replace(R.id.fl_action_completion, fac, CUnitActionRenderer.FRAG_ASSET_ACTIONS);
                    }

                    ft.commit();
                }
            });
        }
    }

    public void UpdateDescriptions(final ArrayList<CPlayerAsset> assetList) {
        if (assetList == null || assetList.size() < 1) {
            FragmentManager fm = DMainActivity.getSupportFragmentManager();
            if (fm != null) {
                FragmentTransaction ft = fm.beginTransaction();
                if (fm.findFragmentByTag(FRAG_BASIC_DESCRIPTION) != null) {
                    ft.remove((fm.findFragmentByTag(FRAG_BASIC_DESCRIPTION)));
                }

                if (fm.findFragmentByTag(FRAG_BATTLE_DESCRIPTION) != null) {
                    ft.remove((fm.findFragmentByTag(FRAG_BATTLE_DESCRIPTION)));
                }

                if (fm.findFragmentByTag(CUnitActionRenderer.FRAG_ASSET_ACTIONS) != null) {
                    ft.remove((fm.findFragmentByTag(CUnitActionRenderer.FRAG_ASSET_ACTIONS)));
                }

                if (!isEmpty) {
                    ft.commit();
                    isEmpty = true;
                }
            }
            return;
        }

        isEmpty = false;
        Fragment f = DMainActivity.getSupportFragmentManager().findFragmentByTag(FRAG_BASIC_DESCRIPTION);

        if (f != null && f instanceof AssetDescriptionFragment) {
            AssetDescriptionFragment tBasicInfo = (AssetDescriptionFragment) f;
            tBasicInfo.setCurrHP(assetList.get(0).HitPoints());
        }

        CPlayerAsset Asset = assetList.get(0);
        int PercentComplete = 0;

        if (aaConstruct == Asset.Action()) {
            SAssetCommand Command = Asset.CurrentCommand();
            PercentComplete = 0;
            if (Command.DAssetTarget != null) { // why does it have this check if all it does is end up updating the progress bar?
                SAssetCommand Command2 = Command.DAssetTarget.CurrentCommand();
                if (Command2.DActivatedCapability != null) {
                    PercentComplete = Command2.DActivatedCapability.PercentComplete(100);
                }
            }
            if (Command.DActivatedCapability != null) {
                PercentComplete = Command.DActivatedCapability.PercentComplete(100);
            }
        } else if (aaCapability == Asset.Action()) {
            SAssetCommand Command = Asset.CurrentCommand();
            PercentComplete = 0;
            if (Command.DActivatedCapability != null) {
                PercentComplete = Command.DActivatedCapability.PercentComplete(100);
            }
            if (Command.DAssetTarget.DType() != null) {
                Rect bounds = new Rect();
                if (Command.DAssetTarget.Speed() == 0) {
                    PercentComplete = 0;
                }
            }

        } else {
            PercentComplete = 0;
        }
        Log.d("TEST", PercentComplete + "");
        Fragment frag = DMainActivity.getSupportFragmentManager().findFragmentByTag
                (CUnitActionRenderer.FRAG_ASSET_ACTIONS);
        if (PercentComplete > 0) {

            if (frag != null && !(frag instanceof AssetCompletionFragment)) {
                // If we need to switch, run draw
                DrawDescriptions(assetList);
            }

            if (frag != null && frag instanceof AssetCompletionFragment) {
                AssetCompletionFragment fac = (AssetCompletionFragment) frag;
                fac.setProgress(PercentComplete);
            }
        } else {
            if (frag instanceof AssetCompletionFragment) {
                DApplicationData.DUnitActionRenderer.DrawUnitAction(DApplicationData.DSelectedPlayerAssets.get
                        (DApplicationData.DPlayerColor.ordinal()), DApplicationData
                        .DCurrentAssetCapability[DApplicationData.DPlayerColor.ordinal()]);
            }
        }
    }
}