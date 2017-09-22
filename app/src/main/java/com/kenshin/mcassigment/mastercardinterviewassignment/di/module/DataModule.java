package com.kenshin.mcassigment.mastercardinterviewassignment.di.module;

import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kenshin.mcassigment.mastercardinterviewassignment.App;
import com.kenshin.mcassigment.mastercardinterviewassignment.database.CurrencyDatabase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kennethleong on 29/2/16.
 */
@Module
public class DataModule {

    public static final String CURRENCY_DB = "CURRENCY_DB";

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(App app) {
        return PreferenceManager.getDefaultSharedPreferences(app);
    }

    @Provides
    @Singleton
    SharedPreferences.Editor providesSharedPreferencesEditor(SharedPreferences sharedPreferences) {
        return sharedPreferences.edit();
    }

    @Provides
    @Singleton
    @Named(CURRENCY_DB)
    CurrencyDatabase providesCurrencyDb(App app) {
        CurrencyDatabase db = Room.databaseBuilder(app.getApplicationContext(),
                CurrencyDatabase.class, "masterCardAssignmentDb").build();

        return db;
    }

}
