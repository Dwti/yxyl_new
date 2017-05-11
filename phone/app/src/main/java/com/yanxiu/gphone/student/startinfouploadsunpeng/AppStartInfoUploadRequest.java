package com.yanxiu.gphone.student.startinfouploadsunpeng;

import com.test.yanxiu.network.RequestBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2017/3/30.
 */

public class AppStartInfoUploadRequest extends RequestBase{

    private List<AppStartInfo> content = new ArrayList<>();
    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected RequestBase.HttpType httpType() {
        return RequestBase.HttpType.POST;
    }

    @Override
    protected String urlServer() {
        return "http://boss.shangruitong.com";
    }

    @Override
    protected String urlPath() {
        return "/logup";
    }

    public List<AppStartInfo> getContent() {
        return content;
    }

    public void setContent(List<AppStartInfo> content) {
        this.content = content;
    }
}
