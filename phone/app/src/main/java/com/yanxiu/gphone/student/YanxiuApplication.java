package com.yanxiu.gphone.student;

import com.facebook.stetho.Stetho;

import org.litepal.LitePalApplication;

public class YanxiuApplication extends LitePalApplication {
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
