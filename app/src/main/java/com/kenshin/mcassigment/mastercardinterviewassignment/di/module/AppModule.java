package com.kenshin.mcassigment.mastercardinterviewassignment.di.module;

import com.kenshin.mcassigment.mastercardinterviewassignment.App;
import com.kenshin.mcassigment.mastercardinterviewassignment.database.CurrencyDatabase;
import com.kenshin.mcassigment.mastercardinterviewassignment.retrofitService.RetroFitService;
import com.kenshin.mcassigment.mastercardinterviewassignment.viewModel.providerFactory.SearchActvityVMproviderFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.kenshin.mcassigment.mastercardinterviewassignment.di.module.DataModule.CURRENCY_DB;

/**
 * Created by kennethleong on 25/2/16.
 */
@Module
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides @Singleton
    App providesApplication() {
        return app;
    }

    @Singleton
    @Provides
    @Named("SearchActivityVMFactory")
    SearchActvityVMproviderFactory provideSearchActivityViewModelFactory(
            @Named(CURRENCY_DB) CurrencyDatabase currencyDatabase,
            RetroFitService retroFitService) {
        return new SearchActvityVMproviderFactory(currencyDatabase, retroFitService);
    }

}
