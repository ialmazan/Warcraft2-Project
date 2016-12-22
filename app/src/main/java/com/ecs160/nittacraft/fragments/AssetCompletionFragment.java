package com.ecs160.nittacraft.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecs160.nittacraft.R;

import java.io.ByteArrayOutputStream;

public class AssetCompletionFragment extends Fragment {
    private ImageView mIcon;
    private TextView mAction;
    private ProgressBar mCompletion;

    private static String ARG_ICON = "progressIcon";
    private static String ARG_ACTION = "progressAction";
    private static String ARG_PROGRESS = "progressBar";

    public AssetCompletionFragment() {}
    public static AssetCompletionFragment newInstance(Bitmap icon, String action, int max) {
        Bundle args = new Bundle();
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        args.putParcelable(ARG_ICON, icon);
        args.putString(ARG_ACTION, action);
        args.putInt(ARG_PROGRESS, max);

        AssetCompletionFragment fragment = new AssetCompletionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        final View completionView = inflater.inflate(R.layout.fragment_asset_completion,
                container, false);

        mIcon = (ImageView)completionView.findViewById(R.id.iv_asset_completion_icon);
        mAction = (TextView)completionView.findViewById(R.id.tv_asset_completion);
        mCompletion = (ProgressBar)completionView.findViewById(R.id.pb_asset_completion);

        Bundle args = getArguments();
        Parcelable icon = args.getParcelable(ARG_ICON);
        if (icon != null && icon instanceof Bitmap) {
            mIcon.setImageBitmap((Bitmap)icon);
        }

        mAction.setText(args.getString(ARG_ACTION));
        mCompletion.setMax(args.getInt(ARG_PROGRESS));
        mCompletion.setProgress(0);

        return completionView;
    }

    public void setProgress(int currProgress) {
        if (currProgress < 0) {
            Log.e("Progress", "Attempted to set research / training / upgrade progress < 0 ::" +
                    currProgress);
            return;
        }

        mCompletion.setProgress(currProgress);
    }

    public void setIcon(Bitmap bmp) {
        if (bmp == null) {
            Log.e("Progress", "Attempted to set a null bitmap");
            return;
        }

        mIcon.setImageBitmap(bmp);
    }

    public void setAction(String action) {
        mAction.setText(action);
    }


}
