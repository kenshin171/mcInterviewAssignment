package com.kenshin.mcassigment.mastercardinterviewassignment.di.module;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by kennethleong on 4/3/17.
 */

@Module
public class BusModule {

    public static final String PROVIDER_STATE_SUBJECT = "PROVIDER_STATE_SUBJECT";
    public static final String PROVIDER_BOTTOM_SUBJECT = "PROVIDER_BOTTOM_SUBJECT";
    public static final String PROVIDER_IMAGE_UPLOAD_SUBJECT = "PROVIDER_IMAGE_UPLOAD_SUBJECT";

    @Provides
    @Singleton
    @Named(PROVIDER_STATE_SUBJECT)
    PublishSubject<Boolean> provideStateSubject() {
        return PublishSubject.create();
    }

    @Provides
    @Singleton
    @Named(PROVIDER_BOTTOM_SUBJECT)
    PublishSubject<String> provideBottomSubject() {
        return PublishSubject.create();
    }

}