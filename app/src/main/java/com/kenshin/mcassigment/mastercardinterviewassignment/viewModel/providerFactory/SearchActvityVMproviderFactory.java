package com.kenshin.mcassigment.mastercardinterviewassignment.viewModel.providerFactory;

import android.arch.lifecycle.ViewModelProvider;

import com.kenshin.mcassigment.mastercardinterviewassignment.database.CurrencyDatabase;
import com.kenshin.mcassigment.mastercardinterviewassignment.retrofitService.RetroFitService;
import com.kenshin.mcassigment.mastercardinterviewassignment.viewModel.SearchActivityViewModel;

/**
 * Created by kennethleong on 22/9/17.
 */
public class SearchActvityVMproviderFactory implements ViewModelProvider.Factory{

    private CurrencyDatabase currencyDatabase;
    private RetroFitService retroFitService;

    public SearchActvityVMproviderFactory(CurrencyDatabase currencyDatabase, RetroFitService retroFitService) {
        this.currencyDatabase = currencyDatabase;
        this.retroFitService = retroFitService;
    }

    @Override
    public SearchActivityViewModel create(Class modelClass) {
        return new SearchActivityViewModel(currencyDatabase, retroFitService);
    }
}
