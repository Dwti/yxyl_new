package com.yanxiu.gphone.student.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by sunpeng on 2017/5/9.
 */

public class DeviceUtil {
    public static String getAppDeviceId() {

        TelephonyManager telephonyManager = (TelephonyManager) YXApplication.getContext().getSystemService(
                Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        WifiManager wifiManager = (WifiManager) YXApplication.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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
