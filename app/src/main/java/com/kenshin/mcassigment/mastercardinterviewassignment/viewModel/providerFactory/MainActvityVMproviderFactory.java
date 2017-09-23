package com.kenshin.mcassigment.mastercardinterviewassignment.viewModel.providerFactory;

import android.arch.lifecycle.ViewModelProvider;

import com.kenshin.mcassigment.mastercardinterviewassignment.retrofitService.RetroFitService;
import com.kenshin.mcassigment.mastercardinterviewassignment.viewModel.MainActivityViewModel;

/**
 * Created by kennethleong on 23/9/17.
 */

public class MainActvityVMproviderFactory implements ViewModelProvider.Factory {

    private RetroFitService retroFitService;

    public MainActvityVMproviderFactory(RetroFitService retroFitService) {
        this.retroFitService = retroFitService;
    }

    @Override
    public MainActivityViewModel create(Class modelClass) {
        return new MainActivityViewModel(retroFitService);
    }

}
