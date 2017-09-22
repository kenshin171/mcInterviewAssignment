package com.kenshin.mcassigment.mastercardinterviewassignment.util;

import io.reactivex.disposables.Disposable;

/**
 * Created by kennethleong on 22/9/17.
 */

public class CommonUtility {

    public static void disposeDisposable(Disposable disposable) {

        if(null != disposable && !disposable.isDisposed())
            disposable.dispose();

    }

}
