package com.yanxiu.gphone.student.login.bean;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.util.DeviceUtil;

/**
 * Created by sunpeng on 2017/5/9.
 */

public class LoginRequest extends RequestBase {
    protected String osType = "0";
    protected String pcode = "010110000";
    protected String version = "2.4.1";
    protected String deviceId = DeviceUtil.getAppDeviceId();
    protected String mobile = "17600808054";
    protected String password = "111111";

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app/user/";
    }

    @Override
    protected String urlPath() {
        return "login.do";
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
