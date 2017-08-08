package com.yanxiu.gphone.student.userevent.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.base.UrlBean;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/8/7 14:16.
 * Function :
 */
public class UploadUserEventRequest extends RequestBase {

    public String yxyl_statistic;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }

    @Override
    protected String urlServer() {
        if (UrlBean.RELEASE.equals(UrlRepository.getInstance().getMode())){
            return "http://boss.shangruitong.com/logup";
        }else {
            return "http://boss.shangruitong.com/upfile";
        }
    }

    @Override
    protected String urlPath() {
        return null;
    }
}
