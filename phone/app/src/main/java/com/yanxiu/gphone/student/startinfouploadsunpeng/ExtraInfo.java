package com.yanxiu.gphone.student.startinfouploadsunpeng;

import android.os.Build;

import com.google.gson.Gson;

/**
 * Created by sunpeng on 2017/3/30.
 */

public class ExtraInfo {
    protected String mobileModel;
    protected String brand;
    protected String system;
    protected String resolution;
    protected String netModel;

    public ExtraInfo() {
        mobileModel = Build.MODEL;
        brand = Build.MANUFACTURER;
        system = Build.VERSION.RELEASE;
        resolution = 1920 + " * " +1080;
        netModel = "0";
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
