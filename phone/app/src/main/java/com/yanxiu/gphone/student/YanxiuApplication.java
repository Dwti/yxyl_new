package com.yanxiu.gphone.student;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class YanxiuApplication extends Application {
    private static YanxiuApplication instance;

    public static YanxiuApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}
