package com.ideproject.mooracle.recommendations.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Shop implements Parcelable {
    public int shop_id;
    public String shop_name;

    protected Shop(Parcel in) {
        this.shop_id = in.readInt();
        this.shop_name = in.readString();
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shop_id);
        dest.writeString(shop_name);
    }
}
