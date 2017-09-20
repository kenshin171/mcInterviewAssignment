package com.kenshin.mcassigment.mastercardinterviewassignment.retrofitService;

import com.kenshin.mcassigment.mastercardinterviewassignment.model.Currency;
import com.kenshin.mcassigment.mastercardinterviewassignment.model.ExchangeResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kennethleong on 20/9/16.
 */

public interface RetroFitService {

    @GET("/api/currency")
    Observable<List<Currency>> getAllCurrency();

    @GET("/api/exchange")
    Observable<ExchangeResponse> getExchangeRate(@Query("baseCode") String baseCode,
                                                 @Query("targetCode") String targetCode);
}