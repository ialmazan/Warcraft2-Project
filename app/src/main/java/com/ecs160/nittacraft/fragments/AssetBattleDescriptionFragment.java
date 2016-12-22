package com.ecs160.nittacraft.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecs160.nittacraft.R;

public class AssetBattleDescriptionFragment extends Fragment {
    private final static String ARG_ICON_ATK_MIN = "battleAtkMin";
    private final static String ARG_ICON_ATK_MAX = "battleAtkMax";
    private final static String ARG_ICON_DEF = "battleDef";
    private final static String ARG_ICON_SPD = "battleSpd";
    private final static String ARG_ICON_SIGHT = "battleSight";

    private ImageView mIconAtk;
    private ImageView mIconDef;
    private ImageView mIconSpd;
    private ImageView mIconSight;

    private TextView mValAtk;
    private TextView mValDef;
    private TextView mValSpd;
    private TextView mValSight;

    public AssetBattleDescriptionFragment() {}
    public static AssetBattleDescriptionFragment newInstance(int atkMin, int atkMax, int def, int
            spd, int sight) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ICON_ATK_MIN, atkMin);
        bundle.putInt(ARG_ICON_ATK_MAX, atkMax);
        bundle.putInt(ARG_ICON_DEF, def);
        bundle.putInt(ARG_ICON_SPD, spd);
        bundle.putInt(ARG_ICON_SIGHT, sight);

        AssetBattleDescriptionFragment frag = new AssetBattleDescriptionFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View battleAssetView = inflater.inflate(R.layout.fragment_asset_battle_description, container, false);
        mIconAtk = (ImageView) battleAssetView.findViewById(R.id.iv_asset_damage);
        mIconDef = (ImageView) battleAssetView.findViewById(R.id.iv_asset_armor);
        mIconSpd = (ImageView) battleAssetView.findViewById(R.id.iv_asset_speed);
        mIconSight = (ImageView) battleAssetView.findViewById(R.id.iv_asset_sight);

        mValAtk = (TextView) battleAssetView.findViewById(R.id.tv_asset_damage);
        mValDef = (TextView) battleAssetView.findViewById(R.id.tv_asset_armor);
        mValSpd = (TextView) battleAssetView.findViewById(R.id.tv_asset_speed);
        mValSight = (TextView) battleAssetView.findViewById(R.id.tv_asset_sight);

        Bundle args = getArguments();
        mValAtk.setText(args.getInt(ARG_ICON_ATK_MIN) + " - " + args.getInt(ARG_ICON_ATK_MAX));
        mValDef.setText(Integer.toString(args.getInt(ARG_ICON_DEF)));
        mValSpd.setText(Integer.toString(args.getInt(ARG_ICON_SPD)));
        mValSight.setText(Integer.toString(args.getInt(ARG_ICON_SIGHT)));

        return battleAssetView;
    }

    public void setAtk(int atkMin, int atkMax) { mValAtk.setText(atkMin + " - " + atkMax); }
    public void setDef(int def) { mValDef.setText(def); }
    public void setSpd(int spd) { mValSpd.setText(spd); }
    public void setSight(int sight) { mValSight.setText(sight); }
}
