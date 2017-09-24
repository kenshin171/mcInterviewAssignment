package com.kenshin.mcassigment.mastercardinterviewassignment.util;

import io.reactivex.disposables.Disposable;

/**
 * Created by kennethleong on 22/9/17.
 */

public class CommonUtility {

    private static final String TAG = "CommonUtility";

    public static void disposeDisposable(Disposable disposable) {

        if(null != disposable && !disposable.isDisposed())
            disposable.dispose();

    }

    public static long roundParseRate(float rate) {

        double roundOff = Math.round(rate * 100.0) / 100.0;
        String parsedString = String.valueOf(roundOff);
        if(parsedString.charAt(parsedString.length() - 2) == '.')
            parsedString += "0";
        parsedString = parsedString.replace(".","");
        return Long.parseLong(parsedString);

    }

    public static long calculateConverstion(float amount, float rate) {

        float calculated = amount * rate;

        return roundParseRate(calculated);

    }
}
