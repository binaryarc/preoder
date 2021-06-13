package com.sample.kakao;

import android.os.Parcel;
import android.os.Parcelable;

public class McDonaldBurger implements Parcelable {

    String imageUrl;
    String name;
    String price;
    int count;

    public McDonaldBurger(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public McDonaldBurger() {
    }

    public McDonaldBurger(Parcel src) {
        imageUrl = src.readString();
        name = src.readString();
        price = src.readString();
        count = src.readInt();
    }

    public static final Creator<McDonaldBurger> CREATOR = new Creator<McDonaldBurger>() {
        @Override
        public McDonaldBurger createFromParcel(Parcel in) {
            return new McDonaldBurger(in);
        }

        @Override
        public McDonaldBurger[] newArray(int size) {
            return new McDonaldBurger[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeInt(count);
    }
}
