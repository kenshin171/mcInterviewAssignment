package com.kenshin.mcassigment.mastercardinterviewassignment.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.kenshin.mcassigment.mastercardinterviewassignment.model.Currency;

import java.util.List;

/**
 * Created by kennethleong on 20/9/17.
 */

public class MainActivityViewModel extends ViewModel {

    MutableLiveData<List<Currency>> currencyList;

    public MainActivityViewModel() {

        currencyList = new MutableLiveData<>();

    }

    public MutableLiveData<List<Currency>> getCurrencyList() {
        return currencyList;
    }
}
