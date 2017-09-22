package com.kenshin.mcassigment.mastercardinterviewassignment.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.kenshin.mcassigment.mastercardinterviewassignment.App;
import com.kenshin.mcassigment.mastercardinterviewassignment.database.CurrencyDatabase;
import com.kenshin.mcassigment.mastercardinterviewassignment.model.Currency;
import com.kenshin.mcassigment.mastercardinterviewassignment.retrofitService.RetroFitService;
import com.kenshin.mcassigment.mastercardinterviewassignment.util.CommonUtility;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kennethleong on 20/9/17.
 */

public class SearchActivityViewModel extends ViewModel {

    private static final String TAG = "SearchActivityViewModel";

    private MutableLiveData<List<Currency>> currencyList;
    private CurrencyDatabase currencyDatabase;
    private RetroFitService retroFitService;
    private Disposable maybeListCallback;
    private Disposable flowableListCallback;
    private Disposable searchListCallback;

    private Disposable retrofitCallback;

    public SearchActivityViewModel(CurrencyDatabase currencyDatabase, RetroFitService retroFitService) {

        this.currencyList = new MutableLiveData<>();
        this.currencyDatabase = currencyDatabase;
        this.retroFitService = retroFitService;
        getCurrencyListFromSources();
    }

    private void getCurrencyListFromSources() {

        maybeListCallback = currencyDatabase.currencyDao().getAllMaybe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newList -> {
                            if(null != newList && !newList.isEmpty()) {
                                this.currencyList.setValue(newList);
                            }
                            else {
                                fetchFromServer();
                            }
                        },
                        error -> App.myLog('e', TAG, error.getMessage()),
                        () -> {
                            this.currencyList.setValue(null);
                            fetchFromServer();
                        }
                    );

        flowableListCallback = currencyDatabase.currencyDao().searchDB("%%")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newList -> {
                    if(null != newList && !newList.isEmpty()) {
                        this.currencyList.setValue(newList);
                    }
                });

    }

    public void searchDB(@NotNull CharSequence searchText) {

        searchListCallback = currencyDatabase.currencyDao().searchDB("%"+searchText+"%")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newList -> {
                    if(null != newList && !newList.isEmpty()) {
                        this.currencyList.setValue(newList);
                    }
                    else {
                        this.currencyList.setValue(null);
                    }
                });

    }


    private void fetchFromServer() {

        retrofitCallback = retroFitService.getAllCurrency()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(newList -> this.currencyDatabase.currencyDao().insertAll(newList));

    }

    public MutableLiveData<List<Currency>> getCurrencyList() {
        return currencyList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        CommonUtility.disposeDisposable(maybeListCallback);
        CommonUtility.disposeDisposable(flowableListCallback);
        CommonUtility.disposeDisposable(retrofitCallback);

    }
}
