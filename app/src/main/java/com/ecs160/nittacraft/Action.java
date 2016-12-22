package com.ecs160.nittacraft;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Action implements Parcelable{
    private Bitmap mIcon;
    private String mTitle;
    private ArrayList mResources;

    public Action(Bitmap icon, String title, ArrayList<Resource> resources) {
        this.mIcon = icon;
        this.mTitle = title;
        this.mResources = resources;
    }

    public Action() {
        mIcon = null;
        mTitle = "";
        mResources = new ArrayList<>();
    }
    public void setIcon(Bitmap icon) {
        mIcon = icon;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setResources(ArrayList<Resource> resources) {
        mResources = resources;
    }

    public Bitmap getIcon() { return mIcon; }
    public String getTitle() { return mTitle; }
    public ArrayList<Resource> getResources() { return mResources; }

    // Parcelable stuff
    private Action(Parcel in) {
        mIcon = Bitmap.CREATOR.createFromParcel(in);
        mTitle = in.readString();
        mResources = in.createTypedArrayList(Resource.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeParcelable(mIcon, i);
        parcel.writeString(mTitle);
        parcel.writeTypedList(mResources);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Action createFromParcel(Parcel in) {
            return new Action(in);
        }

        public Action[] newArray(int size) {
            return new Action[size];
        }
    };
}