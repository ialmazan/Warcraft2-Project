package com.ecs160.nittacraft.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ecs160.nittacraft.Action;
import com.ecs160.nittacraft.ActionAdapter;
import com.ecs160.nittacraft.CApplicationData;
import com.ecs160.nittacraft.R;
import com.ecs160.nittacraft.views.GameView;

import java.util.ArrayList;

import static com.ecs160.nittacraft.CApplicationData.DMainActivity;
import static com.ecs160.nittacraft.views.GameView.DApplicationData;

public class AssetActionsFragment extends Fragment {

    private static String ARG_ACTIONS = "assetActions";

    public AssetActionsFragment() {}
    public static AssetActionsFragment newInstance(ArrayList<Action> actions)
    {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ACTIONS, actions);
        AssetActionsFragment f = new AssetActionsFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        final View actionsView = inflater.inflate(R.layout.fragment_asset_actions , container,
                false);

        ListView mActions = (ListView) actionsView.findViewById(R.id.lv_asset_actions);
        mActions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DApplicationData.selectedCapability = DApplicationData.DUnitActionRenderer
                        .Selection(i);
                DApplicationData.DInputState = CApplicationData.EInputState.isCommand;
                DApplicationData.ProcessInput(GameView.getInstance(DMainActivity
                        .getApplicationContext()));
            }
        });

        Bundle args = getArguments();

        ArrayList<Action> actionList = args.getParcelableArrayList(ARG_ACTIONS);
        Log.d("MDA", actionList.size() + " actions");
        ActionAdapter actions = new ActionAdapter(actionsView.getContext(), actionList);
        mActions.setAdapter(actions);

        return actionsView;
    }
}