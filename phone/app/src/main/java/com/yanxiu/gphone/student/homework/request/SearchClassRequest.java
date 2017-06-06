package com.yanxiu.gphone.student.homework.request;

import com.yanxiu.gphone.student.base.YxylBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by sp on 17-5-19.
 */

public class SearchClassRequest extends YxylBaseRequest {
    protected String classId;
    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
    }

    @Override
    protected String urlPath() {
        return "/class/searchClass.do";
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
