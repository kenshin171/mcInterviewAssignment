package com.kenshin.mcassigment.mastercardinterviewassignment.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by kennethleong on 20/9/17.
 */

@Entity
public class Currency implements Parcelable {

    @PrimaryKey
    @ColumnInfo(name = "code")
    @NonNull
    private String code;

    @ColumnInfo(name = "country")
    private String country;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "flagPath")
    private String flagPath;

    @ColumnInfo(name = "rate")
    private float rate;

    public Currency() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlagPath() {
        return flagPath;
    }

    public void setFlagPath(String flagPath) {
        this.flagPath = flagPath;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;

        Currency currency = (Currency) o;

        return Float.compare(currency.getRate(), getRate()) == 0 &&
                getCode().equals(currency.getCode()) &&
                getCountry().equals(currency.getCountry()) &&
                getName().equals(currency.getName()) &&
                getFlagPath().equals(currency.getFlagPath());
    }

    @Override
    public int hashCode() {
        int result = getCode().hashCode();
        result = 31 * result + getCountry().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getFlagPath().hashCode();
        result = 31 * result + (getRate() != +0.0f ? Float.floatToIntBits(getRate()) : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.country);
        dest.writeString(this.name);
        dest.writeString(this.flagPath);
        dest.writeFloat(this.rate);
    }

    protected Currency(Parcel in) {
        this.code = in.readString();
        this.country = in.readString();
        this.name = in.readString();
        this.flagPath = in.readString();
        this.rate = in.readFloat();
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel source) {
            return new Currency(source);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };
}
