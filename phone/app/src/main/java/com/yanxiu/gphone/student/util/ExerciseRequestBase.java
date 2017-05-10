package com.yanxiu.gphone.student.util;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.login.LoginInfo;

import okhttp3.Interceptor;

/**
 * Created by sunpeng on 2017/5/8.
 */

public abstract class ExerciseRequestBase extends RequestBase {
    protected String osType = "0";
    protected String pcode = "010110000";
    protected String token = LoginInfo.token;
    protected String trace_uid = LoginInfo.uid;
    protected String version = "2.4.1";

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTrace_uid() {
        return trace_uid;
    }

    public void setTrace_uid(String trace_uid) {
        this.trace_uid = trace_uid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
