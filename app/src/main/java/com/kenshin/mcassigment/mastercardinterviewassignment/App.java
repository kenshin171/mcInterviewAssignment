package com.kenshin.mcassigment.mastercardinterviewassignment;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.kenshin.mcassigment.mastercardinterviewassignment.di.component.DaggerApplicationComponent;
import com.kenshin.mcassigment.mastercardinterviewassignment.di.module.AppModule;
import com.kenshin.mcassigment.mastercardinterviewassignment.di.module.BusModule;
import com.kenshin.mcassigment.mastercardinterviewassignment.di.module.DataModule;
import com.kenshin.mcassigment.mastercardinterviewassignment.di.module.NetworkModule;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by kennethleong on 20/9/17.
 */

public class App extends Application implements HasActivityInjector,
        HasServiceInjector,
        HasSupportFragmentInjector
{

    private static final String TAG = "App";

    private static boolean loggingEnabled = true;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidActivityInjector;

    @Inject
    DispatchingAndroidInjector<Service> dispatchingAndroidServiceInjector;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidFragmentInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidFragmentInjector;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidActivityInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingAndroidServiceInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }

        initDagger();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        Stetho.initializeWithDefaults(this);

    }

    private void initDagger() {

        AppModule appModule = new AppModule(this);
        NetworkModule networkModule = new NetworkModule();
        BusModule busModule = new BusModule();
        DataModule dataModule = new DataModule();

        DaggerApplicationComponent
                .builder()
                .busModule(busModule)
                .dataModule(dataModule)
                .appModule(appModule)
                .networkModule(networkModule)
                .build().inject(this);
    }

    private static Toast generalMessage;

    public static void ToastMessage(final String Message,Context context) {

        Runnable toastRunner = () -> {
            if (generalMessage != null){
                generalMessage.setText(Message);
                //generalMessage.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                generalMessage.show();
            }
            else{
                generalMessage = Toast.makeText(context, Message, Toast.LENGTH_SHORT);
                //generalMessage.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                generalMessage.show();
            }
        };

        new Handler(context.getMainLooper()).post(toastRunner);

    }

    public static void myLog(char logLevel,String tag,String message){

        if(message != null)
            if(BuildConfig.DEBUG && loggingEnabled){
                switch (logLevel) {
                    case 'd' :  Log.d(tag, message);
                        break;
                    case 'e' :  Log.e(tag, message);
                        break;
                    case 'v' :  Log.d(tag, message);
                        break;
                    case 'i' :  Log.i(tag, message);
                        break;
                    case 'w' :  Log.w(tag, message);
                        break;
                    default: Log.d(tag, message);

                }
            }
    }

    public static void myLog(String logLevel,String tag,String message){
        if(message != null)
            if(BuildConfig.DEBUG && loggingEnabled){
                switch (logLevel) {
                    case "wtf" :  Log.wtf(tag, message);
                        break;
                    default: Log.d(tag, message);

                }
            }
    }

}