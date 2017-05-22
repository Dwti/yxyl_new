package com.yanxiu.gphone.student.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

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
}
