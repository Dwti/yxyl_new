package com.yanxiu.gphone.student.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yanxiu.gphone.student.YanxiuApplication;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/17 14:58.
 * Function :
 */
public class NetWorkUtils {
    /**
     * 判断网络是否是可用
     * @return
     */
    public static boolean isNetAvailable() {
        return NetWorkUtils.getAvailableNetWorkInfo() != null;
    }

    public static NetworkInfo getAvailableNetWorkInfo() {
        NetworkInfo activeNetInfo = null;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) YanxiuApplication.getInstance().getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (activeNetInfo != null && activeNetInfo.isAvailable()) {
            return activeNetInfo;
        } else {
            return null;
        }
    }

    public static String getNetWorkType() {

        String netWorkType = "2";
        NetworkInfo netWorkInfo = getAvailableNetWorkInfo();

        if (netWorkInfo != null) {
            if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                netWorkType = "1";
            } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                netWorkType = "0";
            }
        }
        return netWorkType;
    }

}
