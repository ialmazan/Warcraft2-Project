package com.ecs160.nittacraft.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecs160.nittacraft.R;

public class AssetDescriptionFragment extends Fragment {
    private static final String ARG_ICON = "assetIcon";
    private static final String ARG_NAME = "assetName";
    private static final String ARG_CURR_HP = "assetCurrHP";
    private static final String ARG_MAX_HP = "assetMaxHP";

    private ImageView mIcon;
    private TextView mName;
    private TextView mHP;
    private int mCurrHP;
    private int mMaxHP;

    public AssetDescriptionFragment() {}
    public static AssetDescriptionFragment newInstance(Bitmap icon, String name, int currHP, int maxHP) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_ICON, icon);
        args.putString(ARG_NAME, name);
        args.putInt(ARG_CURR_HP, currHP);
        args.putInt(ARG_MAX_HP, maxHP);

        final AssetDescriptionFragment fragment = new AssetDescriptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AssetDescriptionFragment newInstance() {
       return new AssetDescriptionFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IAssetDescription) {
            IAssetDescription mListener = (IAssetDescription) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement IAssetDescription");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View assetView = inflater.inflate(R.layout.fragment_asset_description, container, false);
        mIcon = (ImageView)assetView.findViewById(R.id.iv_asset_icon);
        mName = (TextView)assetView.findViewById(R.id.tv_asset_name);
        mHP = (TextView)assetView.findViewById(R.id.tv_asset_hp);

        // Fragment can recall initial parameters from its persisted arguments
        final Bundle args = getArguments();
        mCurrHP = args.getInt(ARG_CURR_HP);
        mMaxHP = args.getInt(ARG_MAX_HP);

        Parcelable icon = args.getParcelable(ARG_ICON);
        if (icon != null && icon instanceof Bitmap) {
            mIcon.setImageBitmap((Bitmap)icon);
        }

        mName.setText(args.getString(ARG_NAME));
        mHP.setText("HP: " + mCurrHP + " / " + mMaxHP);

        // Inflate the layout for this fragment
        return assetView;
    }

    public void setName(String name) {
        mName.setText(name);
    }

    public void setIcon(Bitmap icon) {
        mIcon.setImageBitmap(icon);
    }

    public void setMaxHP(int maxHP) {
        mMaxHP = maxHP;
        mHP.setText("HP: " + mCurrHP + " / " + mMaxHP);
    }

    public void setCurrHP(int currHP) {
        mCurrHP = currHP;
        mHP.setText("HP: " + mCurrHP + " / " + mMaxHP);
    }

    // Fragment communicate to MainActivity
    // To communicate MainActivity -> Fragment, get an instance of this fragment and invoke its public methods
    public interface IAssetDescription {
        void setAssetDescription(Bitmap icon, String name, int currHP, int maxHP);
        void updateHP(int currHP);
    }
}
