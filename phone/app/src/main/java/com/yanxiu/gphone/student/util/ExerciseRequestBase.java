package com.yanxiu.gphone.student.util;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.constant.Constants;

/**
 * Created by sunpeng on 2017/5/8.
 */

public abstract class ExerciseRequestBase extends RequestBase {
    protected String osType = Constants.osType;
    protected String pcode = Constants.pcode;
    protected String token = LoginInfo.getToken();
    protected String trace_uid = LoginInfo.getUID();
    protected String version = Constants.version;

    @Override
    protected boolean shouldLog() {
        return false;
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
