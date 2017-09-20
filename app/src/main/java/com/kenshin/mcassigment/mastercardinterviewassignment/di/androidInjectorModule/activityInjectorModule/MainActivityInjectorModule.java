package com.kenshin.mcassigment.mastercardinterviewassignment.di.androidInjectorModule.activityInjectorModule;

import com.kenshin.mcassigment.mastercardinterviewassignment.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by kennethleong on 6/5/17.
 */

@Module
public abstract class MainActivityInjectorModule {

    @ContributesAndroidInjector
    public abstract MainActivity contributeMainActivityInjector();

}
