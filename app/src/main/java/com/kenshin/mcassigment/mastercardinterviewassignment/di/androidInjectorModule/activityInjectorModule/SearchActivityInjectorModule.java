package com.kenshin.mcassigment.mastercardinterviewassignment.di.androidInjectorModule.activityInjectorModule;

import com.kenshin.mcassigment.mastercardinterviewassignment.activity.SearchActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by kennethleong on 22/9/17.
 */

@Module
public abstract class SearchActivityInjectorModule {

    @ContributesAndroidInjector
    public abstract SearchActivity contributeSearchActivityInjector();

}
