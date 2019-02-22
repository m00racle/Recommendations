package com.ideproject.mooracle.recommendations.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Listing implements Parcelable {
    public int listing_id;
    public String title;
    public String description;
    public String price;
    public String url;
    public Shop Shop;
    public Image[] Images;

    protected Listing(Parcel in) {
        this.listing_id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.price = in.readString();
        this.url = in.readString();
        this.Shop = in.readParcelable(Shop.class.getClassLoader());
        this.Images = (Image[]) in.readParcelableArray(Image.class.getClassLoader());
    }

    public static final Creator<Listing> CREATOR = new Creator<Listing>() {
        @Override
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        @Override
        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(listing_id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeString(url);
        dest.writeParcelable(Shop, 0);
        dest.writeParcelableArray(Images, 0);
    }
}