package com.ideproject.mooracle.recommendations.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
    public int rank;
    public int full_height;
    public int full_width;
    public String hex_code;
    public String url_75x75;
    public String url_170x135;
    public String url_570xN;
    public String url_fullxfull;

    protected Image(Parcel in) {
        this.rank = in.readInt();
        this.full_height = in.readInt();
        this.full_width = in.readInt();
        this.hex_code = in.readString();
        this.url_75x75 = in.readString();
        this.url_170x135 = in.readString();
        this.url_570xN = in.readString();
        this.url_fullxfull = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rank);
        dest.writeInt(full_height);
        dest.writeInt(full_width);
        dest.writeString(hex_code);
        dest.writeString(url_75x75);
        dest.writeString(url_170x135);
        dest.writeString(url_570xN);
        dest.writeString(url_fullxfull);
    }
}
