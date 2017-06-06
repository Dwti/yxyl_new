package com.yanxiu.gphone.student.user.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/31 12:22.
 * Function :
 */
public class BaseLoginRequest extends RequestBase {

    public String pcode= Constants.pcode;
    public String version=Constants.version;
    public String osType=Constants.osType;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
    }

    @Override
    protected String urlPath() {
        return null;
    }
}
