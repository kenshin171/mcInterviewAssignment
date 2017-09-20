package com.kenshin.mcassigment.mastercardinterviewassignment.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kennethleong on 20/9/17.
 */

public class ExchangeResponse implements Parcelable {

    private String baseCode;
    private String targetCode;
    private float rate;

    public ExchangeResponse() {
    }

    public ExchangeResponse(String baseCode, String targetCode, float rate) {
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.rate = rate;
    }

    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
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
        if (!(o instanceof ExchangeResponse)) return false;

        ExchangeResponse that = (ExchangeResponse) o;

        return Float.compare(that.getRate(), getRate()) == 0 &&
                getBaseCode().equals(that.getBaseCode()) &&
                getTargetCode().equals(that.getTargetCode());
    }

    @Override
    public int hashCode() {
        int result = getBaseCode().hashCode();
        result = 31 * result + getTargetCode().hashCode();
        result = 31 * result + (getRate() != +0.0f ? Float.floatToIntBits(getRate()) : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.baseCode);
        dest.writeString(this.targetCode);
        dest.writeFloat(this.rate);
    }

    protected ExchangeResponse(Parcel in) {
        this.baseCode = in.readString();
        this.targetCode = in.readString();
        this.rate = in.readFloat();
    }

    public static final Parcelable.Creator<ExchangeResponse> CREATOR = new Parcelable.Creator<ExchangeResponse>() {
        @Override
        public ExchangeResponse createFromParcel(Parcel source) {
            return new ExchangeResponse(source);
        }

        @Override
        public ExchangeResponse[] newArray(int size) {
            return new ExchangeResponse[size];
        }
    };
}
