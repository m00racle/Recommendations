package com.ideproject.mooracle.recommendations.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ActiveListings implements Parcelable {
    public int count;
    public Listing[] results;


    protected ActiveListings(Parcel in) {
        this.count = in.readInt();
        this.results = (Listing[]) in.readParcelableArray(Listing.class.getClassLoader());
    }

    public static final Creator<ActiveListings> CREATOR = new Creator<ActiveListings>() {
        @Override
        public ActiveListings createFromParcel(Parcel in) {
            return new ActiveListings(in);
        }

        @Override
        public ActiveListings[] newArray(int size) {
            return new ActiveListings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeParcelableArray(results, 0);
    }
}
