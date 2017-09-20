package com.kenshin.mcassigment.mastercardinterviewassignment.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kennethleong on 20/9/17.
 */

public class Currency implements Parcelable {

    private String code;
    private String country;
    private String name;
    private String flagPath;
    private String rate;

    public Currency() {
    }

    public Currency(String code, String country, String name, String flagPath, String rate) {
        this.code = code;
        this.country = country;
        this.name = name;
        this.flagPath = flagPath;
        this.rate = rate;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;

        Currency currency = (Currency) o;

        return getCode().equals(currency.getCode()) &&
                getCountry().equals(currency.getCountry()) &&
                getName().equals(currency.getName()) &&
                getFlagPath().equals(currency.getFlagPath()) &&
                getRate().equals(currency.getRate());
    }

    @Override
    public int hashCode() {
        int result = getCode().hashCode();
        result = 31 * result + getCountry().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getFlagPath().hashCode();
        result = 31 * result + getRate().hashCode();
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
        dest.writeString(this.rate);
    }

    protected Currency(Parcel in) {
        this.code = in.readString();
        this.country = in.readString();
        this.name = in.readString();
        this.flagPath = in.readString();
        this.rate = in.readString();
    }

    public static final Parcelable.Creator<Currency> CREATOR = new Parcelable.Creator<Currency>() {
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
