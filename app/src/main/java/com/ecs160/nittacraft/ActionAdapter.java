package com.ecs160.nittacraft;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ActionAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Action> mActions;

    public ActionAdapter (Context c) {
        this.mContext = c;
        this.mActions = new ArrayList<>();
    }

    public ActionAdapter(Context c, ArrayList<Action> actions) {
        this(c);
        this.mActions = actions;

    }

    public void setActions(ArrayList<Action> actions) {
        this.mActions.clear();
        this.mActions = actions;
    }

    public void clearActions() {
        this.mActions.clear();
    }
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public int getCount() {
        return mActions.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Action currentAction = mActions.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_asset_action,
                    parent, false);
        }

        ImageView actionIcon = (ImageView)convertView.findViewById(R.id.iv_asset_action_icon);
        TextView actionTitle = (TextView)convertView.findViewById(R.id.tv_asset_action_title);
        LinearLayout actionCosts = (LinearLayout)convertView.findViewById(R.id.ll_asset_cost);

        actionIcon.setImageBitmap(currentAction.getIcon());
        actionTitle.setText(currentAction.getTitle());

        int iconSize = (int) Math.ceil(mContext.getResources().getDimension(R.dimen.resource_icon));
        float textSize = mContext.getResources().getDimension(R.dimen.resource_text);
        int textColor  = ContextCompat.getColor(mContext, R.color.resourceText);

        for (Resource cost : currentAction.getResources()) {
            ImageView costIcon = new ImageView(mContext);
            TextView costValue = new TextView(mContext);

            costIcon.setImageBitmap(cost.getIcon());
            costIcon.setLayoutParams(new ViewGroup.LayoutParams(iconSize, iconSize));

            costValue.setText(cost.getCost());
            costValue.setTextSize(textSize);
            costValue.setTextColor(textColor);
            costValue.setTypeface(CApplicationData.DTypeface);

            actionCosts.addView(costIcon);
            actionCosts.addView(costValue);
        }

        return convertView;
    }
}