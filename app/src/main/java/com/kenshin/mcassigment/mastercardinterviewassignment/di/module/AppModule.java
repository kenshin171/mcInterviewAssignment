package com.kenshin.mcassigment.mastercardinterviewassignment.di.module;

import com.kenshin.mcassigment.mastercardinterviewassignment.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
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

//    @Singleton
//    @Provides
//    @Named("MealDetailActivityVMFactory")
//    MealDetailActivityViewModelProviderFactory provideMealDetailActivityViewModelFactory(
//            @Named("mealsRef") DatabaseReference mealsRef) {
//        return new MealDetailActivityViewModelProviderFactory(mealsRef);
//    }

}
