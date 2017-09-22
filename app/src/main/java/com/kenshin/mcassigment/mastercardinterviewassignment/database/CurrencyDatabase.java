package com.kenshin.mcassigment.mastercardinterviewassignment.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.kenshin.mcassigment.mastercardinterviewassignment.database.dao.CurrencyDao;
import com.kenshin.mcassigment.mastercardinterviewassignment.model.Currency;

/**
 * Created by kennethleong on 22/9/17.
 */

@Database(entities = {Currency.class}, version = 1)
public abstract class CurrencyDatabase extends RoomDatabase{
    public abstract CurrencyDao currencyDao();

}
