package com.kenshin.mcassigment.mastercardinterviewassignment.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.os.Looper;

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
    private MutableLiveData<String> errorNotifier;
    private CurrencyDatabase currencyDatabase;
    private RetroFitService retroFitService;
    private Disposable maybeListCallback;
    private Disposable flowableListCallback;
    private Disposable searchListCallback;

    private Disposable retrofitCallback;

    public SearchActivityViewModel(CurrencyDatabase currencyDatabase, RetroFitService retroFitService) {

        this.currencyList = new MutableLiveData<>();
        this.errorNotifier = new MutableLiveData<>();
        this.currencyDatabase = currencyDatabase;
        this.retroFitService = retroFitService;
        getCurrencyListFromSources();
    }

    private void getCurrencyListFromSources() {

        //get currencies from local db, it empty fetch from server
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
                        error -> {
                            App.myLog('e', TAG, error.getMessage());
                            errorNotifier.setValue(error.getMessage());
                        },
                        () -> {
                            this.currencyList.setValue(null);
                            fetchFromServer();
                        }
                    );

        //observie for general changes in the db
        flowableListCallback = currencyDatabase.currencyDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newList -> {
                    errorNotifier.setValue(null);
                    if(null != newList && !newList.isEmpty()) {
                        this.currencyList.setValue(newList);
                    }
                },
                    error -> {
                        App.myLog('e', TAG, error.getMessage());
                        errorNotifier.setValue(error.getMessage());
                    });
    }

    /**
     * Performs a SQLite query search on currency name and currency code
     * @param searchText String Search term
     */
    public void searchDB(@NotNull CharSequence searchText) {
        if(null != getCurrencyList().getValue()) {
            searchListCallback = currencyDatabase.currencyDao().searchDB("%" + searchText + "%")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(newList -> {

                                errorNotifier.setValue(null);

                                if (null != newList && !newList.isEmpty()) {
                                    this.currencyList.setValue(newList);
                                } else {
                                    this.currencyList.setValue(null);
                                }
                            },
                            error -> {
                                App.myLog('e', TAG, error.getMessage());
                                errorNotifier.setValue(error.getMessage());
                            });
        }
        else {
            getCurrencyListFromSources();
        }
    }

    private void fetchFromServer() {

        retrofitCallback = retroFitService.getAllCurrency()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(newList -> {
                            if(null != newList) {
                                postErrorOnMainThread(null);
                                this.currencyDatabase.currencyDao().insertAll(newList);
                            }
                        },
                        error -> {
                            App.myLog('e', TAG, error.getMessage());
                            postErrorOnMainThread(error.getMessage());
                });
    }

    public MutableLiveData<List<Currency>> getCurrencyList() {
        return currencyList;
    }

    public MutableLiveData<String> getErrorNotifier() {
        return errorNotifier;
    }

    private void postErrorOnMainThread(String error) {

        // Get a handler that can be used to post to the main thread
        Handler mainHandler = new Handler(Looper.getMainLooper());

        Runnable myRunnable = () -> errorNotifier.setValue(error);
        mainHandler.post(myRunnable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        CommonUtility.disposeDisposable(maybeListCallback);
        CommonUtility.disposeDisposable(flowableListCallback);
        CommonUtility.disposeDisposable(retrofitCallback);
        CommonUtility.disposeDisposable(searchListCallback);
    }
}
