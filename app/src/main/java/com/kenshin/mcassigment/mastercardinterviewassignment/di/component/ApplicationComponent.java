package com.kenshin.mcassigment.mastercardinterviewassignment.di.component;

import com.kenshin.mcassigment.mastercardinterviewassignment.App;
import com.kenshin.mcassigment.mastercardinterviewassignment.di.androidInjectorModule.activityInjectorModule.MainActivityInjectorModule;
import com.kenshin.mcassigment.mastercardinterviewassignment.di.module.AppModule;
import com.kenshin.mcassigment.mastercardinterviewassignment.di.module.BusModule;
import com.kenshin.mcassigment.mastercardinterviewassignment.di.module.DataModule;
import com.kenshin.mcassigment.mastercardinterviewassignment.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by kennethleong on 20/9/17.
 */

@Singleton
@Component(modules = {MainActivityInjectorModule.class,

                        AndroidSupportInjectionModule.class,

                        NetworkModule.class,
                        BusModule.class,
                        DataModule.class,
                        AppModule.class})
public interface ApplicationComponent {
    void inject(App application);
}
