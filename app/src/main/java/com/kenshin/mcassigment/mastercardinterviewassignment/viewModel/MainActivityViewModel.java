package com.kenshin.mcassigment.mastercardinterviewassignment.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.kenshin.mcassigment.mastercardinterviewassignment.model.Currency;
import com.kenshin.mcassigment.mastercardinterviewassignment.model.ExchangeResponse;
import com.kenshin.mcassigment.mastercardinterviewassignment.retrofitService.RetroFitService;
import com.kenshin.mcassigment.mastercardinterviewassignment.util.CommonUtility;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kennethleong on 23/9/17.
 */

public class MainActivityViewModel extends ViewModel {

    private RetroFitService retroFitService;
    private MutableLiveData<Currency> iHaveCur;
    private MutableLiveData<Currency> iWantCur;
    private MutableLiveData<ExchangeResponse[]> iHaveWantExc;
    private Disposable getExchangeRateDis;
    private MutableLiveData<String> errorNotifier;

    public MainActivityViewModel(RetroFitService retroFitService) {
        this.retroFitService = retroFitService;
        this.iHaveCur = new MutableLiveData<>();
        this.iWantCur = new MutableLiveData<>();
        this.iHaveWantExc = new MutableLiveData<>();
        this.errorNotifier = new MutableLiveData<>();
    }

    public MutableLiveData<Currency> getiHaveCur() {
        return iHaveCur;
    }

    public MutableLiveData<Currency> getiWantCur() {
        return iWantCur;
    }

    public MutableLiveData<ExchangeResponse[]> getiHaveWantExc() {
        return iHaveWantExc;
    }

    public MutableLiveData<String> getErrorNotifier() {
        return errorNotifier;
    }

    public void getExchangeRates() {

        if(null != iHaveCur.getValue() && null != iWantCur.getValue()) {

            Observable<ExchangeResponse> iHaveCurExchange = retroFitService.getExchangeRate(iHaveCur.getValue().getCode(), iWantCur.getValue().getCode());

            Observable<ExchangeResponse> iWantCurExchange = retroFitService.getExchangeRate(iWantCur.getValue().getCode(), iHaveCur.getValue().getCode());

            Observable<ExchangeResponse[]> combined =
                    Observable.zip(iHaveCurExchange, iWantCurExchange,
                            (haveExchange, wantExchange) -> new ExchangeResponse[]{haveExchange, wantExchange})
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());

            getExchangeRateDis = combined.subscribe(exchangeResponses -> {

                iHaveWantExc.setValue(exchangeResponses);
            }, error -> {
                errorNotifier.setValue(error.getMessage());
            });
        }
        else {
            iHaveWantExc.setValue(null);
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();

        CommonUtility.disposeDisposable(getExchangeRateDis);

    }
}
