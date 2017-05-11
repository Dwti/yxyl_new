package com.yanxiu.gphone.student.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.yanxiu.gphone.student.YanxiuApplication;

/**
 * Created by sunpeng on 2017/5/9.
 */

public class DeviceUtil {
    public static String getAppDeviceId() {

        TelephonyManager telephonyManager = (TelephonyManager) YanxiuApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        WifiManager wifiManager = (WifiManager) YanxiuApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo != null) {
            String macAddress = wifiInfo.getMacAddress();
            deviceId = deviceId + macAddress;
        }

        if (deviceId == null || "".equals(deviceId.trim())) {
            deviceId = "-";
        }

        return SysEncryptUtil.getMD5(deviceId);
    }
}
