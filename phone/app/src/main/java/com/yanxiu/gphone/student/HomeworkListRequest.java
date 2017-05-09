package com.yanxiu.gphone.student;

import com.test.yanxiu.network.RequestBase;

/**
 * Created by sunpeng on 2017/5/8.
 */

public class HomeworkListRequest extends ExerciseRequestBase {
    private String page = "1";
    private String pageSize = "20";
    private String groupId = "126100";
    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return "http://mobile.hwk.yanxiu.com/app/class/";
    }

    @Override
    protected String urlPath() {
        return "listGroupPaper.do?";
    }

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
