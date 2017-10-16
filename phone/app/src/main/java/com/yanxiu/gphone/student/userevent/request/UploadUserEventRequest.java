package com.yanxiu.gphone.student.userevent.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.base.UrlBean;
import com.yanxiu.gphone.student.db.UrlRepository;

import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

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
        return "http://boss.shangruitong.com/logup";
    }

    @Override
    protected String urlPath() {
        return null;
    }

    @Override
    protected Request generateRequest(UUID uuid) throws NullPointerException, IllegalAccessException, IllegalArgumentException {
        Request.Builder builder = new Request.Builder()
                .tag(uuid)
                .url(fullUrl());

        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, yxyl_statistic);
        builder.post(body);

        return builder.build();
    }
}
