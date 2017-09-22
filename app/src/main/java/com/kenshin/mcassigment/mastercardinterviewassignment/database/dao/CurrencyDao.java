package com.kenshin.mcassigment.mastercardinterviewassignment.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.kenshin.mcassigment.mastercardinterviewassignment.model.Currency;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by kennethleong on 22/9/17.
 */

@Dao
public interface CurrencyDao {

    @Query("SELECT * FROM currency ORDER BY code")
    Flowable<List<Currency>> getAll();

    @Query("SELECT * FROM currency")
    Maybe<List<Currency>> getAllMaybe();

    @Query("SELECT * FROM currency WHERE code LIKE :searchText OR name LIKE :searchText ORDER BY code")
    Maybe<List<Currency>> searchDB(String searchText);

    @Update
    int loadAllByIds(Currency... currencies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Currency> currencies);

    @Delete
    int delete(Currency... currencies);

}
