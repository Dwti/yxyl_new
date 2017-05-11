package com.yanxiu.gphone.student.homework.datasunpeng;

import com.yanxiu.gphone.student.utilsunpneg.ExerciseRequestBase;

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
        return "listGroupPaper.do";
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
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
