package com.yanxiu.gphone.student.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.yanxiu.gphone.student.YanxiuApplication;

/**
 * Created by 戴延枫 on 2017/5/19.
 */

public class SystemUtil {
    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode() {
        int version = -1;
        try {
            PackageManager manager = YanxiuApplication.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(YanxiuApplication.getInstance().getPackageName(), 0);
            version = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String getVersionName() {
        String version = "";
        try {
            PackageManager manager = YanxiuApplication.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(YanxiuApplication.getInstance().getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    public static boolean checkBrowser(Context context, String packageName) {
        boolean isInstalled = false;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isInstalled = false;
        }
        return isInstalled;
    }

    public static String getChannelName() {
        String channelName = "other";
        try {
            PackageManager packageManager = YanxiuApplication.getInstance().getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(YanxiuApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString("InstallChannel");
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(channelName)){
            return "other";
        }else {
            return channelName;
        }
    }

}
