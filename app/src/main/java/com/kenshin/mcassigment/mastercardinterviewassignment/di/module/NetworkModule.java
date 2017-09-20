package com.kenshin.mcassigment.mastercardinterviewassignment.di.module;

import android.app.Application;

import com.kenshin.mcassigment.mastercardinterviewassignment.R;
import com.kenshin.mcassigment.mastercardinterviewassignment.retrofitService.RetroFitService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kennethleong on 2/3/16.
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(60 * 1000, TimeUnit.MILLISECONDS);

        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRestAdapter(Application application, OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient)
                //baseURL always end wth /
                .baseUrl(application.getString(R.string.endpoint))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        return builder.build();
    }

    @Provides
    @Singleton
    public RetroFitService provideRetroFitApiService(Retrofit restAdapter) {
        return restAdapter.create(RetroFitService.class);
    }
}