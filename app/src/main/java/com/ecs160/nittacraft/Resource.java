package com.ecs160.nittacraft;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Resource implements Parcelable {
    private Bitmap mIcon;
    private String mCost;

    public Resource(Bitmap icon, String cost) {
        mIcon = icon;
        mCost = cost;
    }

    public void setIcon(Bitmap icon) { mIcon = icon; }
    public void setCost(String cost) { mCost = cost; }

    public Bitmap getIcon() { return mIcon; }
    public String getCost() { return mCost; }


    // Parcelable stuff
    private Resource(Parcel in) {
        mCost = in.readString();
        mIcon = Bitmap.CREATOR.createFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(mCost);
        out.writeParcelable(mIcon, i);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Resource createFromParcel(Parcel in) {
            return new Resource(in);
        }

        public Resource[] newArray(int size) {
            return new Resource[size];
        }
    };
}