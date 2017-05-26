package com.yanxiu.gphone.student.homework.data;

import com.yanxiu.gphone.student.base.ExerciseBaseRequest;
import com.yanxiu.gphone.student.db.UrlRepository;

/**
 * Created by sunpeng on 2017/5/8.
 */

public class HomeworkDetailRequest extends ExerciseBaseRequest {
    protected String page = "1";
    protected String pageSize = "10";
    protected String groupId ;
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
        return "/class/listGroupPaper.do";
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
        return HttpType.GET;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
